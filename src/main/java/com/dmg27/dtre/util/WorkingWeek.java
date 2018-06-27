/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.util;

import com.dmg27.dtre.util.Util;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the relationship between currency and working weeks.
 * @author douglasmcgee
 */
public class WorkingWeek {
    
    /**
     * Working week Sunday to Thursday.
     */
    private static final List<DayOfWeek> mondayToFriday = Arrays.asList(
        new DayOfWeek [] {
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        }
    );
    
    /**
     * Working week Sunday to Thursday.
     */
    private static final List<DayOfWeek> sundayToThursday = Arrays.asList(
        new DayOfWeek [] {
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY
        }
    );
    
    /**
     * Collection of working weeks keyed on currency.
     */
    private static final Map<String, List<DayOfWeek>> workingWeeks = new HashMap<>();
    static {
        workingWeeks.put("AED", sundayToThursday);
        workingWeeks.put("SAR", sundayToThursday);
    }
    
    /**
     * Get the date of the next working day for the currency code and the input date.
     * @param currencyCode The currency code.
     * @param date the input date
     * @return The date of the next working day, or the input date if it is the date of a working day.
     */
    static public LocalDate getNextWorkingDate(String currencyCode, LocalDate date) {
         List<DayOfWeek> workingWeek = getWorkingWeek(currencyCode);
         DayOfWeek dayOfWeek = DayOfWeek.from(date);
         if (workingWeek.contains(dayOfWeek)) {
             return date;
         }
         
         dayOfWeek = getFirstWorkingDayOfWeek(currencyCode);
         Temporal adjustedTemporal = dayOfWeek.adjustInto(date);
         LocalDate adjustedDate = LocalDate.from(adjustedTemporal);
         
         // Temopral week boundary on a Sunday will adjust the date to the begining of
         // the week so we need to compensate for that.
         
         // OK, the adjusted date is later in the week.
         if (date.isBefore(adjustedDate)) {
            return adjustedDate;
         }
         
         // The adjusted date is earlier in the week, so add a week and move it
         // into next week as required.
         adjustedTemporal = dayOfWeek.adjustInto(date.plusWeeks(1));
         adjustedDate = LocalDate.from(adjustedTemporal);
         return adjustedDate;
    }
    
    /**
     * Get the working week for the currency code.
     * @param currencyCode The currency code.
     * @return The working week.
     */
    static List<DayOfWeek> getWorkingWeek(String currencyCode) {
        Util.validateCurrencyCode(currencyCode);
        List<DayOfWeek> workingWeek = workingWeeks.get(currencyCode);
        return workingWeek == null ? mondayToFriday : workingWeek;
    }
    
    /**
     * Get the next working day for the currency code.
     * @param currencyCode The currency code.
     * @return The next working day.
     */
    static DayOfWeek getFirstWorkingDayOfWeek(String currencyCode) {
        Util.validateCurrencyCode(currencyCode);
        return getWorkingWeek(currencyCode).get(0);
    }
    
}
