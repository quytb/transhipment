package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcCtvPriceByDistanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TcCtvPriceByDistanceRepository extends JpaRepository<TcCtvPriceByDistanceEntity, Integer> {
    Optional<TcCtvPriceByDistanceEntity> findByPriceId(Integer priceId);
}
