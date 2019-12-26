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
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tc_ca")
@NamedNativeQueries({
                            @NamedNativeQuery(
                                    name = "getTaiXeTrungChuyen",
                                    query = "SELECT adm_id, adm_name " +
                                            "FROM  admin_lv2_user " +
                                            "INNER JOIN admin_lv2_user_group_id " +
                                            "ON admg_admin_id = adm_id " +
                                            "WHERE adm_active = 1 AND admg_group_id = 2 " +
                                            "ORDER BY adm_name ASC"
                            ),
                            @NamedNativeQuery(
                                    name = "getTaiXeTrungChuyenByChiNhanhId",
                                    query = "SELECT adm_id, adm_name " +
                                            "FROM  admin_lv2_user " +
                                            "INNER JOIN admin_lv2_user_group_id " +
                                            "ON admg_admin_id = adm_id " +
                                            "WHERE adm_active = 1 AND admg_group_id = 2 " +
                                            "AND adm_noi_lam_viec IN :chiNhanhId " +
                                            "ORDER BY adm_name ASC"
                            )
                    })
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class TcCaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "tc_ca_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tcCaId;

    @Column(name = "ma_ca")
    private String maCa;

    @Column(name = "ten_ca")
    private String tenCa;

    @Column(name = "gio_bat_dau", columnDefinition = "FLOAT")
    private Float gioBatDau;

    @Column(name = "gio_ket_thuc")
    private Float gioKetThuc;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "trang_thai", columnDefinition = "INTEGER")
    private Boolean trangThai;

    @Column(name = "hous_in_ca")
    private Double housInCa;

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
                .append("tcCaId", tcCaId)
                .append("maCa", maCa)
                .append("tenCa", tenCa)
                .append("gioBatDau", gioBatDau)
                .append("gioKetThuc", gioKetThuc)
                .append("ghiChu", ghiChu)
                .append("trangThai", trangThai)
                .append("housInCa", housInCa)
                .append("createdDate", createdDate)
                .append("createdBy", createdBy)
                .append("lastUpdatedDate", lastUpdatedDate)
                .append("lastUpdatedBy", lastUpdatedBy)
                .toString();
    }
}
