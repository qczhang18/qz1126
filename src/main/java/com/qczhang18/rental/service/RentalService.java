package com.qczhang18.rental.service;

import com.qczhang18.rental.model.Agreement;
import com.qczhang18.rental.model.RentalRequest;
import com.qczhang18.rental.model.Type;
import com.qczhang18.rental.util.ChargingDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class RentalService {

    public Agreement checkout(RentalRequest rentalRequest) {

        log.info("Processing rental request: {}", rentalRequest);

        Agreement agreement = calculateRentalCharge(rentalRequest);

        log.info("Generated agreement:\n{}", agreement);
        return agreement;
    }

    private Agreement calculateRentalCharge(RentalRequest rentalRequest) {
        LocalDate startDate = rentalRequest.getCheckoutDate().plusDays(1);
        LocalDate dueDate = rentalRequest.getCheckoutDate().plusDays(rentalRequest.getRentalDay());

        int chargeableDays = getChargingDates(startDate, dueDate, rentalRequest.getTool().getType()).size();
        BigDecimal chargeAmount = rentalRequest.getTool().getType().getDailyCharge().multiply(BigDecimal.valueOf(chargeableDays));
        BigDecimal discountAmount = chargeAmount.multiply(BigDecimal.valueOf(rentalRequest.getDiscountPercent())).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

        return Agreement.builder()
                .toolCode(rentalRequest.getTool().getCode())
                .toolType(rentalRequest.getTool().getType().getName())
                .toolBrand(rentalRequest.getTool().getBrand())
                .rentalDays(rentalRequest.getRentalDay())
                .checkoutDate(rentalRequest.getCheckoutDate())
                .dueDate(dueDate)
                .dailyRentalCharge(rentalRequest.getTool().getType().getDailyCharge())
                .chargeDays(chargeableDays)
                .preDiscountCharge(chargeAmount)
                .discountPercent(rentalRequest.getDiscountPercent())
                .discountAmount(discountAmount)
                .finalCharge(chargeAmount.subtract(discountAmount))
                .build();
    }

    public List<LocalDate> getChargingDates(LocalDate startDate, LocalDate dueDate, Type toolType) {
        List<LocalDate> chargingDates = new ArrayList<>();
        List<LocalDate> inspectedDates = new ArrayList<>();

        // Log rental period and tool charging configuration
        log.info("=".repeat(80));
        log.info("Starting charge calculation for tool rental");
        log.info("Tool Type: {}", toolType.getName());
        log.info("Daily Charge: ${}", toolType.getDailyCharge());
        log.info("Charging Flags - Weekday: {}, Weekend: {}, Holiday: {}",
                toolType.isWeekdayCharge(),
                toolType.isWeekendCharge(),
                toolType.isHolidayCharge());
        log.info("Rental Period: {} to {} (inclusive)", startDate, dueDate);
        log.info("=".repeat(80));

        // Iterate through each day in the rental period (both dates inclusive)
        LocalDate currentDate = startDate;
        int dayNumber = 1;

        while (!currentDate.isAfter(dueDate)) {
            inspectedDates.add(currentDate);

            boolean isHoliday = ChargingDateUtil.isHoliday(currentDate);
            boolean isWeekend = ChargingDateUtil.isWeekend(currentDate);
            boolean shouldCharge = shouldChargeForDate(currentDate, toolType);

            // Determine date classification for logging
            String classification;
            if (isHoliday) {
                String holidayName = ChargingDateUtil.isIndependenceDay(currentDate)
                        ? "Independence Day"
                        : "Labor Day";
                classification = String.format("Holiday (%s)", holidayName);
            } else if (isWeekend) {
                classification = String.format("Weekend (%s)", currentDate.getDayOfWeek());
            } else {
                classification = String.format("Weekday (%s)", currentDate.getDayOfWeek());
            }

            // Log each day inspection
            if (shouldCharge) {
                log.info("Day {}: {} | {} | Classification: {} | CHARGED ✓",
                        dayNumber, currentDate, currentDate.getDayOfWeek(), classification);
                chargingDates.add(currentDate);
            } else {
                log.info("Day {}: {} | {} | Classification: {} | NOT CHARGED ✗",
                        dayNumber, currentDate, currentDate.getDayOfWeek(), classification);
            }

            currentDate = currentDate.plusDays(1);
            dayNumber++;
        }

        // Log summary
        log.info("=".repeat(80));
        log.info("Charge Calculation Summary");
        log.info("Total Days Inspected: {}", inspectedDates.size());
        log.info("Total Charge Days: {}", chargingDates.size());
        log.info("Total Non-Charge Days: {}", inspectedDates.size() - chargingDates.size());
        log.info("=".repeat(80));
        return chargingDates;
    }

    /**
     * Determines if a specific date should be charged based on tool type rules
     * Uses ChargingDateUtil for holiday and weekend detection
     *
     * @param date     the date to check
     * @param toolType the type of tool
     * @return true if this date should be charged
     */
    public boolean shouldChargeForDate(LocalDate date, Type toolType) {
        boolean isHoliday = ChargingDateUtil.isHoliday(date);
        boolean isWeekend = ChargingDateUtil.isWeekend(date);

        // Priority: Holiday check first, then weekend/weekday
        if (isHoliday) {
            return toolType.isHolidayCharge();
        } else if (isWeekend) {
            return toolType.isWeekendCharge();
        } else {
            return toolType.isWeekdayCharge();
        }
    }

}