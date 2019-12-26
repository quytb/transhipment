package com.havaz.transport.api.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LenhContainGXBDTO {
    private static final long serialVersionUID = 1L;

    private Integer tcLenhId;

    private Integer trangThai;

    private Integer kieuLenh;

    private Integer xeId;

    private LocalDateTime createdDate;

    private String gioXuatBen;
}
