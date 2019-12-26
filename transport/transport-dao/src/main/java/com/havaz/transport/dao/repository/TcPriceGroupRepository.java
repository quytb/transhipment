package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcCtvPriceGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TcPriceGroupRepository extends JpaRepository<TcCtvPriceGroupEntity, Integer> {
}
