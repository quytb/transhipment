package com.havaz.transport.api.repository.impl;

import com.havaz.transport.api.model.BaoCaoChamCongDTO;
import com.havaz.transport.api.repository.TcChamCongRepositoryCustom;
import com.havaz.transport.dao.repository.AbstractRepository;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Repository
public class TcChamCongRepositoryCustomImpl extends AbstractRepository implements TcChamCongRepositoryCustom {

    @Override
    public List<BaoCaoChamCongDTO> getDuLieuBaoCaoChamCong(Integer thang, Integer nam, Integer chinhanh) {
        String sql = "SELECT " +
                "       CC.tai_xe_id                        AS taiXeId, " +
                "       AD.adm_name                         AS taiXeTen, " +
                "       CC.ngay_cham_cong                   AS ngayChamCong, " +
                "       COALESCE(SUM(CC.gio_thuc_te / CA.hous_in_ca), 0) AS cong " +
                " FROM tc_cham_cong CC " +
                "         LEFT JOIN admin_lv2_user AD ON CC.tai_xe_id = AD.adm_id " +
                "         LEFT JOIN tc_ca CA ON CC.tc_ca_id = CA.tc_ca_id " +
                " WHERE MONTH(CC.ngay_cham_cong) = :thang " +
                "   AND YEAR(CC.ngay_cham_cong) = :nam ";

        final String groupBy = " GROUP BY CC.tai_xe_id, CC.ngay_cham_cong ";
        final String orderBy = " ORDER BY CC.tai_xe_id, CC.ngay_cham_cong";
        final String whereClause = " AND AD.adm_noi_lam_viec = :chinhanh ";
        Map<String, Object> paramMap = new HashMap<>(1);
        if (chinhanh != null && chinhanh > 0) {
            sql = sql + whereClause;
            paramMap.put("chinhanh", chinhanh);
        }

        sql = sql + groupBy + orderBy;

        Session session = getCurrentSession();
        NativeQuery<BaoCaoChamCongDTO> query = session.createNativeQuery(sql);
        query.setParameter("thang", thang, IntegerType.INSTANCE);
        query.setParameter("nam", nam, IntegerType.INSTANCE);
        paramMap.forEach(query::setParameter);
        return query.setResultTransformer(Transformers.aliasToBean(BaoCaoChamCongDTO.class)).list();
    }
}
