package com.havaz.transport.api.repository.impl;

import com.havaz.transport.api.body.request.tcve.GetTcVeDonRequest;
import com.havaz.transport.api.body.response.tcve.GetTcVeDonResponse;
import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.common.LenhConstants;
import com.havaz.transport.api.repository.TcVeRepositoryCustom;
import com.havaz.transport.core.constant.VeConstants;
import com.havaz.transport.dao.entity.TcVeEntity;
import com.havaz.transport.dao.query.SelectOptions;
import com.havaz.transport.dao.repository.AbstractRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Slf4j
@Repository
public class TcVeRepositoryCustomImpl extends AbstractRepository implements TcVeRepositoryCustom {

    @Override
    public void traKhach(List<Integer> listVe) {
        String hql = "UPDATE TcVeEntity tcv SET tcv.tcTrangThaiTra = :trangThaiTra, tcv.khachHangMoi = :khachMoi, tcv.lastUpdatedDate = :lastUpdatedDate WHERE tcv.bvvId IN (:listVe)";
        Query query = getEntityManager().createQuery(hql);
        query.setParameter("trangThaiTra", VeConstants.TC_STATUS_DA_TRA);
        query.setParameter("khachMoi", Constant.ZERO);
        query.setParameter("listVe",listVe);
        query.setParameter("lastUpdatedDate",LocalDateTime.now());
        query.executeUpdate();
    }

    @Override
    public List<Object[]> getListTripObjectTripId(int tripId, int type) {
        String queryName = "";
        if (type == LenhConstants.LENH_DON) {
            queryName = "getListTripDonObject";
        } else if (type == LenhConstants.LENH_TRA) {
            queryName = "getListTripTraObject";
        }
        Query query = getEntityManager().createNamedQuery(queryName);
        List<Integer> listAcceptXe = new ArrayList<>();
        listAcceptXe.add(Constant.XE_TUYEN);
        listAcceptXe.add(Constant.XE_TRUNG_CHUYEN);
        query.setParameter("tripId", tripId);
        query.setParameter("listAcceptXe", listAcceptXe);
        return query.getResultList();
    }

    @Override
    public List<TcVeEntity> getListVeTcByBvvId(List<Integer> listBvvId) {
        String hql = "SELECT BVV FROM TcVeEntity BVV WHERE BVV.bvvId in (:listBvvId)";
        Query query = getEntityManager().createQuery(hql);
        query.setParameter("listBvvId", listBvvId);
        List<TcVeEntity> listBvvEntity = query.getResultList();
        return listBvvEntity;
    }

    @Override
    public List<TcVeEntity> getByTaiXeIdAndLenhId(int taiXeId, int lenhId) {
        //Select nhung ve chua duoc don
        String hql = "SELECT TCV FROM TcVeEntity TCV Where TCV.laiXeIdDon=:taiXeId AND TCV.tcLenhId = :lenhId AND TCV.tcTrangThaiDon <>3 AND TCV.isTcDon = true ";
        Query query = getEntityManager().createQuery(hql);
        query.setParameter("taiXeId", taiXeId);
        query.setParameter("lenhId", lenhId);
        List<TcVeEntity> listTcVeEntity = query.getResultList();

        return listTcVeEntity;
    }

    @Override
    public List<TcVeEntity> getVeTcByLenhIdAndTaiXeId(int taiXeId, int lenhId) {
        //Select nhung ve chua duoc tra
        String hql = "SELECT TCV FROM TcVeEntity TCV Where TCV.laiXeIdTra=:taiXeId AND TCV.tcLenhTraId = :lenhId AND TCV.tcTrangThaiTra <> 3 AND TCV.isTcTra = true ";
        Query query = getEntityManager().createQuery(hql);
        query.setParameter("taiXeId", taiXeId);
        query.setParameter("lenhId", lenhId);
        List<TcVeEntity> listTcVeEntity = query.getResultList();

        return listTcVeEntity;
    }

    @Override
    public List<TcVeEntity> getVeTcActiveByLaiXeId(int laiXeId){
        //Select nhung ve chua duoc tra
        String hql = "SELECT TCV FROM TcVeEntity TCV WHERE (TCV.laiXeIdTra=:laiXeId OR TCV.laiXeIdDon=:laiXeId) AND (TCV.tcTrangThaiDon IN (1,2) OR TCV.tcTrangThaiTra IN (1,2)) ";
        Query query = getEntityManager().createQuery(hql);
        query.setParameter("laiXeId", laiXeId);
        List<TcVeEntity> listTcVeEntity = query.getResultList();
        return listTcVeEntity;
    }

    @Override
    public List<GetTcVeDonResponse> findAllByGetTcVeDonRequest(GetTcVeDonRequest request, SelectOptions options) {
        LocalDate ngayXuatben = LocalDate.now();
        LocalTime curTime = LocalTime.now();
        List<Integer> trangthaidonList = Arrays.asList(1, 2, 5);
        Integer thoigianGiokhoihanh = 3600 * 2; // 7200 seconds
        Session session = getCurrentSession();
        String sql = "SELECT " +
                " GROUP_CONCAT(v.bvv_id SEPARATOR ',') AS bvvIdsStr , " +
                " GROUP_CONCAT(v.version SEPARATOR ',') AS versionsStr , " +
                " v.bvv_ten_khach_hang_di AS tenHanhkhach, " +
                " v.bvv_phone_di AS sdtHanhkhach," +
                " CAST(FROM_UNIXTIME(d.did_time_xuat_ben, '%Y-%m-%d') AS DATE )AS ngayxuatben, " +
                " v.bvv_diem_don_khach AS diachiHanhkhach, " +
                " v.last_location AS currentHubId," +
                " t.tuy_ten AS tuyen, " +
                " d.did_gio_dieu_hanh AS giodieuhanh, "+
                " v.tc_trang_thai_don as status, "+
                " bx.bex_ten AS currentHub," +
                " x.xe_bien_kiem_soat AS bks, " +
                " v.ghi_chu_don  AS ghichu, " +
                " count(v.tc_ve_id) AS soluongKhach" +
                " FROM tc_ve v " +
                " LEFT JOIN ben_xe bx ON bx.bex_id = v.last_location " +
                " LEFT JOIN dieu_do_temp d ON v.did_id = d.did_id " +
                " LEFT JOIN not_tuyen n ON n.not_id = d.did_not_id " +
                " LEFT JOIN tuyen t ON t.tuy_id = n.not_tuy_id " +
                " LEFT JOIN xe x ON x.xe_id = d.did_xe_id";
        String countSQLFormat = "SELECT count(1) " +
                " FROM (" +
                "     %s " +
                " ) SUB ";
        String whereClause = " WHERE t.tuy_active = 1 " +
                " AND v.tc_trang_thai_don NOT IN (:trangthaidonList) " +
                " AND DATE_FORMAT(FROM_UNIXTIME(d.did_time_xuat_ben),'%Y-%m-%d') = :ngayXuatben " +
                " AND d.did_gio_dieu_hanh BETWEEN CURRENT_TIME AND ADDTIME(CURRENT_TIME , SEC_TO_TIME(:thoigianGiokhoihanh)) ";
        String groupBy = " GROUP BY v.bvv_diem_don_khach, v.bvv_phone_di, d.did_id, v.tc_trang_thai_don ";
        String orderBy = " ORDER BY ISNULL(v.last_location), v.tc_ve_id ASC ";

        Map<String, Object> paramMap = new HashMap<>();

        if (request.getNgayXuatben() != null) {
            ngayXuatben = request.getNgayXuatben();
        }

        if (request.getCurrentHub() != null && request.getCurrentHub() !=0) {
            whereClause += " AND v.last_location = :lastHub";
            paramMap.put("lastHub", request.getCurrentHub());
        }

        if (StringUtils.isNotBlank(request.getSdtHanhkhach())) {
            whereClause += " AND v.bvv_phone_di LIKE :sdtHanhkhach";
            paramMap.put("sdtHanhkhach", "%" + request.getSdtHanhkhach());
        }

        if (!CollectionUtils.isEmpty(request.getNotIds())) {
            whereClause += " AND n.not_id IN :notIds";
            paramMap.put("notIds", request.getNotIds());
        }

        if (!CollectionUtils.isEmpty(request.getTuyenIds())) {
            whereClause += " AND t.tuy_id IN :tuyenIds";
            paramMap.put("tuyenIds", request.getTuyenIds());
        }

        if (request.getThoigianGiokhoihanh() != null && request.getThoigianGiokhoihanh() > 0) {
            thoigianGiokhoihanh = request.getThoigianGiokhoihanh()*60;
        }

        sql += whereClause + groupBy;
        String countSQL = String.format(countSQLFormat, sql);
        sql += orderBy;

        NativeQuery query = session.createNativeQuery(sql);
        query.setFirstResult(options.getOffset());
        query.setMaxResults(options.getLimit());
        NativeQuery countQuery = session.createNativeQuery(countSQL);

        paramMap.put("ngayXuatben", ngayXuatben);
        paramMap.put("thoigianGiokhoihanh", thoigianGiokhoihanh);
        paramMap.put("trangthaidonList", trangthaidonList);
        paramMap.forEach((k, v) -> {
            query.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        if (options.isCount()) {
            long singleResult = (long) countQuery.getSingleResult();
            options.setCount(singleResult);
        }

        return query.setResultTransformer(Transformers.aliasToBean(
                GetTcVeDonResponse.class)).getResultList();
    }

    @Override
    public List<GetTcVeDonResponse> findAllOfCommand(Integer lenhId) {
        Session session = getCurrentSession();
        String sql = "SELECT " +
                " GROUP_CONCAT(v.bvv_id SEPARATOR ',') AS bvvIdsStr , " +
                " GROUP_CONCAT(v.version SEPARATOR ',') AS versionsStr , " +
                " v.bvv_ten_khach_hang_di AS tenHanhkhach, " +
                " v.bvv_phone_di AS sdtHanhkhach, " +
                " v.bvv_diem_don_khach AS diachiHanhkhach, " +
                " v.tc_trang_thai_don AS status, " +
                " v.last_location AS currentHubId," +
                " bx.bex_ten AS currentHub," +
                " t.tuy_ten AS tuyen," +
                " v.ghi_chu_don  AS ghichu, " +
                " d.did_gio_dieu_hanh AS giodieuhanh, " +
                " CAST(FROM_UNIXTIME(d.did_time_xuat_ben, '%Y-%m-%d') AS DATE )AS ngayxuatben, " +
                " x.xe_bien_kiem_soat AS bks, " +
                " count(v.tc_ve_id) AS soluongKhach" +
                " FROM tc_ve v " +
                " LEFT JOIN ben_xe bx ON bx.bex_id = v.last_location " +
                " LEFT JOIN dieu_do_temp d ON v.did_id = d.did_id " +
                " LEFT JOIN not_tuyen n ON n.not_id = d.did_not_id " +
                " LEFT JOIN tuyen t ON t.tuy_id = n.not_tuy_id " +
                " LEFT JOIN xe x ON x.xe_id = d.did_xe_id";

        String whereClause = " WHERE v.tc_lenh_id =:lenhId ";
        String groupBy = " GROUP BY v.bvv_phone_di ";
        String orderBy = " ORDER BY ISNULL(v.last_location), v.tc_ve_id ASC ";

        sql += whereClause + groupBy;
        sql += orderBy;
        NativeQuery query = session.createNativeQuery(sql);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("lenhId", lenhId);
        paramMap.forEach((k, v) -> {
            query.setParameter(k, v);
        });
        return query.setResultTransformer(Transformers.aliasToBean(
                GetTcVeDonResponse.class)).getResultList();
    }
}
