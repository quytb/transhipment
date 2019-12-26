package com.havaz.transport.api.form;

import lombok.Data;

@Data
public class CustomerRankData {
    private String phone;
    private Integer point;
    private String rank;
    private boolean app_used;
    private String icon;
}
