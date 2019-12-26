package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NowHubDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String hubId;
    private String hubName;
    private String hubLongitude;
    private String hubLatitude;
}
