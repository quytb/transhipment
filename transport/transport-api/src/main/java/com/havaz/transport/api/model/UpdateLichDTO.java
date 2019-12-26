package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateLichDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Integer tcLichId;
    private Integer taiXeId;
    private Integer tcCaId;
    private Integer xeId;
    private LocalDate ngayTruc;
    private String ghiChu;

}
