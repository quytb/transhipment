package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ThoiGianTraDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Integer thoiGianTra;
    private List<Integer> listBvvId = new ArrayList<>();
}
