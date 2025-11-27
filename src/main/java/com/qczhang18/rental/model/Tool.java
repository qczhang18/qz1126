package com.qczhang18.rental.model;

import lombok.Getter;

@Getter
public enum Tool {
    CHNS("CHNS", Type.Chainsaw, "Stihl"),
    LADW("LADW", Type.Ladder, "Werner"),
    JAKD("JAKD", Type.Jackhammer, "DeWalt"),
    JAKR("JAKR", Type.Jackhammer, "Ridgid");

    private final String code;
    private final Type type;
    private final String brand;

    Tool(String code, Type type, String brand) {
        this.code = code;
        this.type = type;
        this.brand = brand;
    }

}

