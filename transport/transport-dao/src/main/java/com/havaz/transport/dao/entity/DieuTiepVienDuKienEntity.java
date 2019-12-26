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

@Getter
@Setter
@Entity
@Table(name = "dieu_tiep_vien_du_kien")
public class DieuTiepVienDuKienEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "dtv_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dtvId;

    @Column(name = "dtv_tiep_vien_id")
    private Integer dtvTiepVienId;

    @Column(name = "dtv_did_id")
    private Integer dtvDidId;

    @Column(name = "dtv_not_id")
    private Integer dtvNotId;

    @Column(name = "dtv_number")
    private Integer dtvNumber;

    @Column(name = "dtv_time")
    private Integer dtvTime;

    @Column(name = "dtv_dao_tao", columnDefinition = "TINYINT")
    private Integer dtvDaoTao;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("dtvId", dtvId)
                .append("dtvTiepVienId", dtvTiepVienId)
                .append("dtvDidId", dtvDidId)
                .append("dtvNotId", dtvNotId)
                .append("dtvNumber", dtvNumber)
                .append("dtvTime", dtvTime)
                .append("dtvDaoTao", dtvDaoTao)
                .toString();
    }
}
