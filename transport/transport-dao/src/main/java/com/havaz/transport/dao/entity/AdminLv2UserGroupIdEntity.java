package com.havaz.transport.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "admin_lv2_user_group_id")
public class AdminLv2UserGroupIdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "admg_admin_id")
    private Integer admgAdminId;

    @Id
    @Column(name = "admg_group_id")
    private Integer admgGroupId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AdminLv2UserGroupIdEntity that = (AdminLv2UserGroupIdEntity) o;

        return new EqualsBuilder()
                .append(admgAdminId, that.admgAdminId)
                .append(admgGroupId, that.admgGroupId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(admgAdminId)
                .append(admgGroupId)
                .toHashCode();
    }
}
