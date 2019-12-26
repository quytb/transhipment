package com.havaz.transport.api.service;

import com.havaz.transport.api.model.TcMonitorTransferTicketDTO;

import java.time.LocalDate;
import java.util.List;

public interface TcMonitorTransferTicketService {
    List<TcMonitorTransferTicketDTO> findByNgayAndTrangThai(LocalDate ngayKiemTra, int trangThai);

    void updateTransferTicket(String didId);
}
