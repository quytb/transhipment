package com.havaz.transport.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "modules_lv2")
public class ModuleLv2Entity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "mod_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "mod_name")
    private String name;

    @Column(name = "mod_folder")
    private String folder;

    @Column(name = "mod_project_id")
    private Integer projectId;

    @Column(name = "mod_order")
    private Integer order;

    @Column(name = "mod_load")
    private Integer load;

    @Column(name = "mod_path")
    private String path;

    @Column(name = "mod_css")
    private String css;

    @Column(name = "mod_parent_id")
    private Integer parentId;

    @Column(name = "mod_group_id")
    private Integer groupId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "module")
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<AdminLv2RoleEntity> admRoles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "module")
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<AdminLv2GroupRoleEntity> admGroupRoles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "admin_lv2_group_role",
               joinColumns = @JoinColumn(name = "agr_module_id", referencedColumnName = "mod_id",
                                         insertable = false, updatable = false),
               inverseJoinColumns = @JoinColumn(name = "agr_group_id", referencedColumnName = "adg_id",
                                                insertable = false, updatable = false))
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<AdminLv2GroupEntity> groups = new HashSet<>();

}
