package com.havaz.transport.api.model;

import com.havaz.transport.api.form.VeForm;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LuuLenhDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int lenhId;
    private int taiXeId;
    private List<VeForm> danhSachVe = new ArrayList<>();
}
