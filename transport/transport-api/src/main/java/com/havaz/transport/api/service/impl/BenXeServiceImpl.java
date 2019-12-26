package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.service.BenXeService;
import com.havaz.transport.dao.entity.BenXeEntity;
import com.havaz.transport.dao.repository.BenXeEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BenXeServiceImpl implements BenXeService {

    @Autowired
    private BenXeEntityRepository benXeEntityRepository;

    @Override
    public List<BenXeEntity> getAll(int diemDonTra) {
        if (diemDonTra == 1) {
            benXeEntityRepository.getAllForHubOnlyKinhDoanh();
        }
        return benXeEntityRepository.getAllForHub();
    }
}
