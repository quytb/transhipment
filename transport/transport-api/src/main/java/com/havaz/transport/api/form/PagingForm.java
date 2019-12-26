package com.havaz.transport.api.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingForm implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String sortType;
    private String sortBy;
    private int page;
    private int size;
}
