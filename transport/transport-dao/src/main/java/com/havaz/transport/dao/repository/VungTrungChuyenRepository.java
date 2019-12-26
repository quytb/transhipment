package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcVungTrungChuyenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VungTrungChuyenRepository extends JpaRepository<TcVungTrungChuyenEntity, Integer> {

}
