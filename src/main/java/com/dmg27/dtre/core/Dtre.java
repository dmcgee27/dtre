/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.core;

import com.dmg27.dtre.trade.Instruction;
import com.dmg27.dtre.trade.Trades;
import static com.dmg27.dtre.trade.WorkingWeek.*;
import static com.dmg27.dtre.view.ViewFactory.textualAccountTransactionsView;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a main method for the Daily Trades Reporting Engine.
 * @author douglasmcgee
 */
public class Dtre {
    
    /**
     * The logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(Dtre.class);
    
    /**
     * The clock.
     */
    private Clock clock = Clock.systemUTC();
    
    /**
     * The application's main method.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Dtre dtre = new Dtre();
        dtre.execute(args);
    }
    
    /**
     * Set the {@link Dtre} clock.
     * @param clock The new clock.
     * @return This for method chaining.
     */
    public Dtre clock(Clock clock) {
        this.clock = clock;
        return this;
    }
    
    /**
     * Execute a trades report..
     * @param args Command line arguments.
     */
    public void execute(String[] args) {
        try {
            this.execute2(args);
        } catch (DtreException ex) {
            LOGGER.error("Encountered a problem creating the report because {}.", ex.getMessage());
        }
    }
    
    /**
     * Execute a trades report..
     * @param args Command line arguments.
     */
    public void execute2(String[] args) {
        // Create and settle some demo trades.
        Trades trades = new Trades()
            .trades(createDemoInstructions())
            .workingWeek(DEMO_WORKING_WEEK)
            .settle();
        
        // Show the report for demo trades.
        showReport(trades);
    }
    
    /**
     * Show the report for the trades.
     * @param trades The trades.
     */
    private static void showReport(Trades trades) {
        String from = trades.getTrades().stream()
            .filter(i -> i.isSettleable())
            .map(i -> i.getEffectiveSettlementDate())
            .min(Comparator.naturalOrder())
            .get()
            .toString();
        String to = trades.getTrades().stream()
            .filter(i -> i.isSettleable())
            .map(i -> i.getEffectiveSettlementDate())
            .max(Comparator.naturalOrder())
            .get()
            .toString();
        showReport(trades, from, to);
    }
    
    /**
     * Show the report for the trades in a period.
     * @param trades The trades.
     * @param from Start of the period.
     * @param to End of the period.
     */
    private static void showReport(Trades trades, String from, String to) {
        String report = textualAccountTransactionsView(trades, LocalDate.parse(from), LocalDate.parse(to));
        System.out.println(report);
    }
    
    /**
     * Create demonstration trade instructions.
     * @return The instructions.
     */
    private static List<Instruction> createDemoInstructions() {
        
        List<Instruction> instructions = new ArrayList<>();
        instructions.add(createInstruction("bar", "B", "0.22", "AED", "03 Jan 2016", "03 Jan 2016", 10, "150.5")); 
        
        instructions.add(createInstruction("bar", "B", "0.22", "AED", "03 Jan 2016", "04 Jan 2016", 11, "150.5")); 
        instructions.add(createInstruction("car", "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction("foo", "B", "0.5", "SGD", "03 Jan 2016", "04 Jan 2016", 20, "100.25")); 
        instructions.add(createInstruction("gla", "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 3000, "1.0")); 
        instructions.add(createInstruction("gla", "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2020", 9999, "1.0")); 
        instructions.add(createInstruction("gla", "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 3001, "1.0")); 
        instructions.add(createInstruction("gla", "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 3002, "1.0")); 
        instructions.add(createInstruction("lam", "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 2000, "1.0")); 
        instructions.add(createInstruction("lam", "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 2001, "1.0")); 
        instructions.add(createInstruction("lam", "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 2002, "1.0")); 
        instructions.add(createInstruction("win", "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 1000, "1.0"));
        
        instructions.add(createInstruction("bar", "B", "0.22", "AED", "03 Jan 2016", "05 Jan 2016", 12, "150.5")); 
        instructions.add(createInstruction("car", "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction("foo", "B", "0.5", "SGD", "03 Jan 2016", "05 Jan 2016", 21, "100.25")); 
        instructions.add(createInstruction("gla", "B", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 3000, "1.0")); 
        instructions.add(createInstruction("gla", "B", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 3001, "1.0")); 
        instructions.add(createInstruction("gla", "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 3002, "1.0")); 
        instructions.add(createInstruction("lam", "B", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 2000, "1.0")); 
        instructions.add(createInstruction("lam", "B", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 2001, "1.0")); 
        instructions.add(createInstruction("lam", "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 2002, "1.0")); 
        
        instructions.add(createInstruction("bar", "B", "0.22", "AED", "03 Jan 2016", "06 Jan 2016", 13, "150.5")); 
        instructions.add(createInstruction("car", "S", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction("foo", "B", "0.5", "SGD", "03 Jan 2016", "06 Jan 2016", 22, "100.25")); 
        instructions.add(createInstruction("gla", "B", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 3000, "1.0")); 
        instructions.add(createInstruction("gla", "B", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 3001, "1.0")); 
        instructions.add(createInstruction("gla", "S", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 3002, "1.0")); 
        
        instructions.add(createInstruction("bar", "B", "0.22", "AED", "03 Jan 2016", "07 Jan 2016", 14, "150.5")); 
        instructions.add(createInstruction("car", "S", "1.0", "USD", "04 Jan 2016", "07 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction("foo", "B", "0.5", "SGD", "03 Jan 2016", "07 Jan 2016", 23, "100.25")); 
        
        instructions.add(createInstruction("bar", "B", "0.22", "AED", "03 Jan 2016", "08 Jan 2016", 15, "150.5")); 
        instructions.add(createInstruction("car", "S", "1.0", "USD", "04 Jan 2016", "08 Jan 2016", 5000, "1.0")); 
        
        instructions.add(createInstruction("car", "S", "1.0", "USD", "04 Jan 2016", "09 Jan 2016", 5001, "1.0")); 
        
        return instructions;
    }
    
    /**
     * Create an {@link Instruction} instance.
     * @param entity
     * @param buySell
     * @param agreedFx
     * @param currencyCode
     * @param instructionDateString
     * @param settlementDateString
     * @param units
     * @param unitPriceString
     * @return 
     */
    public static Instruction createInstruction(
            String entity, 
            String buySell, 
            String agreedFx, 
            String currencyCode, 
            String instructionDateString, 
            String settlementDateString, 
            int units,
            String unitPriceString) {
        return new Instruction()
            .entity(entity)
            .buySell(buySell)
            .agreedFx(agreedFx)
            .currencyCode(currencyCode)
            .instructionDate(instructionDateString)
            .settlementDate(settlementDateString)
            .units(units)
            .unitPrice(unitPriceString)
            .workingWeek(DEMO_WORKING_WEEK)
            ;
    }
    
}
