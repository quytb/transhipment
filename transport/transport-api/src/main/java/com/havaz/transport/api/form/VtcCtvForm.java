package com.havaz.transport.api.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class VtcCtvForm {

    private Integer vtcCtvId;

    @NotNull(message = "Vui lòng điền vùng")
    private Integer vtcId;

    @NotNull(message = "Vui lòng điền cộng tác viên")
    private Integer ctvId;

    private Integer status;

    private String note;

    private Integer createdBy;

    @NotNull(message = "Vui lòng điền kinh độ")
    private Double coordinateLat;

    @NotNull(message = "Vui lòng điền vĩ độ")
    private Double coordinateLong;

    @NotNull(message = "Vui lòng điền Xe")
    private Integer xeId;
}
