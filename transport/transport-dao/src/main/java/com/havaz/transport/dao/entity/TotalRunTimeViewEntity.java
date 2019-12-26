package com.havaz.transport.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Immutable
@Getter
@Setter
@Table(name = "total_run_time_view")
public class TotalRunTimeViewEntity {
    @Id
    @Column(name = "tuyen_id")
    private Integer tuyenId;
    @Column(name = "dich_vu_id")
    private Integer dichVuId;
    @Column(name = "ro_id")
    private Integer roId;
    @Column(name = "run_time")
    private BigDecimal totalRunTime;
}
