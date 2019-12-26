package com.havaz.transport.api.form;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PriceGroupSearchForm extends PagingForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String partnerName;
}
