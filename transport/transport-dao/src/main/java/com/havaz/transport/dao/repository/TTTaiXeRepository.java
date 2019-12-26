package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TTTaiXe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TTTaiXeRepository extends JpaRepository<TTTaiXe,Integer> {
    TTTaiXe findTopByTaiXeIdOrderByLastUpdatedAtDesc(int taiXeId);
}
