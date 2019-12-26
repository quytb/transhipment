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
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "xe")
public class XeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "xe_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer xeId;

    @Column(name = "xe_bien_kiem_soat")
    private String xeBienKiemSoat;

    @Column(name = "xe_bien_kiem_soat_so")
    private String xeBienKiemSoatSo;

    @Column(name = "xe_bien_kiem_soat_md5")
    private String xeBienKiemSoatMd5;

    @Column(name = "xe_so_dien_thoai")
    private String xeSoDienThoai;

    @Column(name = "xe_so_cho")
    private Integer xeSoCho;

    @Column(name = "xe_nam_san_xuat")
    private Integer xeNamSanXuat;

    @Column(name = "xe_loai")
    private Integer xeLoai;

    @Column(name = "xe_loai_so_do_giuong")
    private Integer xeLoaiSoDoGiuong;

    @Column(name = "xe_nhom_id")
    private Integer xeNhomId;

    @Column(name = "xe_nhom_order")
    private Integer xeNhomOrder;

    @Column(name = "xe_trung_tam")
    private Integer xeTrungTam;

    @Column(name = "xe_hang")
    private Integer xeHang;

    @Column(name = "xe_han_luu_hanh")
    private Integer xeHanLuuHanh;

    @Column(name = "xe_han_bao_hiem_dan_su")
    private Integer xeHanBaoHiemDanSu;

    @Column(name = "xe_han_bao_hiem_than_vo")
    private Integer xeHanBaoHiemThanVo;

    @Column(name = "xe_han_dang_ky")
    private Integer xeHanDangKy;

    @Column(name = "xe_han_phi_duong_bo")
    private Integer xeHanPhiDuongBo;

    @Column(name = "xe_don_vi_bhds")
    private String xeDonViBhds;

    @Column(name = "xe_don_vi_bhtv")
    private String xeDonViBhtv;

    @Column(name = "xe_don_vi_ngan_hang")
    private String xeDonViNganHang;

    @Column(name = "xe_lai_1")
    private Integer xeLai1;

    @Column(name = "xe_lai_2")
    private Integer xeLai2;

    @Column(name = "xe_not")
    private Integer xeNot;

    @Column(name = "xe_du_phong", columnDefinition = "TINYINT")
    private Boolean xeDuPhong;

    @Column(name = "xe_tiep_vien")
    private Integer xeTiepVien;

    @Column(name = "xe_tiep_vien_2")
    private Integer xeTiepVien2;

    @Column(name = "xe_time_update")
    private Integer xeTimeUpdate;

    @Column(name = "xe_cong_tac_vien", columnDefinition = "TINYINT")
    private Boolean xeCongTacVien;

    @Column(name = "xe_km_day")
    private Integer xeKmDay;

    @Column(name = "xe_active", columnDefinition = "TINYINT")
    private Boolean xeActive = true;

    @Column(name = "xe_adm_create")
    private Integer xeAdmCreate;

    @Column(name = "xe_status", columnDefinition = "TINYINT")
    private Integer xeStatus;

    @Column(name = "xe_ma_app")
    private String xeMaApp;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "xe_xm_id")
    private Integer xeXmId;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("xeId", xeId)
                .append("xeBienKiemSoat", xeBienKiemSoat)
                .append("xeBienKiemSoatSo", xeBienKiemSoatSo)
                .append("xeBienKiemSoatMd5", xeBienKiemSoatMd5)
                .append("xeSoDienThoai", xeSoDienThoai)
                .append("xeSoCho", xeSoCho)
                .append("xeNamSanXuat", xeNamSanXuat)
                .append("xeLoai", xeLoai)
                .append("xeLoaiSoDoGiuong", xeLoaiSoDoGiuong)
                .append("xeNhomId", xeNhomId)
                .append("xeNhomOrder", xeNhomOrder)
                .append("xeTrungTam", xeTrungTam)
                .append("xeHang", xeHang)
                .append("xeHanLuuHanh", xeHanLuuHanh)
                .append("xeHanBaoHiemDanSu", xeHanBaoHiemDanSu)
                .append("xeHanBaoHiemThanVo", xeHanBaoHiemThanVo)
                .append("xeHanDangKy", xeHanDangKy)
                .append("xeHanPhiDuongBo", xeHanPhiDuongBo)
                .append("xeDonViBhds", xeDonViBhds)
                .append("xeDonViBhtv", xeDonViBhtv)
                .append("xeDonViNganHang", xeDonViNganHang)
                .append("xeLai1", xeLai1)
                .append("xeLai2", xeLai2)
                .append("xeNot", xeNot)
                .append("xeDuPhong", xeDuPhong)
                .append("xeTiepVien", xeTiepVien)
                .append("xeTiepVien2", xeTiepVien2)
                .append("xeTimeUpdate", xeTimeUpdate)
                .append("xeCongTacVien", xeCongTacVien)
                .append("xeKmDay", xeKmDay)
                .append("xeActive", xeActive)
                .append("xeAdmCreate", xeAdmCreate)
                .append("xeStatus", xeStatus)
                .append("xeMaApp", xeMaApp)
                .append("deletedAt", deletedAt)
                .append("xeXmId", xeXmId)
                .toString();
    }
}
