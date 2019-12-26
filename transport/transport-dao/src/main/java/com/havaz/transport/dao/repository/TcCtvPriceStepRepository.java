package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcCtvPriceByStepEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TcCtvPriceStepRepository extends JpaRepository<TcCtvPriceByStepEntity, Integer> {
}
