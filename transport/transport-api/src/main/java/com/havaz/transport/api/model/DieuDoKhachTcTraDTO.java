package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class DieuDoKhachTcTraDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Integer> bvvIds;
    private String tenTuyen;
    private String gioXuatBen;
    private String tenKhach;
    private String sdtKhach;
    private String diemTraKhach;
    private int soKhach;
    private String taiXeTra;
    private int thuTuTra;
    private int trangThaiTra;
    private String lyDo;
    private String bvvGhiChu;
    private String vungTCTra;
    private Integer hubDiemTraId;
    private String hubDiemTra;
    private Integer didId;
    private Integer timeToHubTra;
    private String gioDieuHanh;
    private String rank;
    private String icon;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("bvvIds", bvvIds)
                .append("tenTuyen", tenTuyen)
                .append("gioXuatBen", gioXuatBen)
                .append("tenKhach", tenKhach)
                .append("sdtKhach", sdtKhach)
                .append("diemTraKhach", diemTraKhach)
                .append("soKhach", soKhach)
                .append("taiXeTra", taiXeTra)
                .append("thuTuTra", thuTuTra)
                .append("trangThaiTra", trangThaiTra)
                .append("lyDo", lyDo)
                .append("bvvGhiChu", bvvGhiChu)
                .append("vungTCTra", vungTCTra)
                .append("hubDiemTraId", hubDiemTraId)
                .append("hubDiemTra", hubDiemTra)
                .append("didId", didId)
                .append("timeToHubTra", timeToHubTra)
                .append("gioDieuHanh", gioDieuHanh)
                .toString();
    }
}
