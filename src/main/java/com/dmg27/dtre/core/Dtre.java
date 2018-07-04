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
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.dmg27.dtre.trade.WorkingWeek.*;
import static com.dmg27.dtre.view.ViewFactory.textualReportView;

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
     * The trades instructions to report upon.
     */
    private List<Instruction> instructions;
    
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
     * Set the instructions to report upon.
     * @param instructions The instructions.
     * @return this
     */
    public Dtre instructions(List<Instruction> instructions) {
        this.instructions = instructions;
        this.instructions.stream()
            .forEach(i -> i.clock(this.clock));
        return this;
    }
    
    /**
     * Execute a trades report with respect to the command line arguments.
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
     * Execute a trades report with respect to the command line arguments.
     * @param args Command line arguments.
     */
    public void execute2(String[] args) {
        // Get the trade instructions
        if (args.length == 0) {
            LOGGER.info("Running in default demo mode with demo trade instructions for the report.");
            this.instructions(createDemoInstructions());
        } else {
            throw new DtreException("there are no trade instructions to report");
        }
        
        // Create and settle the trades.
        Trades trades = new Trades()
            .trades(this.instructions)
            .workingWeek(DEMO_WORKING_WEEK)
            .settle();
        
        // Show the report for trades.
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
        String report = textualReportView(trades, LocalDate.parse(from), LocalDate.parse(to));
        System.out.println(report);
    }
    
    /**
     * Create demonstration trade instructions.
     * @return The instructions.
     */
    private static List<Instruction> createDemoInstructions() {
        
        List<Instruction> instructions = new ArrayList<>();
        instructions.add(createInstruction("bar", "B", "0.22", "AED", "01 Jan 2018", "07 Jan 2018", 10, "150.5"));  // SUN
        
        instructions.add(createInstruction("bar", "B", "0.22", "AED", "01 Jan 2018", "08 Jan 2018", 11, "150.5")); 
        instructions.add(createInstruction("car", "S", "1.00", "USD", "01 Jan 2018", "08 Jan 2018", 5000, "1.0")); 
        instructions.add(createInstruction("foo", "B", "0.50", "SGD", "01 Jan 2018", "08 Jan 2018", 20, "100.25")); 
        instructions.add(createInstruction("gla", "B", "1.00", "USD", "01 Jan 2018", "08 Jan 2018", 3000, "1.0")); 
        instructions.add(createInstruction("gla", "B", "1.00", "USD", "01 Jan 2019", "08 Jan 2019", 9999, "1.0")); // future
        instructions.add(createInstruction("gla", "B", "1.00", "USD", "01 Jan 2018", "08 Jan 2018", 3001, "1.0")); 
        instructions.add(createInstruction("gla", "S", "1.00", "USD", "01 Jan 2018", "08 Jan 2018", 3002, "1.0")); 
        instructions.add(createInstruction("lam", "B", "1.00", "USD", "01 Jan 2018", "08 Jan 2018", 2000, "1.0")); 
        instructions.add(createInstruction("lam", "B", "1.00", "USD", "01 Jan 2018", "08 Jan 2018", 2001, "1.0")); 
        instructions.add(createInstruction("lam", "S", "1.00", "USD", "01 Jan 2018", "08 Jan 2018", 2002, "1.0")); 
        instructions.add(createInstruction("win", "B", "1.00", "USD", "01 Jan 2018", "08 Jan 2018", 1000, "1.0"));

        instructions.add(createInstruction("bar", "B", "0.22", "AED", "01 Jan 2018", "09 Jan 2018", 12, "150.5")); 
        instructions.add(createInstruction("car", "S", "1.00", "USD", "01 Jan 2018", "09 Jan 2018", 5000, "1.0")); 
        instructions.add(createInstruction("foo", "B", "0.50", "SGD", "01 Jan 2018", "09 Jan 2018", 21, "100.25")); 
        instructions.add(createInstruction("gla", "B", "1.00", "USD", "01 Jan 2018", "09 Jan 2018", 3000, "1.0")); 
        instructions.add(createInstruction("gla", "B", "1.00", "USD", "01 Jan 2018", "09 Jan 2018", 3001, "1.0")); 
        instructions.add(createInstruction("gla", "S", "1.00", "USD", "01 Jan 2018", "09 Jan 2018", 3002, "1.0")); 
        instructions.add(createInstruction("lam", "B", "1.00", "USD", "01 Jan 2018", "09 Jan 2018", 2000, "1.0")); 
        instructions.add(createInstruction("lam", "B", "1.00", "USD", "01 Jan 2018", "09 Jan 2018", 2001, "1.0")); 
        instructions.add(createInstruction("lam", "S", "1.00", "USD", "01 Jan 2018", "09 Jan 2018", 2002, "1.0")); 

        instructions.add(createInstruction("bar", "B", "0.22", "AED", "01 Jan 2018", "10 Jan 2018", 13, "150.5")); 
        instructions.add(createInstruction("car", "S", "1.00", "USD", "01 Jan 2018", "10 Jan 2018", 5000, "1.0")); 
        instructions.add(createInstruction("foo", "B", "0.50", "SGD", "01 Jan 2018", "10 Jan 2018", 22, "100.25")); 
        instructions.add(createInstruction("gla", "B", "1.00", "USD", "01 Jan 2018", "10 Jan 2018", 3000, "1.0")); 
        instructions.add(createInstruction("gla", "B", "1.00", "USD", "01 Jan 2018", "10 Jan 2018", 3001, "1.0")); 
        instructions.add(createInstruction("gla", "S", "1.00", "USD", "01 Jan 2018", "10 Jan 2018", 3002, "1.0")); 

        instructions.add(createInstruction("bar", "B", "0.22", "AED", "01 Jan 2018", "11 Jan 2018", 14, "150.5")); 
        instructions.add(createInstruction("car", "S", "1.00", "USD", "01 Jan 2018", "11 Jan 2018", 5000, "1.0")); 
        instructions.add(createInstruction("foo", "B", "0.50", "SGD", "01 Jan 2018", "11 Jan 2018", 23, "100.25")); 

        instructions.add(createInstruction("bar", "B", "0.22", "AED", "01 Jan 2018", "12 Jan 2018", 15, "150.5")); 
        instructions.add(createInstruction("car", "S", "1.00", "USD", "01 Jan 2018", "12 Jan 2018", 5000, "1.0")); 

        instructions.add(createInstruction("car", "S", "1.00", "USD", "01 Jan 2018", "13 Jan 2018", 5001, "1.0")); // SAT
        
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
