/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.trade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import static com.dmg27.dtre.trade.InstructionTest.createInstruction;
import static com.dmg27.dtre.trade.WorkingWeek.*;
import static com.dmg27.dtre.trade.WorkingWeekTestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link Trades} class.
 * @author douglasmcgee
 */
@RunWith(Suite.class)
@SuiteClasses({TradesTest.StubbedInstructionsTests.class, TradesTest.OneDayTests.class, TradesTest.MultiDayTests.class})
public class TradesTest {
    /**
     * Entity names.
     */
    public static final String BAR = "bar";
    public static final String CAR = "car";
    public static final String FOO = "foo";
    public static final String GLA = "gla";
    public static final String LAM = "lam";
    public static final String WIN = "win";
    
    /**
     * Tests built around stubbed instructions.
     */
    public static class StubbedInstructionsTests {

        @Test
        public void createTradesTest() {
            Trades trades = new Trades()
                .workingWeek(DEMO_WORKING_WEEK);
            List<Instruction> tradesCltn = trades.getTrades();
            assertTrue(tradesCltn.isEmpty());

            tradesCltn = trades.trades(createInstructionStubs()).getTrades();
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
            Trades trades = createSettledTrades(createInstructionStubs());
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
            this.assertSettlement(trades.getTrades(), expectedSettled, expectedSettledAmount);
        }

        /**
         * Assert the settlement sate of a collection of instructions.
         * @param tradesCltn
         * @param expectedSettled
         * @param expectedSettledAmount
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
            this.assertGetTradesOnDate(THU_07_JAN_2016, new int[] {0, 1, 2, 4});
        }

        @Test
        public void getTradesOnDateForEntityBarTest01() {
            this.assertGetTradesOnDateForEntity(THU_07_JAN_2016, BAR, new int[] {0, 1, 2, 4});
        }

        @Test
        public void getTradesOnDateForEntityFooTest01() {
            this.assertGetTradesOnDateForEntity(THU_07_JAN_2016, FOO, new int[] {});
        }

        @Test
        public void getTotalSettledOutgoingOnAndFor01() {
    //        this.assertTotalSettledOnAndFor(BuySell.B, THU_07_JAN_2016, BAR, expectedAmount);
        }

        @Test
        public void getTradesOnDateTest02() {
            this.assertGetTradesOnDate(SAT_02_JAN_2016, new int[] {});
        }

        @Test
        public void getTradesOnDateAndForBarTest02() {
            this.assertGetTradesOnDateForEntity(SAT_02_JAN_2016, BAR, new int[] {});
        }

        @Test
        public void getTradesOnDateAndForFooTest02() {
            this.assertGetTradesOnDateForEntity(SAT_02_JAN_2016, FOO, new int[] {});
        }

        @Test
        public void getTradesOnDateTest02a() {
            this.assertGetTradesOnDate(MON_04_JAN_2016, new int[] {5, 7, 9});
        }

        @Test
        public void getTradesOnDateAndForBarTest02a() {
            this.assertGetTradesOnDateForEntity(MON_04_JAN_2016, BAR, new int[] {});
        }

        @Test
        public void getTradesOnDateAndForFooTest02a() {
            this.assertGetTradesOnDateForEntity(MON_04_JAN_2016, FOO, new int[] {5, 7, 9});
        }

        @Test
        public void getTradesOnDateTest02b() {
            this.assertGetTradesOnDate(SUN_03_JAN_2016, new int[] {8});
        }

        @Test
        public void getTradesOnDateForEntityBarTest02b() {
            this.assertGetTradesOnDateForEntity(SUN_03_JAN_2016, BAR, new int[] {});
        }

        @Test
        public void getTradesOnDateForEntityFooTest02b() {
            this.assertGetTradesOnDateForEntity(SUN_03_JAN_2016, FOO, new int[] {8});
        }

        @Test
        public void getTradesOnDateTest03() {
            this.assertGetTradesOnDate(SAT_06_FEB_2016, new int[] {});
        }

        @Test
        public void getTradesOnDateForEntityBarTest03() {
            this.assertGetTradesOnDateForEntity(SAT_06_FEB_2016, BAR, new int[] {});
        }

        @Test
        public void getTradesOnDateForEntityFooTest03() {
            this.assertGetTradesOnDateForEntity(SAT_06_FEB_2016, FOO, new int[] {});
        }

        @Test
        public void getTradesOnDateTest03a() {
            this.assertGetTradesOnDate(MON_08_FEB_2016, new int[] {3});
        }

        @Test
        public void getTradesOnDateForEntityBarTest03a() {
            this.assertGetTradesOnDateForEntity(MON_08_FEB_2016, BAR, new int[] {3});
        }

        @Test
        public void getTradesOnDateForEntityFooTest03a() {
            this.assertGetTradesOnDateForEntity(MON_08_FEB_2016, FOO, new int[] {});
        }

        @Test
        public void getTradesOnDateTest04() {
            this.assertGetTradesOnDate(SAT_05_MAR_2016, new int[] {});
        }

        @Test
        public void getTradesOnDateForEntityBarTest04() {
            this.assertGetTradesOnDateForEntity(SAT_05_MAR_2016, BAR, new int[] {});
        }

        @Test
        public void getTradesOnDateForEntityFooTest04() {
            this.assertGetTradesOnDateForEntity(SAT_05_MAR_2016, FOO, new int[] {});
        }

        @Test
        public void getTradesOnDateTest04a() {
            this.assertGetTradesOnDate(MON_07_MAR_2016, new int[] {6});
        }

        @Test
        public void getTradesOnDateForEntityBarTest04a() {
            this.assertGetTradesOnDateForEntity(MON_07_MAR_2016, BAR, new int[] {});
        }

        @Test
        public void getTradesOnDateForEntityFooTest04a() {
            this.assertGetTradesOnDateForEntity(MON_07_MAR_2016, FOO, new int[] {6});
        }

        /**
         * Assert the list of trades on a settlement date.
         * @param settlementDate
         * @param expectedIds 
         */
        private void assertGetTradesOnDate(String settlementDate, int[] expectedIds) {
            Trades trades = createUnsettledTrades(createInstructionStubs());
            List<Instruction> tradesOn = trades.getTradesOn(LocalDate.parse(settlementDate));
            assertEquals(expectedIds.length, tradesOn.size());
            assertTradesRank(tradesOn, expectedIds);
            for (int i = 0; i < tradesOn.size(); i++) {
                assertEquals(expectedIds[i], tradesOn.get(i).getId());
            }
        }

        /**
         * Assert the list of trades on a settlement date for an entity.
         * @param settlementDate
         * @param expectedIds 
         */
        private void assertGetTradesOnDateForEntity(String settlementDate, String entity, int[] expectedIds) {
            Trades trades = createUnsettledTrades(createInstructionStubs());
            List<Instruction> tradesOnAndFor = trades.getTradesOnAndFor(LocalDate.parse(settlementDate), Optional.of(entity));
            assertEquals(expectedIds.length, tradesOnAndFor.size());
            assertTradesRank(tradesOnAndFor, expectedIds);
            for (int i = 0; i < tradesOnAndFor.size(); i++) {
                assertEquals(expectedIds[i], tradesOnAndFor.get(i).getId());
            }
        }

        /**
         * Assert the ranking of the trades.
         * @param trades
         * @param expectedIds 
         */
        private static void assertTradesRank(List<Instruction> trades, int[] expectedIds) {
            assertNotNull(expectedIds);
            assertEquals(expectedIds.length, trades.size());
            for (int i = 0; i < trades.size(); i++) {
                assertEquals(expectedIds[i], trades.get(i).getId());
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
            Trades trades = createUnsettledTrades(createInstructionStubs());
            BigDecimal amount = trades.getTotalSettledOnAndFor(buySell, LocalDate.parse(settlementDate), entity);
            assertEquals(expectedAmount, amount);
        }

        /**
         * Create an unsettled trades instance.
         * @param instructions
         * @return 
         */
        static Trades createUnsettledTrades(List<Instruction> instructions) {
            return new Trades()
                .trades(instructions)
                .workingWeek(DEMO_WORKING_WEEK);
        }

        /**
         * Create a settled trades instance.
         * @param instructions
         * @return 
         */
        public static Trades createSettledTrades(List<Instruction> instructions) {
            return createUnsettledTrades(instructions)
                 .settle();
        }

        /**
         * Create a settled trades instance for trades settled on a date.
         * @param instructions
         * @param date
         * @return 
         */
        static Trades createSettledTradesOn(List<Instruction> instructions, LocalDate date) {
            return createUnsettledTrades(instructions)
                 .settleOn(date);
        }

        /**
         * Create a collection of trade instructions.
         * @return The trade instructions.
         */
        static private List<Instruction> createInstructionStubs() {
            List<Instruction> trades = new ArrayList<>();
            trades.add(createInstruction(BAR, "S", "0.2",  "AED", "05 Jan 2016", THU_07_JAN_2016_JPM, 450, "150.5"));    // 0;sunday to thursday;OK
            trades.add(createInstruction(BAR, "B", "0.2",  "ALL", "05 Jan 2016", THU_07_JAN_2016_JPM, 250, "150.5"));    // 1;K
            trades.add(createInstruction(BAR, "B", "0.2",  "BAM", "05 Jan 2016", THU_07_JAN_2016_JPM, 550, "150.5"));    // 2;OK
            trades.add(createInstruction(BAR, "S", "0.2",  "BBD", "01 Feb 2016", SAT_06_FEB_2016_JPM, 650, "150.5"));    // 3;to MON 08 Feb 2016;future;unsettleable
            trades.add(createInstruction(BAR, "S", "0.2",  "CAD", "05 Jan 2016", THU_07_JAN_2016_JPM, 150, "150.5"));    // 4;OK
            trades.add(createInstruction(FOO, "B", "0.50", "CDF", "01 Jan 2016", SAT_02_JAN_2016_JPM, 200, "100.25"));   // 5;to MON 04 Jan 2016
            trades.add(createInstruction(FOO, "S", "0.50", "ETB", "01 Mar 2016", SAT_05_MAR_2016_JPM, 100, "100.25"));   // 6;future;unsettleable
            trades.add(createInstruction(FOO, "S", "0.50", "EUR", "01 Jan 2016", SAT_02_JAN_2016_JPM, 150, "100.25"));   // 7;to MON 04 Jan 2016
            trades.add(createInstruction(FOO, "B", "0.50", "SAR", "01 Jan 2016", SAT_02_JAN_2016_JPM, 300, "100.25"));   // 8;sunday to thursday;to SUN 03 Jan 2016 
            trades.add(createInstruction(FOO, "B", "0.50", "SGD", "01 Jan 2016", SAT_02_JAN_2016_JPM,  50, "100.25"));   // 9;to MON 04 Jan 2016
            return trades;
        }
    }
    
    /**
     * Configuration data for mock instructions.
     */
    private static class MockInstructionConfig {
        String entity;
        BuySell buysell;
        String effectiveSettlementDate;
        String settledAmount;

        public MockInstructionConfig(String entity, BuySell buysell, String effectiveSettlementDate, String settledAmount) {
            this.entity = entity;
            this.buysell = buysell;
            this.effectiveSettlementDate = effectiveSettlementDate;
            this.settledAmount = settledAmount;
        }
    }
    
    /**
     * Tests for instructions on one day.
     */
    public static class OneDayTests {
        @Test
        public void descendingBuyTrades() {
            
            MockInstructionConfig[] config = new MockInstructionConfig[] {
                new MockInstructionConfig(BAR, BuySell.B, MON_04_JAN_2016, "6000"),
                new MockInstructionConfig(CAR, BuySell.B, MON_04_JAN_2016, "5000"),
                new MockInstructionConfig(FOO, BuySell.B, MON_04_JAN_2016, "4000"),
                new MockInstructionConfig(GLA, BuySell.B, MON_04_JAN_2016, "3000"),
                new MockInstructionConfig(LAM, BuySell.B, MON_04_JAN_2016, "2000"),
                new MockInstructionConfig(WIN, BuySell.B, MON_04_JAN_2016, "1000"),
            };
            List<Instruction> instructions = InstructionFactory.create(config);
            
            Trades trades = StubbedInstructionsTests.createSettledTrades(instructions);
            int[] expectedIds = new int[] {0, 1, 2, 3, 4, 5};
            StubbedInstructionsTests.assertTradesRank(trades.getTrades(), expectedIds);
            
            // Total buys for all entities on the settlement date.
            assertEquals(new BigDecimal("21000.00"), trades.getTotalSettledOutgoingOn(LocalDate.parse(MON_04_JAN_2016)));
            
            // Total buys for all entities on a non settlement date.
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOn(LocalDate.parse(SUN_03_JAN_2016)));
            
            // Total sells for all entities on the settlement date.
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledIncommingOn(LocalDate.parse(MON_04_JAN_2016)));
            
            // Total buys for entities on the settlement date.
            assertEquals(new BigDecimal("6000.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), BAR));
            assertEquals(new BigDecimal("5000.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), CAR));
            assertEquals(new BigDecimal("4000.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), FOO));
            assertEquals(new BigDecimal("3000.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), GLA));
            assertEquals(new BigDecimal("2000.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), LAM));
            assertEquals(new BigDecimal("1000.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), WIN));
            
            // Total buys for entities on non settlement date.
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), BAR));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), CAR));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), FOO));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), GLA));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), LAM));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), WIN));
            
            // Total sells for entities on the settlement date.
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), BAR));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), CAR));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), FOO));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), GLA));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), LAM));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), WIN));
        }
        
        @Test
        public void ascendingSellTrades() {
            
            MockInstructionConfig[] config = new MockInstructionConfig[] {
                new MockInstructionConfig(WIN, BuySell.S, MON_04_JAN_2016, "1000"),
                new MockInstructionConfig(LAM, BuySell.S, MON_04_JAN_2016, "2000"),
                new MockInstructionConfig(GLA, BuySell.S, MON_04_JAN_2016, "3000"),
                new MockInstructionConfig(FOO, BuySell.S, MON_04_JAN_2016, "4000"),
                new MockInstructionConfig(CAR, BuySell.S, MON_04_JAN_2016, "5000"),
                new MockInstructionConfig(BAR, BuySell.S, MON_04_JAN_2016, "6000"),
            };
            List<Instruction> instructions = InstructionFactory.create(config);
            
            Trades trades = StubbedInstructionsTests.createSettledTrades(instructions);
            int[] expectedIds = new int[] {5, 4, 3, 2, 1, 0};
            StubbedInstructionsTests.assertTradesRank(trades.getTrades(), expectedIds);
            
            // Total buys for all entities on the settlement date.
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOn(LocalDate.parse(MON_04_JAN_2016)));
            
            // Total buys for all entities on a non settlement date.
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOn(LocalDate.parse(SUN_03_JAN_2016)));
            
            // Total sells for all entities on the settlement date.
            assertEquals(new BigDecimal("21000.00"), trades.getTotalSettledIncommingOn(LocalDate.parse(MON_04_JAN_2016)));
            
            // Total buys for entities on the settlement date.
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), BAR));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), CAR));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), FOO));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), GLA));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), LAM));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), WIN));
            
            // Total buys for entities on non settlement date.
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), BAR));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), CAR));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), FOO));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), GLA));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), LAM));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), WIN));
            
            // Total sells for entities on the settlement date.
            assertEquals(new BigDecimal("6000.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), BAR));
            assertEquals(new BigDecimal("5000.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), CAR));
            assertEquals(new BigDecimal("4000.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), FOO));
            assertEquals(new BigDecimal("3000.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), GLA));
            assertEquals(new BigDecimal("2000.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), LAM));
            assertEquals(new BigDecimal("1000.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), WIN));
        }
        
        @Test
        public void mixedOrderMixedBuysellTrades() {
            
            MockInstructionConfig[] config = new MockInstructionConfig[] {
                new MockInstructionConfig(LAM, BuySell.B, MON_04_JAN_2016, "2000"),
                new MockInstructionConfig(WIN, BuySell.S, MON_04_JAN_2016, "1000"),
                new MockInstructionConfig(GLA, BuySell.S, MON_04_JAN_2016, "3000"),
                new MockInstructionConfig(BAR, BuySell.S, MON_04_JAN_2016, "6000"),
                new MockInstructionConfig(CAR, BuySell.S, MON_04_JAN_2016, "5000"),
                new MockInstructionConfig(FOO, BuySell.B, MON_04_JAN_2016, "4000"),
            };
            List<Instruction> instructions = InstructionFactory.create(config);
            
            Trades trades = StubbedInstructionsTests.createSettledTrades(instructions);
            int[] expectedIds = new int[] {3, 4, 5, 2, 0, 1};
            StubbedInstructionsTests.assertTradesRank(trades.getTrades(), expectedIds);
            
            // Total buys for all entities on the settlement date.
            assertEquals(new BigDecimal("6000.00"), trades.getTotalSettledOutgoingOn(LocalDate.parse(MON_04_JAN_2016)));
            
            // Total buys for all entities on a non settlement date.
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOn(LocalDate.parse(SUN_03_JAN_2016)));
            
            // Total sells for all entities on the settlement date.
            assertEquals(new BigDecimal("15000.00"), trades.getTotalSettledIncommingOn(LocalDate.parse(MON_04_JAN_2016)));
            
            // Total buys for entities on the settlement date.
            assertEquals(new BigDecimal(   "0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), BAR));
            assertEquals(new BigDecimal(   "0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), CAR));
            assertEquals(new BigDecimal("4000.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), FOO));
            assertEquals(new BigDecimal(   "0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), GLA));
            assertEquals(new BigDecimal("2000.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), LAM));
            assertEquals(new BigDecimal(   "0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), WIN));
            
            // Total buys for entities on non settlement date.
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), BAR));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), CAR));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), FOO));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), GLA));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), LAM));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), WIN));
            
            // Total sells for entities on the settlement date.
            assertEquals(new BigDecimal("6000.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), BAR));
            assertEquals(new BigDecimal("5000.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), CAR));
            assertEquals(new BigDecimal(   "0.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), FOO));
            assertEquals(new BigDecimal("3000.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), GLA));
            assertEquals(new BigDecimal(   "0.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), LAM));
            assertEquals(new BigDecimal("1000.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), WIN));
        }
        
        @Test
        public void mixedOrderMixedBuysellMultiTrades() {
            
            MockInstructionConfig[] config = new MockInstructionConfig[] {
                new MockInstructionConfig(LAM, BuySell.B, MON_04_JAN_2016, "2000"), // 0
                new MockInstructionConfig(LAM, BuySell.S, MON_04_JAN_2016, "2000"), // 1
                new MockInstructionConfig(LAM, BuySell.B, MON_04_JAN_2016, "2000"), // 2
                new MockInstructionConfig(WIN, BuySell.S, MON_04_JAN_2016, "1000"), // 3
                new MockInstructionConfig(GLA, BuySell.S, MON_04_JAN_2016, "3000"), // 4
                new MockInstructionConfig(BAR, BuySell.S, MON_04_JAN_2016, "6000"), // 5
                new MockInstructionConfig(CAR, BuySell.S, MON_04_JAN_2016, "5000"), // 6
                new MockInstructionConfig(FOO, BuySell.S, MON_04_JAN_2016, "4000"), // 7
                new MockInstructionConfig(FOO, BuySell.B, MON_04_JAN_2016, "4000")  // 8
            };
            List<Instruction> instructions = InstructionFactory.create(config);
            
            Trades trades = StubbedInstructionsTests.createSettledTrades(instructions);
            int[] expectedIds = new int[] {5, 6, 7, 8, 4, 0, 1, 2, 3};
            StubbedInstructionsTests.assertTradesRank(trades.getTrades(), expectedIds);
            
            // Total buys for all entities on the settlement date.
            assertEquals(new BigDecimal("8000.00"), trades.getTotalSettledOutgoingOn(LocalDate.parse(MON_04_JAN_2016)));
            
            // Total buys for all entities on a non settlement date.
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOn(LocalDate.parse(SUN_03_JAN_2016)));
            
            // Total sells for all entities on the settlement date.
            assertEquals(new BigDecimal("21000.00"), trades.getTotalSettledIncommingOn(LocalDate.parse(MON_04_JAN_2016)));
            
            // Total buys for entities on the settlement date.
            assertEquals(new BigDecimal(   "0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), BAR));
            assertEquals(new BigDecimal(   "0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), CAR));
            assertEquals(new BigDecimal("4000.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), FOO));
            assertEquals(new BigDecimal(   "0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), GLA));
            assertEquals(new BigDecimal("4000.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), LAM));
            assertEquals(new BigDecimal(   "0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(MON_04_JAN_2016), WIN));
            
            // Total buys for entities on non settlement date.
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), BAR));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), CAR));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), FOO));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), GLA));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), LAM));
            assertEquals(new BigDecimal("0.00"), trades.getTotalSettledOutgoingOnAndFor(LocalDate.parse(SUN_03_JAN_2016), WIN));
            
            // Total sells for entities on the settlement date.
            assertEquals(new BigDecimal("6000.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), BAR));
            assertEquals(new BigDecimal("5000.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), CAR));
            assertEquals(new BigDecimal("4000.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), FOO));
            assertEquals(new BigDecimal("3000.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), GLA));
            assertEquals(new BigDecimal("2000.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), LAM));
            assertEquals(new BigDecimal("1000.00"), trades.getTotalSettledIncommingOnAndFor(LocalDate.parse(MON_04_JAN_2016), WIN));
        }
    }
    
    /**
     * Tests for instructions on one day.
     */
    public static class MultiDayTests {
        @Test
        public void descendingBuyTrades() {
            
            MockInstructionConfig[] config = new MockInstructionConfig[] {
                new MockInstructionConfig(BAR, BuySell.B, MON_04_JAN_2016, "6000"), // 0
                new MockInstructionConfig(CAR, BuySell.B, MON_04_JAN_2016, "5000"), // 1
                new MockInstructionConfig(FOO, BuySell.B, MON_04_JAN_2016, "4000"), // 2
                new MockInstructionConfig(GLA, BuySell.B, MON_04_JAN_2016, "3000"), // 3
                new MockInstructionConfig(LAM, BuySell.B, MON_04_JAN_2016, "2000"), // 4
                new MockInstructionConfig(WIN, BuySell.B, MON_04_JAN_2016, "1000"), // 5
                
                new MockInstructionConfig(BAR, BuySell.B, TUE_05_JAN_2016, "6000"), // 6
                new MockInstructionConfig(CAR, BuySell.B, TUE_05_JAN_2016, "5000"), // 7
                new MockInstructionConfig(FOO, BuySell.B, TUE_05_JAN_2016, "4000"), // 8
                new MockInstructionConfig(GLA, BuySell.B, TUE_05_JAN_2016, "3000"), // 9
                new MockInstructionConfig(LAM, BuySell.B, TUE_05_JAN_2016, "2000"), // 10
                
                new MockInstructionConfig(BAR, BuySell.B, WED_06_JAN_2016, "6000"), // 11
                new MockInstructionConfig(CAR, BuySell.B, WED_06_JAN_2016, "5000"), // 12
                new MockInstructionConfig(FOO, BuySell.B, WED_06_JAN_2016, "4000"), // 13
                new MockInstructionConfig(GLA, BuySell.B, WED_06_JAN_2016, "3000"), // 14
                
                new MockInstructionConfig(BAR, BuySell.B, THU_07_JAN_2016, "6000"), // 15
                new MockInstructionConfig(CAR, BuySell.B, THU_07_JAN_2016, "5000"), // 16
                new MockInstructionConfig(FOO, BuySell.B, THU_07_JAN_2016, "4000"), // 17
                
                new MockInstructionConfig(BAR, BuySell.B, FRI_08_JAN_2016, "6000"), // 18
                new MockInstructionConfig(CAR, BuySell.B, FRI_08_JAN_2016, "5000"), // 19
            };
            List<Instruction> instructions = InstructionFactory.create(config);
            
            LocalDate from = LocalDate.parse(SUN_03_JAN_2016);
            LocalDate to = LocalDate.parse(SAT_09_JAN_2016);
            for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
                Trades trades = StubbedInstructionsTests.createSettledTradesOn(instructions, date);
                int[] expectedIds;
                if (LocalDate.parse(SUN_03_JAN_2016).equals(date)) {
                    expectedIds = new int[] {};
                } else if (LocalDate.parse(MON_04_JAN_2016).equals(date)) {
                    expectedIds = new int[] {0, 1, 2, 3, 4, 5};
                } else if (LocalDate.parse(TUE_05_JAN_2016).equals(date)) {
                    expectedIds = new int[] {6, 7, 8, 9, 10};
                } else if (LocalDate.parse(WED_06_JAN_2016).equals(date)) {
                    expectedIds = new int[] {11, 12, 13, 14};
                } else if (LocalDate.parse(THU_07_JAN_2016).equals(date)) {
                    expectedIds = new int[] {15, 16, 17};
                } else if (LocalDate.parse(FRI_08_JAN_2016).equals(date)) {
                    expectedIds = new int[] {18, 19};
                } else if (LocalDate.parse(SAT_09_JAN_2016).equals(date)) {
                    expectedIds = new int[] {};
                } else {
                    expectedIds = null;
                }
                StubbedInstructionsTests.assertTradesRank(trades.getTrades(), expectedIds);
            }
        }
        
        @Test
        public void ascendingBuyTrades() {
            
            MockInstructionConfig[] config = new MockInstructionConfig[] {
                new MockInstructionConfig(WIN, BuySell.B, MON_04_JAN_2016, "1000"), // 0
                new MockInstructionConfig(LAM, BuySell.B, MON_04_JAN_2016, "2000"), // 1
                new MockInstructionConfig(GLA, BuySell.B, MON_04_JAN_2016, "3000"), // 2
                new MockInstructionConfig(FOO, BuySell.B, MON_04_JAN_2016, "4000"), // 3
                new MockInstructionConfig(CAR, BuySell.B, MON_04_JAN_2016, "5000"), // 4
                new MockInstructionConfig(BAR, BuySell.B, MON_04_JAN_2016, "6000"), // 5
                
                new MockInstructionConfig(LAM, BuySell.B, TUE_05_JAN_2016, "2000"), // 6
                new MockInstructionConfig(GLA, BuySell.B, TUE_05_JAN_2016, "3000"), // 7
                new MockInstructionConfig(FOO, BuySell.B, TUE_05_JAN_2016, "4000"), // 8
                new MockInstructionConfig(CAR, BuySell.B, TUE_05_JAN_2016, "5000"), // 9
                new MockInstructionConfig(BAR, BuySell.B, TUE_05_JAN_2016, "6000"), // 10
                
                new MockInstructionConfig(GLA, BuySell.B, WED_06_JAN_2016, "3000"), // 11
                new MockInstructionConfig(FOO, BuySell.B, WED_06_JAN_2016, "4000"), // 12
                new MockInstructionConfig(CAR, BuySell.B, WED_06_JAN_2016, "5000"), // 13
                new MockInstructionConfig(BAR, BuySell.B, WED_06_JAN_2016, "6000"), // 14
                
                new MockInstructionConfig(FOO, BuySell.B, THU_07_JAN_2016, "4000"), // 15
                new MockInstructionConfig(CAR, BuySell.B, THU_07_JAN_2016, "5000"), // 16
                new MockInstructionConfig(BAR, BuySell.B, THU_07_JAN_2016, "6000"), // 17
                
                new MockInstructionConfig(CAR, BuySell.B, FRI_08_JAN_2016, "5000"), // 18
                new MockInstructionConfig(BAR, BuySell.B, FRI_08_JAN_2016, "6000"), // 19
            };
            List<Instruction> instructions = InstructionFactory.create(config);
            
            LocalDate from = LocalDate.parse(SUN_03_JAN_2016);
            LocalDate to = LocalDate.parse(SAT_09_JAN_2016);
            for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
                Trades trades = StubbedInstructionsTests.createSettledTradesOn(instructions, date);
                int[] expectedIds;
                if (LocalDate.parse(SUN_03_JAN_2016).equals(date)) {
                    expectedIds = new int[] {};
                } else if (LocalDate.parse(MON_04_JAN_2016).equals(date)) {
                    expectedIds = new int[] {5, 4, 3, 2, 1, 0};
                } else if (LocalDate.parse(TUE_05_JAN_2016).equals(date)) {
                    expectedIds = new int[] {10, 9, 8, 7, 6};
                } else if (LocalDate.parse(WED_06_JAN_2016).equals(date)) {
                    expectedIds = new int[] {14, 13, 12, 11};
                } else if (LocalDate.parse(THU_07_JAN_2016).equals(date)) {
                    expectedIds = new int[] {17, 16, 15};
                } else if (LocalDate.parse(FRI_08_JAN_2016).equals(date)) {
                    expectedIds = new int[] {19, 18};
                } else if (LocalDate.parse(SAT_09_JAN_2016).equals(date)) {
                    expectedIds = new int[] {};
                } else {
                    expectedIds = null;
                }
                StubbedInstructionsTests.assertTradesRank(trades.getTrades(), expectedIds);
            }
        }
        
        @Test
        public void tradesReportTest() {
            
            MockInstructionConfig[] config = new MockInstructionConfig[] {
                new MockInstructionConfig(BAR, BuySell.B, MON_04_JAN_2016, "6000"), // 0
                new MockInstructionConfig(CAR, BuySell.S, MON_04_JAN_2016, "5000"), // 1
                new MockInstructionConfig(FOO, BuySell.S, MON_04_JAN_2016, "4000"), // 2
                new MockInstructionConfig(GLA, BuySell.B, MON_04_JAN_2016, "3000"), // 3
                new MockInstructionConfig(GLA, BuySell.B, MON_04_JAN_2016, "3001"), // 4
                new MockInstructionConfig(GLA, BuySell.S, MON_04_JAN_2016, "3002"), // 5
                new MockInstructionConfig(LAM, BuySell.B, MON_04_JAN_2016, "2000"), // 6
                new MockInstructionConfig(LAM, BuySell.B, MON_04_JAN_2016, "2001"), // 7
                new MockInstructionConfig(LAM, BuySell.S, MON_04_JAN_2016, "2002"), // 8
                new MockInstructionConfig(WIN, BuySell.B, MON_04_JAN_2016, "1000"), // 9
                
                new MockInstructionConfig(BAR, BuySell.B, TUE_05_JAN_2016, "6000"), // 10
                new MockInstructionConfig(CAR, BuySell.S, TUE_05_JAN_2016, "5000"), // 11
                new MockInstructionConfig(FOO, BuySell.S, TUE_05_JAN_2016, "4000"), // 12
                new MockInstructionConfig(GLA, BuySell.B, TUE_05_JAN_2016, "3000"), // 13
                new MockInstructionConfig(GLA, BuySell.B, TUE_05_JAN_2016, "3001"), // 14
                new MockInstructionConfig(GLA, BuySell.S, TUE_05_JAN_2016, "3002"), // 15
                new MockInstructionConfig(LAM, BuySell.B, TUE_05_JAN_2016, "2000"), // 16
                new MockInstructionConfig(LAM, BuySell.B, TUE_05_JAN_2016, "2001"), // 17
                new MockInstructionConfig(LAM, BuySell.S, TUE_05_JAN_2016, "2002"), // 18
                
                new MockInstructionConfig(BAR, BuySell.B, WED_06_JAN_2016, "6000"), // 19
                new MockInstructionConfig(CAR, BuySell.S, WED_06_JAN_2016, "5000"), // 20
                new MockInstructionConfig(FOO, BuySell.S, WED_06_JAN_2016, "4000"), // 21
                new MockInstructionConfig(GLA, BuySell.B, WED_06_JAN_2016, "3000"), // 22
                new MockInstructionConfig(GLA, BuySell.B, WED_06_JAN_2016, "3001"), // 23
                new MockInstructionConfig(GLA, BuySell.S, WED_06_JAN_2016, "3002"), // 24
                
                new MockInstructionConfig(BAR, BuySell.B, THU_07_JAN_2016, "6000"), // 25
                new MockInstructionConfig(CAR, BuySell.S, THU_07_JAN_2016, "5000"), // 26
                new MockInstructionConfig(FOO, BuySell.S, THU_07_JAN_2016, "4000"), // 27
                
                new MockInstructionConfig(BAR, BuySell.B, FRI_08_JAN_2016, "6000"), // 28
                new MockInstructionConfig(CAR, BuySell.S, FRI_08_JAN_2016, "5000"), // 29
            };
            List<Instruction> instructions = InstructionFactory.create(config);
            
            LocalDate from = LocalDate.parse(SUN_03_JAN_2016);
            LocalDate to = LocalDate.parse(SAT_09_JAN_2016);
            for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
                Trades trades = StubbedInstructionsTests.createSettledTradesOn(instructions, date);
                assertReportOneDayOfTrades(trades, date);
            }
        }
        
        /**
         * Assert one day of trades as if being reported.
         * @param trades
         * @param date 
         */
        private void assertReportOneDayOfTrades(Trades trades, LocalDate date) {
            if (LocalDate.parse(SUN_03_JAN_2016).equals(date)) {
                this.assertReportOneDayOfTrades(
                    trades, 
                    date, 
                    new String[] {},
                    new String[] {}, // highest
                    new String[] {}, // out / buy
                    new String[] {}, // in / sell
                    "0.00",
                    "0.00"
                    );
            } else if (LocalDate.parse(MON_04_JAN_2016).equals(date)) {
                this.assertReportOneDayOfTrades(
                    trades, 
                    date, 
                    new String[] {     BAR,        CAR,       FOO,       GLA,       LAM,      WIN},
                    new String[] {"6000.00", "5000.00", "4000.00", "3002.00", "2002.00", "1000.00"}, // highest
                    new String[] {"6000.00",    "0.00",    "0.00", "6001.00", "4001.00", "1000.00"}, // out / buy
                    new String[] {   "0.00", "5000.00", "4000.00", "3002.00", "2002.00",    "0.00"}, // in / sell
                    "17002.00",
                    "14004.00"
                    );
            } else if (LocalDate.parse(TUE_05_JAN_2016).equals(date)) {
                this.assertReportOneDayOfTrades(
                    trades, 
                    date, 
                    new String[] {     BAR,        CAR,      FOO,        GLA,      LAM},
                    new String[] {"6000.00", "5000.00", "4000.00", "3002.00", "2002.00"}, // highest
                    new String[] {"6000.00",    "0.00",    "0.00", "6001.00", "4001.00"}, // out / buy
                    new String[] {   "0.00", "5000.00", "4000.00", "3002.00", "2002.00"}, // in / sell
                    "16002.00",
                    "14004.00"
                    );
            } else if (LocalDate.parse(WED_06_JAN_2016).equals(date)) {
                this.assertReportOneDayOfTrades(
                    trades, 
                    date, 
                    new String[] {      BAR,       CAR,       FOO,       GLA},
                    new String[] {"6000.00", "5000.00", "4000.00", "3002.00"}, // highest
                    new String[] {"6000.00",    "0.00",    "0.00", "6001.00"}, // out / buy
                    new String[] {   "0.00", "5000.00", "4000.00", "3002.00"}, // in / sell
                    "12001.00",
                    "12002.00"
                    );
            } else if (LocalDate.parse(THU_07_JAN_2016).equals(date)) {
                this.assertReportOneDayOfTrades(
                    trades, 
                    date, 
                    new String[] {     BAR,        CAR,      FOO},
                    new String[] {"6000.00", "5000.00", "4000.00"}, // highest
                    new String[] {"6000.00",    "0.00",    "0.00"}, // out / buy
                    new String[] {   "0.00", "5000.00", "4000.00"}, // in / sell
                    "6000.00",
                    "9000.00"
                    );
            } else if (LocalDate.parse(FRI_08_JAN_2016).equals(date)) {
                this.assertReportOneDayOfTrades(
                    trades, 
                    date, 
                    new String[] {     BAR,        CAR},
                    new String[] {"6000.00", "5000.00"}, // highest
                    new String[] {"6000.00",    "0.00"}, // out / buy
                    new String[] {   "0.00", "5000.00"}, // in / sell
                    "6000.00",
                    "5000.00"
                    );
            } else if (LocalDate.parse(SAT_09_JAN_2016).equals(date)) {
                this.assertReportOneDayOfTrades(
                    trades, 
                    date, 
                    new String[] {},
                    new String[] {}, // highest
                    new String[] {}, // out / buy
                    new String[] {}, // in / sell
                    "0.00",
                    "0.00"
                    );
            } else {
            }
        }
        
        /**
         * Assert one day of trades as if being reported.
         * @param trades
         * @param date
         * @param expectedEntity
         * @param expectedhighestSettlementAmount
         * @param expectedTotalOutOnFor
         * @param expectedTotalInOnFor
         * @param expectedTotalOut
         * @param expectedTotalIn 
         */
        private void assertReportOneDayOfTrades(
                Trades trades, 
                LocalDate date,
                String[] expectedEntity, 
                String[] expectedhighestSettlementAmount,
                String[] expectedTotalOutOnFor,
                String[] expectedTotalInOnFor,
                String expectedTotalOut,
                String expectedTotalIn) {
            
            // The instructiosn are already ranked for entity with highest settlement aount.
            List<Instruction> instructions = trades.getTrades();
            String lastEntity = null;
            for (int i = 0, j = -1; i < instructions.size(); i++) {
                // Report one row per entity per day.
                Instruction ins = instructions.get(i);
                String entity = ins.getEntity();
                if (entity.equals(lastEntity)) {
                    continue;
                }
                
                ++j;
                lastEntity = entity;
                assertEquals(expectedEntity[j], entity);
                
                // Total trades in and out for the enity on the day.
                BigDecimal totalOutOnFor = trades.getTotalSettledOutgoingOnAndFor(date, entity);
                BigDecimal totalInOnFor = trades.getTotalSettledIncommingOnAndFor(date, entity);
                assertEquals(new BigDecimal(expectedTotalOutOnFor[j]), totalOutOnFor);
                assertEquals(new BigDecimal(expectedTotalInOnFor[j]), totalInOnFor);
                
                // TODO Fix the mock so that it can get the max settlement amount or it is a Mockito bug.
                // Get the highest settlement amount for the entity on the day.
//                Optional<BigDecimal> highestSettlementAmount = trades.getTradesOnAndFor(date, Optional.of(entity))..stream()
//                    .max(Comparator.naturalOrder());
//                assertTrue(highestSettlementAmount.isPresent());
//                assertEquals(new BigDecimal(expectedhighestSettlementAmount[j]), highestSettlementAmount.get());
            }

            // Totals trades for all entities in and out on the day.
            BigDecimal totalOut = trades.getTotalSettledOutgoingOn(date);
            BigDecimal totalIn = trades.getTotalSettledIncommingOn(date);
            assertEquals(new BigDecimal(expectedTotalOut), totalOut);
            assertEquals(new BigDecimal(expectedTotalIn), totalIn);
        }
    }
    
    /**
     * A factory class for creating mock instructions.
     */
    private static class InstructionFactory {
        
        /**
         * Create a collection of mock instructions.
         * @return The trade instructions.
         * @return 
         */
        static List<Instruction> create(MockInstructionConfig... config) {
            List<Instruction> instructions = new ArrayList<>();
            for (int i = 0; i < config.length; i++) { 
                instructions.add(create(i, config[i]));
            }
            
            return instructions;
        }
        
        /**
         * Create a mock instruction.
         * @param id
         * @return 
         */
        static Instruction create(int id, MockInstructionConfig config) {
            Instruction ins = mock(Instruction.class);
            when(ins.getId()).thenReturn(id);
            when(ins.getEntity()).thenReturn(config.entity);
            when(ins.getBuySell()).thenReturn(config.buysell);
            when(ins.getEffectiveSettlementDate()).thenReturn(LocalDate.parse(config.effectiveSettlementDate));
            when(ins.getSettledAmount()).thenReturn(Optional.of(new BigDecimal(config.settledAmount)));
            when(ins.isSettled()).thenReturn(true);
            when(ins.compareTo(any(Instruction.class))).thenCallRealMethod();
            return ins;
        }
    }
}
