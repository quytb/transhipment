package com.havaz.transport.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
public class TripStationDTO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("station")
    private String station;

    @JsonProperty("time")
    private int time;

    @JsonProperty("order")
    private int order;

    @JsonProperty("lng")
    private Double lng;

    @JsonProperty("lat")
    private Double lat;

    @JsonProperty("is_point")
    private int isPoint;

    @JsonProperty("is_business")
    private int isBusiness;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("station", station)
                .append("time", time)
                .append("order", order)
                .append("lng", lng)
                .append("lat", lat)
                .append("isPoint", isPoint)
                .append("isBusiness", isBusiness)
                .toString();
    }
}
