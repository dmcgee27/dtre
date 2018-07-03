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
import static com.dmg27.dtre.trade.TradesTest.StubbedInstructionsTests.createSettledTrades;
import static com.dmg27.dtre.view.ViewFactory.textualAccountTransactionsView;
import static com.dmg27.dtre.trade.WorkingWeekTestConstants.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Unit tests for the view classes
 * @author douglasmcgee
 */
public class ViewTest {
    
    @Test
    public void basicOneDayNoInstructionsTest() {
        List<Instruction> instructions = new ArrayList<>();
        Trades trades = createSettledTrades(instructions);
        String report = textualAccountTransactionsView(trades, LocalDate.parse(MON_04_JAN_2016), LocalDate.parse(MON_04_JAN_2016));
        System.out.println(report);
    }
    
    @Test
    public void basicThreeDayNoInstructionsTest() {
        List<Instruction> instructions = new ArrayList<>();
        Trades trades = createSettledTrades(instructions);
        String report = textualAccountTransactionsView(trades, LocalDate.parse(MON_04_JAN_2016), LocalDate.parse(WED_06_JAN_2016));
        System.out.println(report);
    }
}

