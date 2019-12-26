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
@Table(name = "tuyen_diem_don_tra_khach")
public class TuyenDiemDonTraEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "tdd_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tddId;

    @Column(name = "tdd_tuyen_id")
    private Integer tddTuyenId;

    @Column(name = "tdd_bex_id")
    private Integer tddBexId;

    @Column(name = "tdd_thoi_gian")
    private Integer tddThoiGian;

    @Column(name = "tdd_order")
    private Integer tddOrder;

    @Column(name = "tdd_is_trung_chuyen", columnDefinition = "TINYINT")
    private Integer tddIsTrungChuyen;

    @Column(name = "tdd_ben_trung_truyen_den")
    private Integer tddBenTrungChuyenDen;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("tddId", tddId)
                .append("tddTuyenId", tddTuyenId)
                .append("tddBexId", tddBexId)
                .append("tddThoiGian", tddThoiGian)
                .append("tddOrder", tddOrder)
                .append("tddIsTrungChuyen", tddIsTrungChuyen)
                .append("tddBenTrungChuyenDen", tddBenTrungChuyenDen)
                .toString();
    }
}
