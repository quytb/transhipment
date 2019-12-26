package com.havaz.transport.api.repository.impl;

import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.form.PriceGroupSearchForm;
import com.havaz.transport.api.model.PartnerPriceGroupDTO;
import com.havaz.transport.api.model.PriceGroupDTO;
import com.havaz.transport.api.repository.TcPriceGroupRepositoryCustom;
import com.havaz.transport.dao.repository.AbstractRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class TcPriceGroupRepositoryCustomImpl extends AbstractRepository implements TcPriceGroupRepositoryCustom {

    @Override
    public PageCustom<PriceGroupDTO> findByPartnerName(PriceGroupSearchForm searchForm) {

        String sqlCount = "SELECT  " +
                "      CAST(count(*) as char) AS totalElement " +
                "FROM tc_ctv_price_group pg " +
                "         LEFT JOIN tc_partner tp ON pg.partner_id = tp.partner_id " +
                "         LEFT JOIN tc_ctv_price cp ON cp.price_id = pg.price_id " +
                " WHERE 1=1 ";

        String sql = "SELECT pg.id             AS id, " +
                "       pg.price_id       AS priceId, " +
                "       pg.partner_id     AS partnerId, " +
                "       tp.partner_name   as partnerName, " +
                "       tp.discount_range AS discountRange, " +
                "       cp.name           AS priceName " +
                "FROM tc_ctv_price_group pg " +
                "         LEFT JOIN tc_partner tp ON pg.partner_id = tp.partner_id " +
                "         LEFT JOIN tc_ctv_price cp ON cp.price_id = pg.price_id " +
                " WHERE 1=1 ";
        if (!StringUtils.isEmpty(searchForm.getPartnerName())) {
            sql = sql + " AND tp.partner_name LIKE :partnerName";
            sqlCount = sqlCount + " AND tp.partner_name LIKE :partnerName";
        }
        sql = sql + " ORDER BY partnerName ";
        Query query = getEntityManager().createNativeQuery(sql);
        if (!StringUtils.isEmpty(searchForm.getPartnerName())) {
            query.setParameter("partnerName", "%" + searchForm.getPartnerName() + "%");
        }
        List<PriceGroupDTO> priceGroupDTOS = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(PriceGroupDTO.class))
                .setMaxResults(searchForm.getSize()).setFirstResult(searchForm.getPage() * searchForm.getSize()).list();
        PageCustom<PriceGroupDTO> pageCustom = new PageCustom<>();
        pageCustom.setContent(priceGroupDTOS);
        if (priceGroupDTOS.size() > 0) {
            Query queryCount =  getEntityManager().createNativeQuery(sqlCount);
            if (!StringUtils.isEmpty(searchForm.getPartnerName())) {
                queryCount.setParameter("partnerName", "%" + searchForm.getPartnerName() + "%");
            }
            Object countObj = queryCount.getSingleResult();
            pageCustom.setTotalElement(Integer.parseInt(countObj.toString()));
            pageCustom.setPage(searchForm.getPage() + 1);
            pageCustom.setSize(searchForm.getSize());
            if (pageCustom.getTotalElement() % pageCustom.getSize() == 0) {
                pageCustom.setTotalPage(pageCustom.getTotalElement() / pageCustom.getSize());
            } else {
                pageCustom.setTotalPage(pageCustom.getTotalElement() / pageCustom.getSize() + 1);
            }
        }
        return pageCustom;
    }

    @Override
    public PartnerPriceGroupDTO findPriceGroupPartner(Integer groupId) {
        String sql = "select tcpg.id AS groupId, " +
                "       tcpg.price_id AS priceId, " +
                "       tp.partner_id AS partnerId, " +
                "       tp.discount_range AS discountRange, " +
                "       tcp.type AS typePrice " +
                "from tc_ctv_price_group tcpg " +
                "         inner join tc_partner tp on tp.partner_id = tcpg.partner_id " +
                "         inner join tc_ctv_price tcp on tcp.price_id = tcpg.price_id " +
                "where tcpg.id =:groupId ";
        return (PartnerPriceGroupDTO) getEntityManager().createNativeQuery(sql).setParameter("groupId", groupId).unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(PartnerPriceGroupDTO.class)).setMaxResults(1).uniqueResult();
    }
}
