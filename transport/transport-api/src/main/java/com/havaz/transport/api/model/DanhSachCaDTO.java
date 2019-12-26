package com.havaz.transport.api.model;

import lombok.Data;

@Data
public class DanhSachCaDTO {
    private int tcCaId;
    private String maCa;
    private String tenCa;
    private Float gioBatDau;
    private Float gioKetThuc;
    private String ghiChu;
    private Boolean trangThai;
}
