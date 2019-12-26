package com.havaz.transport.api.model;

import com.havaz.transport.api.form.location.Point;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class XacNhanLenhDTO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Integer lenhId;
    private Integer taixeId;
    private Point toaDoTaiXe;
    private List<VeTcDTO> listVeTc = new ArrayList<>();
}
