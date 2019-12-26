package com.havaz.transport.api.model;

import com.havaz.transport.api.form.PagingForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaPagingDTO extends PagingForm {

    private static final long serialVersionUID = 1L;

    private String maCa;
    private String tenCa;
    private Float gioBatDau;
    private Float gioKetThuc;
    private String ghiChu;
    private int trangThai;
}
