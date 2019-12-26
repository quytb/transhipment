package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.service.ChiNhanhService;
import com.havaz.transport.dao.entity.ChiNhanhEntity;
import com.havaz.transport.dao.repository.ChiNhanhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ChiNhanhServiceImpl implements ChiNhanhService {

    @Autowired
    private ChiNhanhRepository chiNhanhRepository;

    @Override
    public List<ChiNhanhEntity> getAll() {
        return chiNhanhRepository.findAll();
    }
}
