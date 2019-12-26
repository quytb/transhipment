package com.havaz.transport.api.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ChamCongDTO {
    private int tcLichId;
    private int taiXeId;
    private String taiXeTen;
    private int xeId;
    private String bks;
    private int caId;
    private String maCa;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayChamCong;

    private Double gioThucTe;
    private int khachPhatSinh;
    private String ghiChu;
    private int tcCongId;

    public ChamCongDTO() {
    }

    public int getTcLichId() {
        return tcLichId;
    }

    public void setTcLichId(int tcLichId) {
        this.tcLichId = tcLichId;
    }

    public int getTaiXeId() {
        return taiXeId;
    }

    public void setTaiXeId(int taiXeId) {
        this.taiXeId = taiXeId;
    }

    public String getTaiXeTen() {
        return taiXeTen;
    }

    public void setTaiXeTen(String taiXeTen) {
        this.taiXeTen = taiXeTen;
    }

    public int getXeId() {
        return xeId;
    }

    public void setXeId(int xeId) {
        this.xeId = xeId;
    }

    public int getCaId() {
        return caId;
    }

    public void setCaId(int caId) {
        this.caId = caId;
    }

    public String getMaCa() {
        return maCa;
    }

    public void setMaCa(String maCa) {
        this.maCa = maCa;
    }

    public LocalDate getNgayChamCong() {
        return ngayChamCong;
    }

    public void setNgayChamCong(LocalDate ngayChamCong) {
        this.ngayChamCong = ngayChamCong;
    }

    public Double getGioThucTe() {
        return gioThucTe;
    }

    public void setGioThucTe(Double gioThucTe) {
        this.gioThucTe = gioThucTe;
    }

    public int getKhachPhatSinh() {
        return khachPhatSinh;
    }

    public void setKhachPhatSinh(int khachPhatSinh) {
        this.khachPhatSinh = khachPhatSinh;
    }

    public String getBks() {
        return bks;
    }

    public void setBks(String bks) {
        this.bks = bks;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public int getTcCongId() {
        return tcCongId;
    }

    public void setTcCongId(int tcCongId) {
        this.tcCongId = tcCongId;
    }
}
