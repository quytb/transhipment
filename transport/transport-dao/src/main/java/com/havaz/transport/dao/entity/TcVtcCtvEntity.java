package com.havaz.transport.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tc_vtc_ctv")
@EntityListeners(AuditingEntityListener.class)
public class TcVtcCtvEntity implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tc_vtt_ctv_id")
    private Integer tcVttCtvId;

    @Column(name = "tc_vtt_id")
    private Integer tcVttId;

    @Column(name = "tc_ctv_id")
    private Integer tcCtvId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "note")
    private String note;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdateDate;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "coordinate_lat")
    private Double coordinateLat;

    @Column(name = "coordinate_long")
    private Double coordinateLong;

    @Column(name = "xe_ctv_id")
    private Integer xeId;

    @Column(name = "price_group_id")
    private Integer priceGroupId;
}
