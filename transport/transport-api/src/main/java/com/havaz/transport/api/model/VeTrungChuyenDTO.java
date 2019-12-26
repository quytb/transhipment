package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class VeTrungChuyenDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private List<Integer> listBvvId = new ArrayList<>();
    private String khachHangMoi;
    private String ten;
    private String sdt;
    private String diaChi;
    private String soGhe;
    private Integer soKhach;
    private Integer thuTuDon;
    private Integer thoiGianDon;
    private Integer trangThaiTrungChuyen;
    private String ghiChuVe;
    private String ghiChuVeBvv;
    private String diaChiTra;
    private String lyDoHuy;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("listBvvId", listBvvId)
                .append("khachHangMoi", khachHangMoi)
                .append("ten", ten)
                .append("sdt", sdt)
                .append("diaChi", diaChi)
                .append("soGhe", soGhe)
                .append("soKhach", soKhach)
                .append("thuTuDon", thuTuDon)
                .append("thoiGianDon", thoiGianDon)
                .append("trangThaiTrungChuyen", trangThaiTrungChuyen)
                .append("ghiChuVe", ghiChuVe)
                .append("ghiChuVeBvv", ghiChuVeBvv)
                .toString();
    }
}
