package com.havaz.transport.api.model;

import com.havaz.transport.dao.entity.TcCtvPriceByDistanceEntity;
import com.havaz.transport.dao.entity.TcCtvPriceByStepEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PartnerPriceGroupDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer groupId;
    private Integer priceId;
    private Double discountRange;
    private Integer typePrice;
    private Integer partnerId;
    private TcCtvPriceByDistanceEntity priceByDistanceEntity;
    private List<TcCtvPriceByStepEntity> priceByStepEntities = new ArrayList<>();
}
