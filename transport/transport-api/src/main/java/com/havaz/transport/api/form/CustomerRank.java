package com.havaz.transport.api.form;

import lombok.Data;

import java.util.List;

@Data
public class CustomerRank {
    private String status;
    private List<CustomerRankData> data;
}
