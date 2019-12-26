package com.havaz.transport.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "ban_ve_huy")
public class BanVeHuyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "bvv_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bvvId;

    @Column(name = "bvv_bvn_id")
    private Integer bvvBvnId;

    @Column(name = "bvv_khach_hang_id")
    private Integer bvvKhachHangId;

    @Column(name = "bvv_order_id")
    private Integer bvvOrderId;

    @Column(name = "bvv_number")
    private Integer bvvNumber;

    @Column(name = "bvv_number_name")
    private String bvvNumberName;

    @Column(name = "bvv_bvd_id")
    private Integer bvvBvdId;

    @Column(name = "bvv_phone")
    private String bvvPhone;

    @Column(name = "bvv_ten_khach_hang")
    private String bvvTenKhachHang;

    @Column(name = "bvv_phone_di")
    private String bvvPhoneDi;

    @Column(name = "bvv_ten_khach_hang_di")
    private String bvvTenKhachHangDi;

    @Column(name = "bvv_time_book")
    private Integer bvvTimeBook;

    @Column(name = "bvv_time_update_seri")
    private Integer bvvTimeUpdateSeri;

    @Column(name = "bvv_time_last_update")
    private Integer bvvTimeLastUpdate;

    @Column(name = "bvv_diem_don_khach")
    private String bvvDiemDonKhach;

    @Column(name = "bvv_diem_tra_khach")
    private String bvvDiemTraKhach;

    @Column(name = "bvv_trung_chuyen_a", columnDefinition = "TINYINT")
    private Boolean bvvTrungChuyenA;

    @Column(name = "bvv_trung_chuyen_b", columnDefinition = "TINYINT")
    private Boolean bvvTrungChuyenB;

    @Column(name = "bvv_lai_xe_don")
    private Integer bvvLaiXeDon;

    @Column(name = "bvv_lai_xe_tra")
    private Integer bvvLaiXeTra;

    @Column(name = "bvv_trung_chuyen_a_order")
    private Integer bvvTrungChuyenAOrder;

    @Column(name = "bvv_trung_chuyen_b_order")
    private Integer bvvTrungChuyenBOrder;

    @Column(name = "bvv_trung_chuyen_a_note")
    private String bvvTrungChuyenANote;

    @Column(name = "bvv_trung_chuyen_b_note")
    private String bvvTrungChuyenBNote;

    @Column(name = "bvv_ghi_chu", columnDefinition = "TEXT")
    private String bvvGhiChu;

    @Column(name = "bvv_ghi_chu_2", columnDefinition = "TEXT")
    private String bvvGhiChu2;

    @Column(name = "bvv_status")
    private Integer bvvStatus;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("bvvId", bvvId)
                .append("bvvBvnId", bvvBvnId)
                .append("bvvKhachHangId", bvvKhachHangId)
                .append("bvvOrderId", bvvOrderId)
                .append("bvvNumber", bvvNumber)
                .append("bvvNumberName", bvvNumberName)
                .append("bvvBvdId", bvvBvdId)
                .append("bvvPhone", bvvPhone)
                .append("bvvTenKhachHang", bvvTenKhachHang)
                .append("bvvPhoneDi", bvvPhoneDi)
                .append("bvvTenKhachHangDi", bvvTenKhachHangDi)
                .append("bvvTimeBook", bvvTimeBook)
                .append("bvvTimeUpdateSeri", bvvTimeUpdateSeri)
                .append("bvvTimeLastUpdate", bvvTimeLastUpdate)
                .append("bvvDiemDonKhach", bvvDiemDonKhach)
                .append("bvvDiemTraKhach", bvvDiemTraKhach)
                .append("bvvTrungChuyenA", bvvTrungChuyenA)
                .append("bvvTrungChuyenB", bvvTrungChuyenB)
                .append("bvvLaiXeDon", bvvLaiXeDon)
                .append("bvvLaiXeTra", bvvLaiXeTra)
                .append("bvvTrungChuyenAOrder", bvvTrungChuyenAOrder)
                .append("bvvTrungChuyenBOrder", bvvTrungChuyenBOrder)
                .append("bvvTrungChuyenANote", bvvTrungChuyenANote)
                .append("bvvTrungChuyenBNote", bvvTrungChuyenBNote)
                .append("bvvGhiChu", bvvGhiChu)
                .append("bvvGhiChu2", bvvGhiChu2)
                .append("bvvStatus", bvvStatus)
                .toString();
    }
}
