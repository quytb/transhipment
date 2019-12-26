package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TuyenDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int tuyId;
    private String tenTuyen;
}
