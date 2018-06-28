/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.core;

import com.dmg27.dtre.util.Util;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * This class represents a trade instruction.
 * @author douglasmcgee
 */
final public class Instruction implements Comparable {
    
    private String entity;
    
    private BuySell buySell;
    
    private BigDecimal agreedFx;
    
    private String currencyCode;
    
    private LocalDate instructionDate;
    
    private LocalDate settlementDate;
    
    private int units;
    
    private BigDecimal unitPrice;
    
    private Clock clock = Clock.systemUTC();
    
    private Optional<BigDecimal> settledAmount = Optional.empty();
    
    private WorkingWeek workingWeek;
    
    public Instruction entity(String entity) {
        this.entity = entity;
        return this;
    }

    public String getEntity() {
        return entity;
    }
    
    public Instruction buySell(String value) {
        try {
            this.buySell = BuySell.valueOf(value);
            return this;
        } catch (IllegalArgumentException ex) {
            throw new DtreException(MessageFormat.format("bad Instruction buy/sell value \"{0}\"", value), ex);
        }
    }

    public BuySell getBuySell() {
        return buySell;
    }
    
    public Instruction agreedFx(String agreedFxString) {
        try {
            this.agreedFx = new BigDecimal(agreedFxString);
            return this;
        } catch (NumberFormatException ex) {
            throw new DtreException(MessageFormat.format("bad Instruction agreedFx value \"{0}\"", agreedFxString), ex);
        }
    }

    public BigDecimal getAgreedFx() {
        return agreedFx;
    }
    
    public Instruction currencyCode(String currencyCode) {
        Util.validateCurrencyCode(currencyCode);
        this.currencyCode = currencyCode;
        return this;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
    
    public Instruction instructionDate(String dateString) {
        try {
            this.instructionDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd MMM uuuu"));
            return this;
        } catch (IllegalArgumentException | DateTimeParseException ex) {
            throw new DtreException(MessageFormat.format("bad Instruction instruction date \"{0}\"", dateString), ex);
        }
    }

    public LocalDate getInstructionDate() {
        return instructionDate;
    }
    
    public Instruction settlementDate(String dateString) {
        try {
            this.settlementDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd MMM uuuu"));
            return this;
        } catch (IllegalArgumentException | DateTimeParseException ex) {
            throw new DtreException(MessageFormat.format("bad Instruction settlement date \"{0}\"", dateString), ex);
        }
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }
    
    public Instruction units(int units) {
        if (units <= 0) {
            throw new DtreException(MessageFormat.format("bad Instruction units \"{0}\"", units));
        }
        
        this.units = units;
        return this;
    }

    public int getUnits() {
        return units;
    }
    
    public Instruction unitPrice(String unitPriceString) {
        try {
            this.unitPrice = new BigDecimal(unitPriceString);
            return this;
        } catch (NumberFormatException ex) {
            throw new DtreException(MessageFormat.format("bad Instruction unit price value \"{0}\"", unitPriceString), ex);
        }
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public Instruction clock(Clock clock) {
        this.clock = clock;
        return this;
    }
    
    public Instruction workingWeek(WorkingWeek workingWeek) {
        this.workingWeek = workingWeek;
        return this;
    }
    
    public void settle() {
        if (!this.isSettleable()) {
            return;
        }
        
        this.settledAmount = Optional.of(this.calculateSettlementAmount());
    }

    public Optional<BigDecimal> getSettledAmount() {
        return this.settledAmount;
    }
    
    boolean isSettleable() {
        LocalDate effectiveSettlementDate = this.calculateEffectiveSettlementDate();
        LocalDate now = LocalDate.now(this.clock);
        return !effectiveSettlementDate.isAfter(now);
    }
    
    LocalDate calculateEffectiveSettlementDate() {
        LocalDate effectiveSettlementDate = this.workingWeek.getWorkingDate(this.currencyCode, this.settlementDate);
        return effectiveSettlementDate;
    }
    
    BigDecimal calculateSettlementAmount() {
        BigDecimal amount = this.unitPrice.multiply(this.agreedFx).multiply(new BigDecimal(this.units)).setScale(2, RoundingMode.HALF_EVEN);
        return amount;
    }
    
    public boolean isSettled() {
        return this.settledAmount.isPresent();
    }

    @Override
    public int compareTo(Object o) {
        Instruction other = (Instruction) o;
        if (!this.isSettled() && other.isSettled()) {
            return -1;
        }
        
        if (!this.isSettled() && !other.isSettled()) {
            return 0;
        }
        
        if (this.isSettled() && !other.isSettled()) {
            return 1;
        }
        
        return this.settledAmount.get().compareTo(other.settledAmount.get());
    }
}
