package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.AdminLv2RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminLv2RoleRepository extends JpaRepository<AdminLv2RoleEntity, Integer> {
}
