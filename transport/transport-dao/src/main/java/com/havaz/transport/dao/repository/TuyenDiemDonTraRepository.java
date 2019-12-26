package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TuyenDiemDonTraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TuyenDiemDonTraRepository extends JpaRepository<TuyenDiemDonTraEntity,Integer> {

    @Query(value = "SELECT u.tdd_id,u.tdd_is_trung_chuyen,v1.bex_ten as benXe,v2.bex_ten as benXeTrungChuyenDen FROM tuyen_diem_don_tra_khach u " +
            "LEFT JOIN ben_xe v1 ON v1.bex_id = u.tdd_bex_id " +
            "LEFT JOIN ben_xe v2 ON u.tdd_ben_trung_truyen_den = v2.bex_id " +
            "WHERE tdd_tuyen_id = ?1 AND tdd_bex_id = ?2",nativeQuery = true)
    List<Object[]> getByTuyenIdAndBenId(int tuyenId, int benXeId);

}
