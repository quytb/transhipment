package com.havaz.transport.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.havaz.transport.api.form.DieuDoForm;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ClientHavazNowDTO implements Serializable {
    private String tenKhachDi;
    private String sdtKhachDi;
    private String diaChiDon;
    private String soKhach;
    private Double thoiGianDon;
    private String tenNhaXe;
    private String tuyenDi;
    private String gioXuatBen;
    private String bienSoXe;
    private String sdtXe;
    private Double distanceDon;
    private Double latitude;
    private Double longitude;
    private Double distanceToHubDon;
    private Integer thoiGianToHubDon;
    private String createTime;
    private Integer hubTraKhach;
    private String ngayXeChay;
    private Boolean isHavazNow;
    private String timeXtToHub;
    private String diaChiTra;
    private Integer typeNotify;

    public Integer getTypeNotify() {
        return typeNotify;
    }

    public void setTypeNotify(Integer typeNotify) {
        this.typeNotify = typeNotify;
    }

    public String getDiaChiTra() {
        return diaChiTra;
    }

    public void setDiaChiTra(String diaChiTra) {
        this.diaChiTra = diaChiTra;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdatedDate;
    private Integer tripId;
    private DieuDoForm dieuDoForm;

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public DieuDoForm getDieuDoForm() {
        return dieuDoForm;
    }

    public void setDieuDoForm(DieuDoForm dieuDoForm) {
        this.dieuDoForm = dieuDoForm;
    }

    public String getTenKhachDi() {
        return tenKhachDi;
    }

    public void setTenKhachDi(String tenKhachDi) {
        this.tenKhachDi = tenKhachDi;
    }

    public String getSdtKhachDi() {
        return sdtKhachDi;
    }

    public void setSdtKhachDi(String sdtKhachDi) {
        this.sdtKhachDi = sdtKhachDi;
    }

    public String getDiaChiDon() {
        return diaChiDon;
    }

    public void setDiaChiDon(String diaChiDon) {
        this.diaChiDon = diaChiDon;
    }

    public String getSoKhach() {
        return soKhach;
    }

    public void setSoKhach(String soKhach) {
        this.soKhach = soKhach;
    }

    public String getTenNhaXe() {
        return tenNhaXe;
    }

    public void setTenNhaXe(String tenNhaXe) {
        this.tenNhaXe = tenNhaXe;
    }

    public String getTuyenDi() {
        return tuyenDi;
    }

    public void setTuyenDi(String tuyenDi) {
        this.tuyenDi = tuyenDi;
    }

    public String getGioXuatBen() {
        return gioXuatBen;
    }

    public void setGioXuatBen(String gioXuatBen) {
        this.gioXuatBen = gioXuatBen;
    }

    public String getBienSoXe() {
        return bienSoXe;
    }

    public void setBienSoXe(String bienSoXe) {
        this.bienSoXe = bienSoXe;
    }

    public String getSdtXe() {
        return sdtXe;
    }

    public void setSdtXe(String sdtXe) {
        this.sdtXe = sdtXe;
    }

    public Double getDistanceDon() {
        return distanceDon;
    }

    public void setDistanceDon(Double distanceDon) {
        this.distanceDon = distanceDon;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getDistanceToHubDon() {
        return distanceToHubDon;
    }

    public void setDistanceToHubDon(Double distanceToHubDon) {
        this.distanceToHubDon = distanceToHubDon;
    }

    public Integer getThoiGianToHubDon() {
        return thoiGianToHubDon;
    }

    public void setThoiGianToHubDon(Integer thoiGianToHubDon) {
        this.thoiGianToHubDon = thoiGianToHubDon;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNgayXeChay() {
        return ngayXeChay;
    }

    public void setNgayXeChay(String ngayXeChay) {
        this.ngayXeChay = ngayXeChay;
    }

    public Double getThoiGianDon() {
        return thoiGianDon;
    }

    public void setThoiGianDon(Double thoiGianDon) {
        this.thoiGianDon = thoiGianDon;
    }



    public Integer getHubTraKhach() {
        return hubTraKhach;
    }

    public void setHubTraKhach(Integer hubTraKhach) {
        this.hubTraKhach = hubTraKhach;
    }

    public Boolean getIsHavazNow() {
        return isHavazNow;
    }

    public void setIsHavazNow(Boolean isHavazNow) {
        this.isHavazNow = isHavazNow;
    }

    public void setTimeXtToHub(String timeXtToHub) {
        this.timeXtToHub = timeXtToHub;
    }

    public String getTimeXtToHub() {
        return timeXtToHub;
    }
}

