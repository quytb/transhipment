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

@Entity
@Table(name = "bv_loai_dich_vu")
@Getter
@Setter
public class BvLoaiDichVuEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bvl_id", nullable = false)
    private Integer bvlId;
    @Column(name = "bvl_name", nullable = false)
    private String bvlName;
    @Column(name = "bvl_intro", nullable = false)
    private String bvlIntro;
    @Column(name = "bvl_image")
    private String bvlImage;
    @Column(name = "bvl_color")
    private String bvlColor;
    @Column(name = "bvl_so_do_giuong", nullable = false)
    private Integer bvlSoDoGiuong;
    @Column(name = "bvl_tien_nghi", nullable = false, columnDefinition = "TEXT")
    private String bvlTienNghi;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("bvlId", bvlId)
                .append("bvlName", bvlName)
                .append("bvlIntro", bvlIntro)
                .append("bvlImage", bvlImage)
                .append("bvlColor", bvlColor)
                .append("bvlSoDoGiuong", bvlSoDoGiuong)
                .append("bvlTienNghi", bvlTienNghi)
                .toString();
    }
}
