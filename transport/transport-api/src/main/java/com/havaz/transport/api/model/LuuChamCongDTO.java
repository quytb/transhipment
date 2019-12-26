package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LuuChamCongDTO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDate ngayChamCong;
    private List<UpdateChamCongDTO> updateChamCongDTOS = new ArrayList<>();
}
