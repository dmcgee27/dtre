/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.trade;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Constants supporting used of the {@link WorkingWeek} class.
 * @author douglasmcgee
 */
final public class WorkingWeekConstants {
    static final String CURRENCY_CODE_AED = "AED";
    static final String CURRENCY_CODE_ALL = "ALL";
    static final String CURRENCY_CODE_GBP = "GBP";
    static final String CURRENCY_CODE_SAR = "SAR";
    static final String CURRENCY_CODE_SGD = "SGD";
    
    static final List<DayOfWeek> MONDAY_TO_FRIDAY = Arrays.asList(
        new DayOfWeek [] {
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        }
    );
    
    static final List<DayOfWeek> SUNDAY_TO_THURSDAY = Arrays.asList(
        new DayOfWeek [] {
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY
        }
    );
    
    static final Map<String, List<DayOfWeek>> DEFAULT_CURRENCY_TO_WORKING_WEEK_MAP = new HashMap<>();
    static {
        DEFAULT_CURRENCY_TO_WORKING_WEEK_MAP.put(CURRENCY_CODE_AED, SUNDAY_TO_THURSDAY);
        DEFAULT_CURRENCY_TO_WORKING_WEEK_MAP.put(CURRENCY_CODE_SAR, SUNDAY_TO_THURSDAY);
    };
    
    static final WorkingWeek DEFAULT_WORKING_WEEK = new WorkingWeek(DEFAULT_CURRENCY_TO_WORKING_WEEK_MAP);
    
    static final Clock CLOCK_MON_01_FEB_2016 = Clock.fixed(Instant.parse("2016-02-01T00:00:00Z"), ZoneOffset.UTC);
    
    static final String SAT_02_JAN_2016 = "2016-01-02";
    static final String SAT_02_JAN_2016_JPM = "02 Jan 2016";
    
    static final String SUN_03_JAN_2016 = "2016-01-03"; 
    static final String MON_04_JAN_2016 = "2016-01-04"; 
    static final String TUE_05_JAN_2016 = "2016-01-05"; 
    static final String WED_06_JAN_2016 = "2016-01-06"; 
    static final String THU_07_JAN_2016 = "2016-01-07"; 
    static final String THU_07_JAN_2016_JPM = "07 Jan 2016"; 
    static final String FRI_08_JAN_2016 = "2016-01-08"; 
    static final String SAT_09_JAN_2016 = "2016-01-09"; 
    
    static final String SAT_06_FEB_2016 = "2016-02-06";
    static final String SAT_06_FEB_2016_JPM = "06 Feb 2016";
    static final String MON_08_FEB_2016 = "2016-02-08";
    static final String SAT_05_MAR_2016 = "2016-03-05";
    static final String SAT_05_MAR_2016_JPM = "05 Mar 2016";
    static final String MON_07_MAR_2016 = "2016-03-07";
}
    
