/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.trade;

import com.dmg27.dtre.core.BuySell;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class represents trades made.
 * @author douglasmcgee
 */
final public class Trades {
    
    private List<Instruction> tradesCltn = new ArrayList<>();
    
    private WorkingWeek workingWeek;
    
    public Trades tradesCltn(List<Instruction> tradesCltn) {
        this.tradesCltn = tradesCltn;
        int id = 0;
        for (Instruction instruction : this.tradesCltn) {
            instruction.id(id++);
        }
        
        return this;
    }

    List<Instruction> getTradesCltn() {
        return tradesCltn;
    }
    
    public List<Instruction> getTradesOn(LocalDate date) {
        List<Instruction> result = this.tradesCltn.stream()
            .filter(i -> date.equals(i.calculateEffectiveSettlementDate()))
            .collect(Collectors.toList());
        return result;
    }
    
    public List<Instruction> getTradesSorted(List<Instruction> trades) {
        List<Instruction> result = trades.stream()
            .sorted()
            .collect(Collectors.toList());
        return result;
    }
    
    public Trades workingWeek(WorkingWeek workingWeek) {
        this.workingWeek = workingWeek;
        return this;
    }

    WorkingWeek getWorkingWeek() {
        return this.workingWeek;
    }
    
    public void settle() {
        this.settle(this.tradesCltn);
    }
    
    public void settleOn(LocalDate date) {
        this.settle(this.getTradesOn(date));
    }
    
    public void settle(List<Instruction> trades) {
        trades.stream()
            .forEach(Instruction::settle);
    }
    
    public BigDecimal getTotalSettledIncommingOn(LocalDate date) {
        return getTotalSettledOnAndFor(BuySell.S, date, Optional.empty());
    }
    
    public BigDecimal getTotalSettledOutgoingOn(LocalDate date) {
        return getTotalSettledOnAndFor(BuySell.B, date, Optional.empty());
    }
    
    public BigDecimal getTotalSettledIncommingOnAndFor(LocalDate date, String entity) {
        return getTotalSettledOnAndFor(BuySell.S, date, Optional.of(entity));
    }
    
    public BigDecimal getTotalSettledOutgoingOnAndFor(LocalDate date, String entity) {
        return getTotalSettledOnAndFor(BuySell.B, date, Optional.of(entity));
    }
    
    BigDecimal getTotalSettledOnAndFor(BuySell buySell, LocalDate date, Optional<String> entity) {
        List<BigDecimal> settlementAmounts = this.tradesCltn.stream()
            .filter(i -> (date.equals(i.calculateEffectiveSettlementDate()) 
                && (entity.isPresent() ? entity.get().equals(i.getEntity()) : true)
                && i.getBuySell() == buySell))
            .map(i -> i.getSettledAmount())
            .filter(o -> o.isPresent())
            .map(o -> o.get())
            .collect(Collectors.toList());
                
        BigDecimal settled = new BigDecimal("0.00");
        for (BigDecimal amount : settlementAmounts) {
            settled = settled.add(amount);
        }
        
        return settled;
    }
    
    
}
