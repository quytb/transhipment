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

@Getter
@Setter
@Entity
@Table(name = "dieu_do_temp")
public class DieuDoTempEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "did_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer didId;

    @Column(name = "did_xe_id")
    private Integer didXeId;

    @Column(name = "did_xe_id_auto")
    private Integer didXeIdAuto;

    @Column(name = "did_not_id")
    private Integer didNotId;

    @Column(name = "did_loai_hinh_ban_ve")
    private Integer didLoaiHinhBanVe;

    @Column(name = "did_loai_so_do")
    private Integer didLoaiSoDo;

    @Column(name = "did_so_cho_da_ban")
    private Integer didSoChoDaBan;

    @Column(name = "did_gio_lai_xe_co_mat_tai_cty")
    private String didGioLaiXeCoMatTaiCty;

    @Column(name = "did_gio_lai_xe_co_mat_tai_cty_that")
    private String didGioLaiXeCoMatTaiCtyThat;

    @Column(name = "did_gio_xe_ra_khoi_cty")
    private String didGioXeRaKhoiCty;

    @Column(name = "did_gio_xe_ra_khoi_cty_that")
    private String didGioXeRaKhoiCtyThat;

    @Column(name = "did_gio_xuat_ben")
    private String didGioXuatBen;

    @Column(name = "did_gio_xuat_ben_that")
    private String didGioXuatBenThat;

    @Column(name = "did_gio_dieu_hanh")
    private String didGioDieuHanh;

    @Column(name = "did_gio_xuat_ben_thuc_te")
    private String didGioXuatBenThucTe;

    @Column(name = "did_gio_ket_thuc")
    private String didGioKetThuc;

    @Column(name = "did_gio_nop_doanh_thu")
    private String didGioNopDoanhThu;

    @Column(name = "did_so_lenh")
    private String didSoLenh;

    @Column(name = "did_lenh_van_doanh")
    private String didLenhVanDoanh;

    @Column(name = "did_time")
    private Integer didTime;

    @Column(name = "did_bvo_id")
    private Integer didBvoId;

    @Column(name = "did_time_xuat_ben")
    private Integer didTimeXuatBen;

    @Column(name = "did_status", columnDefinition = "TINYINT")
    private Integer didStatus;

    @Column(name = "did_status_change", columnDefinition = "TINYINT")
    private Integer didStatusChange;

    @Column(name = "did_stt_update", columnDefinition = "TINYINT")
    private Integer didSttUpdate;

    @Column(name = "did_comment")
    private String didComment;

    @Column(name = "did_truong_phong_duyet", columnDefinition = "TINYINT")
    private Integer didTruongPhongDuyet;

    @Column(name = "did_truong_phong_note")
    private String didTruongPhongNote;

    @Column(name = "did_giam_doc_duyet", columnDefinition = "TINYINT")
    private Integer didGiamDocDuyet;

    @Column(name = "did_giam_doc_note")
    private String didGiamDocNote;

    @Column(name = "did_loai_xe", columnDefinition = "TINYINT")
    private Integer didLoaiXe;

    @Column(name = "did_ban_ve_not")
    private String didBanVeNot;

    @Column(name = "did_dong_ban_ve", columnDefinition = "TINYINT")
    private Integer didDongBanVe;

    @Column(name = "did_admin_dong_ban_ve")
    private Integer didAdminDongBanVe;

    @Column(name = "did_xuong_xe", columnDefinition = "TINYINT")
    private Integer didXuongXe;

    @Column(name = "did_accept_status", columnDefinition = "TINYINT")
    private Integer didAcceptStatus;

    @Column(name = "did_accept_id")
    private Integer didAcceptId;

    @Column(name = "did_xuat_ben_stt", columnDefinition = "TINYINT")
    private Integer didXuatBenStt;

    @Column(name = "did_xuat_ben_admin_id")
    private Integer didXuatBenAdminId;

    @Column(name = "did_xuat_ben_time_start")
    private Integer didXuatBenTimeStart;

    @Column(name = "did_ve_ben_stt")
    private Integer didVeBenStt;

    @Column(name = "did_ve_ben_admin_id")
    private Integer didVeBenAdminId;

    @Column(name = "did_ve_ben_time_start")
    private Integer didVeBenTimeStart;

    @Column(name = "did_doanh_thu_ve")
    private Integer didDoanhThuVe;

    @Column(name = "did_doanh_thu_hang")
    private Integer didDoanhThuHang;

    @Column(name = "did_doanh_thu_hang_vp")
    private Integer didDoanhThuHangVp;

    @Column(name = "did_chi_phi")
    private Integer didChiPhi;

    @Column(name = "did_airlink", columnDefinition = "TINYINT")
    private Integer didAirlink;

    @Column(name = "did_doanh_thu_khac")
    private Integer didDoanhThuKhac;

    @Column(name = "did_doanh_thu_van_phong")
    private Integer didDoanhThuVanPhong;

    @Column(name = "did_lock", columnDefinition = "TINYINT")
    private Integer didLock;

    @Column(name = "did_status_xe")
    private Integer didStatusXe;

    @Column(name = "did_long_start")
    private Double didLongStart;

    @Column(name = "did_lat_start")
    private Double didLatStart;

    @Column(name = "did_long_end")
    private Double didLongEnd;

    @Column(name = "did_lat_end")
    private Double didLatEnd;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("didId", didId)
                .append("didXeId", didXeId)
                .append("didXeIdAuto", didXeIdAuto)
                .append("didNotId", didNotId)
                .append("didLoaiHinhBanVe", didLoaiHinhBanVe)
                .append("didLoaiSoDo", didLoaiSoDo)
                .append("didSoChoDaBan", didSoChoDaBan)
                .append("didGioLaiXeCoMatTaiCty", didGioLaiXeCoMatTaiCty)
                .append("didGioLaiXeCoMatTaiCtyThat", didGioLaiXeCoMatTaiCtyThat)
                .append("didGioXeRaKhoiCty", didGioXeRaKhoiCty)
                .append("didGioXeRaKhoiCtyThat", didGioXeRaKhoiCtyThat)
                .append("didGioXuatBen", didGioXuatBen)
                .append("didGioXuatBenThat", didGioXuatBenThat)
                .append("didGioDieuHanh", didGioDieuHanh)
                .append("didGioXuatBenThucTe", didGioXuatBenThucTe)
                .append("didGioKetThuc", didGioKetThuc)
                .append("didGioNopDoanhThu", didGioNopDoanhThu)
                .append("didSoLenh", didSoLenh)
                .append("didLenhVanDoanh", didLenhVanDoanh)
                .append("didTime", didTime)
                .append("didBvoId", didBvoId)
                .append("didTimeXuatBen", didTimeXuatBen)
                .append("didStatus", didStatus)
                .append("didStatusChange", didStatusChange)
                .append("didSttUpdate", didSttUpdate)
                .append("didComment", didComment)
                .append("didTruongPhongDuyet", didTruongPhongDuyet)
                .append("didTruongPhongNote", didTruongPhongNote)
                .append("didGiamDocDuyet", didGiamDocDuyet)
                .append("didGiamDocNote", didGiamDocNote)
                .append("didLoaiXe", didLoaiXe)
                .append("didBanVeNot", didBanVeNot)
                .append("didDongBanVe", didDongBanVe)
                .append("didAdminDongBanVe", didAdminDongBanVe)
                .append("didXuongXe", didXuongXe)
                .append("didAcceptStatus", didAcceptStatus)
                .append("didAcceptId", didAcceptId)
                .append("didXuatBenStt", didXuatBenStt)
                .append("didXuatBenAdminId", didXuatBenAdminId)
                .append("didXuatBenTimeStart", didXuatBenTimeStart)
                .append("didVeBenStt", didVeBenStt)
                .append("didVeBenAdminId", didVeBenAdminId)
                .append("didVeBenTimeStart", didVeBenTimeStart)
                .append("didDoanhThuVe", didDoanhThuVe)
                .append("didDoanhThuHang", didDoanhThuHang)
                .append("didDoanhThuHangVp", didDoanhThuHangVp)
                .append("didChiPhi", didChiPhi)
                .append("didAirlink", didAirlink)
                .append("didDoanhThuKhac", didDoanhThuKhac)
                .append("didDoanhThuVanPhong", didDoanhThuVanPhong)
                .append("didLock", didLock)
                .append("didStatusXe", didStatusXe)
                .append("didLongStart", didLongStart)
                .append("didLatStart", didLatStart)
                .append("didLongEnd", didLongEnd)
                .append("didLatEnd", didLatEnd)
                .toString();
    }
}
