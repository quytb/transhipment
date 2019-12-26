package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaLenhDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Integer lenhId;
    private Integer kieuLenh;
    private String maLenh;
}
