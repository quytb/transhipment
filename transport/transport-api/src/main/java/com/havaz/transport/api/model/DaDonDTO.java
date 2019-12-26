package com.havaz.transport.api.model;

import java.util.List;

public class DaDonDTO {
    private int taiXeId;
    private List<Integer> listBvvId;

    public int getTaiXeId() {
        return taiXeId;
    }

    public void setTaiXeId(int taiXeId) {
        this.taiXeId = taiXeId;
    }

    public List<Integer> getListBvvId() {
        return listBvvId;
    }

    public void setListBvvId(List<Integer> listBvvId) {
        this.listBvvId = listBvvId;
    }

    public DaDonDTO(int taiXeId, List<Integer> listBvvId) {
        this.taiXeId = taiXeId;
        this.listBvvId = listBvvId;
    }

    public DaDonDTO() {
    }
}
