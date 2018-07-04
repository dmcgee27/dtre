/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.trade;

import com.dmg27.dtre.core.DtreException;
import static com.dmg27.dtre.trade.Instruction.NO_ID;
import static com.dmg27.dtre.trade.WorkingWeek.*;
import static com.dmg27.dtre.trade.WorkingWeekTestConstants.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit tests for class {@link Instruction}.
 * @author douglasmcgee
 */
public class InstructionTest {
    
    @Test
    public void createInstructionTest() {
        Instruction instruction = this.createInstruction();
        assertEquals("foo", instruction.getEntity());
        assertEquals(BuySell.B, instruction.getBuySell());
        assertEquals(new BigDecimal("0.50"), instruction.getAgreedFx());
        assertEquals(CURRENCY_CODE_SGD, instruction.getCurrencyCode());
        assertEquals(LocalDate.parse("2016-01-01"), instruction.getInstructionDate());
        assertEquals(LocalDate.parse("2016-01-02"), instruction.getSettlementDate());
        assertEquals(200, instruction.getUnits());
        assertEquals(new BigDecimal("100.25"), instruction.getUnitPrice());
        assertFalse(instruction.getSettledAmount().isPresent());
        assertEquals(NO_ID, instruction.getId());
    }
    
    @Test
    public void setInstructionIdTest() {
        Instruction instruction = this.createInstruction()
            .id(1234);
        assertEquals(1234, instruction.getId());
        
    }
    
    @Test
    public void setInstructionEqualityTest() {
        Instruction instruction1 = this.createInstruction();
        Instruction instruction2 = this.createInstruction();
        assertFalse(instruction1.equals(instruction2));
        assertFalse(instruction2.equals(instruction1));
        
        instruction1.id(1234);
        instruction2.id(1234);
        assertTrue(instruction1.equals(instruction2));
        assertTrue(instruction2.equals(instruction1));
        
        instruction1.id(1234);
        instruction2.id(4321);
        assertFalse(instruction1.equals(instruction2));
        assertFalse(instruction2.equals(instruction1));
    }
    
    @Test
    public void createBuyInstructionTest() {
        Instruction instruction = this.createInstruction();
        assertEquals(BuySell.B, instruction.getBuySell());
    }
    
    @Test
    public void createSellInstructionTest() {
        Instruction instruction = this.createInstruction()
            .buySell("S");
        assertEquals(BuySell.S, instruction.getBuySell());
    }
    
    @Test (expected = DtreException.class)
    public void createInstructionBadBuySellTest() {
        this.createInstruction()
            .buySell("BOGUS");
    }
    
    @Test (expected = DtreException.class)
    public void createInstructionBadAgreedFxTest() {
        this.createInstruction()
            .agreedFx("BOGUS");
    }
    
    @Test (expected = DtreException.class)
    public void createInstructionBadCurrencyCodeTest() {
        this.createInstruction()
            .currencyCode("BOGUS");
    }
    
    @Test (expected = DtreException.class)
    public void createInstructionBadInstructionDateTest() {
        this.createInstruction()
            .instructionDate("1 Jan 2016");
    }
    
    @Test (expected = DtreException.class)
    public void createInstructionBadSettlementDateTest() {
        this.createInstruction()
            .settlementDate("01 JAN 2016");
    }
    
    @Test (expected = DtreException.class)
    public void createInstructionBadUnitsTest() {
        this.createInstruction()
            .units(0);
    }
    
    @Test (expected = DtreException.class)
    public void createInstructionBadUnitPriceTest() {
        this.createInstruction()
            .unitPrice("BOGUS");
    }
    
    @Test 
    public void isSettleableTest() {
        Instruction instruction = this.createInstruction()
            .settlementDate("01 Jan 2016");
        assertEquals(LocalDate.parse("2016-01-01"), instruction.getEffectiveSettlementDate());
        assertTrue(instruction.isSettleable());
    }
    
    @Test 
    public void isNotSettleableTest() {
        Instruction instruction = this.createInstruction()
            .settlementDate("01 Dec 2016");
        assertEquals(LocalDate.parse("2016-12-01"), instruction.getEffectiveSettlementDate());
        assertFalse(instruction.isSettleable());
    }
    
    @Test 
    public void settlementAmountTestOnWorkingDay() {
        Instruction instruction = this.createInstruction()
            .settlementDate("01 Jan 2016");
        this.assertSettlementTest(instruction, "2016-01-01", "10025.00");
    }
    
    @Test 
    public void settlementAmountNextWorkingDayTest() {
        Instruction instruction = this.createInstruction()
            .unitPrice("150.5");
        this.assertSettlementTest(instruction, "2016-01-04", "15050.00");
    }
    
    /**
     * Assert the settlement of an instruction.
     * @param instruction
     * @param expectedSettlementDate
     * @param expectedSettledAmount 
     */
    private void assertSettlementTest(Instruction instruction, String expectedSettlementDate, String expectedSettledAmount) {
        assertEquals(LocalDate.parse(expectedSettlementDate), instruction.getEffectiveSettlementDate());
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
        Instruction instruction = this.createInstruction()
            .settlementDate("01 Jan 2016")
            .agreedFx(agreedFx)
            .unitPrice(unitPrice)
            .units(units);
        this.assertSettlementTest(instruction, "2016-01-01", expectedSettledAmount);
    }
    
    @Test
    public void firstUnsettledSecondSettledCompareTest() {
        Instruction instruction1 = this.createInstruction(); 
        Instruction instruction2 = this.createInstruction();
        instruction2.settle();
        assertEquals(1, instruction1.compareTo(instruction2));
    }
    
    @Test
    public void firstUnsettledSecondUnettledCompareTest() {
        Instruction instruction1 = this.createInstruction(); 
        Instruction instruction2 = this.createInstruction();
        assertEquals(0, instruction1.compareTo(instruction2));
    }
    
    @Test
    public void firstSettledSecondUnsettledCompareTest() {
        Instruction instruction1 = this.createInstruction(); 
        instruction1.settle();
        Instruction instruction2 = this.createInstruction();
        assertEquals(-1, instruction1.compareTo(instruction2));
    }
    
    @Test
    public void firstSettledAmountLessThanSecondTest() {
        Instruction instruction1 = this.createInstruction()
            .units(1); 
        Instruction instruction2 = this.createInstruction();
        instruction1.settle();
        instruction2.settle();
        assertEquals(1, instruction1.compareTo(instruction2));
    }
    
    @Test
    public void settledAmountsAreEqualTest() {
        Instruction instruction1 = this.createInstruction();
        Instruction instruction2 = this.createInstruction();
        instruction1.settle();
        instruction2.settle();
        assertEquals(0, instruction1.compareTo(instruction2));
    }
    
    @Test
    public void firstSettledAmountGreaterThanSecondTest() {
        Instruction instruction1 = this.createInstruction()
            .units(1000); 
        Instruction instruction2 = this.createInstruction();
        instruction1.settle();
        instruction2.settle();
        assertEquals(-1, instruction1.compareTo(instruction2));
    }
    
    /**
     * Create a {@link Instruction} instance.
     * @return 
     */
    private Instruction createInstruction() {
        return createInstruction(
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
            .clock(CLOCK_MON_01_FEB_2016)
            .workingWeek(DEMO_WORKING_WEEK)
            ;
    }
    
}
