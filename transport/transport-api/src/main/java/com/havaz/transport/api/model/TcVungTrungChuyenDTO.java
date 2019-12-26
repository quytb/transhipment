package com.havaz.transport.api.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TcVungTrungChuyenDTO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int tcVttId;
    private String tcVttName;
    private String tcVttCode;
    private String tcVttContent;
    private int createdBy;
    private LocalDateTime createdDate;
    private int lastUpdatedBy;
    private LocalDateTime lastUpdatedDate;
    private Integer tcVttLocale;
    private String tcVttNote;
    private Double tcVAverageSpeed;
    private int status;
    private Double tcCentroidLat;
    private Double tcCentroidLong;
    private Integer tcConfirmedTime;
}
