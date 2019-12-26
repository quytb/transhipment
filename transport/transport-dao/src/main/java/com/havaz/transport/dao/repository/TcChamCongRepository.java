package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcChamCongEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TcChamCongRepository extends JpaRepository<TcChamCongEntity, Integer> {
    @Query("SELECT e.taiXeId, a.admName, e.xeId, x.xeBienKiemSoat, e.tcCaId, c.maCa, e.ngayChamCong, e.gioThucTe, e.khachPhatSinh, e.ghiChu, e.tcCongId " +
            "FROM TcChamCongEntity e " +
            "LEFT JOIN AdminLv2UserEntity a ON e.taiXeId = a.admId " +
            "LEFT JOIN  XeEntity  x ON x.xeId = e.xeId " +
            "LEFT JOIN TcCaEntity c ON c.tcCaId = e.tcCaId " +
            "LEFT JOIN ChiNhanhEntity b ON b.id = a.admNoiLamViec " +
            "WHERE e.ngayChamCong =:ngayChamCong")
    List<Object[]> getDuLieuChamCong(@Param("ngayChamCong") LocalDate ngayChamCong);

    @Query("SELECT e.taiXeId, a.admName, e.xeId, x.xeBienKiemSoat, e.tcCaId, c.maCa, e.ngayChamCong, e.gioThucTe, e.khachPhatSinh, e.ghiChu, e.tcCongId " +
            "FROM TcChamCongEntity e " +
            "LEFT JOIN AdminLv2UserEntity a ON e.taiXeId = a.admId " +
            "LEFT JOIN  XeEntity  x ON x.xeId = e.xeId " +
            "LEFT JOIN TcCaEntity c ON c.tcCaId = e.tcCaId " +
            "LEFT JOIN ChiNhanhEntity b ON b.id = a.admNoiLamViec " +
            "WHERE e.ngayChamCong =:ngayChamCong AND b.id IN :cnId ")
    List<Object[]> getDuLieuChamCongChiNhanh(@Param("ngayChamCong") LocalDate ngayChamCong, @Param("cnId") List<Integer> cnId);

    /*@Query(value = "SELECT CC.tai_xe_id AS taiXeId,AD.adm_name AS taiXeTen,CC.ngay_cham_cong AS ngayChamCong, SUM(CC.gio_thuc_te/CA.hous_in_ca) AS cong" +
            " FROM tc_cham_cong CC" +
            " LEFT JOIN admin_lv2_user AD ON CC.tai_xe_id = AD.adm_id" +
            " LEFT JOIN tc_ca CA ON CC.tc_ca_id = CA.tc_ca_id" +
            " WHERE MONTH(CC.ngay_cham_cong) = ?1 AND YEAR(CC.ngay_cham_cong) = ?2" +
            " GROUP BY CC.tai_xe_id, CC.ngay_cham_cong" +
            " ORDER BY CC.tai_xe_id, ngayChamCong  "
            ,nativeQuery = true)
    List<Object[]> getDuLieuBaoCaoChamCong(int thang, int nam);*/

    TcChamCongEntity findTopByTaiXeIdAndNgayChamCongOrderByCreatedDateDesc(int taiXeId, LocalDate ngayChamCong);

    /*@Query(value = "SELECT CC.tai_xe_id AS taiXeId,AD.adm_name AS taiXeTen,CC.ngay_cham_cong AS ngayChamCong, SUM(CC.gio_thuc_te/CA.hous_in_ca) AS cong" +
            " FROM tc_cham_cong CC" +
            " LEFT JOIN admin_lv2_user AD ON CC.tai_xe_id = AD.adm_id" +
            " LEFT JOIN tc_ca CA ON CC.tc_ca_id = CA.tc_ca_id" +
            " WHERE MONTH(CC.ngay_cham_cong) = ?1 AND YEAR(CC.ngay_cham_cong) = ?2 AND AD.adm_noi_lam_viec = ?3" +
            " GROUP BY CC.tai_xe_id, CC.ngay_cham_cong" +
            " ORDER BY CC.tai_xe_id, ngayChamCong  "
            ,nativeQuery = true)
    List<Object[]> getDuLieuBaoCaoChamCong(int thang, int nam,int chiNhanh);*/

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM TcChamCongEntity tc WHERE tc.taiXeId = :taiXeId AND tc.ngayChamCong = :ngayChamCong")
    void deleteByTaiXeAndNgayTruc(@Param("taiXeId") int taiXeId, @Param("ngayChamCong") LocalDate ngayChamCong);

    @Query(value = "SELECT * FROM tc_cham_cong e " +
            "INNER JOIN tc_ca c ON c.tc_ca_id = e.tc_ca_id " +
            "WHERE e.ngay_cham_cong =:ngayChamCong " +
            "AND e.tai_xe_id =:taiXeId " +
            "AND (HOUR(CURRENT_TIME) + 1) BETWEEN c.gio_bat_dau AND " +
            "(CASE WHEN c.gio_ket_thuc < c.gio_bat_dau " +
            " THEN (c.gio_ket_thuc + 24) " +
            " ELSE c.gio_ket_thuc END ) ", nativeQuery = true)
    List<TcChamCongEntity> findByTaiXeIdAndNgayChamCongAndCaTruc(@Param("taiXeId") int taiXeId,@Param("ngayChamCong") LocalDate ngayChamCong);
}
