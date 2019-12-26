package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcLichTrucEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TcLichTrucRepository extends JpaRepository<TcLichTrucEntity, Integer> {
    @Query(value = "SELECT e.tcLichId, e.taiXeId, a.admName, x.xeBienKiemSoat, e.ngayTruc,c.maCa, e.tcCaId, e.xeId, e.ghiChu FROM TcLichTrucEntity e " +
            "LEFT JOIN AdminLv2UserEntity a ON e.taiXeId = a.admId " +
            "LEFT JOIN XeEntity x ON e.xeId = x.xeId " +
            "LEFT JOIN TcCaEntity c ON e.tcCaId=c.tcCaId " +
            "WHERE e.taiXeId =:taixeId AND e.ngayTruc BETWEEN :startDate  AND :endDate  ")
    List<Object[]> getLichTrucByNgayTruc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("taixeId") int taixeId);


    @Query("SELECT e.taiXeId FROM TcLichTrucEntity e " +
            "WHERE e.ngayTruc BETWEEN :startDate AND :endDate " +
            "GROUP BY e.taiXeId")
    Page<Integer> getListTaiXeIdByNgayTruc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);


    //Lấy danh sách tài xế theo vùng làm việc(chi nhánh)
    @Query("SELECT e.taiXeId FROM TcLichTrucEntity e " +
            "LEFT JOIN AdminLv2UserEntity a ON e.taiXeId = a.admId " +
            "LEFT JOIN ChiNhanhEntity c ON a.admNoiLamViec = c.id "+
            "WHERE c.id IN :cnId AND " +
            "(e.ngayTruc BETWEEN :startDate AND :endDate)" +
            "GROUP BY e.taiXeId")
    Page<Integer> getListTaiXeIdByNgayTrucAndChiNhanh(@Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate,
                                                      @Param("cnId") List<Integer> cnId, Pageable pageable);


    // Lấy lịch trực theo tài xế và ngày trực
    List<TcLichTrucEntity> findByTaiXeIdAndNgayTruc(int taixeId, LocalDate ngayTruc);

    // Lấy lịch trực theo ca trực và bks xe
    List<TcLichTrucEntity> findByTcCaIdAndXeIdAndNgayTruc(int tcCaId, int xeId, LocalDate ngayTruc);

//    @Query("SELECT e FROM TcLichTrucEntity e WHERE e.ngayTruc =:ngayTruc")
//    public List<TcLichTrucEntity> findByNgayTruc(@Param("ngayTruc") Date ngayTruc);

    // Lấy danh sách Lịch trực theo ngày
    List<TcLichTrucEntity> findByNgayTruc(LocalDate ngayTruc);

    // Lấy danh sách Lịch trực theo ngày và chi nhánh
    @Query("SELECT e FROM TcLichTrucEntity e " +
            "LEFT JOIN AdminLv2UserEntity a ON e.taiXeId = a.admId " +
            "WHERE a.admNoiLamViec IN :chiNhanhIds AND " +
            "e.ngayTruc = :ngayTruc " +
            "GROUP BY e.taiXeId")
    List<TcLichTrucEntity> findByNgayTrucAndChiNhanh(@Param("ngayTruc") LocalDate ngayTruc,@Param("chiNhanhIds")  List<Integer> chiNhanhIds);

    List<TcLichTrucEntity> findByNgayTrucAndTcLichIdAndTaiXeIdAndTcCaIdAndXeId(LocalDate ngayTruc, Integer tcLichId, Integer taiXeId, Integer tcCaId, Integer xeId);

    //Lấy xeId theo tai xe va ngay hien tai
    @Query(value = "SELECT * FROM tc_lich_truc WHERE ngay_truc = ?1 AND tai_xe_id = ?2 ORDER BY created_date DESC LIMIT 1",
           nativeQuery = true)
    TcLichTrucEntity findTopByNgayTrucAndTaiXeId(String ngayTruc,Integer taixeId);

}
