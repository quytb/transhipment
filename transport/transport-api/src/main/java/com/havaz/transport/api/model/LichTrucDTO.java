package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class LichTrucDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Integer tcLichId;
    private Integer tcCaId;

    private String maCa;
    private Integer taiXeId;

    private String taiXeTen;

    private Integer xeId;

    private String bks;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayTruc;

    private String ghiChu;

}
