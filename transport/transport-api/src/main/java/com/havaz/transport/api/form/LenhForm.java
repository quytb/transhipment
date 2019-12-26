package com.havaz.transport.api.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.havaz.transport.api.model.VeTrungChuyenDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LenhForm implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Integer lenhId;
    private Integer laiXeId;
    private String laiXeTen;
    private String trangThai;
    private Integer idXe;
    private Integer soKhach;
    private Integer seats;
    private String bks;
    private List<VeTrungChuyenDTO> veTrungChuyenDTOList = new ArrayList<>();
}
