package com.havaz.transport.api.model;

import java.util.List;

public class BaoCaoTaiXeDTO {
    private int taiXeId;
    private String taiXeTen;
    private double tongCong;
    private List<NgayCongDTO> duLieuThang;

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

    public double getTongCong() {
        return tongCong;
    }

    public void setTongCong(double tongCong) {
        this.tongCong = tongCong;
    }

    public List<NgayCongDTO> getDuLieuThang() {
        return duLieuThang;
    }

    public void setDuLieuThang(List<NgayCongDTO> duLieuThang) {
        this.duLieuThang = duLieuThang;
    }
}

