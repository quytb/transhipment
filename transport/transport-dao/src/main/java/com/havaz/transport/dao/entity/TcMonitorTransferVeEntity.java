package com.havaz.transport.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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
@EqualsAndHashCode
@Entity
@Table(name = "tc_monitor_transfer_ve")
@EntityListeners(AuditingEntityListener.class)
public class TcMonitorTransferVeEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "monitor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer monitorId;

    @Column(name = "monitor_trip")
    private String monitorTrip;

    @Column(name = "monitor_bks")
    private String monitorBks;

    @Column(name = "monitor_chieu")
    private String monitorChieu;

    @Column(name = "monitor_gxb")
    private String monitorGxb;

    @Column(name = "monitor_count_ve_erp_don")
    private Integer monitorCountVeErpDon;

    @Column(name = "monitor_count_ve_erp_tra")
    private Integer monitorCountVeErpTra;

    @Column(name = "monitor_count_ve_tc_don")
    private Integer monitorCountVeTcDon;

    @Column(name = "monitor_count_ve_tc_tra")
    private Integer monitorCountVeTcTra;

    @Column(name = "status")
    private Integer status;

    @CreatedDate
    @Column(name = "monitor_created_date")
    private LocalDateTime monitorCreatedDate;

    @LastModifiedDate
    @Column(name = "monitor_last_updated_date")
    private LocalDateTime monitorLastUpdatedDate;

    @Column(name = "monitor_tuyen")
    private String monitorTuyen;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("monitorId", monitorId)
                .append("monitorTrip", monitorTrip)
                .append("monitorBks", monitorBks)
                .append("monitorChieu", monitorChieu)
                .append("monitorGxb", monitorGxb)
                .append("monitorCountVeErpDon", monitorCountVeErpDon)
                .append("monitorCountVeErpTra", monitorCountVeErpTra)
                .append("monitorCountVeTcDon", monitorCountVeTcDon)
                .append("monitorCountVeTcTra", monitorCountVeTcTra)
                .append("status", status)
                .append("monitorCreatedDate", monitorCreatedDate)
                .append("monitorLastUpdatedDate", monitorLastUpdatedDate)
                .append("monitorTuyen", monitorTuyen)
                .toString();
    }
}
