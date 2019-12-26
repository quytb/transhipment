package com.havaz.transport.dao.entity;

import com.havaz.transport.core.constant.PermissionType;
import com.havaz.transport.core.constant.UserPermission;
import com.havaz.transport.dao.hibernate.converter.PermissionTypeConverter;
import com.havaz.transport.dao.hibernate.converter.UserPermissionConverter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tc_permissions")
@EntityListeners(AuditingEntityListener.class)
public class TcPermissionEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Convert(converter = UserPermissionConverter.class)
    @Column(name = "permission_code")
    private UserPermission code;

    @Column(name = "description")
    private String description;

    // default = true
    @Column(name = "enabled")
    private Boolean enabled = true;

    // default = 1
    @Convert(converter = PermissionTypeConverter.class)
    @Column(name = "permission_type")
    private PermissionType type = PermissionType.BASIC;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private Integer createdBy;

    @LastModifiedDate
    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;

    @Column(name = "last_updated_by")
    private Integer lastUpdatedBy;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<AdminLv2UserEntity> users = new HashSet<>();

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<TcRoleEntity> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TcPermissionEntity that = (TcPermissionEntity) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(name, that.name)
                .append(description, that.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(description)
                .toHashCode();
    }
}
