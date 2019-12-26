package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class KhachTrungChuyenDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int khachHangMoi;
    private List<Integer> veId = new ArrayList<>();
    private String tenKhachDi;
    private String sdtKhachDi;
    private String diaChiDon;
    private String soKhach;
    private String soGhe;
    private int thuTuDon;
    private int thoiGianDon;
    private int trangThaiTrungChuyen;
    private Integer lenhId;
    private int lenhStatus;
    private int taiXeId;
    private String tenNhaXe;
    private String tuyenDi;
    private String gioXuatBen;
    private String bienSoXe;
    private String sdtXe;
    private String gioDieuHanh;
    private String ghiChu;
    private Integer distanceDon;
    private Double latitude;
    private Double longitude;
    private Double distanceToHubDon;
    private int thoiGianToHubDon;
    private String createTime;
    private String gioVeBen;
    private String hubTraKhach;
    private String ngayXeChay;
    private String isHavazNow;
    private String timeXtToHub;
    //Default rank là Member
    private String rank = "Member";
}
