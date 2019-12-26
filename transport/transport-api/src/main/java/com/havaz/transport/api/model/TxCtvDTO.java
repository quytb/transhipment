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
public class TxCtvDTO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private Integer id;
    private Byte isCtv;
}
