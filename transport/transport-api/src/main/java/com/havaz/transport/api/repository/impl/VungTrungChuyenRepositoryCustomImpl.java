package com.havaz.transport.api.repository.impl;

import com.havaz.transport.api.model.TcVungTrungChuyenDTO;
import com.havaz.transport.api.model.VtcDTO;
import com.havaz.transport.api.repository.VungTrungChuyenRepositoryCustom;
import com.havaz.transport.dao.entity.TcVungTrungChuyenEntity;
import com.havaz.transport.dao.repository.AbstractRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class VungTrungChuyenRepositoryCustomImpl extends AbstractRepository implements VungTrungChuyenRepositoryCustom {

    @Override
    public void addPolygon(TcVungTrungChuyenDTO vttDto) {
        String sql = "INSERT INTO tc_vung_trung_chuyen (tc_vtt_name, tc_vtt_code, tc_average_speed, status, created_by, last_updated_by,\n" +
                " created_date, last_updated_date, tc_vtt_content, tc_vtt_note, tc_centroid_lat, tc_centroid_long, tc_confirmed_time ) " +
                "VALUES(:vtcName, :vtcCode, :vtcSpeed, :status, :createdBy, :lastUpdatedBy, :createdDate, :lastUpdatedDate," +
                " GeomFromText(:vtcContent), :note, :centroidLat, :centroidLong, :confirmedTime )";
        Query query = getEntityManager().createNativeQuery(sql);
        query.setParameter("vtcName", vttDto.getTcVttName());
        query.setParameter("vtcCode", vttDto.getTcVttCode());
        query.setParameter("vtcSpeed", vttDto.getTcVAverageSpeed());
        query.setParameter("status", vttDto.getStatus());
        query.setParameter("createdBy", vttDto.getCreatedBy());
        query.setParameter("lastUpdatedBy", vttDto.getLastUpdatedBy());
        query.setParameter("createdDate", vttDto.getCreatedDate());
        query.setParameter("lastUpdatedDate", vttDto.getLastUpdatedDate());
        query.setParameter("vtcContent", vttDto.getTcVttContent());
        query.setParameter("note", vttDto.getTcVttNote());
        query.setParameter("centroidLat", vttDto.getTcCentroidLat());
        query.setParameter("centroidLong", vttDto.getTcCentroidLong());
        query.setParameter("confirmedTime", vttDto.getTcConfirmedTime());
        query.executeUpdate();
//        entityManager.createNativeQuery("INSERT INTO tc_vung_trung_chuyen (tc_vtt_name, tc_vtt_code, tc_average_speed, status, created_by, last_updated_by, created_date, last_updated_date, tc_vtt_content) " +
//                "VALUES ('" + vttDto.getTcVttName() + "','" + vttDto.getTcVttCode() + "'," + vttDto.getTcVAverageSpeed() + "," + vttDto.getStatus() + "," + vttDto.getCreatedBy() + "," + vttDto.getLastUpdatedBy() + ",'" + vttDto.getCreatedDate() + "','" + vttDto.getLastUpdatedDate() + "', GeomFromText('" + vttDto.getTcVttContent() + "'))").executeUpdate();
    }

    @Override
    public void updatePolygon(TcVungTrungChuyenDTO vttDto) {
        StringBuilder sql = new StringBuilder("UPDATE tc_vung_trung_chuyen SET status=:status, tc_vtt_name =:vtt_name, tc_vtt_code=:vtt_code, created_by=:create_by, last_updated_by=:last_update, " +
                "tc_vtt_note =:note, tc_confirmed_time=:confirmedTime ");
        if (!StringUtils.isEmpty(vttDto.getTcVttContent())) {
            sql.append(", tc_vtt_content = GeomFromText(:content) ");
            sql.append(", tc_centroid_lat=:centroidLat, tc_centroid_long=:centroidLong");
        }
        sql.append(" WHERE tc_vtt_id =:vtt_id ");
        Query query = getEntityManager().createNativeQuery(sql.toString());
        query.setParameter("vtt_name", vttDto.getTcVttName());
        query.setParameter("vtt_code", vttDto.getTcVttCode());
        query.setParameter("status", vttDto.getStatus());
        query.setParameter("create_by", vttDto.getCreatedBy());
        query.setParameter("last_update", vttDto.getLastUpdatedBy());
        if (!StringUtils.isEmpty(vttDto.getTcVttContent())) {
            query.setParameter("content", vttDto.getTcVttContent());
            query.setParameter("centroidLat", vttDto.getTcCentroidLat());
            query.setParameter("centroidLong", vttDto.getTcCentroidLong());
        }
        query.setParameter("note", vttDto.getTcVttNote());
        query.setParameter("confirmedTime", vttDto.getTcConfirmedTime());
        query.setParameter("vtt_id", vttDto.getTcVttId());
        query.executeUpdate();
    }

    @Override
    public List<Object[]> getall() {
        return getEntityManager().createNativeQuery("SELECT tc_vtt_id, tc_vtt_name, tc_vtt_code, created_by, last_updated_by, created_date, AsText(tc_vtt_content), tc_average_speed, status FROM tc_vung_trung_chuyen").getResultList();

    }

    @Override
    public List<Object[]> getDataByName(String tenVung) {
        return getEntityManager().createNativeQuery("SELECT tc_vtt_id, tc_vtt_name, tc_vtt_code, created_by, last_updated_by, created_date, AsText(tc_vtt_content), tc_average_speed, status FROM tc_vung_trung_chuyen WHERE tc_vtt_name LIKE :tenVung").setParameter("tenVung", "%" + tenVung + "%").getResultList();
    }

    @Override
    public double getAverageSpeedById(int id) {
        Object obj = getEntityManager().createNativeQuery("SELECT tc_average_speed FROM tc_vung_trung_chuyen WHERE tc_vtt_id = " + id).getSingleResult();
        return obj != null ? Double.valueOf(obj + "") : 0;
    }

    @Override
    public List<VtcDTO> findAllByStatus(Integer status) {
        return getEntityManager().createNativeQuery("SELECT tc_vtt_code AS vtcCode, tc_vtt_id as vtcId FROM " +
                "tc_vung_trung_chuyen WHERE status =:status").setParameter("status", status).unwrap(org.hibernate.query.Query.class).setResultTransformer(Transformers.aliasToBean(VtcDTO.class)).list();
    }

    @Override
    public TcVungTrungChuyenEntity findByIdVtc(Integer idVtc) {
            String sql = "SELECT vc.tc_vtt_id AS tcVttId , vc.tc_vtt_name AS tcVttName FROM tc_vung_trung_chuyen vc WHERE vc.tc_vtt_id =:id";
                return (TcVungTrungChuyenEntity) getEntityManager().createNativeQuery(sql).unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(TcVungTrungChuyenEntity.class)).setParameter("id", idVtc).setMaxResults(1).uniqueResult();
    }
}
