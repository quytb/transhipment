package com.havaz.transport.dao.entity;

import com.havaz.transport.core.constant.Journey;
import com.havaz.transport.dao.hibernate.converter.JourneyConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "not_tuyen")
public class NotTuyenEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "not_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notId;

    @Column(name = "not_tuy_id")
    private Integer notTuyId;

    @Column(name = "not_tuy_id_2")
    private Integer notTuyId2;

    @Column(name = "not_ma")
    private String notMa;

    @Column(name = "not_nhom_tuyen_id")
    private Integer notNhomTuyenId;

    @Convert(converter = JourneyConverter.class)
    @Column(name = "not_chieu_di", columnDefinition = "TINYINT")
    private Journey notChieuDi;

    @Column(name = "not_xe_id")
    private Integer notXeId;

    @Column(name = "not_tai_1_id")
    private Integer notTai1Id;

    @Column(name = "not_tai_2_id")
    private Integer notTai2Id;

    @Column(name = "not_tv_id")
    private Integer notTvId;

    @Column(name = "not_doi_luu_id")
    private Integer notDoiLuuId;

    @Column(name = "not_type")
    private Integer notType;

    @Column(name = "not_loai_hinh_ban_ve")
    private Integer notLoaiHinhBanVe;

    @Column(name = "not_so_do_giuong")
    private Integer notSoDoGiuong;

    @Column(name = "not_comment")
    private String notComment;

    @Column(name = "not_tang_cuong", columnDefinition = "TINYINT")
    private Integer notTangCuong;

    @Column(name = "not_gio_xuat_ben")
    private String notGioXuatBen;

    @Column(name = "not_gio_xuat_ben_int")
    private Integer notGioXuatBenInt;

    @Column(name = "not_gio_xuat_ben_du_kien")
    private String notGioXuatBenDuKien;

    @Column(name = "not_gio_dieu_hanh")
    private String notGioDieuHanh;

    @Column(name = "not_gio_ket_thuc")
    private String notGioKetThuc;

    @Column(name = "not_gio_lai_xe_co_mat_tai_cty")
    private String notGioLaiXeCoMatTaiCty;

    @Column(name = "not_gio_xe_ra_khoi_cty")
    private String notGioXeRaKhoiCty;

    @Column(name = "not_gio_xe_co_mat_tai_ben")
    private String notGioXeCoMatTaiBen;

    @Column(name = "not_gio_nop_doanh_thu")
    private String notGioNopDoanhThu;

    @Column(name = "not_cong_tac_vien", columnDefinition = "TINYINT")
    private Integer notCongTacVien;

    @Column(name = "not_order")
    private Integer notOrder;

    @Column(name = "not_xe_nhanh", columnDefinition = "TINYINT")
    private Boolean notXeNhanh;

    @Column(name = "not_loai_dich_vu")
    private Integer notLoaiDichVu;

    @Column(name = "not_ngay_mai", columnDefinition = "TINYINT")
    private Integer notNgayMai;

    @Column(name = "not_airlink", columnDefinition = "TINYINT")
    private Integer notAirlink;

    @Column(name = "not_active", columnDefinition = "INTEGER")
    private Boolean notActive;

    @Column(name = "not_routing_option_id")
    private Integer notRoutingOptionId;

}
