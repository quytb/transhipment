package com.havaz.transport.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CanhBaoDTO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer veId;
    private Integer tripId;
    private Integer kieuCanhBao;
    private Integer lenhId;
    private Integer thoiGianDon;
    private String kieuCanhBaoText;
    private String cung;
    private Integer tuyenId;
    private String tuyen;
    private String gioXuatBen;
    private String tenKhach;
    private String sdtKhach;
    private String diaChiDon;
    private String vungTrungChuyen;
    private Integer trangThaiDon;
    private String hub;
    private Integer taiXeId;
    private Integer taiXeXeTCId;
    private String nguoiDon;

    @JsonIgnore
    private Double lat;

    @JsonIgnore
    private Double lng;

    @JsonIgnore
    private Double hubLat;

    @JsonIgnore
    private Double hubLng;
}
