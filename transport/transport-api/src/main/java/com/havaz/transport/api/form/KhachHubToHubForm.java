package com.havaz.transport.api.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class KhachHubToHubForm  implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull
    private List<Integer> versions;
    @NotNull
    private List<Integer> bvvIds;
    //1 = len xe 2 = da dieu
    private Integer status;
}
