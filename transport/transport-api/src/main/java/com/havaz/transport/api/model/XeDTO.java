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
public class XeDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Integer xeId;
    private String xeBks;
}
