package com.havaz.transport.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotActiveDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("notId")
    private Integer notId;

    @JsonProperty("MixData")
    private String tripData;
}
