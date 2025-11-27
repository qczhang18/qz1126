package com.qczhang18.rental.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Agreement {

    private String toolCode;
    private String toolType;
    private String toolBrand;
    private int rentalDays;
    @JsonFormat(pattern = "MM/dd/yy")
    private LocalDate checkoutDate;
    @JsonFormat(pattern = "MM/dd/yy")
    private LocalDate dueDate;
    private BigDecimal dailyRentalCharge;
    private int chargeDays;
    private BigDecimal preDiscountCharge;
    private int discountPercent;
    private BigDecimal discountAmount;
    private BigDecimal finalCharge;

    @Override
    public String toString() {
        return """
                Tool code: %s
                Tool type: %s
                Tool brand: %s
                Rental days: %d
                Checkout date: %s
                Due date: %s
                Daily rental charge: $%.2f
                Charge days: %d
                Pre-discount charge: $%.2f
                Discount percent: %d%%
                Discount amount: $%.2f
                Final charge: $%.2f
                """.formatted(
                toolCode,
                toolType,
                toolBrand,
                rentalDays,
                checkoutDate.format(DateTimeFormatter.ofPattern("MM/dd/yy")),
                dueDate.format(DateTimeFormatter.ofPattern("MM/dd/yy")),
                dailyRentalCharge,
                chargeDays,
                preDiscountCharge,
                discountPercent,
                discountAmount,
                finalCharge
        );

    }

}
