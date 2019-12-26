package com.havaz.transport.api.model;

public class CaTcDTO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int tcCaId;
    private String maCa;
    private String tenCa;
    private Float gioBatDau;
    private Float gioKetThuc;
    private String ghiChu;
    private Boolean trangThai;
//    private String createdBy;

    public CaTcDTO() {
    }
    public String getMaCa() {
        return maCa;
    }

    public void setMaCa(String maCa) {
        this.maCa = maCa;
    }

    public String getTenCa() {
        return tenCa;
    }

    public void setTenCa(String tenCa) {
        this.tenCa = tenCa;
    }

    public Float getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(Float gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public Float getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(Float gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

//
//    public String getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(String createdBy) {
//        this.createdBy = createdBy;
//    }

    public int getTcCaId() {
        return tcCaId;
    }

    public void setTcCaId(int tcCaId) {
        this.tcCaId = tcCaId;
    }
}
