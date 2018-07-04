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
import static com.dmg27.dtre.trade.InstructionTest.createInstruction;
import static com.dmg27.dtre.trade.TradesTest.BAR;
import static com.dmg27.dtre.trade.TradesTest.CAR;
import static com.dmg27.dtre.trade.TradesTest.FOO;
import static com.dmg27.dtre.trade.TradesTest.GLA;
import static com.dmg27.dtre.trade.TradesTest.LAM;
import static com.dmg27.dtre.trade.TradesTest.StubbedInstructionsTests.createSettledTrades;
import static com.dmg27.dtre.trade.TradesTest.WIN;
import static com.dmg27.dtre.view.ViewFactory.textualAccountTransactionsView;
import static com.dmg27.dtre.trade.WorkingWeekTestConstants.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Unit tests for the view classes
 * @author douglasmcgee
 */
public class ViewTest {
    
    @Test
    public void basicOneDayNoInstructionsTest() {
        System.out.println("\nTest :" + new Object(){}.getClass().getEnclosingMethod());
        List<Instruction> instructions = new ArrayList<>();
        Trades trades = createSettledTrades(instructions);
        assertShowReport(trades, MON_04_JAN_2016, MON_04_JAN_2016);

    }
    
    @Test
    public void basicThreeDayNoInstructionsTest() {
        System.out.println("\nTest :" + new Object(){}.getClass().getEnclosingMethod());
        List<Instruction> instructions = new ArrayList<>();
        Trades trades = createSettledTrades(instructions);
        assertShowReport(trades, MON_04_JAN_2016, WED_06_JAN_2016);

    }
    
    @Test 
    public void basicOneDayWithDescendingBuyUsdInstructionsMonToFriTest() {
        System.out.println("\nTest :" + new Object(){}.getClass().getEnclosingMethod());
        List<Instruction> instructions = new ArrayList<>();
        instructions.add(createInstruction(BAR, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 4000, "1.0")); 
        instructions.add(createInstruction(GLA, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 3000, "1.0")); 
        instructions.add(createInstruction(LAM, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 2000, "1.0")); 
        instructions.add(createInstruction(WIN, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 1000, "1.0"));
        
        Trades trades = createSettledTrades(instructions);
        assertShowReport(trades);
    }
    
    @Test 
    public void basicOneDayWithAscendingSellUsdInstructionsMonToFriTest() {
        System.out.println("\nTest :" + new Object(){}.getClass().getEnclosingMethod());
        List<Instruction> instructions = new ArrayList<>();
        instructions.add(createInstruction(WIN, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 1000, "1.0"));
        instructions.add(createInstruction(LAM, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 2000, "1.0")); 
        instructions.add(createInstruction(GLA, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 3000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 4000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(BAR, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 6000, "1.0")); 
        
        Trades trades = createSettledTrades(instructions);
        assertShowReport(trades);
    }
    
    @Test 
    public void basicOneDayWithmixedOrderBuysellUsdInstructionsMonToFriTest() {
        System.out.println("\nTest :" + new Object(){}.getClass().getEnclosingMethod());
        List<Instruction> instructions = new ArrayList<>();
        instructions.add(createInstruction(LAM, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 2000, "1.0")); 
        instructions.add(createInstruction(WIN, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 1000, "1.0"));
        instructions.add(createInstruction(GLA, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 3000, "1.0")); 
        instructions.add(createInstruction(BAR, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 4000, "1.0")); 
        
        Trades trades = createSettledTrades(instructions);
        assertShowReport(trades);
    }
    
    @Test 
    public void basicOneDayWithmixedOrderBuysellUsdMultiInstructionsMonToFriTest() {
        System.out.println("\nTest :" + new Object(){}.getClass().getEnclosingMethod());
        List<Instruction> instructions = new ArrayList<>();
        instructions.add(createInstruction(LAM, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 2000, "1.0")); 
        instructions.add(createInstruction(LAM, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 2000, "1.0")); 
        instructions.add(createInstruction(LAM, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 2000, "1.0")); 
        instructions.add(createInstruction(WIN, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 1000, "1.0"));
        instructions.add(createInstruction(GLA, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 3000, "1.0")); 
        instructions.add(createInstruction(BAR, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 4000, "1.0")); 
        instructions.add(createInstruction(FOO, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 4000, "1.0")); 
        
        Trades trades = createSettledTrades(instructions);
        assertShowReport(trades);
    }
    
    @Test 
    public void multiDayWithDescendingBuyUsdInstructionsMonToFriTest() {
        System.out.println("\nTest :" + new Object(){}.getClass().getEnclosingMethod());
        List<Instruction> instructions = new ArrayList<>();
        instructions.add(createInstruction(BAR, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 4000, "1.0")); 
        instructions.add(createInstruction(GLA, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 3000, "1.0")); 
        instructions.add(createInstruction(LAM, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 2000, "1.0")); 
        instructions.add(createInstruction(WIN, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 1000, "1.0"));
        
        instructions.add(createInstruction(BAR, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 4000, "1.0")); 
        instructions.add(createInstruction(GLA, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 3000, "1.0")); 
        instructions.add(createInstruction(LAM, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 2000, "1.0")); 
        
        instructions.add(createInstruction(BAR, "S", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 4000, "1.0")); 
        instructions.add(createInstruction(GLA, "S", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 3000, "1.0")); 
        
        instructions.add(createInstruction(BAR, "S", "1.0", "USD", "04 Jan 2016", "07 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "07 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "07 Jan 2016", 4000, "1.0")); 
        
        instructions.add(createInstruction(BAR, "S", "1.0", "USD", "04 Jan 2016", "08 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "08 Jan 2016", 5000, "1.0")); 
        
        Trades trades = createSettledTrades(instructions);
        assertShowReport(trades);
    }
    
    @Test 
    public void multiDayWithDescendingBuysellUsdInstructionsWithBarAndCarNextWorkingDaySunSatTest() {
        System.out.println("\nTest :" + new Object(){}.getClass().getEnclosingMethod());
        List<Instruction> instructions = new ArrayList<>();
        instructions.add(createInstruction(BAR, "S", "1.0", "USD", "04 Jan 2016", "03 Jan 2016", 6001, "1.0")); 
        
        instructions.add(createInstruction(BAR, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 4000, "1.0")); 
        instructions.add(createInstruction(GLA, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 3000, "1.0")); 
        instructions.add(createInstruction(LAM, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 2000, "1.0")); 
        instructions.add(createInstruction(WIN, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 1000, "1.0"));
        
        instructions.add(createInstruction(BAR, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 4000, "1.0")); 
        instructions.add(createInstruction(GLA, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 3000, "1.0")); 
        instructions.add(createInstruction(LAM, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 2000, "1.0")); 
        
        instructions.add(createInstruction(BAR, "S", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 4000, "1.0")); 
        instructions.add(createInstruction(GLA, "S", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 3000, "1.0")); 
        
        instructions.add(createInstruction(BAR, "S", "1.0", "USD", "04 Jan 2016", "07 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "07 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "07 Jan 2016", 4000, "1.0")); 
        
        instructions.add(createInstruction(BAR, "S", "1.0", "USD", "04 Jan 2016", "08 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "08 Jan 2016", 5000, "1.0")); 
        
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "09 Jan 2016", 5001, "1.0")); 
        
        Trades trades = createSettledTrades(instructions);
        assertShowReport(trades);
    }
    
    @Test 
    public void multiDayWithDescendingBuysellUsdMultiEntityInstructionsMonToFriTest() {
        System.out.println("\nTest :" + new Object(){}.getClass().getEnclosingMethod());
        List<Instruction> instructions = new ArrayList<>();
        instructions.add(createInstruction(BAR, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 4000, "1.0")); 
        instructions.add(createInstruction(GLA, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 3000, "1.0")); 
        instructions.add(createInstruction(GLA, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 3001, "1.0")); 
        instructions.add(createInstruction(GLA, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 3002, "1.0")); 
        instructions.add(createInstruction(LAM, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 2000, "1.0")); 
        instructions.add(createInstruction(LAM, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 2001, "1.0")); 
        instructions.add(createInstruction(LAM, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 2002, "1.0")); 
        instructions.add(createInstruction(WIN, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 1000, "1.0"));
        
        instructions.add(createInstruction(BAR, "B", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 4000, "1.0")); 
        instructions.add(createInstruction(GLA, "B", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 3000, "1.0")); 
        instructions.add(createInstruction(GLA, "B", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 3001, "1.0")); 
        instructions.add(createInstruction(GLA, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 3002, "1.0")); 
        instructions.add(createInstruction(LAM, "B", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 2000, "1.0")); 
        instructions.add(createInstruction(LAM, "B", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 2001, "1.0")); 
        instructions.add(createInstruction(LAM, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 2002, "1.0")); 
        
        instructions.add(createInstruction(BAR, "B", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 4000, "1.0")); 
        instructions.add(createInstruction(GLA, "B", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 3000, "1.0")); 
        instructions.add(createInstruction(GLA, "B", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 3001, "1.0")); 
        instructions.add(createInstruction(GLA, "S", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 3002, "1.0")); 
        
        instructions.add(createInstruction(BAR, "B", "1.0", "USD", "04 Jan 2016", "07 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "07 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "07 Jan 2016", 4000, "1.0")); 
        
        instructions.add(createInstruction(BAR, "B", "1.0", "USD", "04 Jan 2016", "08 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "08 Jan 2016", 5000, "1.0")); 
        
        Trades trades = createSettledTrades(instructions);
        assertShowReport(trades);
    }
    
    @Test 
    public void multiDayWithDescendingBuysellUsdMultiEntityInstructionsWithBarAndCarNextWorkingDaySunSatTest() {
        System.out.println("\nTest :" + new Object(){}.getClass().getEnclosingMethod());
        List<Instruction> instructions = new ArrayList<>();
        instructions.add(createInstruction(BAR, "B", "1.0", "USD", "03 Jan 2016", "04 Jan 2016", 6001, "1.0")); 
        
        instructions.add(createInstruction(BAR, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 4000, "1.0")); 
        instructions.add(createInstruction(GLA, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 3000, "1.0")); 
        instructions.add(createInstruction(GLA, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 3001, "1.0")); 
        instructions.add(createInstruction(GLA, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 3002, "1.0")); 
        instructions.add(createInstruction(LAM, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 2000, "1.0")); 
        instructions.add(createInstruction(LAM, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 2001, "1.0")); 
        instructions.add(createInstruction(LAM, "S", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 2002, "1.0")); 
        instructions.add(createInstruction(WIN, "B", "1.0", "USD", "04 Jan 2016", "04 Jan 2016", 1000, "1.0"));
        
        instructions.add(createInstruction(BAR, "B", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 4000, "1.0")); 
        instructions.add(createInstruction(GLA, "B", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 3000, "1.0")); 
        instructions.add(createInstruction(GLA, "B", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 3001, "1.0")); 
        instructions.add(createInstruction(GLA, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 3002, "1.0")); 
        instructions.add(createInstruction(LAM, "B", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 2000, "1.0")); 
        instructions.add(createInstruction(LAM, "B", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 2001, "1.0")); 
        instructions.add(createInstruction(LAM, "S", "1.0", "USD", "04 Jan 2016", "05 Jan 2016", 2002, "1.0")); 
        
        instructions.add(createInstruction(BAR, "B", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 4000, "1.0")); 
        instructions.add(createInstruction(GLA, "B", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 3000, "1.0")); 
        instructions.add(createInstruction(GLA, "B", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 3001, "1.0")); 
        instructions.add(createInstruction(GLA, "S", "1.0", "USD", "04 Jan 2016", "06 Jan 2016", 3002, "1.0")); 
        
        instructions.add(createInstruction(BAR, "B", "1.0", "USD", "04 Jan 2016", "07 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "07 Jan 2016", 5000, "1.0")); 
        instructions.add(createInstruction(FOO, "S", "1.0", "USD", "04 Jan 2016", "07 Jan 2016", 4000, "1.0")); 
        
        instructions.add(createInstruction(BAR, "B", "1.0", "USD", "04 Jan 2016", "08 Jan 2016", 6000, "1.0")); 
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "08 Jan 2016", 5000, "1.0")); 
        
        instructions.add(createInstruction(CAR, "S", "1.0", "USD", "04 Jan 2016", "09 Jan 2016", 5001, "1.0")); 
        
        Trades trades = createSettledTrades(instructions);
        assertShowReport(trades);
    }
    
    private void assertShowReport(Trades trades) {
        String from = trades.getTrades().stream()
            .map(i -> i.getEffectiveSettlementDate())
            .min(Comparator.naturalOrder())
            .get()
            .toString();
        String to = trades.getTrades().stream()
            .map(i -> i.getEffectiveSettlementDate())
            .max(Comparator.naturalOrder())
            .get()
            .toString();
        this.assertShowReport(trades, from, to);
    }
    
    private void assertShowReport(Trades trades, String from, String to) {
        String report = textualAccountTransactionsView(trades, LocalDate.parse(from), LocalDate.parse(to));
        assertFalse(report.isEmpty());
        System.out.println(report);
    }
}

