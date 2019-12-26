package com.havaz.transport.api.repository;

import com.havaz.transport.api.model.NotActiveDTO;
import com.havaz.transport.api.model.ThongTinNodeDto;

import java.util.List;

public interface DieuDoTempRepositoryCustom {

    List<NotActiveDTO> getListNotActive(ThongTinNodeDto thongTinNodeDto);

    List<Integer> getListTripId();
}
