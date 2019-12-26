package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcTripDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TcTripDetailRepository extends JpaRepository<TcTripDetailEntity, Integer> {

}
