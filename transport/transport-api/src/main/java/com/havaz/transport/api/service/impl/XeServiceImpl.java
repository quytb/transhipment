package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.common.LenhConstants;
import com.havaz.transport.api.model.XeDTO;
import com.havaz.transport.api.service.XeService;
import com.havaz.transport.dao.entity.XeEntity;
import com.havaz.transport.dao.repository.XeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class XeServiceImpl implements XeService {

    @Autowired
    private XeRepository xeRepository;

    @Override
    public List<XeEntity> getByXeTrungTam(int xeTrungTam, boolean xeActive) {
        return xeRepository.getDanhSachXeTrungChuyen(xeTrungTam, xeActive);
    }

    @Override
    public List<XeDTO> getXeCtvVtc() {
        List<XeEntity> xeEntities = xeRepository.findByXeCongTacVienAndXeActiveOrXeTc(LenhConstants.XE_TRUNG_TAM_IS_XE_TRUNG_CHUYEN, true , true);
        return xeEntities.stream().map(xeEntity -> {
            XeDTO xeDTO = new XeDTO();
            xeDTO.setXeId(xeEntity.getXeId());
            xeDTO.setXeBks(xeEntity.getXeBienKiemSoat());
            return xeDTO;
        }).collect(Collectors.toList());

    }

}
