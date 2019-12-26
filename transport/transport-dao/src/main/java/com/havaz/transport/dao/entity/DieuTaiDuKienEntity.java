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
@Table(name = "dieu_tai_du_kien")
public class DieuTaiDuKienEntity implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "dit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ditId;

    @Column(name = "dit_tai_id")
    private Integer ditTaiId;

    @Column(name = "dit_did_id")
    private Integer ditDidId;

    @Column(name = "dit_not_id")
    private Integer ditNotId;

    @Column(name = "dit_number")
    private Integer ditNumber;

    @Column(name = "dit_time")
    private Integer ditTime;

    @Column(name = "dit_dao_tao")
    private Integer ditDaoTao;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("ditId", ditId)
                .append("ditTaiId", ditTaiId)
                .append("ditDidId", ditDidId)
                .append("ditNotId", ditNotId)
                .append("ditNumber", ditNumber)
                .append("ditTime", ditTime)
                .append("ditDaoTao", ditDaoTao)
                .toString();
    }
}
