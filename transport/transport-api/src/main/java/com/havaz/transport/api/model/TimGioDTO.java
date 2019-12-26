package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TimGioDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int khoangThoiGian;
    private List<Integer> didIds = new ArrayList<>();
    private String loaiGio;
}
