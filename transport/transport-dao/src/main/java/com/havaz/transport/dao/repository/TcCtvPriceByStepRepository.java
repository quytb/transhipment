package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcCtvPriceByStepEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TcCtvPriceByStepRepository extends JpaRepository<TcCtvPriceByStepEntity, Integer> {
    List<TcCtvPriceByStepEntity> findByPriceId(Integer idPrice);
}
