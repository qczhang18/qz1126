package com.qczhang18.rental.util;

import com.qczhang18.rental.model.Type;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for calculating rental charging dates based on tool type and date rules.
 * Handles weekday, weekend, and holiday charging logic.
 */
public class ChargingDateUtil {

    /**
     * Checks if a given date is a recognized holiday
     * @param date the date to check
     * @return true if the date is a holiday
     */
    public static boolean isHoliday(LocalDate date) {
        return isIndependenceDay(date) || isLaborDay(date);
    }

    /**
     * Checks if a given date is Independence Day (July 4th) or its observed date
     * @param date the date to check
     * @return true if the date is Independence Day or its observed date
     */
    public static boolean isIndependenceDay(LocalDate date) {
        if (date.getMonth() != Month.JULY) {
            return false;
        }

        int day = date.getDayOfMonth();
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        // If July 4th falls on a weekday, it's observed on that day
        if (day == 4 && dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
            return true;
        }

        // If July 4th is Saturday, observed on Friday (July 3rd)
        if (day == 3 && dayOfWeek == DayOfWeek.FRIDAY) {
            LocalDate july4th = date.plusDays(1);
            if (july4th.getDayOfWeek() == DayOfWeek.SATURDAY) {
                return true;
            }
        }

        // If July 4th is Sunday, observed on Monday (July 5th)
        if (day == 5 && dayOfWeek == DayOfWeek.MONDAY) {
            LocalDate july4th = date.minusDays(1);
            if (july4th.getDayOfWeek() == DayOfWeek.SUNDAY) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a given date is Labor Day (first Monday in September)
     * @param date the date to check
     * @return true if the date is Labor Day
     */
    public static boolean isLaborDay(LocalDate date) {
        if (date.getMonth() != Month.SEPTEMBER) {
            return false;
        }

        if (date.getDayOfWeek() != DayOfWeek.MONDAY) {
            return false;
        }

        // First Monday in September
        LocalDate firstMondayInSeptember = LocalDate.of(date.getYear(), Month.SEPTEMBER, 1)
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));

        return date.equals(firstMondayInSeptember);
    }

    /**
     * Checks if a given date is a weekend (Saturday or Sunday)
     * @param date the date to check
     * @return true if the date is a weekend
     */
    public static boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

}