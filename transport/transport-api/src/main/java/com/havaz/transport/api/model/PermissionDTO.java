package com.havaz.transport.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.havaz.transport.core.constant.PermissionType;
import com.havaz.transport.core.constant.UserPermission;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("code")
    private UserPermission code;

    @JsonProperty("enabled")
    private Boolean enabled;

    @JsonProperty("description")
    private String description;

    @JsonProperty("type")
    private PermissionType type;

}
