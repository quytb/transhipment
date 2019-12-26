package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcCtvPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TcCtvPriceRepository extends JpaRepository<TcCtvPriceEntity, Integer> {
}
