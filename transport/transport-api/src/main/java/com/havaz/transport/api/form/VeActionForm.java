package com.havaz.transport.api.form;

import com.havaz.transport.core.constant.VeConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

public class VeActionForm implements Validator {
    private List<Integer> bvvIds;
    private int bvvIdFrom;
    private int bvvIdTo;
    private String action;

    public List<Integer> getBvvIds() {
        return bvvIds;
    }

    public void setBvvIds(List<Integer> bvvIds) {
        this.bvvIds = bvvIds;
    }

    public int getBvvIdFrom() {
        return bvvIdFrom;
    }

    public void setBvvIdFrom(int bvvIdFrom) {
        this.bvvIdFrom = bvvIdFrom;
    }

    public int getBvvIdTo() {
        return bvvIdTo;
    }

    public void setBvvIdTo(int bvvIdTo) {
        this.bvvIdTo = bvvIdTo;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return VeActionForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        VeActionForm veActionForm = (VeActionForm) o;
        List<Integer> bvvIds = veActionForm.getBvvIds();
        int bvvIdFrom = veActionForm.getBvvIdFrom();
        int bvvIdTo = veActionForm.getBvvIdTo();
        String action = veActionForm.getAction();

        if (VeConstants.VE_ACTION.containsValue(action)) {
            if (VeConstants.VE_ACTION.get(VeConstants.CHUYEN_CHO).equals(action)) {
                if (bvvIdFrom <= 0) {
                    errors.reject("bvvIdFrom", "bvvIdFrom.required");
                }
                if (bvvIdTo <= 0) {
                    errors.reject("bvvIdTo", "bvvIdTo.required");
                }
            } else {
                if (bvvIds == null || bvvIds.isEmpty()) {
                    errors.reject("bvvIds", "bvvIds.required");
                }
            }
        } else {
            errors.reject("action", "Action not valid.");
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("bvvIds", bvvIds)
                .append("bvvIdFrom", bvvIdFrom)
                .append("bvvIdTo", bvvIdTo)
                .append("action", action)
                .toString();
    }
}
