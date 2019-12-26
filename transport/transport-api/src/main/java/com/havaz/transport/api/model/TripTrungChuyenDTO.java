package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TripTrungChuyenDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String sdtTc;
    private List<XeTuyenDTO> listXeTuyen = new ArrayList<>();
}
