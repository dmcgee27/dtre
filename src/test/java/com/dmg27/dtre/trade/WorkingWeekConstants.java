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
    
    static final Clock CLOCK = Clock.fixed(Instant.parse("2016-02-01T00:00:00Z"), ZoneOffset.UTC);
}
