package com.havaz.transport.api.model;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class TaoLichDTO implements Validator {
//    private int tcLichId;

    @NotBlank (message = "Phải chọn mã ca")
    private int tcCaId;

    @NotBlank (message = "Phải chọn tài xế")
    private int taiXeId;

    @NotBlank (message = "Phải chọn biển kiểm soát")
    private int xeId;

    @NotNull(message = "Phải nhập vào ngày trực")
    private LocalDate ngayTruc;
    private String ghiChu;

    public TaoLichDTO() {
    }

    public int getTcCaId() {
        return tcCaId;
    }

    public void setTcCaId(int tcCaId) {
        this.tcCaId = tcCaId;
    }

    public int getTaiXeId() {
        return taiXeId;
    }

    public void setTaiXeId(int taiXeId) {
        this.taiXeId = taiXeId;
    }

    public int getXeId() {
        return xeId;
    }

    public void setXeId(int xeId) {
        this.xeId = xeId;
    }

    public LocalDate getNgayTruc() {
        return ngayTruc;
    }

    public void setNgayTruc(LocalDate ngayTruc) {
        this.ngayTruc = ngayTruc;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(TaoLichDTO.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        TaoLichDTO lichTcDTO = (TaoLichDTO) o;

    }
}
