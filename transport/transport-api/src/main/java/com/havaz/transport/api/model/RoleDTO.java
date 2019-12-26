package com.havaz.transport.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.havaz.transport.core.constant.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RoleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private UserRole name;

    @JsonProperty("description")
    private String description;
}
