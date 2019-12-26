package com.havaz.transport.api.model;

import java.util.List;

public class TrungChuyenResultDTO {
    private String bks;
    private String sdt;
    private String topic;
//    private String merchantId;
//    private String tripId;
//    private String taiXeId;
    private List<String> sdtKhach;

    public TrungChuyenResultDTO() {
    }

    public TrungChuyenResultDTO(String bks, String sdt, String topic, List<String> sdtKhach) {
        this.bks = bks;
        this.sdt = sdt;
        this.topic = topic;
//        this.tripId = tripId;
//        this.taiXeId = taiXeId;
        this.sdtKhach = sdtKhach;
    }

    public String getBks() {
        return bks;
    }

    public void setBks(String bks) {
        this.bks = bks;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

//    public String getMerchantId() {
//        return merchantId;
//    }
//
//    public void setMerchantId(String merchantId) {
//        this.merchantId = merchantId;
//    }
//
//    public String getTripId() {
//        return tripId;
//    }
//
//    public void setTripId(String tripId) {
//        this.tripId = tripId;
//    }
//
//    public String getTaiXeId() {
//        return taiXeId;
//    }
//
//    public void setTaiXeId(String taiXeId) {
//        this.taiXeId = taiXeId;
//    }

    public List<String> getSdtKhach() {
        return sdtKhach;
    }

    public void setSdtKhach(List<String> sdtKhach) {
        this.sdtKhach = sdtKhach;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
