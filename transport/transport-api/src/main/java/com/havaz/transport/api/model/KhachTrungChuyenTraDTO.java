package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KhachTrungChuyenTraDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String tenKhachTra;
    private String sdtKhachTra;
    private String diaChiTra;
    private Integer thuTuTra;
    private Integer thoiGianTra;
    private Integer trangThaiTrungChuyenTra;
    private Integer lenhIdTra;
    private Integer taiXeTraId;
    private String tuyenDi;
    private String gioXuatBen;
    private String bienSoXe;
    private Integer khachHangMoi;
    private String soGhe;
    private String soKhach;
    private List<Integer> veId;
    private String sdtXe;
    private String tenNhaXe;
    private Integer lenhTraStatus;
    private String ghiChu;
    private String gioDieuHanh;
    private Integer distanceTra;
    private String hubDonKhach;
    private Double latitude;
    private Double longitude;
    private Double distanceToHubTra;
    private Integer thoiGianToHubTra;
    private String createTime;
    private String gioVeBen;
    private String isHavazNow;
    //Default rank là Member
    private String rank = "Member";
}
