package com.havaz.transport.core.constant;

public enum TrangThaiVe {
    DON (1),
    TRA(2);
    private final int trangThai;

    TrangThaiVe(int trangThai) {
        this.trangThai = trangThai;
    }

    public int getTrangThai() {
        return trangThai;
    }
}
