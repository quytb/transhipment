package com.havaz.transport.api.body.response.tcve;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class GetTcVeDonResponse implements java.io.Serializable {

    private static final long serialVersionUID = -2401242535558637453L;
    private Integer tcVeId;
    private Integer status;
    private String tuyen;
    private String bks;
    private String giodieuhanh;
    private String bvvIdsStr;
    private String versionsStr;
    private String tenHanhkhach;
    private String sdtHanhkhach;
    private String diachiHanhkhach;
    private Integer currentHubId;
    private String currentHub;
    private String rank;
    private String icon;
    private Integer trangthaiDon;
    private Long soluongKhach;
    private List<Integer> bvvIds;
    private List<Integer> versions;
    private String ghichu;
    private LocalDate ngayxuatben;
}
