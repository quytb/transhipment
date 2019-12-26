package com.havaz.transport.api.model;

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
public class UpdateChamCongDTO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int tcCongId;
    private int taiXeId;
    private String taiXeTen;
    private int caId;
    private String maCa;
    private int xeId;
    private String bks;

    private Double gioThucTe;
    private int khachPhatSinh;
    private String ghiChu;
}
