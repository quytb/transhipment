package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcUserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TcUserRoleRepository extends JpaRepository<TcUserRoleEntity, Integer> {

    Optional<TcUserRoleEntity> findByUserIdAndRoleId(Integer userId, Integer roleId);
}
