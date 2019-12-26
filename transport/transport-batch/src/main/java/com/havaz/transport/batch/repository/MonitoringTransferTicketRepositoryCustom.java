package com.havaz.transport.batch.repository;

import com.havaz.transport.batch.model.MonitorTcvDTO;
import com.havaz.transport.batch.model.MonitorTcvTraDTO;

import java.util.List;

public interface MonitoringTransferTicketRepositoryCustom {
    List<MonitorTcvDTO> calculateData(boolean isPickup);

    MonitorTcvTraDTO calculateDataTra(int didId);
}
