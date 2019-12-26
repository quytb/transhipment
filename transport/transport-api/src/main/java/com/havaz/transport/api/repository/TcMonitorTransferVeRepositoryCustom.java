package com.havaz.transport.api.repository;

import com.havaz.transport.api.model.TcMonitorTransferTicketDTO;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TcMonitorTransferVeRepositoryCustom {
    List<TcMonitorTransferTicketDTO> findByNgayAndTrangThai(@Param("ngayKiemTra") LocalDate ngayKiemTra, int trangThai);
}
