package com.havaz.transport.api.model;

import java.util.List;

public class UpdateThuTuDonDTO {
    private int taiXeId;
    private int lenhId;
    private List<UpdateThuTuDonVeTcDTO> listVeTc;

    public int getLenhId() {
        return lenhId;
    }

    public int getTaiXeId() {
        return taiXeId;
    }

    public void setTaiXeId(int taiXeId) {
        this.taiXeId = taiXeId;
    }

    public void setLenhId(int lenhId) {
        this.lenhId = lenhId;
    }

    public List<UpdateThuTuDonVeTcDTO> getListVeTc() {
        return listVeTc;
    }

    public void setListVeTc(List<UpdateThuTuDonVeTcDTO> listVeTc) {
        this.listVeTc = listVeTc;
    }
}
