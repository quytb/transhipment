package com.havaz.transport.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChiTietLenhDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Integer tcLenhId;
    private Integer laiXeId;
    private String taiXeTen;
    private String trangThaiText;
    private List<VeTrungChuyenDTO> veTrungChuyenDTOList = new ArrayList<>();
    private String maLenh;
    private int kieuLenh;
    private String kieuLenhText;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdDate;

    private int trangThai;
    private int tongKhach;
    private Long khachDon;
    private Long khachTra;
    private String tuyen;
    private String nguoiTao;
    private String bks;
    private String nguoiHuy;
    private Long khachHuy;
    private String lyDoHuy;
    private Integer diemgiaokhach;
    private String diemgiaokhachStr;
    private Long slKhach;
    private String diemTCDen;
    private Long diemTCDenId;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("tcLenhId", tcLenhId)
                .append("laiXeId", laiXeId)
                .append("taiXeTen", taiXeTen)
                .append("trangThaiText", trangThaiText)
                .append("maLenh", maLenh)
                .append("kieuLenh", kieuLenh)
                .append("kieuLenhText", kieuLenhText)
                .append("createdDate", createdDate)
                .append("trangThai", trangThai)
                .append("tongKhach", tongKhach)
                .append("khachDon", khachDon)
                .append("khachTra", khachTra)
                .append("tuyen", tuyen)
                .append("nguoiTao", nguoiTao)
                .append("bks", bks)
                .toString();
    }
}
