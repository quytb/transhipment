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
@Table(name = "tc_lenh")
@EntityListeners(AuditingEntityListener.class)
@NamedNativeQueries({
                            @NamedNativeQuery(
                                    name = "getHistoryByTaiXeId",
                                    query = "SELECT LenhHuy.soLenhHuy, " +
                                            "LenhDon.soLenhDon, " +
                                            "KhachDon.soKhachDon, " +
                                            "KhachHuy.soKhachHuy " +
                                            "FROM " +
                                            "(SELECT IFNULL(( " +
                                            "SELECT COUNT(1) AS soLenhHuy " +
                                            "FROM tc_lenh LENH " +
                                            "WHERE LENH.trang_thai = :lenh_finished " +
                                            "AND LENH.lai_xe_id = :inputTaiXeId " +
                                            "AND MONTH(LENH.last_updated_date) = :inputMonth " +
                                            "AND YEAR(LENH.last_updated_date) = :inputYear " +
                                            "GROUP BY LENH.lai_xe_id), NULL) soLenhHuy) LenhHuy, " +
                                            "(SELECT IFNULL(( " +
                                            "SELECT COUNT(1) AS soLenhDon " +
                                            "FROM tc_lenh LENH " +
                                            "WHERE LENH.trang_thai = :lenh_canceled " +
                                            "AND LENH.lai_xe_id = :inputTaiXeId " +
                                            "AND MONTH(LENH.last_updated_date) = :inputMonth " +
                                            "GROUP BY LENH.lai_xe_id), NULL) soLenhDon) LenhDon, " +
                                            "(SELECT IFNULL(( " +
                                            "SELECT COUNT(1) AS soKhachDon " +
                                            "FROM tc_ve VE " +
                                            "WHERE VE.tc_trang_thai_don = :ve_pickedUp " +
                                            "AND VE.lai_xe_id_don = :inputTaiXeId " +
                                            "AND MONTH(VE.last_updated_date) = :inputMonth " +
                                            "AND YEAR(VE.last_updated_date) = :inputYear " +
                                            "GROUP BY VE.lai_xe_id_don), NULL) soKhachDon) KhachDon, " +
                                            "(SELECT IFNULL( " +
                                            "(SELECT COUNT(1) " +
                                            "FROM tc_ve VE " +
                                            "WHERE VE.tc_trang_thai_don = :ve_canceled " +
                                            "AND VE.lai_xe_id_don = :inputTaiXeId " +
                                            "AND MONTH(VE.last_updated_date) = :inputMonth " +
                                            "AND YEAR(VE.last_updated_date) = :inputYear " +
                                            "GROUP BY VE.lai_xe_id_don) , NULL) soKhachHuy) KhachHuy "
                            ),
                            @NamedNativeQuery(
                                    name = "getXeTuyenInfor",
                                    query = " SELECT XE.xe_bien_kiem_soat, XE.xe_so_dien_thoai, DD.did_id, tcv.is_tc_don, tcv.is_tc_tra" +
                                            " FROM" +
                                            " tc_ve tcv" +
                                            " LEFT JOIN" +
                                            " tc_lenh lenh" +
                                            " ON tcv.tc_lenh_id = lenh.tc_lenh_id " +
                                            " LEFT JOIN" +
                                            " dieu_do_temp DD" +
                                            " ON tcv.did_id = DD.did_id" +
                                            " LEFT JOIN" +
                                            " xe XE" +
                                            " ON DD.did_xe_id = XE.xe_id" +
                                            " WHERE" +
                                            " (tcv.tc_lenh_id = :tc_lenh_id OR tcv.tc_lenh_tra_id = :tc_lenh_id ) AND " +
                                            " lenh.kieu_lenh IN (1, 2) AND" +
                                            " (tcv.is_tc_don = 1 OR tcv.is_tc_tra = 1)" +
                                            " GROUP BY DD.did_id ORDER BY DD.did_id"
                            )
                    })
@Getter
@Setter
public class TcLenhEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "tc_lenh_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tcLenhId;

    @Column(name = "lai_xe_id")
    private Integer laiXeId;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "chi_tiet_lenh", columnDefinition = "TEXT")
    private String chiTietLenh;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "kieu_lenh")
    private Integer kieuLenh;

    @Column(name = "xe_id")
    private Integer xeId;

    @Column(name = "is_havaz_now", columnDefinition = "integer default 0")
    private Boolean isHavazNow = false;

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

    @Column(name = "canceled_by", columnDefinition = "integer default 0")
    private Integer canceledBy;

    @Column(name = "ly_do_huy")
    private String lyDoHuy;

    @Column(name = "diem_giao_khach")
    private Integer diemgiaokhach;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("tcLenhId", tcLenhId)
                .append("laiXeId", laiXeId)
                .append("trangThai", trangThai)
                .append("chiTietLenh", chiTietLenh)
                .append("ghiChu", ghiChu)
                .append("kieuLenh", kieuLenh)
                .append("xeId", xeId)
                .append("isHavazNow", isHavazNow)
                .append("createdDate", createdDate)
                .append("createdBy", createdBy)
                .append("lastUpdatedDate", lastUpdatedDate)
                .append("lastUpdatedBy", lastUpdatedBy)
                .append("diemgiaokhach", diemgiaokhach)
                .toString();
    }
}
