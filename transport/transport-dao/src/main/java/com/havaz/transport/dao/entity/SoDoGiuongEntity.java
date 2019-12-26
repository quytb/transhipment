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
@Table(name = "so_do_giuong")
public class SoDoGiuongEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "sdg_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sdgId;

    @Column(name = "sdg_name")
    private String sdgName;

    @Column(name = "sdg_khoa_ban_ve", columnDefinition = "TEXT")
    private String sdgKhoaBanVe;

    @Column(name = "sdg_khoa_ban_ve_le_tet", columnDefinition = "TEXT")
    private String sdgKhoaBanVeLeTet;

    @Column(name = "sdg_khoa_ban_ve_khach", columnDefinition = "TEXT")
    private String sdgKhoaBanVeKhach;

    @Column(name = "sdg_khoa_ban_ve_khach_le_tet", columnDefinition = "TEXT")
    private String sdgKhoaBanVeKhachLeTet;

    @Column(name = "sdg_khoa_ban_ve_chieu_b", columnDefinition = "TEXT")
    private String sdgKhoaBanVeChieuB;

    @Column(name = "sdg_khoa_ban_ve_le_tet_chieu_b", columnDefinition = "TEXT")
    private String sdgKhoaBanVeLeTetChieuB;

    @Column(name = "sdg_khoa_ban_ve_khach_chieu_b", columnDefinition = "TEXT")
    private String sdgKhoaBanVeKhachChieuB;

    @Column(name = "sdg_khoa_ban_ve_khach_le_tet_chieu_b", columnDefinition = "TEXT")
    private String sdgKhoaBanVeKhachLeTetChieuB;

    @Column(name = "sdg_so_hang")
    private Integer sdgSoHang;

    @Column(name = "sdg_so_cho")
    private Integer sdgSoCho;

    @Column(name = "sdg_so_cho_san")
    private Integer sdgSoChoSan;

    @Column(name = "sdg_so_cho_tong")
    private Integer sdgSoChoTong;

    @Column(name = "sdg_type")
    private Integer sdgType;

    @Column(name = "sdg_status", columnDefinition = "TINYINT")
    private Integer sdgStatus;

    @Column(name = "sdg_tang_1_so_hang")
    private Integer sdgTang1SoHang;

    @Column(name = "sdg_tang_1_so_cot")
    private Integer sdgTang1SoCot;

    @Column(name = "sdg_tang_2_so_hang")
    private Integer sdgTang2SoHang;

    @Column(name = "sdg_tang_2_so_cot")
    private Integer sdgTang2SoCot;

    @Column(name = "sdg_tang_3_so_hang")
    private Integer sdgTang3SoHang;

    @Column(name = "sdg_tang_3_so_cot")
    private Integer sdgTang3SoCot;

    @Column(name = "sdg_tang_4_so_hang")
    private Integer sdgTang4SoHang;

    @Column(name = "sdg_tang_4_so_cot")
    private Integer sdgTang4SoCot;

    @Column(name = "sdg_tang_5_so_hang")
    private Integer sdgTang5SoHang;

    @Column(name = "sdg_tang_5_so_cot")
    private Integer sdgTang5SoCot;

    @Column(name = "sdg_xe_loai")
    private Integer sdgXeLoai;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("sdgId", sdgId)
                .append("sdgName", sdgName)
                .append("sdgKhoaBanVe", sdgKhoaBanVe)
                .append("sdgKhoaBanVeLeTet", sdgKhoaBanVeLeTet)
                .append("sdgKhoaBanVeKhach", sdgKhoaBanVeKhach)
                .append("sdgKhoaBanVeKhachLeTet", sdgKhoaBanVeKhachLeTet)
                .append("sdgKhoaBanVeChieuB", sdgKhoaBanVeChieuB)
                .append("sdgKhoaBanVeLeTetChieuB", sdgKhoaBanVeLeTetChieuB)
                .append("sdgKhoaBanVeKhachChieuB", sdgKhoaBanVeKhachChieuB)
                .append("sdgKhoaBanVeKhachLeTetChieuB", sdgKhoaBanVeKhachLeTetChieuB)
                .append("sdgSoHang", sdgSoHang)
                .append("sdgSoCho", sdgSoCho)
                .append("sdgSoChoSan", sdgSoChoSan)
                .append("sdgSoChoTong", sdgSoChoTong)
                .append("sdgType", sdgType)
                .append("sdgStatus", sdgStatus)
                .append("sdgTang1SoHang", sdgTang1SoHang)
                .append("sdgTang1SoCot", sdgTang1SoCot)
                .append("sdgTang2SoHang", sdgTang2SoHang)
                .append("sdgTang2SoCot", sdgTang2SoCot)
                .append("sdgTang3SoHang", sdgTang3SoHang)
                .append("sdgTang3SoCot", sdgTang3SoCot)
                .append("sdgTang4SoHang", sdgTang4SoHang)
                .append("sdgTang4SoCot", sdgTang4SoCot)
                .append("sdgTang5SoHang", sdgTang5SoHang)
                .append("sdgTang5SoCot", sdgTang5SoCot)
                .append("sdgXeLoai", sdgXeLoai)
                .toString();
    }
}
