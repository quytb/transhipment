package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class NgayCongDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDate ngay;
    private double cong;
}
