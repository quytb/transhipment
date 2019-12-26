package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NowTripDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String tripId;
    private String tuyenId;
    private String tuyenTen;
    private String xeTuyenBKS;
    private String sdt;
    private String gioXuatBen;
    private String chieuDi;
    private String taiXeId;
    private String taiXeTen;
    private Integer notChieuDi;
}
