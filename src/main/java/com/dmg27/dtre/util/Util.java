/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.util;

import com.dmg27.dtre.core.DtreException;
import java.text.MessageFormat;
import java.util.Currency;

/**
 * Utility methods.
 * @author douglasmcgee
 */
final public class Util {
    /**
     * Get the currency represented by the currency code.
     * @param currencyCode The currency code.
     * @return  The currency.
     * @throws DtreException When there is a problem with the currency code.
     */
    static public Currency currencyOf(String currencyCode) {
        if (currencyCode == null) {
            throw new DtreException("null currency code");
        }
        
        try {
            return Currency.getInstance(currencyCode);
        } catch (IllegalArgumentException ex) {
            throw new DtreException(MessageFormat.format("invalid currency code \"{0}\"", currencyCode), ex);
        }
    }
    
    /**
     * Validate the currency code.
     * @param currencyCode The currency code.
     * @throws DtreException When there is a problem with the currency code.
     */
    static public void validateCurrencyCode(String currencyCode) {
        currencyOf(currencyCode);
    }
}
