package com.havaz.transport.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tc_lai_xe_ve")
@Data
public class TcTaiXeVeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "lai_xe_id")
    private Integer laiXeId;

    @Column(name = "lenh_id")
    private Integer lenhId;

    @Column(name = "tc_ve_id")
    private Integer tcVeId;

    @Column(name = "ve_status")
    private Integer veStatus;

    @Column(name = "ly_do_huy")
    private String lyDoHuy;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private Integer createdBy;

    @LastModifiedDate
    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;

    @Column(name = "last_updated_by")
    private Integer lastUpdatedBy;
}
