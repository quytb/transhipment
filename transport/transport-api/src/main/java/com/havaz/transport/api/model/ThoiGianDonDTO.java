package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ThoiGianDonDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int thoiGianDon;
    private List<Integer> listBvvId = new ArrayList<>();
}
