package com.havaz.transport.api.repository;

import com.havaz.transport.api.model.TcVungTrungChuyenDTO;
import com.havaz.transport.api.model.VtcDTO;
import com.havaz.transport.dao.entity.TcVungTrungChuyenEntity;

import java.util.List;

public interface VungTrungChuyenRepositoryCustom {
    void addPolygon(TcVungTrungChuyenDTO vttDto);

    double getAverageSpeedById(int id);

    void updatePolygon(TcVungTrungChuyenDTO vttDto);

    List<Object[]> getall();

    List<Object[]> getDataByName(String tenVung);

    List<VtcDTO> findAllByStatus(Integer status);

    TcVungTrungChuyenEntity findByIdVtc(Integer idVtc);
}
