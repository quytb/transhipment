package com.havaz.transport.dao.repository;

import com.havaz.transport.core.constant.PermissionType;
import com.havaz.transport.core.constant.UserPermission;
import com.havaz.transport.dao.entity.TcPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TcPermissionRepository extends JpaRepository<TcPermissionEntity, Integer> {

    @Query("SELECT p FROM TcPermissionEntity p JOIN FETCH p.roles r WHERE r.id = :roleId AND p.enabled = true ")
    List<TcPermissionEntity> findAllByRoleId(@Param("roleId") int roleId);

    Optional<TcPermissionEntity> findByCode(UserPermission code);

    List<TcPermissionEntity> findAllByType(PermissionType type);

    List<TcPermissionEntity> findAllByEnabled(Boolean enabled);

    List<TcPermissionEntity> findAllByCodeAndEnabled(UserPermission code, Boolean enabled);

    List<TcPermissionEntity> findAllByTypeAndEnabled(PermissionType type, Boolean enabled);

    @Query("SELECT p FROM TcPermissionEntity p JOIN FETCH p.users u WHERE u.admId = :userId AND p.enabled = true ")
    List<TcPermissionEntity> findAllByUserId(@Param("userId") int userId);
}


