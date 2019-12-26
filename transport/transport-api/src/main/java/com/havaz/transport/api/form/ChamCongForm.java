package com.havaz.transport.api.form;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ChamCongForm {
    private LocalDate ngayChamCong;
    private List<Integer> chiNhanh;
}
