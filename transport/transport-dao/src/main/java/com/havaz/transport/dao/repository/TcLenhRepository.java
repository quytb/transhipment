package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcLenhEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TcLenhRepository extends JpaRepository<TcLenhEntity, Integer> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE TcLenhEntity LL SET LL.laiXeId = :laiXeId, LL.lastUpdatedDate = :updatedDate WHERE LL.tcLenhId = :lenhId")
    void updateLenhLaiXeId(@Param("laiXeId") int laiXeId, @Param("updatedDate") LocalDateTime updatedDate,@Param("lenhId") int lenhId);

    @Query("SELECT tcl FROM TcLenhEntity tcl WHERE tcl.laiXeId = :laiXeId AND tcl.trangThai IN (:trangThai) AND tcl.kieuLenh =:kieulenh")
    List<TcLenhEntity> getAllByLaiXeIdAAndTrangThai(@Param("laiXeId") int laiXeId, @Param("trangThai") Set<Integer> trangThai,@Param("kieulenh") int kieulenh);

    List<TcLenhEntity> getAllByKieuLenhAndTrangThaiNotIn(int kieuLenh, Set<Integer> trangThai);

    List<TcLenhEntity> getAllByKieuLenhAndTrangThaiIn(int kieuLenh, Set<Integer> trangThai);

    Optional<TcLenhEntity> findByTcLenhIdAndTrangThaiIn(Integer lenhId, List<Integer> trangThai);

    @Query("SELECT tcl FROM TcLenhEntity tcl WHERE tcl.laiXeId = :laiXeId AND tcl.trangThai IN (:trangThai)")
    List<TcLenhEntity> findByLaiXeIdAndTrangThai(@Param("laiXeId") int laiXeId, @Param("trangThai") Set<Integer> trangThai);

    List<TcLenhEntity> findByLaiXeIdAndTrangThaiIn(Integer driverId, List<Integer> status);

    List<TcLenhEntity> findByLaiXeIdAndTrangThaiAndIsHavazNowIsNot(Integer driverId, Integer statusRunning, Boolean isHavazNow);

    List<TcLenhEntity> findByLaiXeIdAndTrangThai(Integer driverId, Integer status);


    @Query("SELECT tcl FROM TcLenhEntity tcl WHERE tcl.laiXeId = :laiXeId AND tcl.trangThai IN (:trangThai) AND tcl.kieuLenh = :type ")
    List<TcLenhEntity> findByLaiXeIdAndTrangThaiAndKieuLenh(@Param("laiXeId") int laiXeId, @Param("trangThai") Set<Integer> trangThai, @Param("type") int type);

    Optional<TcLenhEntity> findByTcLenhIdAndTrangThai(Integer id, Integer status);
}
