package com.havaz.transport.api.common;

public enum AudioType {
    CMD_DBS("dieubosung"), CANCEL_CMD("lenhuy"), NEW_CMD("lenhmoi"), UPDATE_CMD("update"), CANCEL_TICKET("vehuy");
    private final String value;

    public String getValue() {
        return value;
    }

    AudioType(String value) {
        this.value = value;
    }
}
