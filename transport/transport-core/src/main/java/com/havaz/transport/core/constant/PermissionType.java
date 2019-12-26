package com.havaz.transport.core.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PermissionType {

    // TODO refactor
    BASIC(1), ADMINISTRATOR(2);

    private int code;

    PermissionType(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    public static PermissionType fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        PermissionType[] values = PermissionType.values();
        for (PermissionType v : values) {
            if (v.getCode() == code) {
                return v;
            }
        }
        return null;
    }
}
