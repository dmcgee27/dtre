/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit tests for class {@link Instruction}.
 * @author douglasmcgee
 */
public class InstructionTest {
    
    private static final String CURRENCY_CODE_AED = "AED";
    private static final String CURRENCY_CODE_GBP = "GBP";
    private static final String CURRENCY_CODE_SAR = "SAR";
    private static final String CURRENCY_CODE_SGD = "SGD";
    
    private static final List<DayOfWeek> SUNDAY_TO_THURSDAY = Arrays.asList(
        new DayOfWeek [] {
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY
        }
    );
    
    private static final Map<String, List<DayOfWeek>> DEFAULT_CURRENCY_TO_WORKING_WEEK_MAP = new HashMap<>();
    static {
        DEFAULT_CURRENCY_TO_WORKING_WEEK_MAP.put(CURRENCY_CODE_AED, SUNDAY_TO_THURSDAY);
        DEFAULT_CURRENCY_TO_WORKING_WEEK_MAP.put(CURRENCY_CODE_SAR, SUNDAY_TO_THURSDAY);
    };
    
    @Test
    public void createInstructionTest() {
        Instruction instruction = this.create();
        assertEquals("foo", instruction.getEntity());
        assertEquals(BuySell.B, instruction.getBuySell());
        assertEquals(new BigDecimal("0.50"), instruction.getAgreedFx());
        assertEquals(CURRENCY_CODE_SGD, instruction.getCurrencyCode());
        assertEquals(LocalDate.parse("2016-01-01"), instruction.getInstructionDate());
        assertEquals(LocalDate.parse("2016-01-02"), instruction.getSettlementDate());
        assertEquals(200, instruction.getUnits());
        assertEquals(new BigDecimal("100.25"), instruction.getUnitPrice());
        assertFalse(instruction.getSettledAmount().isPresent());
    }
    
    @Test
    public void createBuyInstructionTest() {
        Instruction instruction = this.create();
        assertEquals(BuySell.B, instruction.getBuySell());
    }
    
    @Test
    public void createSellInstructionTest() {
        Instruction instruction = this.create()
            .buySell("S");
        assertEquals(BuySell.S, instruction.getBuySell());
    }
    
    @Test (expected = DtreException.class)
    public void createInstructionBadBuySellTest() {
        this.create()
            .buySell("BOGUS");
    }
    
    @Test (expected = DtreException.class)
    public void createInstructionBadAgreedFxTest() {
        this.create()
            .agreedFx("BOGUS");
    }
    
    @Test (expected = DtreException.class)
    public void createInstructionBadCurrencyCodeTest() {
        this.create()
            .currencyCode("BOGUS");
    }
    
    @Test (expected = DtreException.class)
    public void createInstructionBadInstructionDateTest() {
        this.create()
            .instructionDate("1 Jan 2016");
    }
    
    @Test (expected = DtreException.class)
    public void createInstructionBadSettlementDateTest() {
        this.create()
            .settlementDate("01 JAN 2016");
    }
    
    @Test (expected = DtreException.class)
    public void createInstructionBadUnitsTest() {
        this.create()
            .units(0);
    }
    
    @Test (expected = DtreException.class)
    public void createInstructionBadUnitPriceTest() {
        this.create()
            .unitPrice("BOGUS");
    }
    
    @Test 
    public void isSettleableTest() {
        Instruction instruction = this.create()
            .settlementDate("01 Jan 2016");
        assertEquals(LocalDate.parse("2016-01-01"), instruction.calculateEffectiveSettlementDate());
        assertTrue(instruction.isSettleable());
    }
    
    @Test 
    public void isNotSettleableTest() {
        Instruction instruction = this.create()
            .settlementDate("01 Dec 2016");
        assertEquals(LocalDate.parse("2016-12-01"), instruction.calculateEffectiveSettlementDate());
        assertFalse(instruction.isSettleable());
    }
    
    @Test 
    public void settlementAmountTestOnWorkingDay() {
        Instruction instruction = this.create()
            .settlementDate("01 Jan 2016");
        this.assertSettlementTest(instruction, "2016-01-01", "10025.00");
    }
    
    @Test 
    public void settlementAmountNextWorkingDayTest() {
        Instruction instruction = this.create();
        this.assertSettlementTest(instruction, "2016-01-04", "10025.00");
    }
    
    /**
     * Assert the settlement of an instruction.
     * @param instruction
     * @param expectedSettlementDate
     * @param expectedSettledAmount 
     */
    private void assertSettlementTest(Instruction instruction, String expectedSettlementDate, String expectedSettledAmount) {
        assertEquals(LocalDate.parse(expectedSettlementDate), instruction.calculateEffectiveSettlementDate());
        assertTrue(instruction.isSettleable());
        assertFalse(instruction.isSettled());
        instruction.settle();
        assertTrue(instruction.isSettled());
        assertTrue(instruction.getSettledAmount().isPresent());
//        BigDecimal amount = instruction.getUnitPrice().multiply(instruction.getAgreedFx()).multiply(new BigDecimal(instruction.getUnits()));
//        BigDecimal amount2 = instruction.getUnitPrice().multiply(instruction.getAgreedFx()).multiply(new BigDecimal(instruction.getUnits())).setScale(2, RoundingMode.HALF_EVEN);
//        System.out.println("amount = " + amount + ", amount2 = " + amount2);
        assertEquals(new BigDecimal(expectedSettledAmount), instruction.getSettledAmount().get());
    }
    
    @Test 
    public void settlementAmountRoundDownTest() {
        this.assertSettlementAmountRoundingTest("2506.25", "25.062544", "100.00", 1);
    }
    
    @Test 
    public void settlementAmountRoundUpTest() {
        this.assertSettlementAmountRoundingTest("2506.27", "25.06266", "100.00", 1);
    }
    
    @Test 
    public void settlementAmountRoundUpEvenTest() {
        this.assertSettlementAmountRoundingTest("2506.24", "25.06235", "100.00", 1);
    }
    
    /**
     * Assert the rounding of the settlement value.
     * @param expectedSettledAmount
     * @param agreedFx
     * @param unitPrice
     * @param units 
     */
    private void assertSettlementAmountRoundingTest(String expectedSettledAmount, String agreedFx, String unitPrice, int units) {
        Instruction instruction = this.create()
            .settlementDate("01 Jan 2016")
            .agreedFx(agreedFx)
            .unitPrice(unitPrice)
            .units(units);
        this.assertSettlementTest(instruction, "2016-01-01", expectedSettledAmount);
    }
    
    @Test
    public void firstUnsettledSecondSettledCompareTest() {
        Instruction instruction1 = this.create(); 
        Instruction instruction2 = this.create();
        instruction2.settle();
        assertEquals(-1, instruction1.compareTo(instruction2));
    }
    
    @Test
    public void firstUnsettledSecondUnettledCompareTest() {
        Instruction instruction1 = this.create(); 
        Instruction instruction2 = this.create();
        assertEquals(0, instruction1.compareTo(instruction2));
    }
    
    @Test
    public void firstSettledSecondUnsettledCompareTest() {
        Instruction instruction1 = this.create(); 
        instruction1.settle();
        Instruction instruction2 = this.create();
        assertEquals(1, instruction1.compareTo(instruction2));
    }
    
    @Test
    public void firstSettledAmountLessThanSecondTest() {
        Instruction instruction1 = this.create()
            .units(1); 
        Instruction instruction2 = this.create();
        instruction1.settle();
        instruction2.settle();
        assertEquals(-1, instruction1.compareTo(instruction2));
    }
    
    @Test
    public void settledAmountsAreEqualTest() {
        Instruction instruction1 = this.create();
        Instruction instruction2 = this.create();
        instruction1.settle();
        instruction2.settle();
        assertEquals(0, instruction1.compareTo(instruction2));
    }
    
    @Test
    public void firstSettledAmountGreaterThanSecondTest() {
        Instruction instruction1 = this.create()
            .units(1000); 
        Instruction instruction2 = this.create();
        instruction1.settle();
        instruction2.settle();
        assertEquals(1, instruction1.compareTo(instruction2));
    }
    
    /**
     * Create a {@link Instruction} instance.
     * @return 
     */
    private Instruction create() {
        return this.create(
            "foo",
            "B",
            "0.50",
            CURRENCY_CODE_SGD,
            "01 Jan 2016",
            "02 Jan 2016",      // SATURDAY
            200,
            "100.25");
    }
    
    /**
     * Create a {@link Instruction} instance.
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
    private Instruction create(
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
            .clock(Clock.fixed(Instant.parse("2016-02-01T00:00:00Z"), ZoneOffset.UTC))
            .workingWeek(new WorkingWeek(DEFAULT_CURRENCY_TO_WORKING_WEEK_MAP))
            ;
    }
    
}
