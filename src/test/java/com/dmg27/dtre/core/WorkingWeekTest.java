/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.core;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link WorkingWeek}.
 * @author douglasmcgee
 */
public class WorkingWeekTest {
    
    private static final String CURRENCY_CODE_AED = "AED";
    private static final String CURRENCY_CODE_GBP = "GBP";
    private static final String CURRENCY_CODE_SAR = "SAR";
    
    private static final List<DayOfWeek> SUNDAY_TO_THURSDAY = Arrays.asList(
        new DayOfWeek [] {
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY
        }
    );
    
    private static final Map<String, List<DayOfWeek>> DEFAULT_CURRENCY_TO_WORKING_WEEK_MAP = new HashMap<>();
    static {
        DEFAULT_CURRENCY_TO_WORKING_WEEK_MAP.put(CURRENCY_CODE_AED, SUNDAY_TO_THURSDAY);
        DEFAULT_CURRENCY_TO_WORKING_WEEK_MAP.put(CURRENCY_CODE_SAR, SUNDAY_TO_THURSDAY);
    };
    
    @Test
    public void mondayToFridayGetWorkingWeekTest() {
        List<Currency> exceptionCurrencies = Arrays.asList(
            new Currency[] {
                Currency.getInstance(CURRENCY_CODE_AED),
                Currency.getInstance(CURRENCY_CODE_SAR)
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
            
            List<DayOfWeek> workingWeek = this.createDefaultWorkingWeek().getWorkingWeek(currency.getCurrencyCode());
            this.assertWorkingWeek(expectedMondayToFriday, workingWeek);
        }
    }
    
    @Test
    public void sundayToThusdayGetWorkingWeekTest() {
        List<Currency> currencies = Arrays.asList(
            new Currency[] {
                Currency.getInstance(CURRENCY_CODE_AED),
                Currency.getInstance(CURRENCY_CODE_SAR)
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
            List<DayOfWeek> workingWeek = this.createDefaultWorkingWeek().getWorkingWeek(currency.getCurrencyCode());
            this.assertWorkingWeek(expectedSundayToThursday, workingWeek);
        }
    }
    
    @Test
    public void mondayToFridayGetFirstWorkingDayOfWeekTest() {
        assertEquals(DayOfWeek.MONDAY, this.createDefaultWorkingWeek().getFirstWorkingDayOfWeek(CURRENCY_CODE_GBP));
    }
    
    @Test
    public void sundayToThursdayGetFirstWorkingDayOfWeekTest() {
        assertEquals(DayOfWeek.SUNDAY, this.createDefaultWorkingWeek().getFirstWorkingDayOfWeek(CURRENCY_CODE_AED));
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
        
        this.assertNextWorkingDate(CURRENCY_CODE_GBP, expectedWorkingDates, workingDates, expectedNextWorkingDates, nonWorkingDates);
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
        
        this.assertNextWorkingDate(CURRENCY_CODE_AED, expectedWorkingDates, workingDates, expectedNextWorkingDates, nonWorkingDates);
    }
    
    @Test (expected = DtreException.class)
    public void emptyCurrencyToWorkingWeekMapTest() {
        new WorkingWeek(new HashMap<String, List<DayOfWeek>>());
    }
    
    @Test (expected = DtreException.class)
    public void bogusCurrencyCodeGetNextWorkingDate() {
        this.createDefaultWorkingWeek().getWorkingDate("BOGUS", LocalDate.parse("2018-06-27"));
    }
    
    @Test (expected = DtreException.class)
    public void bogusCurrencyCodeGetWorkingWeek() {
        this.createDefaultWorkingWeek().getWorkingWeek("BOGUS");
    }
    
    @Test (expected = DtreException.class)
    public void bogusCurrencyCodeGetFirstWorkingDayOfWeek() {
        this.createDefaultWorkingWeek().getFirstWorkingDayOfWeek("BOGUS");
    }
    
    /**
     * Create the default {@link WorkingWeek} instance.
     * @return The {@link WorkingWeek} instance.
     */
    private WorkingWeek createDefaultWorkingWeek() {
        return new WorkingWeek(DEFAULT_CURRENCY_TO_WORKING_WEEK_MAP);
    }
    
    /**
     * Assert that the working week matches the expected working week.
     * @param expectedWorkingWeekList the expected working week.
     * @param workingWeekList the working week.
     */
    private void assertWorkingWeek(List<DayOfWeek> expectedWorkingWeekList, List<DayOfWeek> workingWeekList) {
        for (int i = 0; i < expectedWorkingWeekList.size(); i++) {
            assertEquals(expectedWorkingWeekList.get(i), workingWeekList.get(i));
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
            assertEquals(expectedWorkingDates.get(i), this.createDefaultWorkingWeek().getWorkingDate(currencyCode, workingDates.get(i)));
        }
        
        for (int i = 0; i < nonWorkingDates.size(); i++) {
            assertEquals(expectedNextWorkingDates.get(i), this.createDefaultWorkingWeek().getWorkingDate(currencyCode, nonWorkingDates.get(i)));
        }
    }
}
