package com.havaz.transport.api.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
public class TTTaiXeForm {

    private int taiXeId;

    @Max(value = 1,message = "Trạng thái không đúng")
    @Min(value = 0,message = "Trạng thái không đúng")
    private int trangThai;
}
