package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrangThaiLenhDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int trangThaiId;
    private String trangThaiText;
}
