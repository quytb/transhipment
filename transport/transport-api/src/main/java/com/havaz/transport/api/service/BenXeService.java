package com.havaz.transport.api.service;

import com.havaz.transport.dao.entity.BenXeEntity;

import java.util.List;

public interface BenXeService {
    List<BenXeEntity> getAll(int diemDonTra);
}
