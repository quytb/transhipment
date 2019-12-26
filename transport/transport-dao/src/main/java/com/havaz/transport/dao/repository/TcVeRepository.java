package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcVeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TcVeRepository extends JpaRepository<TcVeEntity, Integer> {
    List<TcVeEntity> getVeTcByTcLenhId(int lenhId);

    List<TcVeEntity> findByTcLenhTraId(Integer lenhTraId);

    TcVeEntity getByBvvId(int bvvId);

    Optional<TcVeEntity> findByBvvId(int bvvId);

    void deleteByBvvId(int bvvId);

    void deleteByBvvIdIn(Set<Integer> bvvIds);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE TcVeEntity TCV SET TCV.veAction = :veAction WHERE TCV.bvvId IN (:bvvId)")
    void updateVeActionByBvvId(@Param("veAction") int veAction, @Param("bvvId") Set<Integer> bvvId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE TcVeEntity TCV SET TCV.tcLenhId = 0, TCV.laiXeIdDon = 0 WHERE TCV.tcLenhId = :tcLenhId")
    void updateVeSetLenhDonIdZero(@Param("tcLenhId") Integer tcLenhId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE TcVeEntity TCV SET TCV.tcLenhTraId = 0, TCV.laiXeIdTra = 0 WHERE TCV.tcLenhTraId = :tcLenhId")
    void updateVeSetLenhTraIdZero(@Param("tcLenhId") Integer tcLenhId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE TcVeEntity TCV SET TCV.thoiGianDon = :thoiGianDon, TCV.tcDistanceDon= :distanceDon WHERE TCV.bvvId IN (:bvvId)")
    void updateThoiGianDon(@Param("bvvId") List<Integer> bvvId, @Param("thoiGianDon") Integer thoiGianDon, @Param("distanceDon") Integer distanceDon);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE TcVeEntity TCV SET TCV.thoiGianTra = :thoiGianTra, TCV.tcDistanceTra=:distanceTra WHERE TCV.bvvId IN (:bvvId)")
    void updateThoiGianTra(@Param("bvvId") List<Integer> bvvId, @Param("thoiGianTra") Integer thoiGianTra, @Param("distanceTra") Integer distanceTra);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE TcVeEntity TCV SET TCV.tcHubDiemDon = :hubDon WHERE TCV.bvvId IN (:veIds)")
    void updateHubDon(@Param("veIds") List<Integer> veIds, @Param("hubDon") Integer hubDon);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE TcVeEntity TCV SET TCV.tcHubDiemTra = :hubTra WHERE TCV.bvvId IN (:veIds)")
    void updateHubTra(@Param("veIds") List<Integer> veIds, @Param("hubTra") Integer hubTra);

    // get list ve by lenh id
    List<TcVeEntity> getVeTcTraByTcLenhTraId(int lenhTraId);

    @Query("SELECT ve FROM TcVeEntity ve WHERE ve.bvvId in :ids")
    List<TcVeEntity> findByBvvIds(@Param("ids") List<Integer> ids);

    @Query("SELECT ve FROM TcVeEntity ve inner join TcLenhEntity lenh ON lenh.tcLenhId = ve.tcLenhId" +
            " WHERE ve.didId = :didId AND ve.tcTrangThaiTra IN (:status) " +
            " AND ve.laiXeIdTra = :txId AND lenh.kieuLenh = :type")
    List<TcVeEntity> findByDidIdAndLaiXeIdTra(@Param("didId") Integer didId, @Param("status") List<Integer> status,
                                              @Param("txId") Integer txId, @Param("type") Integer type);

    @Query("SELECT ve FROM TcVeEntity ve inner join TcLenhEntity lenh ON lenh.tcLenhId = ve.tcLenhId" +
            " WHERE ve.didId = :didId AND ve.tcTrangThaiDon IN (:status) " +
            " AND ve.laiXeIdDon = :txId AND lenh.kieuLenh = :type")
    List<TcVeEntity> findByDidIdAndLaiXeIdDon(@Param("didId") Integer didId, @Param("status") List<Integer> status,
                                              @Param("txId") Integer txId, @Param("type") Integer type);

    List<TcVeEntity> findByDidIdAndTcLenhIdIsNullOrDidIdAndTcLenhId(Integer didId, Integer didId2, Integer lenhId);

    List<TcVeEntity> findByDidIdAndTcLenhTraIdIsNullOrDidIdAndTcLenhTraId(Integer didId, Integer didId2, Integer lenhTraId);

    List<TcVeEntity> findByBvvIdIn(List<Integer> bvvids);

    List<TcVeEntity> findByTcLenhId(Integer lenhId);

    Optional<TcVeEntity> findByBvvId(Integer bvvId);
}
