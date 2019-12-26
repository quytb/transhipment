package com.havaz.transport.api.model;

import java.util.ArrayList;
import java.util.List;

public class NowTripDetailDTO {

    private String tripId;
    private String xtBks;
    private String xtSdt;
    private String xtTen;
    private Integer lxTuyenId;
    private String xtLongitude;
    private String xtLatitude;
    private List<XeTcNowDTO> dsXeTc = new ArrayList<>();

    public Integer getLxTuyenId() {
        return lxTuyenId;
    }

    public void setLxTuyenId(Integer lxTuyenId) {
        this.lxTuyenId = lxTuyenId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getXtBks() {
        return xtBks;
    }

    public void setXtBks(String xtBks) {
        this.xtBks = xtBks;
    }

    public String getXtSdt() {
        return xtSdt;
    }

    public void setXtSdt(String xtSdt) {
        this.xtSdt = xtSdt;
    }

    public String getXtTen() {
        return xtTen;
    }

    public void setXtTen(String xtTen) {
        this.xtTen = xtTen;
    }

    public String getXtLongitude() {
        return xtLongitude;
    }

    public void setXtLongitude(String xtLongitude) {
        this.xtLongitude = xtLongitude;
    }

    public String getXtLatitude() {
        return xtLatitude;
    }

    public void setXtLatitude(String xtLatitude) {
        this.xtLatitude = xtLatitude;
    }

    public List<XeTcNowDTO> getDsXeTc() {
        return dsXeTc;
    }

    public void setDsXeTc(List<XeTcNowDTO> dsXeTc) {
        this.dsXeTc = dsXeTc;
    }
}
