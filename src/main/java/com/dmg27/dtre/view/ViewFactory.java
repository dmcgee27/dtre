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
 * A factory class for creating various views of a financial plan.
 * 
 * @author Douglas McGee (dmg27i@gmail.com)
 */
public class ViewFactory {
    /**
     * Create a textual view of the trades.
     * @param trades The trades for the period.
     * @param from Start of the report period.
     * @param to End of report period.
     * @return The trades view.
     */
    static public String textualAccountTransactionsView(Trades trades, LocalDate from, LocalDate to) {
        return createAccountTransactionsView(new TextualReportViewParts(trades), from, to);
    }
    
    /**
     * Create a view of an account.
     * @param viewParts The parts from which to create the type of view required.
     * @param from Start of the report period.
     * @param to End of report period.
     * @return The trades view.
     */
    private static String createAccountTransactionsView(AbstractReportViewParts viewParts, LocalDate from, LocalDate to) {
        StringBuilder sb = new StringBuilder(viewParts.header(from, to));
        
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            sb.append(viewParts.tradesPerDay(date));
        }
        
        sb.append(viewParts.footer());
        return sb.toString();
    }
}
