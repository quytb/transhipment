package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VtcCtvDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String tenVung;
    private String codeVung;
    private String tenCtv;
    private Integer status;
    private String note;
    private LocalDateTime createdDate;
    private String creatorName;
    private Integer vungId;
    private Integer ctvId;
    private Integer xeId;
    private String xeBks;
    private Double coordinateLat;
    private Double coordinateLong;
}
