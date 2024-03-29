/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.view;

import com.dmg27.dtre.trade.Trades;
import java.time.LocalDate;


/**
 * This class is the base class for views of the {@link Trades} class.
 * A view consists of:
 * <ul>
 * <li>A header</li>
 * <li>Zero or more entity trades rows per day</li>
 * <li>One total trades row per day</li>
 * <li>A footer</li>
 * </ul>
 * 
 * @author Douglas McGee (dmg27i@gmail.com)
 */
abstract public class AbstractReportViewParts {
    
    /**
     * New line.
     */
    final static String NL = System.getProperty("line.separator");
    
    /**
     * The transactions to view.
     */
    final Trades trades;
    
    /**
     * Create an instance of a subclass of {@link AbstractReportViewParts}.
     * @param trades The trades to view.
     */
    AbstractReportViewParts(Trades trades) {
        this.trades = trades;
    }
    
    /**
     * @return The trades to view.
     */
    Trades getTrades() {
        return this.trades;
    }
    
    /**
     * Create the view header.
     * @param from The view period start.
     * @param to The view period end.
     * @return The view header.
     */
    abstract String header(LocalDate from, LocalDate to);
    
    /**
     * @return A row for the view.
     */
    abstract String tradesPerDay(LocalDate date);
    
    /**
     * @return The view footer.
     */
    abstract String footer();
}
