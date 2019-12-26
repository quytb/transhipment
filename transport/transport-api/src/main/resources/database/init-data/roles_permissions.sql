INSERT INTO tc_permissions (id, name, permission_code, permission_type, description, enabled, created_date, created_by, last_updated_date, last_updated_by) VALUES (1, 'Quản trị hệ thống', 'P90000001', 2, 'P90000001', 1, '1970-01-01 08:00:01', 1, '1970-01-01 08:00:01', 1);
INSERT INTO tc_permissions (id, name, permission_code, permission_type, description, enabled, created_date, created_by, last_updated_date, last_updated_by) VALUES (2, 'Điều hành', 'P10000001', 1, 'P10000001', 1, '1970-01-01 08:00:01', 1, '1970-01-01 08:00:01', 1);
INSERT INTO tc_permissions (id, name, permission_code, permission_type, description, enabled, created_date, created_by, last_updated_date, last_updated_by) VALUES (3, 'Quản lý', 'P20000001', 1, 'P20000001', 1, '1970-01-01 08:00:01', 1, '1970-01-01 08:00:01', 1);
INSERT INTO tc_permissions (id, name, permission_code, permission_type, description, enabled, created_date, created_by, last_updated_date, last_updated_by) VALUES (4, 'Báo cáo', 'P30000001', 1, 'P30000001', 1, '1970-01-01 08:00:01', 1, '1970-01-01 08:00:01', 1);

INSERT INTO tc_roles (id, name, description, created_date, created_by, last_updated_date, last_updated_by) VALUES (1, 'ADMINISTRATOR', 'ADMINISTRATOR', '2019-10-15 13:53:00', 1, '2019-10-15 13:53:02', 1);
INSERT INTO tc_roles (id, name, description, created_date, created_by, last_updated_date, last_updated_by) VALUES (2, 'MODERATOR', 'MODERATOR', '2019-10-15 13:56:39', 1, '2019-10-15 13:56:40', 1);

INSERT INTO tc_roles_permissions (id, role_id, permission_id) VALUES (1, 1, 1);
INSERT INTO tc_roles_permissions (id, role_id, permission_id) VALUES (2, 1, 2);
INSERT INTO tc_roles_permissions (id, role_id, permission_id) VALUES (3, 1, 3);
INSERT INTO tc_roles_permissions (id, role_id, permission_id) VALUES (4, 1, 4);
INSERT INTO tc_roles_permissions (id, role_id, permission_id) VALUES (5, 2, 2);
INSERT INTO tc_roles_permissions (id, role_id, permission_id) VALUES (6, 2, 3);
INSERT INTO tc_roles_permissions (id, role_id, permission_id) VALUES (7, 2, 4);