package com.havaz.transport.api.common;

public enum TrangThaiTc {
    DADIEU("Đã Điều"),
    DANGDON("Đang Đón"),
    DADON("Đã Đón"),
    DAHUY("Đã Hủy"),
    CHUADIEU("Chưa Điều"),
    DANGTRA("Đang Trả"),
    DATRA("Đã Trả");

    private String trangThai;

    TrangThaiTc(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
