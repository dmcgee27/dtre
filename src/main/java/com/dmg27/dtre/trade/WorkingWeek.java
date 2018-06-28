/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.trade;

import com.dmg27.dtre.core.DtreException;
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
final public class WorkingWeek {
    
    /**
     * The key for the default working week.
     */
    private static final String DEFAULT_CURRENCY = "default";
    
    /**
     * Collection of working weeks keyed on currency.
     */
    private Map<String, List<DayOfWeek>> workingWeeks;
    
    /**
     * Create an instance of the {@link WorkingWeek} class.
     * <p>
     * Note populates the working weeks with a default working week and
     * with working weeks for currencies AED and SAR.
     * </p>
     */
    public WorkingWeek() {
        List<DayOfWeek> mondayToFriday = Arrays.asList(
            new DayOfWeek [] {
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY
            }
        );
        this.workingWeeks = new HashMap<>();
        this.workingWeeks.put(DEFAULT_CURRENCY, mondayToFriday);
    }
        
    /**
     * Create an instance of the {@link WorkingWeek} class.
     * @param workingWeeks The working weeks map keyed on currency.
     * @throws DtreException When working weeks map is null or empty.
     */
    public WorkingWeek(Map<String, List<DayOfWeek>> workingWeeks) {
        this();
        if (workingWeeks.isEmpty()) {
            throw new DtreException("empty currency to work weeks map in WorkingWeek constructor");
        }
        
        this.workingWeeks.putAll(workingWeeks);
    }
    
    /**
     * Get the date of the working day for the currency code and the input date, adjusting
     * for the next working day when the date is not a working day.
     * @param currencyCode The currency code.
     * @param date the input date
     * @return The date of the next working day, or the input date if it is the date of a working day.
     */
    public LocalDate getWorkingDate(String currencyCode, LocalDate date) {
         List<DayOfWeek> workingWeek = this.getWorkingWeek(currencyCode);
         DayOfWeek dayOfWeek = DayOfWeek.from(date);
         if (workingWeek.contains(dayOfWeek)) {
             return date;
         }
         
         dayOfWeek = this.getFirstWorkingDayOfWeek(currencyCode);
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
    List<DayOfWeek> getWorkingWeek(String currencyCode) {
        Util.validateCurrencyCode(currencyCode);
        List<DayOfWeek> workingWeek = this.workingWeeks.get(currencyCode);
        return workingWeek == null ? this.workingWeeks.get(DEFAULT_CURRENCY) : workingWeek;
    }
    
    /**
     * Get the next working day for the currency code.
     * @param currencyCode The currency code.
     * @return The next working day.
     */
    DayOfWeek getFirstWorkingDayOfWeek(String currencyCode) {
        Util.validateCurrencyCode(currencyCode);
        return this.getWorkingWeek(currencyCode).get(0);
    }
}
