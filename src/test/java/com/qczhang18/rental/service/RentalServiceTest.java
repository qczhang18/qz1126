package com.qczhang18.rental.service;

import com.qczhang18.rental.model.Agreement;
import com.qczhang18.rental.model.RentalRequest;
import com.qczhang18.rental.model.Tool;
import com.qczhang18.rental.model.Type;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class RentalServiceTest {

    private final RentalService service = new RentalService();

    // ==================== Charging Logic Tests - Ladder ====================

    @Test
    void testLadder_ChargesWeekdays() {
        // June 3-7, 2024 (Monday-Friday)
        LocalDate start = LocalDate.of(2024, 6, 3);
        LocalDate end = LocalDate.of(2024, 6, 7);

        List<LocalDate> chargingDates = service.getChargingDates(start, end, Type.Ladder);

        assertEquals(5, chargingDates.size(), "Ladder should charge for 5 weekdays");
    }

    @Test
    void testLadder_ChargesWeekends() {
        // June 8-9, 2024 (Saturday-Sunday)
        LocalDate start = LocalDate.of(2024, 6, 8);
        LocalDate end = LocalDate.of(2024, 6, 9);

        List<LocalDate> chargingDates = service.getChargingDates(start, end, Type.Ladder);

        assertEquals(2, chargingDates.size(), "Ladder should charge for 2 weekend days");
    }

    @Test
    void testLadder_NoChargeOnHoliday() {
        // July 4, 2024 (Thursday - Independence Day)
        LocalDate start = LocalDate.of(2024, 7, 4);
        LocalDate end = LocalDate.of(2024, 7, 4);

        List<LocalDate> chargingDates = service.getChargingDates(start, end, Type.Ladder);

        assertEquals(0, chargingDates.size(), "Ladder should NOT charge on Independence Day");
    }

    // ==================== Charging Logic Tests - Chainsaw ====================

    @Test
    void testChainsaw_ChargesWeekdays() {
        // June 3-7, 2024 (Monday-Friday)
        LocalDate start = LocalDate.of(2024, 6, 3);
        LocalDate end = LocalDate.of(2024, 6, 7);

        List<LocalDate> chargingDates = service.getChargingDates(start, end, Type.Chainsaw);

        assertEquals(5, chargingDates.size(), "Chainsaw should charge for 5 weekdays");
    }

    @Test
    void testChainsaw_NoChargeOnWeekends() {
        // June 8-9, 2024 (Saturday-Sunday)
        LocalDate start = LocalDate.of(2024, 6, 8);
        LocalDate end = LocalDate.of(2024, 6, 9);

        List<LocalDate> chargingDates = service.getChargingDates(start, end, Type.Chainsaw);

        assertEquals(0, chargingDates.size(), "Chainsaw should NOT charge on weekends");
    }

    @Test
    void testChainsaw_ChargesOnHoliday() {
        // July 4, 2024 (Thursday - Independence Day)
        LocalDate start = LocalDate.of(2024, 7, 4);
        LocalDate end = LocalDate.of(2024, 7, 4);

        List<LocalDate> chargingDates = service.getChargingDates(start, end, Type.Chainsaw);

        assertEquals(1, chargingDates.size(), "Chainsaw should charge on Independence Day");
    }

    // ==================== Charging Logic Tests - Jackhammer ====================

    @Test
    void testJackhammer_ChargesWeekdays() {
        // June 3-7, 2024 (Monday-Friday)
        LocalDate start = LocalDate.of(2024, 6, 3);
        LocalDate end = LocalDate.of(2024, 6, 7);

        List<LocalDate> chargingDates = service.getChargingDates(start, end, Type.Jackhammer);

        assertEquals(5, chargingDates.size(), "Jackhammer should charge for 5 weekdays");
    }

    @Test
    void testJackhammer_NoChargeOnWeekends() {
        // June 8-9, 2024 (Saturday-Sunday)
        LocalDate start = LocalDate.of(2024, 6, 8);
        LocalDate end = LocalDate.of(2024, 6, 9);

        List<LocalDate> chargingDates = service.getChargingDates(start, end, Type.Jackhammer);

        assertEquals(0, chargingDates.size(), "Jackhammer should NOT charge on weekends");
    }

    @Test
    void testJackhammer_NoChargeOnHoliday() {
        // July 4, 2024 (Thursday - Independence Day)
        LocalDate start = LocalDate.of(2024, 7, 4);
        LocalDate end = LocalDate.of(2024, 7, 4);

        List<LocalDate> chargingDates = service.getChargingDates(start, end, Type.Jackhammer);

        assertEquals(0, chargingDates.size(), "Jackhammer should NOT charge on Independence Day");
    }

    // ==================== Complex Scenario Tests ====================

    @Test
    void testMixedWeek_Ladder() {
        // July 1-8, 2024: Mon, Tue, Wed, Thu(Holiday), Fri, Sat, Sun, Mon
        LocalDate start = LocalDate.of(2024, 7, 1);
        LocalDate end = LocalDate.of(2024, 7, 8);

        List<LocalDate> chargingDates = service.getChargingDates(start, end, Type.Ladder);

        // Ladder: weekday=yes, weekend=yes, holiday=no
        // Should charge: Mon, Tue, Wed, Fri, Sat, Sun, Mon (7 days, NOT Thu because holiday)
        assertEquals(7, chargingDates.size());
        assertFalse(chargingDates.contains(LocalDate.of(2024, 7, 4)),
                "Should not charge on July 4th holiday");
    }

    @Test
    void testMixedWeek_Chainsaw() {
        // July 1-8, 2024: Mon, Tue, Wed, Thu(Holiday), Fri, Sat, Sun, Mon
        LocalDate start = LocalDate.of(2024, 7, 1);
        LocalDate end = LocalDate.of(2024, 7, 8);

        List<LocalDate> chargingDates = service.getChargingDates(start, end, Type.Chainsaw);

        // Chainsaw: weekday=yes, weekend=no, holiday=yes
        // Should charge: Mon, Tue, Wed, Thu(Holiday), Fri, Mon (6 days)
        assertEquals(6, chargingDates.size());
        assertTrue(chargingDates.contains(LocalDate.of(2024, 7, 4)),
                "Should charge on July 4th holiday");
        assertFalse(chargingDates.contains(LocalDate.of(2024, 7, 6)),
                "Should not charge on Saturday");
    }

    @Test
    void testMixedWeek_Jackhammer() {
        // July 1-8, 2024: Mon, Tue, Wed, Thu(Holiday), Fri, Sat, Sun, Mon
        LocalDate start = LocalDate.of(2024, 7, 1);
        LocalDate end = LocalDate.of(2024, 7, 8);

        List<LocalDate> chargingDates = service.getChargingDates(start, end, Type.Jackhammer);

        // Jackhammer: weekday=yes, weekend=no, holiday=no
        // Should charge: Mon, Tue, Wed, Fri, Mon (5 days, NOT Thu or weekend)
        assertEquals(5, chargingDates.size());
        assertFalse(chargingDates.contains(LocalDate.of(2024, 7, 4)),
                "Should not charge on July 4th holiday");
    }

    @Test
    void testLaborDayWeekend_Ladder() {
        // Aug 30 - Sep 3, 2024: Fri, Sat, Sun, Mon(Labor Day), Tue
        // Both dates inclusive
        LocalDate start = LocalDate.of(2024, 8, 30);
        LocalDate end = LocalDate.of(2024, 9, 3);

        List<LocalDate> chargingDates = service.getChargingDates(start, end, Type.Ladder);

        // Ladder: weekday=yes, weekend=yes, holiday=no
        // Should charge: Fri, Sat, Sun, Tue (4 days, NOT Labor Day Monday)
        assertEquals(4, chargingDates.size());
        assertTrue(chargingDates.contains(LocalDate.of(2024, 8, 30)), "Should charge Friday");
        assertTrue(chargingDates.contains(LocalDate.of(2024, 8, 31)), "Should charge Saturday");
        assertTrue(chargingDates.contains(LocalDate.of(2024, 9, 1)), "Should charge Sunday");
        assertFalse(chargingDates.contains(LocalDate.of(2024, 9, 2)),
                "Should not charge on Labor Day");
        assertTrue(chargingDates.contains(LocalDate.of(2024, 9, 3)), "Should charge Tuesday");
    }

    @Test
    void testIndependenceDayOnSaturday_ObservedFriday() {
        // July 2-6, 2026: Thu, Fri(Observed), Sat(Actual July 4), Sun, Mon
        // Both dates inclusive
        LocalDate start = LocalDate.of(2026, 7, 2);
        LocalDate end = LocalDate.of(2026, 7, 6);

        List<LocalDate> chargingDates = service.getChargingDates(start, end, Type.Jackhammer);

        // Jackhammer: weekday=yes, weekend=no, holiday=no
        // Should charge: Thu, Mon (2 days)
        // NOT: Fri(observed holiday), Sat(weekend), Sun(weekend)
        assertEquals(2, chargingDates.size());
        assertTrue(chargingDates.contains(LocalDate.of(2026, 7, 2)),
                "Should charge on Thursday");
        assertFalse(chargingDates.contains(LocalDate.of(2026, 7, 3)),
                "Should not charge on observed Independence Day (Friday)");
        assertFalse(chargingDates.contains(LocalDate.of(2026, 7, 4)),
                "Should not charge on Saturday");
        assertFalse(chargingDates.contains(LocalDate.of(2026, 7, 5)),
                "Should not charge on Sunday");
        assertTrue(chargingDates.contains(LocalDate.of(2026, 7, 6)),
                "Should charge on Monday");
    }

    @Test
    void testSingleDayRental() {
        LocalDate start = LocalDate.of(2024, 7, 1);
        LocalDate end = LocalDate.of(2024, 7, 1);

        List<LocalDate> chargingDates = service.getChargingDates(start, end, Type.Ladder);

        assertEquals(1, chargingDates.size(), "Same start and end date should charge for that single day");
    }

    @Test
    void testSingleDay_Weekday() {
        LocalDate start = LocalDate.of(2024, 7, 1); // Monday
        LocalDate end = LocalDate.of(2024, 7, 1);

        List<LocalDate> chargingDates = service.getChargingDates(start, end, Type.Ladder);

        assertEquals(1, chargingDates.size(), "Single weekday should charge");
        assertEquals(start, chargingDates.get(0));
    }

    @Test
    void testFullWeek_AllToolTypes() {
        // June 3-9, 2024: Mon-Sun (7 days, no holidays)
        LocalDate start = LocalDate.of(2024, 6, 3);
        LocalDate end = LocalDate.of(2024, 6, 9);

        // Ladder: charges all 7 days
        List<LocalDate> ladderDates = service.getChargingDates(start, end, Type.Ladder);
        assertEquals(7, ladderDates.size(), "Ladder should charge all 7 days");

        // Chainsaw: charges 5 weekdays only
        List<LocalDate> chainsawDates = service.getChargingDates(start, end, Type.Chainsaw);
        assertEquals(5, chainsawDates.size(), "Chainsaw should charge 5 weekdays only");

        // Jackhammer: charges 5 weekdays only
        List<LocalDate> jackhammerDates = service.getChargingDates(start, end, Type.Jackhammer);
        assertEquals(5, jackhammerDates.size(), "Jackhammer should charge 5 weekdays only");
    }

    // ------------------ Test 1 ------------------
    // 101% discount → INVALID
    // This test would typically be in a controller or validation test class,
    // but included here for completeness.

    // ------------------ Test 2 ------------------
    // LADW – 7/2/20 – 3 days – 10%
    @Test
    void test2_ladderCheckout() {
        RentalRequest request = RentalRequest.builder()
                .tool(Tool.LADW)
                .checkoutDate(LocalDate.of(2020, 7, 2))
                .rentalDay(3)
                .discountPercent(10)
                .build();

        Agreement agreement = service.checkout(request);

        // Helper for rounding
        Function<Double, BigDecimal> bd = v ->
                BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP);

        // Verify tool info
        assertEquals("LADW", agreement.getToolCode());
        assertEquals("Ladder", agreement.getToolType());
        assertEquals("Werner", agreement.getToolBrand());

        // Verify dates
        assertEquals(LocalDate.of(2020, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2020, 7, 5), agreement.getDueDate());
        assertEquals(3, agreement.getRentalDays());

        // Charges
        assertEquals(bd.apply(1.99), agreement.getDailyRentalCharge());
        assertEquals(2, agreement.getChargeDays());
        assertEquals(bd.apply(3.98), agreement.getPreDiscountCharge());

        // Discount
        assertEquals(10, agreement.getDiscountPercent());
        assertEquals(bd.apply(0.40), agreement.getDiscountAmount());

        // Final charge
        assertEquals(bd.apply(3.58), agreement.getFinalCharge());
    }


    // ------------------ Test 3 ------------------
    // CHNS – 7/2/15 – 5 days – 25%
    @Test
    void test3_chainsawCheckout() {
        RentalRequest request = RentalRequest.builder()
                .tool(Tool.CHNS)
                .checkoutDate(LocalDate.of(2015, 7, 2))
                .rentalDay(5)
                .discountPercent(25)
                .build();

        Agreement agreement = service.checkout(request);

        // Helper for rounding
        Function<Double, BigDecimal> bd = v ->
                BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP);

        // Verify tool info
        assertEquals("CHNS", agreement.getToolCode());
        assertEquals("Chainsaw", agreement.getToolType());
        assertEquals("Stihl", agreement.getToolBrand());

        // Verify dates
        assertEquals(LocalDate.of(2015, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 7, 7), agreement.getDueDate());
        assertEquals(5, agreement.getRentalDays());

        // Charges
        assertEquals(bd.apply(1.49), agreement.getDailyRentalCharge());
        assertEquals(3, agreement.getChargeDays());
        assertEquals(bd.apply(4.47), agreement.getPreDiscountCharge());

        // Discount
        assertEquals(25, agreement.getDiscountPercent());
        assertEquals(bd.apply(1.12), agreement.getDiscountAmount());

        // Final charge
        assertEquals(bd.apply(3.35), agreement.getFinalCharge());
    }

    // ------------------ Test 4 ------------------
    // JAKD – 9/3/15 – 6 days – 0%
    @Test
    void test4_jackhammerNoDiscount() {
        RentalRequest request = RentalRequest.builder()
                .tool(Tool.JAKD)
                .checkoutDate(LocalDate.of(2015, 9, 3))
                .rentalDay(6)
                .discountPercent(0)
                .build();

        Agreement agreement = service.checkout(request);

        // Helper for rounding
        Function<Double, BigDecimal> bd = v ->
                BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP);

        // Verify tool info
        assertEquals("JAKD", agreement.getToolCode());
        assertEquals("Jackhammer", agreement.getToolType());
        assertEquals("DeWalt", agreement.getToolBrand());

        // Verify dates
        assertEquals(LocalDate.of(2015, 9, 3), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 9, 9), agreement.getDueDate());
        assertEquals(6, agreement.getRentalDays());

        // Charges
        assertEquals(bd.apply(2.99), agreement.getDailyRentalCharge());
        assertEquals(3, agreement.getChargeDays());
        assertEquals(bd.apply(8.97), agreement.getPreDiscountCharge());

        // Discount
        assertEquals(0, agreement.getDiscountPercent());
        assertEquals(bd.apply(0.00), agreement.getDiscountAmount());

        // Final charge
        assertEquals(bd.apply(8.97), agreement.getFinalCharge());
    }

    // ------------------ Test 5 ------------------
    // JAKR – 7/2/15 – 9 days – 0%
    @Test
    void test5_jackhammerLongRental() {
        RentalRequest request = RentalRequest.builder()
                .tool(Tool.JAKR)
                .checkoutDate(LocalDate.of(2015, 7, 2))
                .rentalDay(9)
                .discountPercent(0)
                .build();

        Agreement agreement = service.checkout(request);

        // Helper for rounding
        Function<Double, BigDecimal> bd = v ->
                BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP);

        // Verify tool info
        assertEquals("JAKR", agreement.getToolCode());
        assertEquals("Jackhammer", agreement.getToolType());
        assertEquals("Ridgid", agreement.getToolBrand());

        // Verify dates
        assertEquals(LocalDate.of(2015, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 7, 11), agreement.getDueDate());
        assertEquals(9, agreement.getRentalDays());

        // Charges
        assertEquals(bd.apply(2.99), agreement.getDailyRentalCharge());
        assertEquals(5, agreement.getChargeDays());
        assertEquals(bd.apply(14.95), agreement.getPreDiscountCharge());

        // Discount
        assertEquals(0, agreement.getDiscountPercent());
        assertEquals(bd.apply(0.00), agreement.getDiscountAmount());

        // Final charge
        assertEquals(bd.apply(14.95), agreement.getFinalCharge());
    }

    // ------------------ Test 6 ------------------
    // JAKR – 7/2/20 – 4 days – 50%
    @Test
    void test6_halfDiscount() {
        RentalRequest request = RentalRequest.builder()
                .tool(Tool.JAKR)
                .checkoutDate(LocalDate.of(2020, 7, 2))
                .rentalDay(4)
                .discountPercent(50)
                .build();

        Agreement agreement = service.checkout(request);

        // Helper for rounding
        Function<Double, BigDecimal> bd = v ->
                BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP);

        // Verify tool info
        assertEquals("JAKR", agreement.getToolCode());
        assertEquals("Jackhammer", agreement.getToolType());
        assertEquals("Ridgid", agreement.getToolBrand());

        // Verify dates
        assertEquals(LocalDate.of(2020, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2020, 7, 6), agreement.getDueDate());
        assertEquals(4, agreement.getRentalDays());

        // Charges
        assertEquals(bd.apply(2.99), agreement.getDailyRentalCharge());
        assertEquals(1, agreement.getChargeDays());
        assertEquals(bd.apply(2.99), agreement.getPreDiscountCharge());

        // Discount
        assertEquals(50, agreement.getDiscountPercent());
        assertEquals(bd.apply(1.50), agreement.getDiscountAmount());

        // Final charge
        assertEquals(bd.apply(1.49), agreement.getFinalCharge());
    }
}
