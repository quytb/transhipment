package com.havaz.transport.api.common;

public enum StatusTicket {
    CHUA_DIEU(0), DA_DIEU(1), DANG_DON(2), DA_DON(3), DA_HUY(4), LEN_XE(5);
    private final int value;

    StatusTicket(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
