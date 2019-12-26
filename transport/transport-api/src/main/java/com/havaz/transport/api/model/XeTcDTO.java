package com.havaz.transport.api.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class XeTcDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int xeId;
    private String sdtXe;
    private String bks;
    private int seats;
}
