package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HuyDonDTO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int lenhId;
    private int taiXeId;
    private List<Integer> listBvvId = new ArrayList<>();
    private String lyDoHuy;
}
