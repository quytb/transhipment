package com.havaz.transport.api.repository.impl;

import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.common.LenhConstants;
import com.havaz.transport.api.form.BaoCaoLenhForm;
import com.havaz.transport.api.form.FilterLenhForm;
import com.havaz.transport.api.model.BaoCaoLenhDTO;
import com.havaz.transport.api.model.ChiTietLenhDTO;
import com.havaz.transport.api.model.ClientHavazNowDTO;
import com.havaz.transport.api.model.LenhContainGXBDTO;
import com.havaz.transport.api.model.MaLenhDTO;
import com.havaz.transport.api.model.VeTrungChuyenDTO;
import com.havaz.transport.api.model.XeTuyenDTO;
import com.havaz.transport.api.repository.TcLenhRepositoryCustom;
import com.havaz.transport.core.constant.VeConstants;
import com.havaz.transport.dao.entity.TcLenhEntity;
import com.havaz.transport.dao.query.SelectOptions;
import com.havaz.transport.dao.repository.AbstractRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LocalDateType;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("unchecked")
@Repository
public class TcLenhRepositoryCustomImpl extends AbstractRepository implements TcLenhRepositoryCustom {

    @Override
    public List<TcLenhEntity> getLenhByTaiXeId(int taiXeId, int kieuLenh) {
        String hql = "SELECT lenh FROM TcLenhEntity lenh WHERE lenh.kieuLenh = :kieuLenh AND lenh.laiXeId = :taiXeId AND (lenh.trangThai =:daDieu OR lenh.trangThai = :dangDon) ORDER BY lenh.trangThai DESC, lenh.createdDate";
        Query query = getEntityManager().createQuery(hql);
        query.setParameter("taiXeId", taiXeId);
        query.setParameter("daDieu", VeConstants.TC_STATUS_DA_DIEU);
        query.setParameter("dangDon", VeConstants.TC_STATUS_DANG_DI_DON);
        query.setParameter("kieuLenh", kieuLenh);
        return query.getResultList();
    }

    @Override
    public List<Object[]> getListKhachTrungChuyen(int lenhId) {
        Query query = getEntityManager().createNamedQuery("getKhachTrungChuyen");
        query.setParameter("tc_lenh_id", lenhId);
        return query.getResultList();
    }

    @Override
    public List<Object[]> getListKhachTrungChuyenTra(int lenhTraId) {
        Query query = getEntityManager().createNamedQuery("getKhachTrungChuyenTra");
        query.setParameter("tc_lenh_tra_id", lenhTraId);
        return query.getResultList();
    }

    @Override
    public List<TcLenhEntity> getLenhDangDon(int taiXeId, int kieuLenh) {
        String hql = "SELECT lenh FROM TcLenhEntity lenh WHERE lenh.kieuLenh = :kieuLenh AND lenh.laiXeId = :taiXeId AND lenh.trangThai = :dangDon";
        Query query = getEntityManager().createQuery(hql);
        query.setParameter("taiXeId", taiXeId);
        query.setParameter("dangDon", VeConstants.TC_STATUS_DANG_DI_DON);
        query.setParameter("kieuLenh", kieuLenh);
        return query.getResultList();
    }

    @Override
    public List<Object[]> getListKhachDangDon(int lenhId) {
        Query query = getEntityManager().createNamedQuery("getKhachDangDon");
        query.setParameter("tc_lenh_id", lenhId);
        return query.getResultList();
    }

    @Override
    public List<XeTuyenDTO> getXeTuyenInfor(int lenhId) {
        final int lenhDon = 1;
        final int lenhTra = 2;
        List<Integer> kieulenh = Arrays.asList(lenhDon, lenhTra);
        String sql = "SELECT " +
                "       XE.xe_bien_kiem_soat AS bks, " +
                "       XE.xe_so_dien_thoai  AS sdt, " +
                "       DD.did_id            AS didId " +
                " FROM tc_ve tcv " +
                "         LEFT JOIN tc_lenh lenh " +
                "     ON tcv.tc_lenh_id = lenh.tc_lenh_id " +
                "         LEFT JOIN dieu_do_temp DD " +
                "     ON tcv.did_id = DD.did_id " +
                "         LEFT JOIN xe XE " +
                "     ON DD.did_xe_id = XE.xe_id " +
                " WHERE (tcv.tc_lenh_id = :tc_lenh_id OR tcv.tc_lenh_tra_id = :tc_lenh_id) " +
                "   AND lenh.kieu_lenh IN :kieulenh " +
                "   AND (tcv.is_tc_don = 1 OR tcv.is_tc_tra = 1) " +
                " GROUP BY DD.did_id " +
                " ORDER BY DD.did_id";

        Session session = getCurrentSession();
        NativeQuery<XeTuyenDTO> query = session.createNativeQuery(sql);
        query.setParameter("tc_lenh_id", lenhId);
        query.setParameter("kieulenh", kieulenh);
        return query.setResultTransformer(Transformers.aliasToBean(XeTuyenDTO.class)).list();
    }

    @Override
    public List<Object[]> getHistoryByTaiXeId(int taiXeId, int month, int year) {
        Query query = getEntityManager().createNamedQuery("getHistoryByTaiXeId");
        query.setParameter("inputMonth", month);
        query.setParameter("inputYear", year);
        query.setParameter("inputTaiXeId", taiXeId);
        query.setParameter("lenh_finished", LenhConstants.LENH_STATUS_DA_HOAN_TAT);
        query.setParameter("lenh_canceled", LenhConstants.LENH_STATUS_DA_BI_HUY);
        query.setParameter("ve_pickedUp", VeConstants.TC_STATUS_DA_DON);
        query.setParameter("ve_canceled", VeConstants.TC_STATUS_DA_HUY);
        return query.getResultList();
    }

    @Override
    public List<ChiTietLenhDTO> getListChiTietLenh(FilterLenhForm filterLenhForm) {
        List<ChiTietLenhDTO> resultList = new ArrayList<>();
        String nativeQueryLenh;

        // Lấy ra tất cả các lệnh đón
        LocalDate now = LocalDate.now();
        nativeQueryLenh = "SELECT LL.tc_lenh_id AS tcLenhId," +
                " LL.lai_xe_id AS laiXeId," +
                " AD.adm_name AS taiXeTen," +
                " LL.trang_thai AS trangThai," +
                " LL.kieu_lenh AS kieuLenh," +
                " LL.created_date AS createdDate," +
                " COALESCE(khachdon, 0)      AS khachDon," +
                " x.xe_bien_kiem_soat AS bks," +
                " COALESCE(khachtra, 0)      AS khachTra," +
                " COALESCE(AD2.adm_name, '') AS nguoiTao, " +
                " COALESCE(AD3.adm_name, '') AS nguoiHuy, " +
                " COALESCE(khachhuy, 0)      AS khachHuy, " +
                " LL.ly_do_huy      AS lyDoHuy," +
                " COALESCE(LL.diem_giao_khach, 0) AS diemTCDenId," +
                " COALESCE(BX.bex_ten, '') AS diemTCDen " +
                " FROM tc_lenh LL"+
                " LEFT JOIN ("+
                    " SELECT LE.tc_lenh_id,"+
                    "COUNT(IF(LE.kieu_lenh = :lenhDon, 1, NULL)) AS khachdon,"+
                    "COUNT(IF(LE.kieu_lenh = :lenhTra, 1, NULL)) AS khachtra "+
                    "FROM tc_ve VE "+
                    "JOIN tc_lenh LE ON VE.tc_lenh_id = LE.tc_lenh_id OR VE.tc_lenh_tra_id = LE.tc_lenh_id "+
                    "WHERE DATE(LE.created_date) = :timequery "+
                    "GROUP BY LE.tc_lenh_id "+
                    ") SUB ON LL.tc_lenh_id = SUB.tc_lenh_id "+
                " LEFT JOIN ("+
                    " SELECT LV.lenh_id,"+
                    "COUNT(IF(ve_status = 4, 1, NULL)) AS khachhuy "+
                    "FROM tc_lai_xe_ve LV "+
                    "INNER JOIN tc_lenh LL ON LV.lenh_id = LL.tc_lenh_id "+
                    "WHERE DATE(LL.created_date) = :timequery "+
                    "GROUP BY LV.lenh_id "+
                    ") SUB2 ON LL.tc_lenh_id = SUB2.lenh_id "+
                "LEFT JOIN admin_lv2_user AD ON AD.adm_id = LL.lai_xe_id " +
                "LEFT JOIN admin_lv2_user AD2 ON AD2.adm_id = LL.created_by " +
                "LEFT JOIN admin_lv2_user AD3 ON AD3.adm_id = LL.canceled_by " +
                "LEFT JOIN ben_xe BX ON BX.bex_id = LL.diem_giao_khach " +
                "LEFT JOIN xe x ON x.xe_id = LL.xe_id ";


        //Neu ko query theo ngày tạo thì lấy mặc định là hôm nay
        LocalDate timestampToQuery = filterLenhForm.getNgayTao() != null ? filterLenhForm.getNgayTao() : now;
        nativeQueryLenh+=" WHERE DATE(LL.created_date) = :timequery";

        //Neu có query theo maLenh
        if (filterLenhForm.getLenhId() > 0)
            nativeQueryLenh += " AND LL.tc_lenh_id = " + filterLenhForm.getLenhId();

        //Neu có query theo taiXeId
        if (filterLenhForm.getTaiXeId() > 0) {
            nativeQueryLenh += " AND LL.lai_xe_id = " + filterLenhForm.getTaiXeId();
        }

        //Neu co query theo trang thai cua lenh
        if (filterLenhForm.getTrangThai() > 0) {
            nativeQueryLenh += " AND LL.trang_thai = " + filterLenhForm.getTrangThai();
        }

        //Neu co query theo kieu lenh
        if (filterLenhForm.getKieuLenh() > 0) {
            nativeQueryLenh += " AND LL.kieu_lenh = " + filterLenhForm.getKieuLenh();
        }

        String sortBy = StringUtils.isEmpty(filterLenhForm.getPaging().getSortBy()) ? "LL.trang_thai" : filterLenhForm.getPaging().getSortBy();
        String sortType = StringUtils.isEmpty(filterLenhForm.getPaging().getSortType()) ? Constant.ASC : Constant.DESC;
        nativeQueryLenh += " ORDER BY " + sortBy + " " + sortType;

        //Order by id lenh
        nativeQueryLenh += (" , LL.tc_lenh_id DESC");
        NativeQuery<ChiTietLenhDTO> nativeQuery = getCurrentSession().createNativeQuery(nativeQueryLenh);
        nativeQuery.setParameter("lenhDon", LenhConstants.LENH_DON, IntegerType.INSTANCE);
        nativeQuery.setParameter("lenhTra", LenhConstants.LENH_TRA, IntegerType.INSTANCE);
        nativeQuery.setParameter("timequery", timestampToQuery, LocalDateType.INSTANCE);
        int page = filterLenhForm.getPaging().getPage() <= 0 ? 1 : filterLenhForm.getPaging().getPage();
        int size = filterLenhForm.getPaging().getSize() <= 0 ? Constant.CONFIGURATION_NO_10 : filterLenhForm.getPaging().getSize();
        resultList = nativeQuery.setFirstResult((page - 1) * size).setMaxResults(size).setResultTransformer(Transformers.aliasToBean(ChiTietLenhDTO.class)).list();
        return resultList;
    }

    @Override
    public long getCountListChiTietLenh(FilterLenhForm filterLenhForm) {
        LocalDate now = LocalDate.now();
        String sql = "SELECT COUNT(*) FROM tc_lenh LL LEFT JOIN ( " +
                "   SELECT LE.tc_lenh_id " +
                "     FROM tc_ve VE JOIN tc_lenh LE " +
                "       ON LE.tc_lenh_id = VE.tc_lenh_id OR LE.tc_lenh_id = VE.tc_lenh_tra_id " +
                "         WHERE DATE(LE.created_date) = :createdDate" +
                "         GROUP BY LE.tc_lenh_id" +
                " ) SUB ON LL.tc_lenh_id = SUB.tc_lenh_id";

        String whereClause = " WHERE DATE(LL.created_date) =  :createdDate";

        Map<String, Object> paramMap = new HashMap<>(4);
        LocalDate timestampToQuery;
        if (filterLenhForm.getNgayTao() != null ) {
            timestampToQuery = filterLenhForm.getNgayTao();
        } else {
            timestampToQuery = now;
        }

        if (filterLenhForm.getLenhId() > 0) {
            whereClause += " AND LL.tc_lenh_id = :lenhId ";
            paramMap.put("lenhId", filterLenhForm.getLenhId());
        }

        if (filterLenhForm.getTaiXeId() > 0) {
            whereClause += " AND LL.lai_xe_id = :taixeId ";
            paramMap.put("taixeId", filterLenhForm.getTaiXeId());
        }

        if (filterLenhForm.getTrangThai() > 0) {
            whereClause += " AND LL.trang_thai = :trangthai ";
            paramMap.put("trangthai", filterLenhForm.getTrangThai());
        }

        if (filterLenhForm.getKieuLenh() > 0) {
            whereClause += " AND LL.kieu_lenh = :kieulenh ";
            paramMap.put("kieulenh", filterLenhForm.getKieuLenh());
        }

        sql += whereClause;

        NativeQuery<Long> nativeQuery = getCurrentSession().createNativeQuery(sql);
        nativeQuery.setParameter("createdDate", timestampToQuery);
        paramMap.forEach(nativeQuery::setParameter);
        return nativeQuery.getSingleResult();
    }

    @Override
    public List<MaLenhDTO> getMaLenh(FilterLenhForm filterLenhForm) {
        LocalDate createdDate = LocalDate.now();
        String sql = "SELECT LL.tc_lenh_id AS lenhId, LL.kieu_lenh AS kieuLenh " +
                " FROM tc_lenh LL " +
                "         LEFT JOIN " +
                "     ( " +
                "         SELECT LE.tc_lenh_id " +
                "           FROM tc_ve VE JOIN tc_lenh LE " +
                "             ON LE.tc_lenh_id = VE.tc_lenh_id OR LE.tc_lenh_id = VE.tc_lenh_tra_id " +
                "           WHERE DATE(LE.created_date) = :createdDate " +
                "           GROUP BY LE.tc_lenh_id " +
                "     ) SUB ON LL.tc_lenh_id = SUB.tc_lenh_id ";


        String whereClause = " WHERE DATE(LL.created_date) = :createdDate ";
        if (filterLenhForm.getNgayTao() != null) {
            createdDate = filterLenhForm.getNgayTao();
        }

        Map<String, Object> paramMap = new HashMap<>(4);
        //Neu có query theo maLenh
        if (filterLenhForm.getLenhId() > 0) {
            whereClause += " AND LL.tc_lenh_id = :lenhId";
            paramMap.put("lenhId", filterLenhForm.getLenhId());
        }

        if (filterLenhForm.getTaiXeId() > 0) {
            whereClause += " AND LL.lai_xe_id = :taixeId ";
            paramMap.put("taixeId", filterLenhForm.getTaiXeId());
        }

        //Neu co query theo trang thai cua lenh
        if (filterLenhForm.getTrangThai() > 0) {
            whereClause += " AND LL.trang_thai = :trangthai ";
            paramMap.put("trangthai", filterLenhForm.getTrangThai());
        }

        //Neu co query theo kieu lenh
        if (filterLenhForm.getKieuLenh() > 0) {
            whereClause += " AND LL.kieu_lenh = :kieuLenh ";
            paramMap.put("kieuLenh", filterLenhForm.getKieuLenh());
        }

        sql += whereClause;

        Session session = getCurrentSession();
        NativeQuery<MaLenhDTO> query = session.createNativeQuery(sql);

        query.setParameter("createdDate", createdDate);
        paramMap.forEach(query::setParameter);
        return query.setResultTransformer(Transformers.aliasToBean(MaLenhDTO.class)).list();
    }

    @Override
    public List<Object[]> getTongSoLenh(BaoCaoLenhForm baoCaoLenhForm) {
        LocalDate now = LocalDate.now();
        StringBuilder nativeQuery = new StringBuilder();
        nativeQuery.append("SELECT LL.lai_xe_id AS taiXeId " +
                ",AD.adm_name AS taiXeTen " +
                ",COUNT(DISTINCT LL.tc_lenh_id) Tong_Lenh " +
                ",LL.kieu_lenh AS kieuLenh " +
                ",SUM(LL.trang_thai = 3) AS Lenh_Huy " +
                ",SUM(LL.trang_thai = 4) AS Lenh_Hoan_Thanh " +
                ",AD.adm_ma AS MaNV " +
                ",CN.cn_name AS noiLamViec " +
                "FROM tc_lenh LL " +
                "LEFT JOIN admin_lv2_user AD ON LL.lai_xe_id = AD.adm_id " +
                "LEFT JOIN chi_nhanh CN ON AD.adm_noi_lam_viec = CN.cn_id");

        LocalDate fromDate = baoCaoLenhForm.getFromDate() != null ? baoCaoLenhForm.getFromDate() : now;
        LocalDate endDate = baoCaoLenhForm.getEndDate() != null ? baoCaoLenhForm.getEndDate() : now;

        nativeQuery.append(" WHERE DATE(LL.created_date) >= '" + fromDate + "' AND DATE(LL.created_date) <= '" + endDate + "'");

        if (baoCaoLenhForm.getChiNhanhId().size() > 0 && baoCaoLenhForm.getTaiXeId() <= 0) {
            nativeQuery.append(" AND AD.adm_noi_lam_viec IN (" + StringUtils.join(baoCaoLenhForm.getChiNhanhId(),",")+ ")");
        }

        if (baoCaoLenhForm.getTaiXeId() > 0) {
            nativeQuery.append(" AND LL.lai_xe_id = " + baoCaoLenhForm.getTaiXeId());
        }
        nativeQuery.append(" GROUP BY LL.lai_xe_id,LL.kieu_lenh");


        Query query = getEntityManager().createNativeQuery(nativeQuery.toString());
        List<Object[]> countResult = query.getResultList();
        return countResult;
    }

    @Override
    public List<Object[]> getTongSoKhach(BaoCaoLenhForm baoCaoLenhForm) {
        LocalDate now = LocalDate.now();
        StringBuilder nativeQuery = new StringBuilder();
        nativeQuery.append("SELECT LL.lai_xe_id AS taiXeId " +
                " ,COUNT(VV.tc_ve_id) as tongVe" +
                " ,LL.kieu_lenh AS kieuLenh" +
                " FROM tc_lenh LL" +
                " LEFT JOIN admin_lv2_user AD ON LL.lai_xe_id = AD.adm_id" +
                " LEFT JOIN tc_ve VV ON LL.tc_lenh_id IN (VV.tc_lenh_id, VV.tc_lenh_tra_id)");

        LocalDate fromDate = baoCaoLenhForm.getFromDate() != null ? baoCaoLenhForm.getFromDate() : now;
        LocalDate endDate = baoCaoLenhForm.getEndDate() != null ? baoCaoLenhForm.getEndDate() : now;

        nativeQuery.append(" WHERE DATE(LL.created_date) >= '" + fromDate + "' AND DATE(LL.created_date) <= '" + endDate + "'");

        if (baoCaoLenhForm.getChiNhanhId().size() > 0 && baoCaoLenhForm.getTaiXeId() <= 0) {
            nativeQuery.append(" AND AD.adm_noi_lam_viec = " + baoCaoLenhForm.getChiNhanhId());
        }

        if (baoCaoLenhForm.getTaiXeId() > 0) {
            nativeQuery.append(" AND LL.lai_xe_id = " + baoCaoLenhForm.getTaiXeId());
        }
        nativeQuery.append(" GROUP BY LL.lai_xe_id,LL.kieu_lenh");

        Query query = getEntityManager().createNativeQuery(nativeQuery.toString());
        List<Object[]> countResult = query.getResultList();
        return countResult;
    }

    @Override
    public List<BaoCaoLenhDTO> getReports(BaoCaoLenhForm baoCaoLenhForm) {
        final int lenhDon = 1;
        final int lenhTra = 2;
        final int lenhHuy = 3;
        final int lenhThanhcong = 4;
        final int laixeGroup = 2;

        String sql = "SELECT LX.adm_id                               AS taiXeId, " +
                "       LX.adm_name                                  AS taiXeTen, " +
                "       LX.adm_ma                                    AS maNV, " +
                "       CN.cn_name                                   AS noiLamViec, " +
                "       COUNT(IF(LL.kieu_lenh = :lenhDon, 1, NULL))  AS tongLenhDon, " +
                "       COUNT(IF(LL.kieu_lenh = :lenhTra, 1, NULL))  AS tongLenhTra, " +
                "       COUNT(IF(LL.trang_thai = :lenhHuy, 1, NULL)) AS tongLenhHuy, " +
                "       COUNT(IF(LL.trang_thai = :lenhThanhcong, 1, NULL)) AS tongLenhThanhCong, " +
                "       COALESCE(SUB.khachdon, 0)             AS tongKhachDon, " +
                "       COALESCE(SUB.khachtra, 0)             AS tongKhachTra, " +
                "       COALESCE(SUB.khachdon, 0) + COALESCE(SUB.khachtra, 0)  AS tongsoKhach " +
                " FROM admin_lv2_user LX " +
                "         JOIN admin_lv2_user_group_id G ON G.admg_admin_id = LX.adm_id AND G.admg_group_id = :laixeGroup " +
                "         LEFT JOIN chi_nhanh CN ON LX.adm_noi_lam_viec = CN.cn_id " +
                "         LEFT JOIN tc_lenh LL " +
                "                   ON LX.adm_id = LL.lai_xe_id AND DATE(LL.created_date) BETWEEN :fromDate AND :endDate " +
                "         JOIN " +
                "     ( " +
                "         SELECT LE.lai_xe_id, " +
                "                COUNT(IF(LE.kieu_lenh = 1, 1, NULL)) AS khachdon, " +
                "                COUNT(IF(LE.kieu_lenh = 2, 1, NULL)) AS khachtra " +
                "         FROM tc_ve VE " +
                "                  JOIN tc_lenh LE ON VE.tc_lenh_id = LE.tc_lenh_id OR VE.tc_lenh_tra_id = LE.tc_lenh_id " +
                "         WHERE DATE(LE.created_date) BETWEEN :fromDate AND :endDate " +
                "         GROUP BY LE.lai_xe_id " +
                "     ) SUB ON LX.adm_id = SUB.lai_xe_id ";

        final String groupBy = " GROUP BY LX.adm_id ";
        final String orderBy = " ORDER BY tongsoKhach DESC ";
        String whereClause = " WHERE 1=1 ";

        Map<String, Object> paramMap = new HashMap<>(2);

        if (baoCaoLenhForm.getTaiXeId() != null && baoCaoLenhForm.getTaiXeId() != 0) {
            whereClause = whereClause + " AND LX.adm_id = :taixeId ";
            paramMap.put("taixeId", baoCaoLenhForm.getTaiXeId());
        }

        if (baoCaoLenhForm.getChiNhanhId() != null && baoCaoLenhForm.getChiNhanhId().size() != 0) {
            whereClause = whereClause + " AND LX.adm_noi_lam_viec IN :chinhanhId ";
            paramMap.put("chinhanhId", baoCaoLenhForm.getChiNhanhId());
        }

        sql = sql + whereClause + groupBy + orderBy;

        NativeQuery<BaoCaoLenhDTO> nativeQuery = getCurrentSession().createNativeQuery(sql);
        nativeQuery.setParameter("lenhDon", lenhDon, IntegerType.INSTANCE);
        nativeQuery.setParameter("lenhTra", lenhTra, IntegerType.INSTANCE);
        nativeQuery.setParameter("lenhHuy", lenhHuy, IntegerType.INSTANCE);
        nativeQuery.setParameter("lenhThanhcong", lenhThanhcong, IntegerType.INSTANCE);
        nativeQuery.setParameter("laixeGroup", laixeGroup, IntegerType.INSTANCE);
        nativeQuery.setParameter("fromDate", baoCaoLenhForm.getFromDate(), LocalDateType.INSTANCE);
        nativeQuery.setParameter("endDate", baoCaoLenhForm.getEndDate(), LocalDateType.INSTANCE);

        paramMap.forEach(nativeQuery::setParameter);

        return nativeQuery.setResultTransformer(Transformers.aliasToBean(BaoCaoLenhDTO.class)).list();
    }

    @Override
    public List<ClientHavazNowDTO> getClientAdditionalHavazNow(Integer bvvId) {
        String sql = "SELECT" +
                "       tv.bvv_ten_khach_hang_di  AS tenKhachDi, " +
                "       tv.bvv_phone_di           AS sdtKhachDi, " +
                "       tv.did_id                 AS tripId, " +
                "       tv.bvv_diem_don_khach     AS diaChiDon, " +
                "       tv.bvv_lat_start          AS latitude, " +
                "       tv.bvv_long_start         AS longitude, " +
                "       tv.tc_distance_to_hub_don AS distanceToHubDon, " +
                "       tv.tc_time_to_hub_don     AS thoiGianToHubDon, " +
                "       tv.tc_hub_diem_don        AS hubTraKhach, " +
                "       tv.last_updated_date      AS lastUpdatedDate,  " +
                "       t.tuy_ten                 AS tuyenDi, " +
                "       ddt.did_gio_xuat_ben_that AS gioXuatBen, " +
                "       xe.xe_so_dien_thoai       AS sdtXe, " +
                "       xe.xe_bien_kiem_soat      AS bienSoXe, " +
                "       FROM_UNIXTIME(did_time, '%Y-%m-%d') AS ngayXeChay " +
                " FROM tc_ve tv " +
                "         LEFT JOIN dieu_do_temp ddt ON ddt.did_id = tv.did_id " +
                "         LEFT JOIN not_tuyen nt ON nt.not_id = ddt.did_not_id " +
                "         LEFT JOIN tuyen t ON t.tuy_id = nt.not_tuy_id " +
                "         LEFT JOIN xe ON ddt.did_xe_id = xe.xe_id " +
                " WHERE bvv_id =:bvvId";
        return getEntityManager().createNativeQuery(sql).setParameter("bvvId", bvvId).unwrap(org.hibernate.query.Query.class).setResultTransformer(Transformers.aliasToBean(ClientHavazNowDTO.class)).list();

    }

    @Override
    public List<Object> findLenhIdsByLaiXeIdAndLastUpdatedDate(int laiXeId, LocalDate lastUpdatedDate) {
        String sql = "SELECT DISTINCT tc_lenh.tc_lenh_id " +
                "FROM tc_lenh " +
                "INNER JOIN tc_lich_truc ON tc_lenh.lai_xe_id = tc_lich_truc.tai_xe_id " +
                "WHERE " +
                "tc_lenh.lai_xe_id =:laiXeId " +
                "AND DATE(tc_lenh.last_updated_date) =:lastUpdatedDate ";
        return getEntityManager().createNativeQuery(sql).setParameter("laiXeId", laiXeId).setParameter("lastUpdatedDate",lastUpdatedDate).getResultList();
    }

    @Override
    public List<Object> findChamCongByLaiXeIdAndLastUpdatedDate(int laiXeId, LocalDate lastUpdatedDate) {
        String sql = "SELECT DISTINCT tc_lenh.tc_lenh_id " +
                "FROM tc_lenh " +
                "INNER JOIN tc_cham_cong ON tc_lenh.lai_xe_id = tc_cham_cong.tai_xe_id  " +
                "WHERE " +
                "tc_lenh.lai_xe_id =:laiXeId " +
                "AND DATE(tc_lenh.last_updated_date) =:lastUpdatedDate ";
        return getEntityManager().createNativeQuery(sql).setParameter("laiXeId", laiXeId).setParameter("lastUpdatedDate",lastUpdatedDate).getResultList();
    }

    @Override
    public List<VeTrungChuyenDTO> getListKhachChoLenhHuy(int lenhId) {
        String sql = "SELECT VE.bvv_ten_khach_hang_di as ten, " +
                "VE.bvv_phone_di as sdt," +
                "VE.bvv_diem_don_khach as diaChi," +
                "VE.bvv_diem_tra_khach as diaChiTra," +
                "LXV.ly_do_huy as lyDoHuy" +
                " FROM tc_lai_xe_ve LXV " +
                "LEFT JOIN tc_ve VE ON LXV.tc_ve_id = VE.tc_ve_id " +
                "where lenh_id = :lenhId";
        return getEntityManager().createNativeQuery(sql).unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(VeTrungChuyenDTO.class)).setParameter("lenhId", lenhId).getResultList();
    }

    @Override
    public List<LenhContainGXBDTO> getListLenhCoChuaGioXuatBen(int laiXeId, int type) {
        String sql_lenh_don = "SELECT ll.tc_lenh_id as tcLenhId," +
                "ll.trang_thai as trangThai," +
                " ll.kieu_lenh as kieuLenh," +
                "ll.xe_id as xeId," +
                "ll.created_date as createdDate, " +
                "COALESCE(MIN(did_gio_xuat_ben_that),'') as gioXuatBen " +
                "FROM tc_lenh ll JOIN Tc_ve vv ON vv.tc_lenh_id = ll.tc_lenh_id " +
                "JOIN dieu_do_temp dd ON dd.did_id = vv.did_id " +
                "WHERE ll.lai_xe_id = :taiXeId AND ll.trang_thai IN :trangThai AND ll.kieu_lenh = :kieuLenh GROUP BY ll.tc_lenh_id";

        String sql_lenh_tra = "SELECT ll.tc_lenh_id as tcLenhId," +
                "ll.trang_thai as trangThai," +
                " ll.kieu_lenh as kieuLenh," +
                "ll.xe_id as xeId," +
                "ll.created_date as createdDate," +
                "COALESCE(MIN(did_gio_xuat_ben_that),'') as gioXuatBen " +
                "FROM tc_lenh ll JOIN Tc_ve vv ON vv.tc_lenh_tra_id = ll.tc_lenh_id " +
                "JOIN dieu_do_temp dd ON dd.did_id = vv.did_id " +
                "WHERE ll.lai_xe_id = :taiXeId AND ll.trang_thai IN :trangThai AND ll.kieu_lenh = :kieuLenh GROUP BY ll.tc_lenh_id";
        Set<Integer> trangThai = new HashSet<>();
        trangThai.add(LenhConstants.LENH_STATUS_DA_DIEU);
        trangThai.add(LenhConstants.LENH_STATUS_DANG_CHAY);
        return getEntityManager().createNativeQuery(type == LenhConstants.LENH_DON ? sql_lenh_don : sql_lenh_tra).unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(LenhContainGXBDTO.class))
                .setParameter("taiXeId", laiXeId)
                .setParameter("trangThai",trangThai)
                .setParameter("kieuLenh",type).getResultList();
    }

    @Override
    public List<ChiTietLenhDTO> getDsLenh(Integer diemgiaokhach, LocalDate dateRequest, SelectOptions selectOptions) {
        LocalDate curDate = LocalDate.now();
        String sql = "SELECT tl.tc_lenh_id AS tcLenhId," +
                " bx.bex_id AS diemgiaokhach," +
                " bx.bex_ten AS diemgiaokhachStr, " +
                " adm.adm_id AS laiXeId, " +
                " adm.adm_name AS taiXeTen," +
                " x.xe_bien_kiem_soat AS bks," +
                " tl.trang_thai AS trangThai," +
                " count(tv.tc_ve_id) AS slKhach," +
                " tl.kieu_lenh AS kieuLenh, " +
                " adm2.adm_name AS nguoiTao, " +
                " tl.created_date as createdDate " +
                " FROM tc_lenh tl " +
                " LEFT JOIN  ben_xe bx ON tl.diem_giao_khach = bx.bex_id " +
                " LEFT JOIN admin_lv2_user adm ON tl.lai_xe_id = adm.adm_id " +
                " LEFT JOIN xe x ON tl.xe_id = x.xe_id " +
                " LEFT JOIN tc_ve tv ON tv.tc_lenh_id = tl.tc_lenh_id " +
                " LEFT JOIN admin_lv2_user adm2 on adm2.adm_id = tl.created_by" +
                " WHERE 1=1 AND CAST(tl.created_date AS DATE) =:curDate";

        String sqlFormat = "SELECT COUNT(1) FROM ( %s ) AS sub";

        Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(diemgiaokhach)) {
            sql += " AND tl.diem_giao_khach =:diemgiaokhach ";
            params.put("diemgiaokhach", diemgiaokhach);
        }
        if(Objects.nonNull(dateRequest)) {
            curDate = dateRequest;
        }
        params.put("curDate", curDate);
        sql += " GROUP BY  tl.tc_lenh_id ";
        String sqlCount = String.format(sqlFormat, sql);
        NativeQuery nativeQuery  = getCurrentSession().createNativeQuery(sql);

        NativeQuery countQuery = getCurrentSession().createNativeQuery(sqlCount);
        params.forEach(nativeQuery::setParameter);
        params.forEach(countQuery::setParameter);
        long count = (long) countQuery.getSingleResult();
        if (selectOptions.isCount()) {
            nativeQuery.setFirstResult(selectOptions.getOffset());
            nativeQuery.setMaxResults(selectOptions.getLimit());
        }
        selectOptions.setCount(count);
        return nativeQuery.setResultTransformer(Transformers.aliasToBean(ChiTietLenhDTO.class)).getResultList();

    }
}
