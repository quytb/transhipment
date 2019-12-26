-- tc_roles
create table tc_roles
(
    id                int auto_increment,
    name              varchar(500)                            not null,
    description       varchar(1000)                           null,
    created_date      timestamp default '1970-01-01 08:00:01' not null,
    created_by        int                                     null,
    last_updated_date timestamp default '1970-01-01 08:00:01' not null,
    last_updated_by   int                                     null,
    constraint tc_roles_pk
        primary key (id)
);

create unique index tc_roles_name_uindex
    on tc_roles (name);

-- tc_permissions
create table tc_permissions
(
    id                int auto_increment,
    name              varchar(1000)                            not null,
    permission_code   varchar(50)                              not null,
    permission_type   int        default 1                     not null,
    description       varchar(1000)                            null,
    enabled           tinyint(1) default 1                     not null,
    created_date      timestamp  default '1970-01-01 08:00:01' not null,
    created_by        int                                      null,
    last_updated_date timestamp  default '1970-01-01 08:00:01' not null,
    last_updated_by   int                                      null,
    constraint tc_permissions_pk
        primary key (id)
);

create unique index tc_permissions_permission_code_uindex
    on tc_permissions (permission_code);

-- tc_roles_permissions
create table tc_roles_permissions
(
    id            int auto_increment,
    role_id       int not null comment 'fk tc_roles.id',
    permission_id int not null comment 'fk tc_permissions.id',
    constraint tc_roles_permissions_pk
        primary key (id)
);

create index tc_roles_permissions_role_id_index
    on tc_roles_permissions (role_id);

create index tc_roles_permissions_permission_id_index
    on tc_roles_permissions (permission_id);

create unique index tc_roles_permissions_role_id_permission_id_uindex
    on tc_roles_permissions (role_id, permission_id);

-- tc_users_roles
create table tc_users_roles
(
    id      int auto_increment,
    user_id int not null comment 'fk admin_lv2_user.admin_id',
    role_id int not null comment 'fk tc_roles.id',
    constraint tc_users_roles_pk
        primary key (id)
);

create index tc_users_roles_user_id_index
    on tc_users_roles (user_id);

create index tc_users_roles_role_id_index
    on tc_users_roles (role_id);

create unique index tc_users_roles_user_id_role_id_uindex
    on tc_users_roles (user_id, role_id);

-- tc_users_permissions
create table tc_users_permissions
(
    id            int auto_increment,
    user_id       int not null comment 'fk admin_lv2_user.admin_id',
    permission_id int not null comment 'fk tc_permissions.id',
    constraint tc_users_permissions_pk
        primary key (id)
);

create index tc_users_permissions_user_id_index
    on tc_users_permissions (user_id);

create index tc_users_permissions_permission_id_index
    on tc_users_permissions (permission_id);

create unique index tc_users_permissions_user_id_permission_id_uindex
    on tc_users_permissions (user_id, permission_id);