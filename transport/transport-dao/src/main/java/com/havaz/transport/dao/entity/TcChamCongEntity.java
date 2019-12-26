package com.havaz.transport.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
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

@Getter
@Setter
@Entity
@Table(name = "tc_cham_cong")

@NamedNativeQueries({
                            @NamedNativeQuery(
                                    name = "getDuLieuChamCong",
                                    query = "SELECT e.tai_xe_id,a.adm_name, e.xe_id, x.xe_bien_kiem_soat,e.tc_ca_id,c.ma_ca,e.ngay_cham_cong,e.gio_thuc_te,e.khach_phat_sinh " +
                                            "FROM tc_cham_cong e " +
                                            "LEFT JOIN admin_lv2_user a ON e.tai_xe_id = a.adm_id " +
                                            "LEFT JOIN xe x ON x.xe_id = e.xe_id " +
                                            "LEFT JOIN tc_ca c ON c.tc_ca_id = e.tc_ca_id " +
                                            "WHERE e.ngay_cham_cong =:ngay_cham_cong"

                            )
                    })
@EntityListeners(AuditingEntityListener.class)
public class TcChamCongEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "tc_cong_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tcCongId;

    @Column(name = "tc_lich_id")
    private Integer tcLichId;

    @Column(name = "tai_xe_id")
    private Integer taiXeId;

    @Column(name = "tc_ca_id")
    private Integer tcCaId;

    @Column(name = "xe_id")
    private Integer xeId;

    @Column(name = "ngay_cham_cong")
    private LocalDate ngayChamCong;

    @Column(name = "gio_thuc_te", columnDefinition = "FLOAT")
    private Double gioThucTe;

    @Column(name = "khach_phat_sinh")
    private Integer khachPhatSinh;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "created_by")
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_updated_by")
    private String lastUpdatedBy;

    @LastModifiedDate
    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;
}
