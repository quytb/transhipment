package com.havaz.transport.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPermissionDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("login_name")
    private String loginName;

    @JsonProperty("ma_nhan_vien")
    private String maNhanvien;
}
