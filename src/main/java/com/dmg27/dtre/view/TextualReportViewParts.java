/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.view;

import com.dmg27.dtre.trade.Instruction;
import com.dmg27.dtre.trade.Trades;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.List;

/**
 * A textual view of transactions transactions.
 * 
 * @author Douglas McGee (dmg27i@gmail.com)
 */
public class TextualReportViewParts extends AbstractReportViewParts {
    
    /**
     * JPM date string format.
     */
    final private DateTimeFormatter DATE_FORMAT_JPM = DateTimeFormatter.ofPattern("dd MMM uuuu");
    
    /**
     * A horizontal line for the view.
     */
    final private static String BOX_LINE = "================================================================";
    
    /**
     * Create an instance of {@link TextualReportViewParts}.
     * @param transactions The transactions transactions to view.
     */
    TextualReportViewParts(Trades trades) {
        super(trades);
    }

    @Override
    String header(LocalDate from, LocalDate to) {
        try (Formatter formatter = new Formatter()) {
            formatter.format(
                "Report for period from %-11.11s to %-11.11s" + NL +
                BOX_LINE + NL +
                "| Date            | Entity | Total Incomming | Total Outgoiing |" + NL +
                BOX_LINE + NL,
                from.format(DATE_FORMAT_JPM),
                to.format(DATE_FORMAT_JPM));
            return formatter.toString();
        }
    }
    
    @Override
    public String tradesPerDay(LocalDate date) {
        String result;
        String dayOfWeek = DayOfWeek.from(date).toString();
        dayOfWeek = dayOfWeek.substring(0, 1) + dayOfWeek.substring(1).toLowerCase();
        
        // Create the per entity totals for the day.
        try (Formatter formatter = new Formatter()) {
            String template = "| %3.3s %-11.11s | %3.3s | %-15.15s | %-15.15s |" + NL;

            // The instructions are already ranked for entity with highest settlement aount.
            List<Instruction> instructions = trades.getTradesOn(date);
            String lastEntity = null;
            for (Instruction ins : instructions) {
                // Report one row per entity per day.
                String entity = ins.getEntity();
                if (entity.equals(lastEntity)) {
                    continue;
                }
                
                lastEntity = entity;
                
                // Total trades in and out for the enity on the day.
                BigDecimal totalInOnFor = trades.getTotalSettledIncommingOnAndFor(date, entity);
                BigDecimal totalOutOnFor = trades.getTotalSettledOutgoingOnAndFor(date, entity);
            
                formatter.format(template,
                    dayOfWeek,
                    date.format(DATE_FORMAT_JPM),
                    entity,
                    totalInOnFor,
                    totalOutOnFor);
            }
            
            result = formatter.toString();
        }
        
        // Create the overal totals for the day.
        try (Formatter formatter = new Formatter()) {
            String template = 
                "|                 |--------|                 |                 |" + NL +
                "| %3.3s %-11.11s | totals | %15.15s | %15.15s |" + NL +
                "|                 |--------|                 |                 |" + NL;            
            
            // Totals trades for all entities in and out on the day.
            BigDecimal totalIn = trades.getTotalSettledIncommingOn(date);
            BigDecimal totalOut = trades.getTotalSettledOutgoingOn(date);
            
            formatter.format(template,
                dayOfWeek,
                date.format(DATE_FORMAT_JPM),
                totalIn,
                totalOut);
            
            result = result + formatter.toString();
        }
            
        return result;
    } 

    @Override
    String footer() {
        return BOX_LINE;
    }
    
}
