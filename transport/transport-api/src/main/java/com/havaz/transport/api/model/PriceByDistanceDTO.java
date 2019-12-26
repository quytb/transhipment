package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Positive;

@Getter
@Setter
public class PriceByDistanceDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Integer priceDistanceId;

    @Positive(message = "Vui Lòng nhập khoảng cách")
    private Double distance;

    @Positive(message = "Vui Lòng nhập giá")
    private Double price;

    private Double priceOver;
}
