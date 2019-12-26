package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class VeTcDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private List<Integer> veId = new ArrayList<>();
    private int thuTuDon;
    private int thoiGianDon;
    private int trangThaiTrungChuyen;
}
