package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.XeEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface XeRepository extends JpaRepository<XeEntity, Integer> {

    @Query("SELECT X FROM XeEntity X WHERE X.xeTrungTam = :xeTrungTam AND X.xeActive = :xeActive")
    List<XeEntity>getDanhSachXeTrungChuyen(@Param("xeTrungTam") int xeTrungTam,
                                           @Param("xeActive") Boolean xeActive);

    @Query("SELECT X FROM XeEntity X WHERE (X.xeTrungTam = :xeTrungTam OR X.xeCongTacVien = :isCtv) AND X.xeActive = :xeActive")
    List<XeEntity> findByXeCongTacVienAndXeActiveOrXeTc(@Param("xeTrungTam") int xeTrungTam,
                                                        @Param("xeActive") Boolean xeActive,
                                                        @Param("isCtv") Boolean isCtv);

    List<XeEntity> getAllByXeIdIn(Set<Integer> xeId);
}
