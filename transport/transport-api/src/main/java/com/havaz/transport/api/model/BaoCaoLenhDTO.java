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
public class BaoCaoLenhDTO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer taiXeId;
    private String taiXeTen;
    private String maNV;
    private String noiLamViec;
    private long tongLenhDon;
    private long tongLenhTra;
    private long tongKhachDon;
    private long tongKhachTra;
    private long tongLenhHuy;
    private long tongLenhThanhCong;
    private long tongsoKhach;
}
