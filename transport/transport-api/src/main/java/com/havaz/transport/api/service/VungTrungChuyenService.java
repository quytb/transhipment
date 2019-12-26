package com.havaz.transport.api.service;

import com.havaz.transport.api.form.VungTrungChuyenForm;
import com.havaz.transport.api.model.DistanceDTO;
import com.havaz.transport.api.model.TcDspVungTrungChuyenDTO;
import com.havaz.transport.api.model.VtcDTO;

import java.util.List;

public interface VungTrungChuyenService {
    void createOrUpdate(VungTrungChuyenForm vungTrungChuyenForm);

    List<TcDspVungTrungChuyenDTO> getDataByName(String tenVung);

    int checkExistsPoint(Double latX, Double longX);

    DistanceDTO getDistance(String latX, String longX, String latY, String longY);

    List<VtcDTO> findAllByStatus(Integer status);
}
