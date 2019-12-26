package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.BenXeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenXeEntityRepository extends JpaRepository<BenXeEntity,Integer> {

    @Query("SELECT b FROM BenXeEntity b WHERE b.diemDonTraKhach = 1 OR b.kinhdoanh = 1")
    List<BenXeEntity> getAllForHub();

    @Query("SELECT b FROM BenXeEntity b WHERE b.kinhdoanh = 1")
    List<BenXeEntity> getAllForHubOnlyKinhDoanh();
}
