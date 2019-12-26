package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcMonitorTransferVeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TcMonitorTransferVeRepository extends JpaRepository<TcMonitorTransferVeEntity,Integer> {
}
