package com.havaz.transport.api.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BaoCaoLenhForm implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private List<Integer> chiNhanhId;
    private Integer taiXeId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fromDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
