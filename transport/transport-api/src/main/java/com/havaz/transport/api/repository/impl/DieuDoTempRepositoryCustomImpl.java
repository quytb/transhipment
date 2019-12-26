package com.havaz.transport.api.repository.impl;

import com.havaz.transport.api.model.NotActiveDTO;
import com.havaz.transport.api.model.ThongTinNodeDto;
import com.havaz.transport.api.repository.DieuDoTempRepositoryCustom;
import com.havaz.transport.dao.repository.AbstractRepository;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unchecked")
@Repository
public class DieuDoTempRepositoryCustomImpl extends AbstractRepository implements DieuDoTempRepositoryCustom {

    @Override
    public List<NotActiveDTO> getListNotActive(ThongTinNodeDto thongTinNodeDto) {

        String sql = "SELECT " +
                "       notT.not_id                                                    AS notId, " +
                "       CONCAT(dieuDo.did_gio_dieu_hanh, ' ← ', dieuDo.did_gio_xuat_ben_that, ' '," +
                "              REPLACE(t.tuy_ma, '-', ' → '), ' ', sdg.sdg_so_cho - dieuDo.did_so_cho_da_ban, '/', " +
                "              sdg.sdg_so_cho, '   ', IFNULL(x.xe_bien_kiem_soat, '')) AS tripData " +
                " FROM not_tuyen notT " +
                "         INNER JOIN tuyen t ON notT.not_tuy_id = t.tuy_id " +
                "         INNER JOIN dieu_do_temp dieuDo ON notT.not_id = dieuDo.did_not_id " +
                "         LEFT JOIN so_do_giuong sdg ON dieuDo.did_loai_so_do = sdg.sdg_id " +
                "         LEFT JOIN xe x ON dieuDo.did_xe_id = x.xe_id " +
                " WHERE dieuDo.did_status > 0 " +
                "   AND notT.not_cong_tac_vien = 0 ";

        final String groupBy = " GROUP BY notT.not_id ";
        final String orderBy = " ORDER BY dieuDo.did_gio_dieu_hanh";
        Map<String, Object> paramMap = new HashMap<>();
        if (Objects.nonNull(thongTinNodeDto.getTruocGioKhoihanh())) {
            sql += " AND dieuDo.did_gio_dieu_hanh BETWEEN CURRENT_TIME " +
                    " AND ADDTIME(CURRENT_TIME, SEC_TO_TIME(:truocgiokhoihanh)) ";
            paramMap.put("truocgiokhoihanh", thongTinNodeDto.getTruocGioKhoihanh());
        }

        if (thongTinNodeDto.getNgayXuatBen() != null) {
            sql += " AND dieuDo.did_time = UNIX_TIMESTAMP(:ngayxuatben) ";
            paramMap.put("ngayxuatben", thongTinNodeDto.getNgayXuatBen());
        }

        if (thongTinNodeDto.getChieuXeChay() != null) {
            sql += " AND notT.not_chieu_di = :chieudi ";
            paramMap.put("chieudi", thongTinNodeDto.getChieuXeChay().getCode());
        }

        if (thongTinNodeDto.getListTuyen() != null && !thongTinNodeDto.getListTuyen().isEmpty()) {
            sql += " AND t.tuy_id IN :tuyIds ";
            paramMap.put("tuyIds", thongTinNodeDto.getListTuyen());
        }

        sql += groupBy + orderBy;

        Session session = getCurrentSession();
        NativeQuery<NotActiveDTO> query = session.createNativeQuery(sql);
        paramMap.forEach(query::setParameter);
        return query.setResultTransformer(Transformers.aliasToBean(NotActiveDTO.class)).list();
    }

    @Override
    public List<Integer> getListTripId() {
        String sql = "SELECT DD.did_id " +
                " FROM dieu_do_temp DD " +
                " WHERE DATE(NOW()) = DATE(FROM_UNIXTIME(DD.did_time)) ";
        return getCurrentSession().createNativeQuery(sql).list();
    }
}
