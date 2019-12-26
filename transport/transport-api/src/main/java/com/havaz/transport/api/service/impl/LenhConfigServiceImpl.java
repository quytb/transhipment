package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.service.LenhConfigService;
import com.havaz.transport.api.utils.SecurityUtils;
import com.havaz.transport.dao.entity.LenhConfigEntity;
import com.havaz.transport.dao.repository.LenhConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class LenhConfigServiceImpl implements LenhConfigService {

    @Autowired
    private LenhConfigRepository lenhConfigRepository;

    @Override
    public int getStatus() {
        LenhConfigEntity lenhConfigEntity = lenhConfigRepository.findStatus();
        return lenhConfigEntity != null ? lenhConfigEntity.getIsMultiple() : 0;
    }

    @Override
    @Transactional
    public void changeStatus(int status) {
        int userLogin = SecurityUtils.getCurrentUserLogin();
        LenhConfigEntity lenhConfigEntity = lenhConfigRepository.findStatus();
        if (lenhConfigEntity == null) {
            lenhConfigEntity = new LenhConfigEntity();
        }
        lenhConfigEntity.setIsMultiple(status);
        lenhConfigEntity.setUpdatedBy(userLogin);
        lenhConfigEntity.setUpdatedAt(LocalDateTime.now());
        lenhConfigRepository.save(lenhConfigEntity);
    }
}
