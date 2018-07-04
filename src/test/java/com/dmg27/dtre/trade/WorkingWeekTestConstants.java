/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.trade;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

/**
 * Constants supporting used of the {@link WorkingWeek} class.
 * @author douglasmcgee
 */
final public class WorkingWeekTestConstants {
    static final String CURRENCY_CODE_ALL = "ALL";
    static final String CURRENCY_CODE_GBP = "GBP";
    static final String CURRENCY_CODE_SGD = "SGD";
    
    static final Clock CLOCK_MON_01_FEB_2016 = Clock.fixed(Instant.parse("2016-02-01T00:00:00Z"), ZoneOffset.UTC);
    
    public static final String SAT_02_JAN_2016 = "2016-01-02";
    public static final String SAT_02_JAN_2016_JPM = "02 Jan 2016";
    
    public static final String SUN_03_JAN_2016 = "2016-01-03"; 
    public static final String MON_04_JAN_2016 = "2016-01-04"; 
    public static final String TUE_05_JAN_2016 = "2016-01-05"; 
    public static final String WED_06_JAN_2016 = "2016-01-06"; 
    public static final String THU_07_JAN_2016 = "2016-01-07"; 
    public static final String THU_07_JAN_2016_JPM = "07 Jan 2016"; 
    public static final String FRI_08_JAN_2016 = "2016-01-08"; 
    public static final String SAT_09_JAN_2016 = "2016-01-09"; 
     
    public static final String SAT_06_FEB_2016 = "2016-02-06";
    public static final String SAT_06_FEB_2016_JPM = "06 Feb 2016";
    public static final String MON_08_FEB_2016 = "2016-02-08";
    public static final String SAT_05_MAR_2016 = "2016-03-05";
    public static final String SAT_05_MAR_2016_JPM = "05 Mar 2016";
    public static final String MON_07_MAR_2016 = "2016-03-07";
}
    
