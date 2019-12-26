package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DanhSachLichDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int taiXeId;
    private List<LichTrucDTO> lichTrucDTOS = new ArrayList<>();
}
