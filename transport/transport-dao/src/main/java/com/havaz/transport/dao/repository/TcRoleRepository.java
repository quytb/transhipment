package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TcRoleRepository extends JpaRepository<TcRoleEntity, Integer> {

    Optional<TcRoleEntity> findByName(String name);

    @Query("SELECT r FROM TcRoleEntity r JOIN FETCH r.users u WHERE u.admId = :userId")
    List<TcRoleEntity> findAllByUserId(@Param("userId") int userId);
}
