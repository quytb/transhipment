package com.havaz.transport.api.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BaoCaoChamCongDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Integer taiXeId;
    private LocalDate ngayChamCong;
    private String taiXeTen;
    private Double cong;
}
