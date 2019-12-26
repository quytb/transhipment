package com.havaz.transport.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tuyen")
public class TuyenEntity implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "tuy_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tuyId;

    @Column(name = "tuy_ma")
    private String tuyMa;

    @Column(name = "tuy_trung_tam")
    private Integer tuyTrungTam;

    @Column(name = "tuy_type")
    private Integer tuyType;

    @Column(name = "tuy_so_hieu")
    private String tuySoHieu;

    @Column(name = "tuy_hanh_trinh")
    private String tuyHanhTrinh;

    @Column(name = "tuy_ten_md5")
    private String tuyTenMd5;

    @Column(name = "tuy_ten")
    private String tuyTen;

    @Column(name = "tuy_image")
    private String tuyImage;

    @Column(name = "tuy_nhom_tuyen_id")
    private Integer tuyNhomTuyenId;

    @Column(name = "tuy_ben_a")
    private Integer tuyBenA;

    @Column(name = "tuy_ben_b")
    private Integer tuyBenB;

    @Column(name = "tuy_so_km")
    private Integer tuySoKm;

    @Column(name = "tuy_thoi_gian_chay")
    private Integer tuyThoiGianChay;

    @Column(name = "tuy_gia_vip")
    private Integer tuyGiaVip;

    @Column(name = "tuy_gia_nho_nhat")
    private Integer tuyGiaNhoNhat;

    @Column(name = "tuy_airlink", columnDefinition = "TINYINT")
    private Integer tuyAirlink;

    @Column(name = "tuy_active", columnDefinition = "TINYINT")
    private Boolean tuyActive = true;

    @Column(name = "tuy_status")
    private Integer tuyStatus;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "tuy_is_thue", columnDefinition = "TINYINT")
    private Boolean tuyIsThue = false;

    @Column(name = "tuy_thue_chieu", columnDefinition = "TINYINT")
    private Integer tuyThueChieu;

    @Column(name = "tuy_thue_gia_min")
    private Integer tuyThueGiaMin;

    @Column(name = "tuy_thue_gia_max")
    private Integer tuyThueGiaMax;

    @Column(name = "tuy_thue_ve_the", columnDefinition = "TINYINT")
    private Integer tuyThueVeThe;

    @Column(name = "tuy_thue_chang", columnDefinition = "TINYINT")
    private Integer tuyThueChang;

    @Column(name = "tuy_thue_ma_hoa_don")
    private String tuyThueMaHoaDon;

}
