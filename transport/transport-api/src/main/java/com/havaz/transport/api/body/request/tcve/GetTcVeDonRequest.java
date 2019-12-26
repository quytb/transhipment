package com.havaz.transport.api.body.request.tcve;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GetTcVeDonRequest implements java.io.Serializable {

    private static final long serialVersionUID = -8417786732174504237L;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayXuatben;

    private Integer currentHub;

    private String sdtHanhkhach;

    private Integer thoigianGiokhoihanh;

    private List<Integer> notIds = new ArrayList<>();

    private List<Integer> tuyenIds = new ArrayList<>();
}
