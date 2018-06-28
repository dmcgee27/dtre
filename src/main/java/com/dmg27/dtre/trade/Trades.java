/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.trade;

import java.util.ArrayList;
import java.util.List;

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
    
    public Trades workingWeek(WorkingWeek workingWeek) {
        this.workingWeek = workingWeek;
        return this;
    }

    WorkingWeek getWorkingWeek() {
        return workingWeek;
    }
    
    public void settle() {
        this.tradesCltn.stream()
            .forEach(Instruction::settle);
    }
}
