package com.havaz.transport.api.body.request.cmdhub;

import com.havaz.transport.api.body.response.tcve.GetTcVeDonResponse;
import com.havaz.transport.api.form.KhachHubToHubForm;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class GetCmdHubRequest implements Serializable {
    private static final long serialVersionUID = -8503969030956536787L;
    @NotNull
    private Integer lenhId;
    @NotNull
    private Integer taixeId;
    @NotNull
    private Integer xeId;
    @NotNull
    private Integer hubId;
    @NotNull
    private List<KhachHubToHubForm> danhsachVe;
}
