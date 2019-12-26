package com.havaz.transport.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tc_trip_detail")
@EntityListeners(AuditingEntityListener.class)
public class TcTripDetailEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "tc_trip_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tcTripId;

    @Column(name = "tc_vtt_ctv_id")
    private Integer tcVttCtvId;

    @Column(name = "lenh_id")
    private Integer lenhId;

    @Column(name = "distance")
    private BigDecimal distance;

    @Column(name = "discount_range")
    private BigDecimal discountRange;

    @Column(name = "status")
    private Integer status;

    @Column(name = "amount")
    private BigDecimal amount;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;

    @Column(name = "created_by")
    @CreatedBy
    private Integer createdBy;

    @Column(name = "last_updated_by")
    @LastModifiedBy
    private Integer lastUpdatedBy;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("tcTripId", tcTripId)
                .append("tcVttCtvId", tcVttCtvId)
                .append("lenhId", lenhId)
                .append("distance", distance)
                .append("discountRange", discountRange)
                .append("status", status)
                .append("amount", amount)
                .append("createdDate", createdDate)
                .append("lastUpdatedDate", lastUpdatedDate)
                .append("createdBy", createdBy)
                .append("lastUpdatedBy", lastUpdatedBy)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TcTripDetailEntity that = (TcTripDetailEntity) o;

        return new EqualsBuilder()
                .append(tcTripId, that.tcTripId)
                .append(tcVttCtvId, that.tcVttCtvId)
                .append(lenhId, that.lenhId)
                .append(distance, that.distance)
                .append(discountRange, that.discountRange)
                .append(status, that.status)
                .append(amount, that.amount)
                .append(createdDate, that.createdDate)
                .append(lastUpdatedDate, that.lastUpdatedDate)
                .append(createdBy, that.createdBy)
                .append(lastUpdatedBy, that.lastUpdatedBy)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(tcTripId)
                .append(tcVttCtvId)
                .append(lenhId)
                .append(distance)
                .append(discountRange)
                .append(status)
                .append(amount)
                .append(createdDate)
                .append(lastUpdatedDate)
                .append(createdBy)
                .append(lastUpdatedBy)
                .toHashCode();
    }
}
