package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TuyenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TuyenRepository extends JpaRepository<TuyenEntity,Integer> {
    // Lấy toàn bộ các tuyến đang hoạt động
    @Query(value = "SELECT t FROM TuyenEntity t WHERE t.tuyActive = true ORDER BY t.tuyId")
    List<TuyenEntity> getListTuyen();

//    public List<TuyenEntity> findByTuyActiveAndOrderByTuyTen(int tuyActive);

    // Lấy toàn bộ tuyId của các tuyến đang hoạt động
    @Query("SELECT t.tuyId FROM TuyenEntity t WHERE t.tuyActive = true ")
    List<Integer> getTuyId();

    @Query("SELECT t.tuyId FROM TuyenEntity t JOIN NotTuyenEntity nt ON t.tuyId = nt.notTuyId JOIN DieuDoTempEntity ddt ON nt.notId = ddt.didNotId WHERE ddt.didId = ?1")
    Integer getTuyIdByBvvBvnId(int bvv_bvn_id);
}
