package com.havaz.transport.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.havaz.transport.api.common.Constant;

import java.util.List;

public class XeTcNowDTO {
    private int xeId;
    private int xtcType;
    private Integer txId;
    private String xtcBks;
    private String xtcSdt;
    private String xtcTen;
    private String xtcLongitude;
    private String xtcLatitude;
    private String xtcHubId;
    private String xtcHubName;

    @JsonIgnore
    private Double duration;
    private String role;
    private List<KhachNowDTO> dsKhachHang;


    public String getRole() {
        if (xtcType == 1) {
            return Constant.CTV;
        }
        if (xtcType == 2) {
            return Constant.TX_TC;
        }
        return "";
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getTxId() {
        return txId;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public void setTxId(Integer txId) {
        this.txId = txId;
    }

    public XeTcNowDTO() {
    }

    public int getXeId() {
        return xeId;
    }

    public void setXeId(int xeId) {
        this.xeId = xeId;
    }

    public int getXtcType() {
        return xtcType;
    }

    public void setXtcType(int xtcType) {
        this.xtcType = xtcType;
    }

    public String getXtcBks() {
        return xtcBks;
    }

    public void setXtcBks(String xtcBks) {
        this.xtcBks = xtcBks;
    }

    public String getXtcSdt() {
        return xtcSdt;
    }

    public void setXtcSdt(String xtcSdt) {
        this.xtcSdt = xtcSdt;
    }

    public String getXtcTen() {
        return xtcTen;
    }

    public void setXtcTen(String xtcTen) {
        this.xtcTen = xtcTen;
    }

    public String getXtcLongitude() {
        return xtcLongitude;
    }

    public void setXtcLongitude(String xtcLongitude) {
        this.xtcLongitude = xtcLongitude;
    }

    public String getXtcLatitude() {
        return xtcLatitude;
    }

    public void setXtcLatitude(String xtcLatitude) {
        this.xtcLatitude = xtcLatitude;
    }

    public String getXtcHubId() {
        return xtcHubId;
    }

    public void setXtcHubId(String xtcHubId) {
        this.xtcHubId = xtcHubId;
    }

    public String getXtcHubName() {
        return xtcHubName;
    }

    public void setXtcHubName(String xtcHubName) {
        this.xtcHubName = xtcHubName;
    }

    public List<KhachNowDTO> getDsKhachHang() {
        return dsKhachHang;
    }

    public void setDsKhachHang(List<KhachNowDTO> dsKhachHang) {
        this.dsKhachHang = dsKhachHang;
    }
}
