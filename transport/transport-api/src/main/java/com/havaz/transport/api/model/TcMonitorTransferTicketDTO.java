package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TcMonitorTransferTicketDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Integer monitorId;
    private String monitorTrip;
    private String monitorBks;
    private String monitorTuyen;
    private String monitorChieu;
    private String monitorGxb;
    private Integer monitorCountVeErpDon;
    private Integer monitorCountVeErpTra;
    private Integer monitorCountVeTcDon;
    private Integer monitorCountVeTcTra;
    private Integer status;
}
