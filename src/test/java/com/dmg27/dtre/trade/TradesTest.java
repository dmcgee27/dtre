/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.trade;

import static com.dmg27.dtre.trade.InstructionTest.createInstruction;
import static com.dmg27.dtre.trade.WorkingWeekConstants.DEFAULT_WORKING_WEEK;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit tests for the {@link Trades} class.
 * @author douglasmcgee
 */
public class TradesTest {
    @Test
    public void createTradesTest() {
        Trades trades = new Trades()
            .workingWeek(DEFAULT_WORKING_WEEK);
        List<Instruction> tradesCltn = trades.getTradesCltn();
        assertTrue(tradesCltn.isEmpty());
        
        tradesCltn = trades.tradesCltn(this.createTrades()).getTradesCltn();
        assertEquals(10, tradesCltn.size());
        
        tradesCltn.stream()
            .forEach(i -> assertFalse(i.isSettled()));
        
        boolean[] settleable = new boolean[] {
            true,
            true,
            true,
            false,
            true,
            true,
            false,
            true,
            true,
            true
        };
        for (int i = 0; i < tradesCltn.size(); i++) {
            assertEquals(settleable[i], tradesCltn.get(i).isSettleable());
            assertEquals(i, tradesCltn.get(i).getId());
        }
    }
    
    @Test
    public void settlementTestUnsorted() {
        Trades trades = new Trades()
            .tradesCltn(this.createTrades())
            .workingWeek(DEFAULT_WORKING_WEEK);
        trades.settle();
        
        boolean[] expectedSettled = new boolean[] {
            true,
            true,
            true,
            false,
            true,
            true,
            false,
            true,
            true,
            true
        };
        String[] expectedSettledAmount = new String[] {
            "13545.00",
            "7525.00",
            "16555.00",
            "",
            "4515.00",
            "10025.00",
            "",
            "7518.75",
            "15037.50",
            "2506.25"
        };
        List<Instruction> tradesCltn = trades.getTradesCltn();
        this.assertSettlement(tradesCltn, expectedSettled, expectedSettledAmount);
    }
    
    @Test
    public void settlementTestSorted() {
        Trades trades = new Trades()
            .tradesCltn(this.createTrades())
            .workingWeek(DEFAULT_WORKING_WEEK);
        trades.settle();
        
        boolean[] expectedSettled = new boolean[] {
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false
        };
        String[] expectedSettledAmount = new String[] {
            "16555.00",
            "15037.50",
            "13545.00",
            "10025.00",
            "7525.00",
            "7518.75",
            "4515.00",
            "2506.25",
            "",
            ""
        };
        List<Instruction> tradesCltn = trades.getTradesCltn();
        tradesCltn = tradesCltn.stream()
            .sorted()
            .collect(Collectors.toList());
        this.assertSettlement(tradesCltn, expectedSettled, expectedSettledAmount);
    }
    
    /**
     * Assert the settlement sate of a collection of instructions.
     * @param tradesCltn
     * @param expectedSettled
     * @param expectedSettledAmountSorted 
     */
    private void assertSettlement(List<Instruction> tradesCltn, boolean[] expectedSettled, String[] expectedSettledAmount) {
        for (int i = 0; i < tradesCltn.size(); i++) {
            this.assertSettlement(tradesCltn.get(i), expectedSettled[i], expectedSettledAmount[i]);
        }
    }
    
    /**
     * Assert the settlement sate of an instruction.
     * @param instruction 
     */
    private void assertSettlement(Instruction instruction, boolean expectedIsSettled, String expectedSettledAmount) {
        assertEquals(expectedIsSettled, instruction.getSettledAmount().isPresent());
        if (expectedIsSettled) {
            assertEquals(new BigDecimal(expectedSettledAmount), instruction.getSettledAmount().get());
        } 
    }
    
    /**
     * Create a collection of trade instructions.
     * @return The trade instructions.
     */
    private List<Instruction> createTrades() {
        List<Instruction> trades = new ArrayList<>();
        trades.add(createInstruction("barS", "S", "0.2", "AED", "05 Jan 2016", "07 Jan 2016", 450, "150.5"));   // sunday to thursday
        trades.add(createInstruction("barS", "S", "0.2", "ALL", "05 Jan 2016", "07 Jan 2016", 250, "150.5"));
        trades.add(createInstruction("barS", "S", "0.2", "BAM", "05 Jan 2016", "07 Jan 2016", 550, "150.5"));
        trades.add(createInstruction("barS", "S", "0.2", "BBD", "01 Feb 2016", "02 Feb 2016", 650, "150.5"));   // unsettleable
        trades.add(createInstruction("barS", "S", "0.2", "CAD", "05 Jan 2016", "07 Jan 2016", 150, "150.5"));
        trades.add(createInstruction("fooB", "B", "0.50", "CDF", "01 Jan 2016", "02 Jan 2016", 200, "100.25"));
        trades.add(createInstruction("fooB", "B", "0.50", "ETB", "01 Mar 2016", "02 Mar 2016", 100, "100.25"));   // unsettleable
        trades.add(createInstruction("fooB", "B", "0.50", "EUR", "01 Jan 2016", "02 Jan 2016", 150, "100.25"));
        trades.add(createInstruction("fooB", "B", "0.50", "SAR", "01 Jan 2016", "02 Jan 2016", 300, "100.25"));   // sunday to thursday
        trades.add(createInstruction("fooB", "B", "0.50", "SGD", "01 Jan 2016", "02 Jan 2016", 50, "100.25"));
        return trades;
    }
}
