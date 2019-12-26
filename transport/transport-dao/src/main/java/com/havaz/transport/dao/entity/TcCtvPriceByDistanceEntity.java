package com.havaz.transport.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tc_ctv_price_by_distance")
public class TcCtvPriceByDistanceEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "price_distance_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer priceDistanceId;

    @Column(name = "distance")
    private Double distance;

    @Column(name = "price")
    private Double price;

    @Column(name = "price_over")
    private Double priceOver;

    @Column(name = "price_id")
    private Integer priceId;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("priceDistanceId", priceDistanceId)
                .append("distance", distance)
                .append("price", price)
                .append("priceOver", priceOver)
                .append("priceId", priceId)
                .toString();
    }
}
