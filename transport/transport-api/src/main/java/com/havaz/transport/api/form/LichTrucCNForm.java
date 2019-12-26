package com.havaz.transport.api.form;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LichTrucCNForm extends LichTrucForm {

    private static final long serialVersionUID = 1L;

    private List<Integer> chiNhanh;
}
