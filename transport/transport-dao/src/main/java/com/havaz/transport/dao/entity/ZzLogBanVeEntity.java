package com.havaz.transport.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "zz_log_ban_ve")
public class ZzLogBanVeEntity implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logId;

    @Column(name = "log_admin_id")
    private Integer logAdminId;

    @Column(name = "log_record_id")
    private Integer logRecordId;

    @Column(name = "log_time")
    private Integer logTime;

    @Column(name = "log_type")
    private Integer logType;

    @Column(name = "log_data", columnDefinition = "TEXT")
    private String logData;

    @Column(name = "log_ip")
    private String logIp;

    @Column(name = "log_status", columnDefinition = "TINYINT")
    private Integer logStatus;

}
