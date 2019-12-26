package com.havaz.transport.api.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

public class PriceGroupForm implements Serializable {
    private Integer id;
    @NotEmpty(message = "Vui Lòng Tên Đối tác")
    private String partnerName;
    private Integer partnerId;
    @PositiveOrZero(message = "Vui Lòng nhập khoảng giảm giá lớn hơn hoặc bằng 0 ")
    private Double discountRange;
    @NotNull(message = "Vui lòng nhập loại giá")
    private Integer priceId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public Double getDiscountRange() {
        return discountRange;
    }

    public void setDiscountRange(Double discountRange) {
        this.discountRange = discountRange;
    }

    public Integer getPriceId() {
        return priceId;
    }

    public void setPriceId(Integer priceId) {
        this.priceId = priceId;
    }
}
