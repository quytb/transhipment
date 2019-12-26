package com.havaz.transport.api.repository.impl;

import com.havaz.transport.api.model.CanhBaoDTO;
import com.havaz.transport.api.model.NowTripDetailDTO;
import com.havaz.transport.api.model.TxCtvDTO;
import com.havaz.transport.api.model.XeTcNowDTO;
import com.havaz.transport.api.repository.HavazNowRepositoryCustom;
import com.havaz.transport.core.constant.TrangThaiVe;
import com.havaz.transport.core.constant.VeConstants;
import com.havaz.transport.dao.repository.AbstractRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HavazNowRepositoryCustomImpl extends AbstractRepository implements HavazNowRepositoryCustom {

    @Override
    public List<Object[]> getListTripForHavazNow(String beforNow, String afterNow) {
        String sql = "SELECT " +
                "DD.did_id, " +
                "T.tuy_id, " +
                "COALESCE(T.tuy_ten,''), " +
                "XE.xe_bien_kiem_soat, " +
                "XE.xe_so_dien_thoai, " +
                "DD.did_gio_xuat_ben, " +
                "AD.adm_id, " +
                "AD.adm_name, " +
                "COALESCE(NOT_T.not_chieu_di,0) "+
                "FROM dieu_do_temp DD " +
                "LEFT JOIN dieu_tai_temp DTC ON DTC.dit_did_id = DD.did_id " +
                "LEFT JOIN admin_lv2_user AD ON DTC.dit_tai_id = AD.adm_id " +
                "LEFT JOIN xe XE ON DD.did_xe_id = XE.xe_id " +
                "LEFT JOIN not_tuyen NOT_T ON DD.did_not_id = NOT_T.not_id " +
                "LEFT JOIN tuyen T  ON NOT_T.not_tuy_id = T.tuy_id " +
                "WHERE " +
                "DATE(NOW()) = DATE(FROM_UNIXTIME(DD.did_time)) AND DD.did_status = 1 AND " +
                "(STR_TO_DATE(DD.did_gio_xuat_ben,'%H:%i') BETWEEN STR_TO_DATE('" + beforNow + "','%H:%i') AND TIME_FORMAT(NOW(), '%H:%i')" +
                "OR STR_TO_DATE(DD.did_gio_xuat_ben,'%H:%i') BETWEEN TIME_FORMAT(NOW(), '%H:%i') AND STR_TO_DATE('" + afterNow + "','%H:%i')) " +

                "GROUP BY DD.did_id ORDER BY DD.did_gio_xuat_ben";
        return getEntityManager().createNativeQuery(sql).getResultList();
    }

    @Override
    public List<XeTcNowDTO> getTaiXeTcByDidIdAndTrangthai(Integer didId, Integer type) {

        String sql = "SELECT tcl.lai_xe_id        AS txId,\n" +
                "       xe.xe_so_dien_thoai  AS xtcSdt,\n" +
                "       xe.xe_id             AS xeId,\n" +
                "       adm.adm_name         AS xtcTen,\n" +
                "       xe.xe_bien_kiem_soat AS xtcBks,\n" +
                "       CASE\n" +
                "           WHEN adm.adm_ctv = 1 THEN 2\n" +
                "           ELSE 1 END       AS xtcType\n" +
                "FROM tc_ve ve\n" +
                "         LEFT JOIN tc_lenh tcl on ve.lai_xe_id_don = tcl.lai_xe_id\n" +
                "         LEFT JOIN xe on xe.xe_id = tcl.xe_id\n" +
                "         LEFT JOIN admin_lv2_user adm on adm.adm_id = tcl.lai_xe_id\n" +
                "WHERE did_id = :didId\n" +
                "  AND tcl.trang_thai in (1, 2)\n" +
                "  AND tcl.kieu_lenh = :type\n" +
                "  AND tcl.lai_xe_id IS NOT NULL\n" +
                "GROUP BY lai_xe_id  \n";
        List<XeTcNowDTO> havazDTOS = getEntityManager().createNativeQuery(sql).unwrap(Query.class).setParameter("didId", didId).setParameter("type", type).setResultTransformer(Transformers.aliasToBean(XeTcNowDTO.class)).list();
        return havazDTOS;
    }

    @Override
    public NowTripDetailDTO getTripByTripIdAndTrangThai(Integer tripId, Integer type) {
        String sqlAddition = "";
        if (type == TrangThaiVe.DON.getTrangThai()) {
            sqlAddition = "       ddt.did_lat_start    AS xtLatitude,\n" + "      ddt.did_long_start   AS xtLongitude,\n";
        } else {
            sqlAddition = "       ddt.did_lat_end    AS xtLatitude,\n" + "      ddt.did_long_end   AS xtLongitude,\n";
        }

        String sql = "SELECT  CAST(ddt.did_id AS CHAR(50))           AS tripId,\n" +
                "       dtt.dit_tai_id       AS lxTuyenId,\n" +
                "       xe.xe_so_dien_thoai  AS xtSdt,\n" +
                sqlAddition +
                "       xe.xe_bien_kiem_soat AS xtBks,\n" +
                "       adm.adm_name         as xtTen\n" +
                "\n" +
                " FROM dieu_do_temp ddt\n" +
                "         LEFT JOIN dieu_tai_temp dtt ON did_id = dit_did_id\n" +
                "         LEFT JOIN xe ON xe_id = ddt.did_xe_id\n" +
                "         LEFT JOIN admin_lv2_user adm ON dtt.dit_tai_id = adm.adm_id\n" +
                " WHERE ddt.did_id =:tripId";
        NowTripDetailDTO nowTripDetailDTOS = (NowTripDetailDTO) getEntityManager().createNativeQuery(sql)
                .unwrap(Query.class).setParameter("tripId", tripId)
                .setResultTransformer(Transformers.aliasToBean(NowTripDetailDTO.class)).setMaxResults(1).uniqueResult();
        return nowTripDetailDTOS;
    }

    @Override
    public List<CanhBaoDTO> layDanhSachDeCanhBao(List<Integer> listTripId) {
        String query = "Select tc_ve_id AS veId," +
                "vv.did_id AS tripId," +
                "lai_xe_id_don AS taiXeId," +
                "DTC.dit_tai_id AS taiXeXeTCId,"+
                "thoi_gian_don AS thoiGianDon," +
                "ad.adm_name AS nguoiDon," +
                "vtc.tc_vtt_name AS vungTrungChuyen," +
                "bvv_ten_khach_hang_di AS tenKhach," +
                "bvv_phone_di AS sdtKhach," +
                "bvv_diem_don_khach AS diaChiDon," +
                "tc_lenh_id AS lenhId," +
                "T.tuy_id AS tuyenId," +
                "T.tuy_ten AS tuyen, " +
                "DD.did_gio_xuat_ben_that AS gioXuatBen,"+
                "bx.bex_ten AS hub,"+
                "bvv_lat_start AS lat,"+
                "bvv_long_start AS lng, "+
                "IFNULL(bex_lat,0) AS hubLat, " +
                "IFNULL(bex_long,0) AS hubLng, " +
                "tc_trang_thai_don AS trangThaiDon "+
                "FROM tc_ve vv " +
                "LEFT JOIN admin_lv2_user ad ON vv.lai_xe_id_don = ad.adm_id " +
                "LEFT JOIN ben_xe bx ON bx.bex_id = vv.tc_hub_diem_don "+
                "LEFT JOIN tc_vung_trung_chuyen vtc ON vv.tc_vtt_id_don = vtc.tc_vtt_id " +
                "LEFT JOIN dieu_do_temp DD ON vv.did_id = DD.did_id "+
                "LEFT JOIN dieu_tai_temp DTC ON DTC.dit_did_id = DD.did_id "+
                "LEFT JOIN not_tuyen NOT_T ON DD.did_not_id = NOT_T.not_id " +
                "LEFT JOIN tuyen T  ON NOT_T.not_tuy_id = T.tuy_id " +
                "WHERE vv.did_id IN ("+ StringUtils.join(listTripId, ',') +") AND vv.tc_trang_thai_don IN ("+ VeConstants.TC_STATUS_DA_DIEU+","+VeConstants.TC_STATUS_DANG_DI_DON+","+VeConstants.TC_STATUS_DA_HUY+")";
        List<CanhBaoDTO> canhBaoDTOS = getEntityManager().createNativeQuery(query).unwrap(Query.class).setResultTransformer(Transformers.aliasToBean(CanhBaoDTO.class)).list();
        return canhBaoDTOS;
    }

    @Override
    public List<TxCtvDTO> getAllTxCtv() {
        String sql= "SELECT adm_id   AS id,\n" +
                "       adm_name AS name,\n" +
                "       adm_ctv  AS isCtv\n" +
                "FROM admin_lv2_user\n" +
                "         INNER JOIN admin_lv2_user_group_id\n" +
                "                    ON admg_admin_id = adm_id\n" +
                "WHERE adm_active = 1\n" +
                "  AND (admg_group_id = 2 or adm_ctv = 1)\n" +
                "ORDER BY adm_name ASC";
        return getEntityManager().createNativeQuery(sql).unwrap(Query.class).setResultTransformer(Transformers.aliasToBean(TxCtvDTO.class)).list();
    }

}
