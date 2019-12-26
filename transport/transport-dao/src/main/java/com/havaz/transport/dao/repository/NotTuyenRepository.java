package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.NotTuyenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotTuyenRepository extends JpaRepository<NotTuyenEntity, Integer> {
    @Query("SELECT e.notId FROM NotTuyenEntity e WHERE e.notTuyId IN (:notTuyIds)")
    List<Integer> getNotId(@Param("notTuyIds") List<Integer> notTuyIds);

    @Query("SELECT e.notId FROM NotTuyenEntity e WHERE e.notTuyId IN (:notTuyIds) AND e.notMa LIKE '%A'")
    List<Integer> getNotIdByGioA(@Param("notTuyIds") List<Integer> notTuyIds);

    @Query("SELECT e.notId FROM NotTuyenEntity e WHERE e.notTuyId IN (:notTuyIds) AND e.notMa LIKE '%B'")
    List<Integer> getNotIdByGioB(@Param("notTuyIds") List<Integer> notTuyIds);
}
