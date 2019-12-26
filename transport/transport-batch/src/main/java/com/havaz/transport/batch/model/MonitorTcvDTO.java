package com.havaz.transport.batch.model;

import com.havaz.transport.dao.entity.BanVeVeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class MonitorTcvDTO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String monitorTrip;
    private String monitorBks;
    private String monitorChieu;
    private String monitorGxb;
    private String monitorCountVeErp;
    private String monitorCountVeTc;
    private Integer status;
    private String monitorSdt;
    private String monitorTuyen;
    private LocalDateTime monitorCreatedDate;
    private LocalDateTime monitorLastUpdatedDate;
    private String erpBvvIds;
    private String tcBvvIds;
    private List<BanVeVeEntity> missedTickets;
}
