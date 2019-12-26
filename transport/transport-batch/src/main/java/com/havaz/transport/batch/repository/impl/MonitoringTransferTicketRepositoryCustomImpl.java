package com.havaz.transport.batch.repository.impl;

import com.havaz.transport.batch.model.MonitorTcvDTO;
import com.havaz.transport.batch.model.MonitorTcvTraDTO;
import com.havaz.transport.batch.repository.MonitoringTransferTicketRepositoryCustom;
import com.havaz.transport.dao.repository.AbstractRepository;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class MonitoringTransferTicketRepositoryCustomImpl extends AbstractRepository implements MonitoringTransferTicketRepositoryCustom {

    @Override
    public List<MonitorTcvDTO> calculateData(boolean isPickup) {
        String sql ="SELECT DISTINCT cast(T1.ID as char(10))          AS monitorTrip," +
                "                cast(T1.bvvCount as char(10))    AS monitorCountVeErp," +
                "                cast(T2.tcvCount as char(10))    AS monitorCountVeTc," +
                "                T1.monitorTuyen," +
                "                T1.monitorBks," +
                "                T1.monitorSdt," +
                "                T1.monitorGxb," +
                "                T1.erpBvvIds," +
                "                T2.tcBvvIds," +
                "                cast(T1.monitorChieu as char(2)) AS monitorChieu " +
                "FROM (SELECT DD.did_id                              AS ID," +
                "             COUNT(BVV.bvv_id)                      AS bvvCount," +
                "             group_concat(BVV.bvv_id separator ',') AS erpBvvIds," +
                "             T.tuy_ten                              AS monitorTuyen," +
                "             XE.xe_bien_kiem_soat                   AS monitorBks," +
                "             XE.xe_so_dien_thoai                    AS monitorSdt," +
                "             DD.did_gio_xuat_ben_that               AS monitorGxb," +
                "             NOT_T.not_chieu_di                     AS monitorChieu " +
                "      FROM dieu_do_temp DD" +
                "               LEFT JOIN ban_ve_ve BVV ON BVV.bvv_bvn_id = DD.did_id" +
                "               LEFT JOIN not_tuyen NOT_T ON DD.did_not_id = NOT_T.not_id" +
                "               LEFT JOIN tuyen T ON NOT_T.not_tuy_id = T.tuy_id" +
                "               LEFT JOIN xe XE ON DD.did_xe_id = XE.xe_id" +
                "      WHERE DATE(NOW()) = DATE(FROM_UNIXTIME(DD.did_time))";

        if (isPickup) {
            sql = sql + "        AND BVV.bvv_trung_chuyen_a = 1";
        } else {
            sql = sql + "        AND BVV.bvv_trung_chuyen_b = 1";
        }
        sql = sql +
                "        AND FROM_UNIXTIME(BVV.bvv_time_last_update) <= DATE_SUB(NOW(), INTERVAL 120 SECOND)" +
                "      GROUP BY DD.did_id" +
                "      ORDER BY bvvCount DESC) T1" +
                "         LEFT JOIN (SELECT DISTINCTROW(DD.did_id)                            AS ID," +
                "                                       group_concat(TCV.bvv_id separator ',') AS tcBvvIds," +
                "                                       COUNT(TCV.tc_ve_id) AS tcvCount" +
                "                     FROM dieu_do_temp DD" +
                "                              LEFT JOIN tc_ve TCV ON DD.did_id = TCV.did_id" +
                "                     WHERE DATE(NOW()) = DATE(FROM_UNIXTIME(DD.did_time))" ;
        if (isPickup) {
            sql = sql + " AND TCV.is_tc_don = 1 ";
        } else {
            sql = sql + " AND  TCV.is_tc_tra = 1";
        }

        sql = sql +
                "                     GROUP BY DD.did_id" +
                "                     ORDER BY tcvCount DESC) T2 ON T1.ID = T2.ID";
        List<MonitorTcvDTO> monitorTcvDTOList = getEntityManager().createNativeQuery(sql)
                .unwrap(Query.class)
                .setResultTransformer(Transformers.aliasToBean(MonitorTcvDTO.class))
                .list();
        return monitorTcvDTOList;
    }

    @Override
    public MonitorTcvTraDTO calculateDataTra(int didId) {
        String sql = "SELECT " +
                " cast(T2.tripId as char(10))          AS monitorTrip, " +
                " T1.bvvCount AS monitorCountVeErpTra, " +
                " T2.tcvCount AS monitorCountVeTcTra, " +
                " T1.erpBvvIds, " +
                " T2.tcBvvIds " +
                "FROM " +
                " (SELECT " +
                " group_concat(BVV.bvv_id separator ',') AS erpBvvIds," +
                " cast(COUNT(BVV.bvv_id) as char(10))  AS bvvCount " +
                " FROM ban_ve_ve BVV " +
                " WHERE BVV.bvv_bvn_id = :didId AND BVV.bvv_trung_chuyen_b = 1) T1, " +
                " (SELECT " +
                " TCV.did_id  as tripId, " +
                " group_concat(TCV.bvv_id separator ',') AS tcBvvIds," +
                " cast(COUNT(TCV.bvv_id) as char(10))  AS tcvCount " +
                " FROM tc_ve TCV " +
                " WHERE TCV.did_id = :didId AND TCV.is_tc_tra = 1) T2 ";
        MonitorTcvTraDTO monitorTcvTraDTO = (MonitorTcvTraDTO) getEntityManager().createNativeQuery(sql)
                .unwrap(Query.class)
                .setResultTransformer(Transformers.aliasToBean(MonitorTcvTraDTO.class))
                .setParameter("didId", didId).setMaxResults(1).uniqueResult();
        return monitorTcvTraDTO;
    }


}
