package com.havaz.transport.api.rest;

import com.havaz.transport.api.model.PermissionDTO;
import com.havaz.transport.api.model.RoleDTO;
import com.havaz.transport.api.model.UserPermissionDTO;
import com.havaz.transport.api.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RolePermissionRest {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/admin/permissions")
    public List<PermissionDTO> getPermissions() {
        return permissionService.getPermissions();
    }

    @GetMapping("/admin/roles")
    public List<RoleDTO> getRoles() {
        return permissionService.getRoles();
    }

    @GetMapping("/admin/users")
    public List<UserPermissionDTO> getUsers() {
        return permissionService.getUsersIsNotAdmin();
    }

    @GetMapping("/admin/roles/{roleId}/permissions")
    public ResponseEntity<List<PermissionDTO>> getPermissionsByRole(
            @PathVariable(name = "roleId") Integer roleId) {
        if (roleId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<PermissionDTO> list = permissionService.getPermissionsByRoleId(roleId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/admin/roles/{roleId}/permissions")
    public ResponseEntity<Void> savePermissionsToRole(@PathVariable(name = "roleId") Integer roleId,
                                                      @RequestBody List<PermissionDTO> permissions) {
        if (roleId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        permissionService.savePermissionsToRole(roleId, permissions);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/admin/roles/{roleId}/permissions")
    public ResponseEntity<Void> removePermissionsFromRole(@PathVariable(name = "roleId") Integer roleId,
                                                          @RequestBody List<PermissionDTO> permissions) {
        if (roleId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        permissionService.removePermissionsFromRole(roleId, permissions);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin/users/{userId}/permissions")
    public ResponseEntity<List<PermissionDTO>> getPermissionsByUser(@PathVariable(name = "userId") Integer userId) {
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<PermissionDTO> list = permissionService.getPermissionsByUserId(userId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/admin/users/{userId}/permissions")
    public ResponseEntity<Void> savePermissionsToUser(@PathVariable(name = "userId") Integer userId,
                                                      @RequestBody List<PermissionDTO> permissions) {
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        permissionService.savePermissionsToUser(userId, permissions);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/admin/users/{userId}/permissions")
    public ResponseEntity<Void> removePermissionsFromUser(
            @PathVariable(name = "userId") Integer userId,
            @RequestBody List<PermissionDTO> permissions) {
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        permissionService.removePermissionsFromUser(userId, permissions);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin/users/{userId}/roles")
    public ResponseEntity<List<RoleDTO>> getRolesByUser(@PathVariable(name = "userId") Integer userId) {
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<RoleDTO> list = permissionService.getRolesByUserId(userId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/admin/users/{userId}/roles")
    public ResponseEntity<Void> saveRolesToUser(@PathVariable(name = "userId") Integer userId,
                                                @RequestBody List<RoleDTO> roles) {
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        permissionService.saveRolesToUser(userId, roles);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/admin/users/{userId}/roles")
    public ResponseEntity<Void> removeRolesFromUser(@PathVariable(name = "userId") Integer userId,
                                                    @RequestBody List<RoleDTO> roles) {
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        permissionService.removeRolesFromUser(userId, roles);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
