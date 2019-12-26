package com.havaz.transport.api.service;

import com.havaz.transport.api.model.XeDTO;
import com.havaz.transport.dao.entity.XeEntity;

import java.util.List;

public interface XeService {
    List<XeEntity> getByXeTrungTam(int xeTrungTam, boolean xeActive);

    List<XeDTO> getXeCtvVtc();
}
