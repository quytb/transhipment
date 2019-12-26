package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcUserPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TcUserPermissionRepository extends JpaRepository<TcUserPermissionEntity, Integer> {

    Optional<TcUserPermissionEntity> findByUserIdAndPermissionId(Integer userId, Integer permissionId);

    List<TcUserPermissionEntity> findByUserId(Integer userId);
}
