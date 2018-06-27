/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.util;

import com.dmg27.dtre.core.DtreException;
import java.util.Currency;
import java.util.Set;
import org.junit.Test;

/**
 * Unit tests for {@link Util} class.
 * @author douglasmcgee
 */
public class UtilTest {
    @Test
    public void getAllCurrencyCodesTest() {
        Set<Currency> allCurrencies = Currency.getAvailableCurrencies();
        allCurrencies.forEach(c -> Util.currencyOf(c.getCurrencyCode()));
    }
    
    @Test (expected = DtreException.class)
    public void getInvalidCurrencyCodeTest() {
        Util.currencyOf("BOGUS");
    }
    
    @Test
    public void validateAllCurrencyCodesTest() {
        Set<Currency> allCurrencies = Currency.getAvailableCurrencies();
        allCurrencies.forEach(c -> Util.validateCurrencyCode(c.getCurrencyCode()));
    }
    
    @Test (expected = DtreException.class)
    public void validateInvalidCurrencyCodeTest() {
        Util.validateCurrencyCode("BOGUS");
    }
}
