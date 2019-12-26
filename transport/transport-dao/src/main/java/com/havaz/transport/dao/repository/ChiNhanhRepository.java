package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.ChiNhanhEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiNhanhRepository extends JpaRepository<ChiNhanhEntity, Integer> {

    @Query(value = "SELECT c.id FROM ChiNhanhEntity c")
    List<Integer> getAllChiNhanhId();
}


