package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.BanVeVeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BvvRepository extends JpaRepository<BanVeVeEntity, Integer> {
    BanVeVeEntity getBanVeVeEntityByBvvId(int bvvId);
}
