/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link WorkingWeek}.
 * @author douglasmcgee
 */
public class WorkingWeekTest {
    @Test
    public void mondayToFridayGetWorkingWeekTest() {
        List<Currency> exceptionCurrencies = Arrays.asList(
            new Currency[] {
                Currency.getInstance("AED"),
                Currency.getInstance("SAR")
            }
        );
        
        List<DayOfWeek> expectedMondayToFriday = Arrays.asList(
            new DayOfWeek [] {
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY
            }
        );
        
        Set<Currency> allCurrencies = Currency.getAvailableCurrencies();
        for (Currency currency : allCurrencies) {
            if (exceptionCurrencies.contains(currency)) {
                continue;
            }
            
            List<DayOfWeek> workingWeek = WorkingWeek.getWorkingWeek(currency.getCurrencyCode());
            this.assertWorkingWeek(expectedMondayToFriday, workingWeek);
        }
    }
    
    @Test
    public void sundayToThusdayGetWorkingWeekTest() {
        List<Currency> currencies = Arrays.asList(
            new Currency[] {
                Currency.getInstance("AED"),
                Currency.getInstance("SAR")
            }
        );
        
        List<DayOfWeek> expectedSundayToThursday = Arrays.asList(
            new DayOfWeek [] {
                DayOfWeek.SUNDAY,
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY
            }
        );
        
        for (Currency currency : currencies) {
            List<DayOfWeek> workingWeek = WorkingWeek.getWorkingWeek(currency.getCurrencyCode());
            this.assertWorkingWeek(expectedSundayToThursday, workingWeek);
        }
    }
    
    @Test
    public void mondayToFridayGetFirstWorkingDayOfWeekTest() {
        assertEquals(DayOfWeek.MONDAY, WorkingWeek.getFirstWorkingDayOfWeek("GBP"));
    }
    
    @Test
    public void sundayToThursdayGetFirstWorkingDayOfWeekTest() {
        assertEquals(DayOfWeek.SUNDAY, WorkingWeek.getFirstWorkingDayOfWeek("AED"));
    }
    
    @Test
    public void mondayToFridayGetNextWorkingDateTest() {
        List<LocalDate> workingDates = Arrays.asList(
            new LocalDate[] {
                LocalDate.parse("2018-06-25"),
                LocalDate.parse("2018-06-26"),
                LocalDate.parse("2018-06-27"),
                LocalDate.parse("2018-06-28"),
                LocalDate.parse("2018-06-29")
            }
        );
        
        List<LocalDate> expectedWorkingDates = Arrays.asList(
            new LocalDate[] {
                LocalDate.parse("2018-06-25"),
                LocalDate.parse("2018-06-26"),
                LocalDate.parse("2018-06-27"),
                LocalDate.parse("2018-06-28"),
                LocalDate.parse("2018-06-29")
            }
        );
        
        List<LocalDate> nonWorkingDates = Arrays.asList(
            new LocalDate[] {
                LocalDate.parse("2018-06-30"),
                LocalDate.parse("2018-07-01")
            }
        );
        
        List<LocalDate> expectedNextWorkingDates = Arrays.asList(
            new LocalDate[] {
                LocalDate.parse("2018-07-02"),
                LocalDate.parse("2018-07-02")
            }
        );
        
        this.assertNextWorkingDate("GBP", expectedWorkingDates, workingDates, expectedNextWorkingDates, nonWorkingDates);
    }
    
    @Test
    public void sundayToThursdayGetNextWorkingDateTest() {
        List<LocalDate> workingDates = Arrays.asList(
            new LocalDate[] {
                LocalDate.parse("2018-06-24"),
                LocalDate.parse("2018-06-25"),
                LocalDate.parse("2018-06-26"),
                LocalDate.parse("2018-06-27"),
                LocalDate.parse("2018-06-28")
            }
        );
        
        List<LocalDate> expectedWorkingDates = Arrays.asList(
            new LocalDate[] {
                LocalDate.parse("2018-06-24"),
                LocalDate.parse("2018-06-25"),
                LocalDate.parse("2018-06-26"),
                LocalDate.parse("2018-06-27"),
                LocalDate.parse("2018-06-28")
            }
        );
        
        List<LocalDate> nonWorkingDates = Arrays.asList(
            new LocalDate[] {
                LocalDate.parse("2018-06-29"),
                LocalDate.parse("2018-06-30")
            }
        );
        
        List<LocalDate> expectedNextWorkingDates = Arrays.asList(
            new LocalDate[] {
                LocalDate.parse("2018-07-01"),
                LocalDate.parse("2018-07-01")
            }
        );
        
        this.assertNextWorkingDate("AED", expectedWorkingDates, workingDates, expectedNextWorkingDates, nonWorkingDates);
    }
    
    /**
     * Assert that the working week matches the expected working week.
     * @param expectedWorkingWeek the expected working week.
     * @param workingWeek the working week.
     */
    private void assertWorkingWeek(List<DayOfWeek> expectedWorkingWeek, List<DayOfWeek> workingWeek) {
        for (int i = 0; i < expectedWorkingWeek.size(); i++) {
            assertEquals(expectedWorkingWeek.get(i), workingWeek.get(i));
        }
    }
    
    /**
     * Assert that for a currency, the next working date matches the expected next working date.
     * @param currencyCode The currency code.
     * @param expectedWorkingDates The expected dates when the input date is a working date.
     * @param workingDates Input working dates.
     * @param expectedNextWorkingDates The expected next working dates when the date is a non working date.
     * @param nonWorkingDates Input non working dates.
     */
    private void assertNextWorkingDate(String currencyCode, 
            List<LocalDate> expectedWorkingDates,
            List<LocalDate> workingDates, 
            List<LocalDate> expectedNextWorkingDates,
            List<LocalDate> nonWorkingDates) {
        for (int i = 0; i < workingDates.size(); i++) {
            assertEquals(expectedWorkingDates.get(i), WorkingWeek.getNextWorkingDate(currencyCode, workingDates.get(i)));
        }
        
        for (int i = 0; i < nonWorkingDates.size(); i++) {
            assertEquals(expectedNextWorkingDates.get(i), WorkingWeek.getNextWorkingDate(currencyCode, nonWorkingDates.get(i)));
        }
    }
}
