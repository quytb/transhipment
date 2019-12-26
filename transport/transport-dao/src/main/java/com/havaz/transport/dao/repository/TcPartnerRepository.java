package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcPartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TcPartnerRepository extends JpaRepository<TcPartnerEntity, Integer> {
}
