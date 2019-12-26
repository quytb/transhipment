package com.havaz.transport.api.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class VungTrungChuyenForm {
    private int id;
    private String ten;
    private String code;
    private MultipartFile fileContent;
    private int taiXeId;
    private String averageSpeed;
    private int status;//0 là inactive, 1 là active
    private String note;
    private Integer confirmedTime;
}
