package com.havaz.transport.api.repository.impl;

import com.havaz.transport.api.model.TcMonitorTransferTicketDTO;
import com.havaz.transport.api.repository.TcMonitorTransferVeRepositoryCustom;
import com.havaz.transport.dao.repository.AbstractRepository;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TcMonitorTranferVeRepositoryCustomImpl extends AbstractRepository implements TcMonitorTransferVeRepositoryCustom {

    @Override
    public List<TcMonitorTransferTicketDTO> findByNgayAndTrangThai(LocalDate ngayKiemTra, int trangThai) {
        String jql = "SELECT e.monitor_id AS monitorId, " +
                "   e.monitor_trip AS monitorTrip, " +
                "   e.monitor_bks AS monitorBks, " +
                "   e.monitor_chieu AS monitorChieu, " +
                "   e.monitor_gxb AS monitorGxb, " +
                "   e.monitor_count_ve_erp_don AS monitorCountVeErpDon, " +
                "   e.monitor_count_ve_tc_don AS monitorCountVeTcDon, " +
                "   e.monitor_count_ve_erp_tra AS monitorCountVeErpTra, " +
                "   e.monitor_count_ve_tc_tra AS monitorCountVeTcTra, " +
                "   e.status AS status, " +
                "   e.monitor_tuyen AS monitorTuyen " +
                "   FROM tc_monitor_transfer_ve e " +
                "     INNER JOIN (" +
                "       SELECT max(e.monitor_id) AS monitorId " +
                "         FROM tc_monitor_transfer_ve e " +
                "         WHERE DATE_FORMAT(e.monitor_last_updated_date,'%Y-%m-%d') =:ngayKiemTra " +
                "         GROUP BY monitor_trip " +
                "         ORDER BY monitorId " +
                "    ) T ON T.monitorId = e.monitor_id " +
                "   WHERE DATE_FORMAT(e.monitor_last_updated_date,'%Y-%m-%d')=:ngayKiemTra " +
                "   GROUP BY monitor_trip " +
                "   ORDER BY e.monitor_gxb ";
        NativeQuery<TcMonitorTransferTicketDTO> queryX = getCurrentSession().createNativeQuery(jql);
        queryX.setParameter("ngayKiemTra", ngayKiemTra);
        return queryX.setResultTransformer(Transformers.aliasToBean(TcMonitorTransferTicketDTO.class)).list();
    }
}
