package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TcDspVungTrungChuyenDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Integer tcVttId;
    private String tcVttName;
    private String tcVttCode;
    private String tcVttContent;
    private String createdDate;
    private String createdBy;// Name of user
    private String tcVAverageSpeed;
    private int status;
}
