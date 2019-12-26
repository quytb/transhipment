package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.AdminLv2GroupRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminLv2GroupRoleRepository extends JpaRepository<AdminLv2GroupRoleEntity, Integer> {
}
