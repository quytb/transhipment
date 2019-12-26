package com.havaz.transport.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "ben_xe")
public class BenXeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "bex_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "bex_ten")
    private String ten;

    @Column(name = "bex_long")
    private Double benxeLong;

    @Column(name = "bex_lat")
    private Double benxeLat;

    @Column(name = "bex_diem_don_tra_khach", columnDefinition = "TINYINT")
    private Integer diemDonTraKhach;

    @Column(name = "bex_kinh_doanh", columnDefinition = "TINYINT")
    private Integer kinhdoanh;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("ten", ten)
                .append("benxeLong", benxeLong)
                .append("benxeLat", benxeLat)
                .append("diemDonTraKhach", diemDonTraKhach)
                .append("kinhdoanh", kinhdoanh)
                .toString();
    }
}
