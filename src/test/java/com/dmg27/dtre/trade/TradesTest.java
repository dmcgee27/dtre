/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.trade;

import com.dmg27.dtre.core.BuySell;
import static com.dmg27.dtre.trade.InstructionTest.createInstruction;
import static com.dmg27.dtre.trade.WorkingWeekConstants.DEFAULT_WORKING_WEEK;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
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
    public void settlementTest() {
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
        List<Instruction> tradesCltn = trades.getTradesSorted(trades.getTradesCltn());
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
    
    @Test
    public void getTradesOnDateTest01() {
        this.assertGetTradesOnDate("2016-01-07", new int[] {0, 1, 2, 4});
    }
    
    @Test
    public void getTotalSettledOutgoingOnAndFor01() {
//        this.assertTotalSettledOnAndFor(BuySell.B, "2016-01-07", "bar", expectedAmount);
    }
    
    @Test
    public void getTradesOnDateTest02() {
        this.assertGetTradesOnDate("2016-01-02", new int[] {});
    }
    
    @Test
    public void getTradesOnDateTest02a() {
        this.assertGetTradesOnDate("2016-01-04", new int[] {5, 7, 9});
    }
    
    @Test
    public void getTradesOnDateTest02b() {
        this.assertGetTradesOnDate("2016-01-03", new int[] {8});
    }
    
    @Test
    public void getTradesOnDateTest03() {
        this.assertGetTradesOnDate("2016-02-06", new int[] {});
    }
    
    @Test
    public void getTradesOnDateTest03a() {
        this.assertGetTradesOnDate("2016-02-08", new int[] {3});
    }
    
    @Test
    public void getTradesOnDateTest04() {
        this.assertGetTradesOnDate("2016-03-05", new int[] {});
    }
    
    @Test
    public void getTradesOnDateTest04a() {
        this.assertGetTradesOnDate("2016-03-07", new int[] {6});
    }
    
    /**
     * Assert the list of trades on a settlement date.
     * @param settlementDate
     * @param expectedIds 
     */
    private void assertGetTradesOnDate(String settlementDate, int[] expectedIds) {
        Trades trades = new Trades()
            .tradesCltn(this.createTrades())
            .workingWeek(DEFAULT_WORKING_WEEK);
        
        List<Instruction> tradesOn = trades.getTradesOn(LocalDate.parse(settlementDate));
        assertEquals(expectedIds.length, tradesOn.size());
        for (int i = 0; i < tradesOn.size(); i++) {
            assertEquals(expectedIds[i], tradesOn.get(i).getId());
        }
    }
    
    /**
     * Assert the amount settled on a date for an optional entity.
     * @param buySell
     * @param settlementDate
     * @param entity Optional
     * @param expectedAmount 
     */
    private void assertTotalSettledOnAndFor(BuySell buySell, String settlementDate, Optional<String> entity, String expectedAmount) {
        Trades trades = new Trades()
            .tradesCltn(this.createTrades())
            .workingWeek(DEFAULT_WORKING_WEEK);
        
        BigDecimal amount = trades.getTotalSettledOnAndFor(buySell, LocalDate.parse(settlementDate), entity);
        assertEquals(expectedAmount, amount);
    }
    
    /**
     * Create a collection of trade instructions.
     * <p>
     * Don't change unless you really know what you are doing.
     * </p>
     * @return The trade instructions.
     */
    private List<Instruction> createTrades() {
        List<Instruction> trades = new ArrayList<>();
        trades.add(createInstruction("bar", "S", "0.2",  "AED", "05 Jan 2016", "07 Jan 2016", 450, "150.5"));    // 0;THU;sunday to thursday;OK
        trades.add(createInstruction("bar", "B", "0.2",  "ALL", "05 Jan 2016", "07 Jan 2016", 250, "150.5"));    // 1;THU;OK
        trades.add(createInstruction("bar", "B", "0.2",  "BAM", "05 Jan 2016", "07 Jan 2016", 550, "150.5"));    // 2;THU;OK
        trades.add(createInstruction("bar", "S", "0.2",  "BBD", "01 Feb 2016", "06 Feb 2016", 650, "150.5"));    // 3;SAT;to MON 08 Feb 2016;future;unsettleable
        trades.add(createInstruction("bar", "S", "0.2",  "CAD", "05 Jan 2016", "07 Jan 2016", 150, "150.5"));    // 4;THU;OK
        trades.add(createInstruction("foo", "B", "0.50", "CDF", "01 Jan 2016", "02 Jan 2016", 200, "100.25"));   // 5;SAT;to MON 04 Jan 2016
        trades.add(createInstruction("foo", "S", "0.50", "ETB", "01 Mar 2016", "05 Mar 2016", 100, "100.25"));   // 6;SAT;future;unsettleable
        trades.add(createInstruction("foo", "S", "0.50", "EUR", "01 Jan 2016", "02 Jan 2016", 150, "100.25"));   // 7;SAT;to MON 04 Jan 2016
        trades.add(createInstruction("foo", "B", "0.50", "SAR", "01 Jan 2016", "02 Jan 2016", 300, "100.25"));   // 8;SAT;sunday to thursday;to SUN 03 Jan 2016 
        trades.add(createInstruction("foo", "B", "0.50", "SGD", "01 Jan 2016", "02 Jan 2016",  50, "100.25"));   // 9;SAT;to MON 04 Jan 2016
        return trades;
    }
}
