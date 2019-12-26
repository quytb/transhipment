package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PriceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer priceId;

    @NotEmpty(message = "Vui lòng nhập tên phương thức")
    private String name;

    private Integer type;

    private PriceByDistanceDTO priceByDistanceDTO;

    private List<PriceByStepDTO> priceByStepDTOS = new ArrayList<>();
}
