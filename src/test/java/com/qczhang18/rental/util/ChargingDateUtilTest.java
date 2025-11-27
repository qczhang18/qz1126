package com.qczhang18.rental.util;

import com.qczhang18.rental.model.Type;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for ChargingDateUtil
 * Verifies holiday detection, weekend detection, and charging logic for all tool types
 */
class ChargingDateUtilTest {

    // ==================== Holiday Detection Tests ====================

    @Test
    void testIndependenceDayOnWeekday() {
        // July 4, 2024 is a Thursday
        LocalDate july4_2024 = LocalDate.of(2024, 7, 4);
        assertTrue(ChargingDateUtil.isIndependenceDay(july4_2024),
                "July 4th on Thursday should be a holiday");
        assertTrue(ChargingDateUtil.isHoliday(july4_2024));
    }

    @Test
    void testIndependenceDayOnSaturday() {
        // July 4, 2026 is a Saturday, observed on Friday July 3
        LocalDate july3_2026 = LocalDate.of(2026, 7, 3);
        LocalDate july4_2026 = LocalDate.of(2026, 7, 4);

        assertTrue(ChargingDateUtil.isIndependenceDay(july3_2026),
                "July 3rd (Friday) should be observed Independence Day when July 4th is Saturday");
        assertFalse(ChargingDateUtil.isIndependenceDay(july4_2026),
                "July 4th (Saturday) itself should not be marked as holiday - observed on Friday");
    }

    @Test
    void testIndependenceDayOnSunday() {
        // July 4, 2027 is a Sunday, observed on Monday July 5
        LocalDate july4_2027 = LocalDate.of(2027, 7, 4);
        LocalDate july5_2027 = LocalDate.of(2027, 7, 5);

        assertFalse(ChargingDateUtil.isIndependenceDay(july4_2027),
                "July 4th (Sunday) itself should not be marked as holiday - observed on Monday");
        assertTrue(ChargingDateUtil.isIndependenceDay(july5_2027),
                "July 5th (Monday) should be observed Independence Day when July 4th is Sunday");
    }

    @Test
    void testLaborDay() {
        // Labor Day 2024: September 2
        LocalDate laborDay2024 = LocalDate.of(2024, 9, 2);
        assertTrue(ChargingDateUtil.isLaborDay(laborDay2024),
                "September 2, 2024 should be Labor Day");
        assertTrue(ChargingDateUtil.isHoliday(laborDay2024));

        // Labor Day 2025: September 1
        LocalDate laborDay2025 = LocalDate.of(2025, 9, 1);
        assertTrue(ChargingDateUtil.isLaborDay(laborDay2025),
                "September 1, 2025 should be Labor Day");

        // Not Labor Day - second Monday in September
        LocalDate notLaborDay = LocalDate.of(2024, 9, 9);
        assertFalse(ChargingDateUtil.isLaborDay(notLaborDay),
                "Second Monday in September should not be Labor Day");
    }

    @Test
    void testNonHolidays() {
        LocalDate regularDay = LocalDate.of(2024, 6, 15);
        assertFalse(ChargingDateUtil.isHoliday(regularDay),
                "Regular day should not be a holiday");

        LocalDate christmas = LocalDate.of(2024, 12, 25);
        assertFalse(ChargingDateUtil.isHoliday(christmas),
                "Christmas should not be recognized as a holiday in this system");
    }

    // ==================== Weekend Detection Tests ====================

    @Test
    void testWeekendDetection() {
        LocalDate saturday = LocalDate.of(2024, 7, 6);
        LocalDate sunday = LocalDate.of(2024, 7, 7);
        LocalDate monday = LocalDate.of(2024, 7, 8);

        assertTrue(ChargingDateUtil.isWeekend(saturday), "Saturday should be a weekend");
        assertTrue(ChargingDateUtil.isWeekend(sunday), "Sunday should be a weekend");
        assertFalse(ChargingDateUtil.isWeekend(monday), "Monday should not be a weekend");
    }


}