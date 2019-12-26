package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcCaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TcCaRepository extends JpaRepository<TcCaEntity, Integer> {
//    public Page<TcCaEntity> findAll(Pageable pageable);

    List<TcCaEntity> findByTrangThai(Boolean trangThai);

    @Query(value = "SELECT t FROM TcCaEntity t WHERE t.maCa=:maCa OR (t.gioBatDau=:gioBatDau AND t.gioKetThuc=:gioKetThuc)")
    List<TcCaEntity> checkCaUnique(@Param("maCa") String maCa, @Param("gioBatDau") Float gioBatDau, @Param("gioKetThuc") Float gioKetThuc);

    @Query(value = "SELECT t FROM TcCaEntity t WHERE t.maCa=:maCa")
    List<TcCaEntity> checkCaUniqueByCode(@Param("maCa") String maCa);

    //Lấy toàn bộ ca trực và không phân trang
    List<TcCaEntity> findAllByOrderByTcCaIdDesc();
}
