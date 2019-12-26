package com.havaz.transport.api.repository.impl;

import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.common.LenhConstants;
import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.form.PagingForm;
import com.havaz.transport.api.model.VtcCtvDTO;
import com.havaz.transport.api.repository.TcVtcCtvRepositoryCustom;
import com.havaz.transport.dao.repository.AbstractRepository;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class TcVtcCtvRepositoryCustomImpl extends AbstractRepository implements TcVtcCtvRepositoryCustom {
    private static final Integer TYPECTV = 1;

    @Override
    public PageCustom<VtcCtvDTO> getListVtcCtv(PagingForm pagingForm, String ctvName) {
        String sql = "SELECT " +
                " vc.tc_vtt_ctv_id as id,\n" +
                "       adm.adm_name     as tenCtv,\n" +
                "       vtt.tc_vtt_code  as codeVung,\n" +
                "       vc.status        AS status,\n" +
                "       vc.note          as note,\n" +
                "       vc.created_date  as createdDate,\n" +
                "       cr.adm_name      as creatorName,\n" +
                "       adm.adm_id       as ctvId, \n" +
                "       xe.xe_id         as xeId,\n" +
                "       xe.xe_bien_kiem_soat as xeBks,\n" +
                "       vc.coordinate_lat as coordinateLat,\n" +
                "       vc.coordinate_long as coordinateLong,\n" +
                "       vtt.tc_vtt_id    as vungId \n" +
                "FROM tc_vtc_ctv vc\n" +
                "   INNER JOIN (SELECT adm1.* FROM admin_lv2_user adm1 LEFT JOIN  admin_lv2_user_group_id adg ON adm1.adm_id = adg.admg_admin_id" +
                "                  WHERE adg.admg_group_id=:isLx) adm ON adm.adm_id = vc.tc_ctv_id\n " +
                "         INNER JOIN tc_vung_trung_chuyen vtt on vtt.tc_vtt_id = vc.tc_vtt_id\n" +
                "         INNER JOIN xe ON vc.xe_ctv_id = xe.xe_id AND ( xe.xe_cong_tac_vien=:isxeCtv or xe.xe_trung_tam=:isXeTc)" +
                "         LEFT JOIN (select * from admin_lv2_user adm2) cr on cr.adm_id = vc.created_by \n" +
                "WHERE  vtt.tc_vtt_code like :ctvName or adm.adm_name like :ctvName";
        List<VtcCtvDTO> result = getEntityManager().createNativeQuery(sql).unwrap(Query.class)
                .setResultTransformer(Transformers.aliasToBean(VtcCtvDTO.class))
                .setParameter("isxeCtv", TYPECTV)
                .setParameter("isLx", Constant.ADM_IS_LXTC)
                .setParameter("isXeTc", LenhConstants.XE_TRUNG_TAM_IS_XE_TRUNG_CHUYEN)
                .setFirstResult(pagingForm.getPage() * pagingForm.getSize())
                .setParameter("ctvName", "%" + ctvName + "%")
                .setMaxResults(pagingForm.getSize()).getResultList();
        int totalElement = 0;
        if (!result.isEmpty()) {
            Object total = getEntityManager().createNativeQuery("SELECT COUNT(*) FROM  tc_vtc_ctv").getSingleResult();
            totalElement = Integer.parseInt(total.toString());
        }
        PageCustom<VtcCtvDTO> pageResult = new PageCustom<>();
        pageResult.setContent(result);
        pageResult.setPage(pagingForm.getPage() + 1);
        pageResult.setSize(pagingForm.getSize());
        pageResult.setTotalElement(totalElement);
        if (totalElement % pagingForm.getSize() == 0) {
            pageResult.setTotalPage(totalElement / pagingForm.getSize());
        } else {
            pageResult.setTotalPage(totalElement / pagingForm.getSize() + 1);
        }
        return pageResult;
    }
}
