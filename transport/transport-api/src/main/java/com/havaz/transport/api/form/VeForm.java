package com.havaz.transport.api.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class VeForm implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private List<Integer> bvvIds = new ArrayList<>();
    private Integer thuTu;
    private String ghiChu;
}
