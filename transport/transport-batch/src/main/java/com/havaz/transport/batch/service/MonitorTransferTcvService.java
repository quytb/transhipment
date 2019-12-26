package com.havaz.transport.batch.service;

import com.havaz.transport.batch.model.MonitorTcvDTO;
import com.havaz.transport.batch.model.MonitorTcvTraDTO;

import java.util.List;

public interface MonitorTransferTcvService {
    List<MonitorTcvDTO> getReport(boolean isPickup);

    MonitorTcvTraDTO getReportTra(int didId);

    void insertMonitorRecord();
}
