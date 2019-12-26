package com.havaz.transport.api.form;

import com.havaz.transport.api.common.Constant;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

public class DieuDoForm implements Validator {

    private int taiXeId;
    private int lenhId;
    private String action;
    private List<VeForm> danhSachVe = new ArrayList<>();
    private Boolean isCmdAdditional;

    public Boolean getCmdAdditional() {
        return isCmdAdditional;
    }

    public void setCmdAdditional(Boolean cmdAdditional) {
        isCmdAdditional = cmdAdditional;
    }

    public DieuDoForm() {
    }

    public DieuDoForm(int taiXeId, int lenhId, List<VeForm> danhSachVe) {
        this.taiXeId = taiXeId;
        this.lenhId = lenhId;
        this.danhSachVe = danhSachVe;
    }

    public int getTaiXeId() {
        return taiXeId;
    }

    public void setTaiXeId(int taiXeId) {
        this.taiXeId = taiXeId;
    }

    public int getLenhId() {
        return lenhId;
    }

    public void setLenhId(int lenhId) {
        this.lenhId = lenhId;
    }

    public List<VeForm> getDanhSachVe() {
        return danhSachVe;
    }

    public void setDanhSachVe(List<VeForm> danhSachVe) {
        this.danhSachVe = danhSachVe;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("taiXeId", taiXeId)
                .append("lenhId", lenhId)
                .append("action", action)
                .append("danhSachVe", danhSachVe)
                .append("isCmdAdditional", isCmdAdditional)
                .toString();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return DieuDoForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        DieuDoForm dieuDoForm = (DieuDoForm) o;
        if(Constant.TAO_HOAC_DIEU_LENH_MOI.equals(dieuDoForm.getAction())){
            if(dieuDoForm.getTaiXeId()<=0) {
                errors.reject("taiXeId", "taiXeId.required");
            }
        }else if(Constant.LUU_HOAC_DIEU_LENH_DA_CO.equals(dieuDoForm.getAction())){
            if(dieuDoForm.getTaiXeId()<=0) {
                errors.reject("taiXeId", "taiXeId.required");
            }
            if(dieuDoForm.getLenhId()<=0) {
                errors.reject("lenhId", "lenhId.required");
            }
        }else{
            errors.reject("action", "action.invalid");
        }
        if(dieuDoForm.getDanhSachVe()==null || dieuDoForm.getDanhSachVe().size()<=0){
            errors.reject("danhSachVe", "danhSachVe.required");
        }else{
            for (VeForm veForm : dieuDoForm.getDanhSachVe()) {
                if(veForm.getBvvIds()==null || veForm.getBvvIds().size()<=0){
                    errors.reject("bvvIds", "bvvIds.required");
                }else{
                    for (int bvvId : veForm.getBvvIds()) {
                        if(bvvId<=0){
                            errors.reject("bvvId", "bvvIds.required");
                        }
                    }
                }
            }
        }
    }
}
