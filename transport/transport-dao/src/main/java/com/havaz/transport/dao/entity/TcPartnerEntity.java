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
@Table(name = "tc_partner")
public class TcPartnerEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "partner_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer partnerId;

    @Column(name = "partner_name")
    private String partnerName;

    @Column(name = "discount_range")
    private Double discountRange;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("partnerId", partnerId)
                .append("partnerName", partnerName)
                .append("discountRange", discountRange)
                .toString();
    }
}
