package com.qczhang18.rental.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum Type {
    Ladder("Ladder", new BigDecimal("1.99"), true, true, false),
    Chainsaw("Chainsaw", new BigDecimal("1.49"), true, false, true),
    Jackhammer("Jackhammer", new BigDecimal("2.99"), true, false, false);

    Type(String name, BigDecimal dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
        this.name = name;
        this.dailyCharge = dailyCharge;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;
    }

    private final String name;
    private final BigDecimal dailyCharge;
    private final boolean weekdayCharge;
    private final boolean weekendCharge;
    private final boolean holidayCharge;

}
