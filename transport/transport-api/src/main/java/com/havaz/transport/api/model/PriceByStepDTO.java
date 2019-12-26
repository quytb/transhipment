package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class PriceByStepDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Integer priceStepId;

    @NotNull(message = "Vui lòng nhập distance From")
    @Positive(message = "Vui lòng nhập distance là số")
    private Double distanceFrom;

    @NotNull(message = "Vui lòng nhập distance To")
    @Positive(message = "Vui lòng nhập distance là số")
    private Double distanceTo;

    @NotNull(message = "Vui lòng nhập giá")
    @Positive(message = "Vui lòng giá tiền là số")
    private Double price;
}
