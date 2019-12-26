package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TripDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String bks;
    private String sdt;
    private String ten;
    private String topic;
    private int xeTrungTam;
    private String merchantId;
    private int tripId;
    private List<KhachDTO> dsKhachHang = new ArrayList<>();

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("bks", bks)
                .append("sdt", sdt)
                .append("ten", ten)
                .append("topic", topic)
                .append("xeTrungTam", xeTrungTam)
                .append("merchantId", merchantId)
                .append("tripId", tripId)
                .append("dsKhachHang", dsKhachHang)
                .toString();
    }
}
