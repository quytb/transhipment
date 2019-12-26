package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.ModuleLv2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleLv2Repository extends JpaRepository<ModuleLv2Entity, Integer> {
}
