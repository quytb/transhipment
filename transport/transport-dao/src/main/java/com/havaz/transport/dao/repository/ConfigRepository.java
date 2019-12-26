package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<ConfigurationEntity, Integer> {
}
