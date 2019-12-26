package com.havaz.transport.api.model;

import java.util.List;

public class DaTraDTO {
    private int taixeId;
    private List<Integer> listBvvId;

    public int getTaixeId() {
        return taixeId;
    }

    public void setTaixeId(int taixeId) {
        this.taixeId = taixeId;
    }

    public List<Integer> getListBvvId() {
        return listBvvId;
    }

    public void setListBvvId(List<Integer> listBvvId) {
        this.listBvvId = listBvvId;
    }

    public DaTraDTO(int taixeId, List<Integer> listBvvId) {
        this.taixeId = taixeId;
        this.listBvvId = listBvvId;
    }

    public DaTraDTO() {
    }
}
