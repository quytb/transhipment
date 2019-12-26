package com.havaz.transport.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "admin_lv2_group_role")
public class AdminLv2GroupRoleEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "agr_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "agr_group_id")
    private Integer groupId;

    @Column(name = "agr_module_id")
    private Integer moduleId;

    @Column(name = "agr_role_view", columnDefinition = "integer")
    private Boolean roleView;

    @Column(name = "agr_role_add", columnDefinition = "integer")
    private Boolean roleAdd;

    @Column(name = "agr_role_edit", columnDefinition = "integer")
    private Boolean roleEdit;

    @Column(name = "agr_role_delete", columnDefinition = "integer")
    private Boolean roleDelete;

    @Column(name = "agr_role_export", columnDefinition = "integer")
    private Boolean roleExport;

    @Column(name = "agr_role_import", columnDefinition = "integer")
    private Boolean roleImport;

    @ManyToOne
    @JoinColumn(name = "agr_group_id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private AdminLv2GroupEntity group;

    @ManyToOne
    @JoinColumn(name = "agr_module_id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private ModuleLv2Entity module;

}
