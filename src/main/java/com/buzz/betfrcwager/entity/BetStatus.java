package com.buzz.betfrcwager.entity;

public enum BetStatus {
    OPEN("O"),
    WON("W"),
    LOST("L"),
    VOID("V");

    private String code;

    private BetStatus(String code) {
        this.code = code;
    }

    public boolean isSettled() {
        return this == WON || this == LOST || this == VOID;
    }

    public String getCode() {
        return this.code;
    }

    public static BetStatus fromCode(String code) {
        for (BetStatus status : BetStatus.values()) {
            if (code.equalsIgnoreCase(status.getCode())) return status;
        }
        return BetStatus.OPEN;
    }
}
