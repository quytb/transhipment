package com.havaz.transport.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
@Getter
@Setter
@Entity
@Table(name = "routing_option")
public class RoutingOptionEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roo_id", nullable = false)
    private Integer rooId;
    @Column(name = "roo_name", nullable = false)
    private String rooName;
    @Column(name = "roo_admin_update", nullable = false)
    private Integer rooAdminUpdate;
    @Column(name = "roo_time_update", nullable = false)
    private Integer rooTimeUpdate;
    @Column(name = "roo_description", nullable = false, length = 20)
    private String rooDescription;
    @Column(name = "roo_color", nullable = false)
    private String rooColor;
    @Column(name = "roo_active", nullable = false, columnDefinition = "TINYINT")
    private Integer rooActive;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("rooId", rooId)
                .append("rooName", rooName)
                .append("rooAdminUpdate", rooAdminUpdate)
                .append("rooTimeUpdate", rooTimeUpdate)
                .append("rooDescription", rooDescription)
                .append("rooColor", rooColor)
                .append("rooActive", rooActive)
                .toString();
    }
}
