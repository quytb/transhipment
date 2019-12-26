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
@Table(name = "routing_option_detail")
public class RoutingOptionDetailEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rod_id", nullable = false)
    private Integer rodId;
    @Column(name = "rod_routing_option_id", nullable = false)
    private Integer rodRoutingOptionId;
    @Column(name = "rod_tuyen_id", nullable = false)
    private Integer rodTuyenId;
    @Column(name = "rod_dich_vu_id", nullable = false)
    private Integer rodDichVuId;
    @Column(name = "rod_ben_xe_id", nullable = false)
    private Integer rodBenXeId;
    @Column(name = "rod_time_run", nullable = false)
    private Integer rodTimeRun;
    @Column(name = "rod_active", nullable = false, columnDefinition = "TINYINT")
    private Boolean rodActive;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("rodId", rodId)
                .append("rodRoutingOptionId", rodRoutingOptionId)
                .append("rodTuyenId", rodTuyenId)
                .append("rodDichVuId", rodDichVuId)
                .append("rodBenXeId", rodBenXeId)
                .append("rodTimeRun", rodTimeRun)
                .append("rodActive", rodActive)
                .toString();
    }
}
