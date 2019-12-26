package com.havaz.transport.api.model;

import com.havaz.transport.api.form.PagingForm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TimKhachDTO extends PagingForm {

    private static final long serialVersionUID = 1L;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayXuatBen;

    private int khoangThoiGian;
    private int loaiGio;
    private List<Integer> notIds = new ArrayList<>();
    private List<Integer> tuyIds = new ArrayList<>();
    private Integer vungTC;
    private List<Integer> trangThaiTC = new ArrayList<>();
    private Integer hubId;
    private List<Integer> diemKDs = new ArrayList<>();
    private List<Integer> bvvIds = new ArrayList<>();
}
