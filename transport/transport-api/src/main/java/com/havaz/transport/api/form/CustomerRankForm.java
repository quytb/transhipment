package com.havaz.transport.api.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerRankForm {
    private String[] phone_list;
}
