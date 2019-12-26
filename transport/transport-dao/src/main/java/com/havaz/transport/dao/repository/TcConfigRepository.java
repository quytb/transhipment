package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TcConfigRepository extends JpaRepository<TcConfigurationEntity, String> {
}
