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
import java.util.Comparator;
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
    
    public Trades trades(List<Instruction> tradesCltn) {
        this.tradesCltn = tradesCltn;
        int id = 0;
        for (Instruction instruction : this.tradesCltn) {
            instruction.id(id++);
        }
        
        return this;
    }

    List<Instruction> getTrades() {
        return tradesCltn;
    }
    
    public List<Instruction> getTradesOn(LocalDate date) {
        return this.getTradesOnAndFor(date, Optional.empty());
    }
    
    public List<Instruction> getTradesOnAndFor(LocalDate date, Optional<String> entity) {
        List<Instruction> result = this.tradesCltn.stream()
            .filter(i -> date.equals(i.getEffectiveSettlementDate()) 
                && (entity.isPresent() ? entity.get().equals(i.getEntity()) : true))
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
    
    public Trades settle() {
        this.tradesCltn = this.settle(this.tradesCltn);
        return this;
    }
    
    public Trades settle(LocalDate date) {
        this.tradesCltn = this.settle(this.getTradesOn(date));
        return this;
    }
    
    public List<Instruction> settle(List<Instruction> trades) {
        trades.stream()
            .forEach(Instruction::settle);
        return trades.stream()
            .sorted()
            .collect(Collectors.toList());
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
        List<BigDecimal> settlementAmounts = this.getTradesOnAndFor(date, entity).stream()
            .filter(i -> i.getBuySell() == buySell)
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
