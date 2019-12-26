package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.BanVeVeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BanVeVeRepository extends JpaRepository<BanVeVeEntity, Integer> {
}
