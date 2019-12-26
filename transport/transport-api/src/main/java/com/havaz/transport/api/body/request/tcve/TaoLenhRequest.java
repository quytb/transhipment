package com.havaz.transport.api.body.request.tcve;

import com.havaz.transport.api.form.KhachHubToHubForm;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class TaoLenhRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer txId;
    private Integer xeId;
    private List<KhachHubToHubForm> khachHubToHubForms;
    @NotNull
    private Integer diemgiaokhach;
    //1 don 2 tra
    @NotNull
    private Integer typeCmd;
    private Integer trangthaiLenh; //1 tao lenh : 2 dieu lenh
}
