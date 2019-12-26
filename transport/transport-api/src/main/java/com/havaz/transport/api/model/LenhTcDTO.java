package com.havaz.transport.api.model;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LenhTcDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int tcLenhId;
    private LocalDateTime lastUpdatedDate;
    private int trangThai;
}
