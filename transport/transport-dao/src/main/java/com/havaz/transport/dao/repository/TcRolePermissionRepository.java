package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcRolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TcRolePermissionRepository extends JpaRepository<TcRolePermissionEntity, Integer> {

    Optional<TcRolePermissionEntity> findByRoleIdAndPermissionId(Integer roleId, Integer permissionId);

    List<TcRolePermissionEntity> findAllByRoleIdAndPermissionIdIn(Integer roleId, List<Integer> permissionIds);
}
