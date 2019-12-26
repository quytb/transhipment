package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.ZzLogBanVeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TcZzLogRepository extends JpaRepository<ZzLogBanVeEntity, Integer> {
}
