package com.havaz.transport.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tc_lich_truc")
@NamedNativeQueries({
                            @NamedNativeQuery(
                                    name = "getLichTrucByNgayTruc",
                                    query = "SELECT e.tc_lich_id,e.tai_xe_id,a.adm_name,x.xe_bien_kiem_soat,e.ngay_truc,c.ma_ca,e.tc_ca_id,e.xe_id,e.ghi_chu, :startDate " +
                                            "FROM tc_lich_truc e " +
                                            "LEFT JOIN admin_lv2_user a ON e.tai_xe_id = a.adm_id " +
                                            "LEFT JOIN xe x ON x.xe_id = e.xe_id " +
                                            "LEFT JOIN tc_ca c ON c.tc_ca_id = e.tc_ca_id " +
                                            "WHERE e.tai_xe_id =:taixeId " +
                                            "AND :startDate <= e.ngay_truc <=:endDate"

                            ),
                            @NamedNativeQuery(
                                    name = "getTaiXeByNgayTruc",
                                    query = "SELECT e.tai_xe_id " +
                                            "FROM tc_lich_truc e " +
                                            "WHERE e.ngay_truc BETWEEN :startDate AND :endDate" +
                                            "GROUP BY e.tai_xe_id"
                            )
                    })
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class TcLichTrucEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "tc_lich_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tcLichId;

    @Column(name = "tai_xe_id")
    private Integer taiXeId;

    @Column(name = "tc_ca_id")
    private Integer tcCaId;

    @Column(name = "xe_id")
    private Integer xeId;

    @Column(name = "ngay_truc")
    private LocalDate ngayTruc;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreatedBy
    @Column(name = "created_by")
    private Integer createdBy;

    @LastModifiedDate
    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;

    @LastModifiedBy
    @Column(name = "last_updated_by")
    private Integer lastUpdatedBy;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("tcLichId", tcLichId)
                .append("taiXeId", taiXeId)
                .append("tcCaId", tcCaId)
                .append("xeId", xeId)
                .append("ngayTruc", ngayTruc)
                .append("ghiChu", ghiChu)
                .append("createdDate", createdDate)
                .append("createdBy", createdBy)
                .append("lastUpdatedDate", lastUpdatedDate)
                .append("lastUpdatedBy", lastUpdatedBy)
                .toString();
    }
}
