package com.havaz.transport.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
public class LocationDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("LocalDateTime")
    private Long time;

    @JsonProperty("taiXeId")
    private Integer taiXeId;

    @JsonProperty("bien_kiem_soat")
    private String bienKiemsoat;

    @JsonProperty("con_code")
    private String code;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("longitude", longitude)
                .append("latitude", latitude)
                .append("time", time)
                .append("taiXeId", taiXeId)
                .append("bienKiemsoat", bienKiemsoat)
                .append("code", code)
                .toString();
    }
}
