package com.havaz.transport.api.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CmdHubToHubForm implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Integer> versions;
    @NotNull
    private List<Integer> bvvIds;

    @NotNull
    private Integer status;
    private Integer typeCmd;
    private Integer createdBy;
    private Integer diemGiaoKhach;

}
