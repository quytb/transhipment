package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.AdminLv2GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminLv2GroupRepository extends JpaRepository<AdminLv2GroupEntity, Integer> {
}
