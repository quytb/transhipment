package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcVungTrungChuyenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TcVungTrungChuyenRepository extends JpaRepository<TcVungTrungChuyenEntity, Integer> {
    List<TcVungTrungChuyenEntity> findByTcVttNameContainingOrderByTcVttIdAsc(String tenVung);
}
