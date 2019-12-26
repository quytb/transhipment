package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PriceGroupDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String partnerName;
    private Integer partnerId;
    private Double discountRange;
    private Integer priceId;
    private String priceName;
}
