package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.DieuDoTempEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DieuDoTempRepository extends JpaRepository<DieuDoTempEntity, Integer> {
    @Query(value = "SELECT d.did_id, t.tuy_ten FROM tuyen t " +
            "LEFT JOIN dieu_do_temp d ON t.tuy_id = d.did_id " +
            "WHERE STR_TO_DATE(d.did_gio_xuat_ben_that,'%H:%i') BETWEEN " +
            "CURRENT_TIME AND ADDTIME(CURRENT_TIME ,SEC_TO_TIME(:khoangThoiGian))",nativeQuery = true)
    List<Object[]> getListTuyen(@Param("khoangThoiGian") int khoangThoiGian);

    @Query(value = "SELECT d.did_id, d.did_gio_xuat_ben_that FROM dieu_do_temp d " +
            "WHERE d.did_id IN (:didIds) AND STR_TO_DATE(d.did_gio_xuat_ben_that,'%H:%i') BETWEEN " +
            "CURRENT_TIME AND ADDTIME(CURRENT_TIME ,SEC_TO_TIME(:khoangThoiGian))",nativeQuery = true)
    List<Object[]> getListGioDi(@Param("didIds") List<Integer> didIds, @Param("khoangThoiGian") int khoangThoiGian);

    @Query(value = "SELECT * FROM dieu_do_temp d JOIN tc_ve v ON d.did_id = v.did_id WHERE v.bvv_id = ?1",nativeQuery = true)
    DieuDoTempEntity getByTcVeId(int tcVeId);

}
