package com.havaz.transport.api.form;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TaiXeChiNhanhForm implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Integer> chiNhanh = new ArrayList<>();
}
