package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
public class KhachHavazDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String merchantId;
    private String tripId;
    private String xeTrungTam;
    private String topic;
    private String sdtKhachHang;
    private String gioDon;
    private String thuTuDon;
    private String trangThaiTc;
    private String longitude;
    private String latitude;
    private String tcvSource;
    private String tcvDistance;
    private String hasSentMsg;
    private String sdtXeTuyen;
    private String pickupTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("merchantId", merchantId)
                .append("tripId", tripId)
                .append("xeTrungTam", xeTrungTam)
                .append("topic", topic)
                .append("sdtKhachHang", sdtKhachHang)
                .append("gioDon", gioDon)
                .append("thuTuDon", thuTuDon)
                .append("trangThaiTc", trangThaiTc)
                .append("longitude", longitude)
                .append("latitude", latitude)
                .append("tcvSource", tcvSource)
                .append("tcvDistance", tcvDistance)
                .append("hasSentMsg", hasSentMsg)
                .append("sdtXeTuyen", sdtXeTuyen)
                .append("pickupTime", pickupTime)
                .toString();
    }
}
