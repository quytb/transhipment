package com.havaz.transport.api.common;

import java.util.stream.Stream;

public enum StatusCmdHub {
    DA_DON(1), DI_DON(2);
    private int value;

    StatusCmdHub(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static StatusCmdHub of(int value) {
        return Stream.of(StatusCmdHub.values()).filter(x -> x.value == value).findFirst().orElse(null);
    }
}
