package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KhachNowDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String khSdt;
    private String khTimeDon;
    private String khTimeToHub;
    private String khTrangThaiTc;
    private String khLongitude;
    private String khLatitude;
    private Double duration;
    private Integer hubTuyen;
    private Double latHub;
    private Double longHub;
    private String tenVtc;
    private String tenKh;
    private String bbvId;
    private String hubName;
}
