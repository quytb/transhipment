package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TaoCaDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Phải nhập mã ca")
    private String maCa;

    @NotNull(message = "Phải nhập tên ca")
    private String tenCa;

    @NotNull(message = "Phải chọn giờ bắt đầu ca")
    private Float gioBatDau;

    @NotNull(message = "Phải chọn giờ kết thúc ca")
    private Float gioKetThuc;

    private String ghiChu;

    private Boolean trangThai;
//    private String createdBy;

//
//    public String getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(String createdBy) {
//        this.createdBy = createdBy;
//    }

//    @Override
//    public boolean supports(Class<?> aClass) {
//        return TaoCaDTO.class.isAssignableFrom(aClass);
//    }
//
//    @Override
//    public void validate(Object o, Errors errors) {
//        TaoCaDTO taoCaDTO = (TaoCaDTO) o;
////        LocalTime startTime = LocalTime.parse(taoCaDTO.gioBatDau);
////        LocalTime endTime = LocalTime.parse(taoCaDTO.gioKetThuc);
////        if(taoCaDTO.getGioBatDau()==null){
////            errors.reject("Giờ bắt đầu phải nhỏ hơn giờ kết thúc");
////        }else if(taoCaDTO.getGioKetThuc()==null){
////            errors.reject("Giờ bắt đầu phải nhỏ hơn giờ kết thúc");
////        }else
//        if (taoCaDTO.getGioBatDau() > taoCaDTO.getGioKetThuc()) {
//            errors.reject("Giờ bắt đầu phải nhỏ hơn giờ kết thúc");
//        }
//    }
}
