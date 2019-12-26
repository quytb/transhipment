package com.havaz.transport.api.service;

import com.havaz.transport.api.model.PermissionDTO;
import com.havaz.transport.api.model.RoleDTO;
import com.havaz.transport.api.model.UserPermissionDTO;

import java.util.List;

public interface PermissionService {
    List<PermissionDTO> getPermissions();

    List<PermissionDTO> getPermissionsByRoleId(int roleId);

    List<RoleDTO> getRoles();

    List<RoleDTO> getRolesByUserId(int userId);

    List<PermissionDTO> getPermissionsByUserId(int userId);

    void savePermissionsToRole(Integer roleId, List<PermissionDTO> permissions);

    void removePermissionsFromRole(Integer roleId, List<PermissionDTO> permissions);

    void savePermissionsToUser(Integer userId, List<PermissionDTO> permissions);

    void removePermissionsFromUser(Integer userId, List<PermissionDTO> permissions);

    void saveRolesToUser(Integer userId, List<RoleDTO> roles);

    void removeRolesFromUser(Integer userId, List<RoleDTO> roles);

    List<UserPermissionDTO> getUsersIsNotAdmin();
}
