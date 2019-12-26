package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcVtcCtvEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TcVtcCtvRepository extends JpaRepository<TcVtcCtvEntity, Integer> {

    List<TcVtcCtvEntity> findByTcVttId(Integer vttId);

    Optional<TcVtcCtvEntity> findByTcVttIdAndTcCtvIdAndStatus(Integer vttId, Integer ctvId, Integer Status);


    @Query("SELECT tvc FROM TcVtcCtvEntity tvc " +
            "WHERE tvc.tcVttId=:vungId " +
            "AND tvc.status =:active " +
            "AND tvc.tcCtvId NOT IN (SELECT l.laiXeId FROM TcLenhEntity l WHERE l.trangThai in (:status))")
    List<TcVtcCtvEntity> findVtcDriverNotBusy(@Param("vungId") Integer vungId, @Param("active") Integer active, @Param("status") List<Integer> status);

    @Query("SELECT tvc FROM TcVtcCtvEntity tvc LEFT JOIN TcLenhEntity tl ON tl.laiXeId = tvc.tcCtvId " +
            " WHERE tvc.tcVttId =:vtcId AND tl.trangThai =:status AND tl.isHavazNow <> :isCmdNow AND tvc.status= :active")
    List<TcVtcCtvEntity> findByDriverByCmdRunning(@Param("vtcId")Integer vtcId, @Param("status") Integer status,
                                                  @Param("isCmdNow") Boolean isCmdNow, @Param("active") Integer vtcActive);


}


