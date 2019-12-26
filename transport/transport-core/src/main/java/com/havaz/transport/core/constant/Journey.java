package com.havaz.transport.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Journey {

    /**
     * Chiều đi / Chiều A
     */
    OUTWARD(1, "Chiều A"),

    /**
     * Chiều về / Chiều B
     */
    RETURN(2, "Chiều B");

    private final int code;
    private final String description;

    Journey(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonCreator
    public static Journey of(int code) {
        Journey[] values = Journey.values();
        for (Journey value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
