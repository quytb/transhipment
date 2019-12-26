package com.havaz.transport.api.model;

import com.havaz.transport.core.constant.Journey;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ThongTinNodeDto implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    //Format: "yyyy-mm-dd"
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayXuatBen;

    private List<Integer> listTuyen = new ArrayList<>();
    private Journey chieuXeChay;
    private Integer truocGioKhoihanh;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("ngayXuatBen", ngayXuatBen)
                .append("listTuyen", listTuyen)
                .append("chieuXeChay", chieuXeChay)
                .append("truocGioKhoihanh", truocGioKhoihanh)
                .toString();
    }
}
