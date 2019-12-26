package com.havaz.transport.core.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

public enum UserPermission {

    // TODO refactor
    P90000001("P90000001", "Quản trị hệ thống"),

    P10000001("P10000001", "Điều hành"),

    P20000001("P20000001", "Quản lý"),

    P30000001("P30000001", "Báo cáo"),

    P40000001("P40000001", "");

    private String code;
    private String description;

    UserPermission(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static UserPermission fromCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        UserPermission[] values = UserPermission.values();

        for (UserPermission v : values) {
            if (code.equals(v.getCode())) {
                return v;
            }
        }

        return null;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
