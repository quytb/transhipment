package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistanceDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String distance;
    private String time;
}
