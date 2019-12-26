package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class NotifyLenhDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String taixeName;
    private String xeBks;
    private Integer loaiXe;
    private String clientNumberPhone;
    private String merchantId;
}
