package com.havaz.transport.api.common;

public enum PriceType {
    BY_STEP(1), BY_DISTANCE(2);
    private final int type;

    PriceType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
