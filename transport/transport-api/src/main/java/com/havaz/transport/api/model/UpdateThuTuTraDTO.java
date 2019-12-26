package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UpdateThuTuTraDTO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Integer taiXeId;
    private Integer lenhId;
    private List<UpdateThuTuTraVeTcDTO> listVeTcUpdate = new ArrayList<>();
}
