package com.havaz.transport.api.model;

import java.util.List;

public class VeLuuLenhDTO {
    private List<Integer> bvvIds;
    private int thuTuDon;
    private String ghiChu;

    public List<Integer> getBvvIds() {
        return bvvIds;
    }

    public void setBvvIds(List<Integer> bvvIds) {
        this.bvvIds = bvvIds;
    }

    public int getThuTuDon() {
        return thuTuDon;
    }

    public void setThuTuDon(int thuTuDon) {
        this.thuTuDon = thuTuDon;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
