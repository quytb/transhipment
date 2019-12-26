package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.service.TcTaiXeVeService;
import com.havaz.transport.dao.entity.TcTaiXeVeEntity;
import com.havaz.transport.dao.repository.TcTaiXeVeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TcTaiXeVeServiceImpl implements TcTaiXeVeService {

    @Autowired
    private TcTaiXeVeRepository tcTaiXeVeRepository;

    @Override
    @Transactional
    public void saveTaiXeVe(int laixeId, int lenhId, int tcVeId, int statusVe,String lyDoHuy, int userId) {
        TcTaiXeVeEntity tcTaiXeVeEntity = new TcTaiXeVeEntity();
        tcTaiXeVeEntity.setLaiXeId(laixeId);
        tcTaiXeVeEntity.setLenhId(lenhId);
        tcTaiXeVeEntity.setTcVeId(tcVeId);
        tcTaiXeVeEntity.setVeStatus(statusVe);
        tcTaiXeVeEntity.setLyDoHuy(lyDoHuy);
        tcTaiXeVeEntity.setCreatedBy(userId);
        tcTaiXeVeEntity.setLastUpdatedBy(userId);
        tcTaiXeVeRepository.save(tcTaiXeVeEntity);
    }
}
