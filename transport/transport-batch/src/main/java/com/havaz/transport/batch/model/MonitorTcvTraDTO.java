package com.havaz.transport.batch.model;

import com.havaz.transport.dao.entity.BanVeVeEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MonitorTcvTraDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private String monitorTrip;
    private String monitorCountVeErpTra;
    private String monitorCountVeTcTra;
    private String tcBvvIds;
    private String erpBvvIds;
    private List<BanVeVeEntity> missedTickets;
}
