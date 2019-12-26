package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.service.BvvService;
import com.havaz.transport.dao.entity.BanVeVeEntity;
import com.havaz.transport.dao.entity.TcVeEntity;
import com.havaz.transport.dao.repository.BvvRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BvvServiceImpl implements BvvService {

    @Autowired
    private BvvRepository bvvRepository;

    @Override
    @Transactional
    public void updateBvvEntity(TcVeEntity tcVeEntity) {
        BanVeVeEntity banVeVeEntity = bvvRepository.getBanVeVeEntityByBvvId(tcVeEntity.getBvvId());

        banVeVeEntity.setBvvLaiXeDon(tcVeEntity.getLaiXeIdDon() != null ? tcVeEntity.getLaiXeIdDon() : 0);
        banVeVeEntity.setBvvTrungChuyenAOrder(tcVeEntity.getThuTuDon() != null ? tcVeEntity.getThuTuDon() : 0);
        banVeVeEntity.setBvvTrungChuyenANote(tcVeEntity.getGhiChuDon() != null ? tcVeEntity.getGhiChuDon() : StringUtils.EMPTY);

        banVeVeEntity.setBvvLaiXeTra(tcVeEntity.getLaiXeIdTra() != null ? tcVeEntity.getLaiXeIdTra() : 0);
        banVeVeEntity.setBvvTrungChuyenBOrder(tcVeEntity.getThuTuTra() != null ? tcVeEntity.getThuTuTra() : 0);
        banVeVeEntity.setBvvTrungChuyenBNote(tcVeEntity.getGhiChuTra() != null ? tcVeEntity.getGhiChuTra() : StringUtils.EMPTY);
        //Save into DB
        bvvRepository.save(banVeVeEntity);
    }
}
