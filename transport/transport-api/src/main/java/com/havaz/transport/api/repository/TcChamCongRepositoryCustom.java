package com.havaz.transport.api.repository;

import com.havaz.transport.api.model.BaoCaoChamCongDTO;

import java.util.List;

public interface TcChamCongRepositoryCustom {

    List<BaoCaoChamCongDTO> getDuLieuBaoCaoChamCong(Integer thang, Integer nam, Integer chinhanh);
}
