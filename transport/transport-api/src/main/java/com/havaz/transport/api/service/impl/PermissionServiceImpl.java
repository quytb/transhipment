package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.exception.ResourceNotfoundException;
import com.havaz.transport.api.model.PermissionDTO;
import com.havaz.transport.api.model.RoleDTO;
import com.havaz.transport.api.model.UserPermissionDTO;
import com.havaz.transport.api.service.PermissionService;
import com.havaz.transport.core.constant.UserRole;
import com.havaz.transport.dao.entity.AdminLv2UserEntity;
import com.havaz.transport.dao.entity.TcPermissionEntity;
import com.havaz.transport.dao.entity.TcRoleEntity;
import com.havaz.transport.dao.entity.TcRolePermissionEntity;
import com.havaz.transport.dao.entity.TcUserPermissionEntity;
import com.havaz.transport.dao.entity.TcUserRoleEntity;
import com.havaz.transport.dao.repository.AdminLv2UserRepository;
import com.havaz.transport.dao.repository.TcPermissionRepository;
import com.havaz.transport.dao.repository.TcRolePermissionRepository;
import com.havaz.transport.dao.repository.TcRoleRepository;
import com.havaz.transport.dao.repository.TcUserPermissionRepository;
import com.havaz.transport.dao.repository.TcUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private TcPermissionRepository tcPermissionRepository;

    @Autowired
    private TcRoleRepository tcRoleRepository;

    @Autowired
    private TcRolePermissionRepository tcRolePermissionRepository;

    @Autowired
    private TcUserPermissionRepository tcUserPermissionRepository;

    @Autowired
    private TcUserRoleRepository tcUserRoleRepository;

    @Autowired
    private AdminLv2UserRepository adminLv2UserRepository;

    @Override
    public List<PermissionDTO> getPermissions() {
        List<TcPermissionEntity> all = tcPermissionRepository.findAllByEnabled(true);
        return all.stream().map(this::permissionEntityToDto).collect(Collectors.toList());
    }

    @Override
    public List<PermissionDTO> getPermissionsByRoleId(int roleId) {
        tcRoleRepository.findById(roleId).orElseThrow(
                () -> new ResourceNotfoundException("role is not found: " + roleId));

        List<TcPermissionEntity> all = tcPermissionRepository.findAllByRoleId(roleId);
        return all.stream().map(this::permissionEntityToDto).collect(Collectors.toList());
    }

    @Override
    public List<RoleDTO> getRoles() {
        List<TcRoleEntity> all = tcRoleRepository.findAll();
        return all.stream().map(this::roleEntityToDto).collect(Collectors.toList());
    }

    @Override
    public List<RoleDTO> getRolesByUserId(int userId) {
        adminLv2UserRepository.findById(userId).orElseThrow(
                () -> new ResourceNotfoundException("user is not found: " + userId));

        List<TcRoleEntity> all = tcRoleRepository.findAllByUserId(userId);
        return all.stream().map(this::roleEntityToDto).collect(Collectors.toList());
    }

    @Override
    public List<PermissionDTO> getPermissionsByUserId(int userId) {
        adminLv2UserRepository.findById(userId).orElseThrow(
                () -> new ResourceNotfoundException("user is not found: " + userId));

        List<TcPermissionEntity> all = tcPermissionRepository.findAllByUserId(userId);
        return all.stream().map(this::permissionEntityToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void savePermissionsToRole(Integer roleId, List<PermissionDTO> permissions) {
        tcRoleRepository.findById(roleId).orElseThrow(
                () -> new ResourceNotfoundException("role is not found: " + roleId));

        List<Integer> permissionIds = permissions.stream().map(PermissionDTO::getId)
                .collect(Collectors.toList());
        for (Integer permissionId : permissionIds) {
            Optional<TcRolePermissionEntity> tcRolePermissionOpt =
                    tcRolePermissionRepository.findByRoleIdAndPermissionId(roleId, permissionId);
            TcRolePermissionEntity entity = tcRolePermissionOpt.orElseGet(TcRolePermissionEntity::new);
            entity.setRoleId(roleId);
            entity.setPermissionId(permissionId);
            tcRolePermissionRepository.save(entity);
        }
    }

    @Override
    @Transactional
    public void removePermissionsFromRole(Integer roleId, List<PermissionDTO> permissions) {
        tcRoleRepository.findById(roleId).orElseThrow(
                () -> new ResourceNotfoundException("role is not found: " + roleId));

        List<Integer> permissionIds = permissions.stream().map(PermissionDTO::getId)
                .collect(Collectors.toList());
        for (Integer permissionId : permissionIds) {
            Optional<TcRolePermissionEntity> tcRolePermissionOpt = tcRolePermissionRepository
                    .findByRoleIdAndPermissionId(roleId, permissionId);
            tcRolePermissionOpt.ifPresent(e -> tcRolePermissionRepository.delete(e));
        }
    }

    @Override
    @Transactional
    public void savePermissionsToUser(Integer userId, List<PermissionDTO> permissions) {
        adminLv2UserRepository.findById(userId).orElseThrow(
                () -> new ResourceNotfoundException("user is not found: " + userId));

        List<Integer> permissionIds = permissions.stream().map(PermissionDTO::getId)
                .collect(Collectors.toList());

        List<TcUserPermissionEntity> list = tcUserPermissionRepository.findByUserId(userId);

        tcUserPermissionRepository.deleteAll(list);

        for (Integer permissionId : permissionIds) {
            Optional<TcUserPermissionEntity> tcUserPermissionOpt =
                    tcUserPermissionRepository.findByUserIdAndPermissionId(userId, permissionId);
            TcUserPermissionEntity entity = tcUserPermissionOpt.orElseGet(TcUserPermissionEntity::new);
            entity.setUserId(userId);
            entity.setPermissionId(permissionId);
            tcUserPermissionRepository.save(entity);
        }
    }

    @Override
    @Transactional
    public void removePermissionsFromUser(Integer userId, List<PermissionDTO> permissions) {
        adminLv2UserRepository.findById(userId).orElseThrow(
                () -> new ResourceNotfoundException("user is not found: " + userId));

        List<Integer> permissionIds = permissions.stream().map(PermissionDTO::getId)
                .collect(Collectors.toList());
        for (Integer permissionId : permissionIds) {
            Optional<TcUserPermissionEntity> tcUserPermissionOpt = tcUserPermissionRepository
                    .findByUserIdAndPermissionId(userId, permissionId);
            tcUserPermissionOpt.ifPresent(e -> tcUserPermissionRepository.delete(e));
        }
    }

    @Override
    @Transactional
    public void saveRolesToUser(Integer userId, List<RoleDTO> roles) {
        adminLv2UserRepository.findById(userId).orElseThrow(
                () -> new ResourceNotfoundException("user is not found: " + userId));

        List<Integer> roleIds = roles.stream().map(RoleDTO::getId)
                .collect(Collectors.toList());
        for (Integer roleId : roleIds) {
            Optional<TcUserRoleEntity> tcUserRoleOpt =
                    tcUserRoleRepository.findByUserIdAndRoleId(userId, roleId);
            TcUserRoleEntity entity = tcUserRoleOpt.orElseGet(TcUserRoleEntity::new);
            entity.setUserId(userId);
            entity.setRoleId(roleId);
            tcUserRoleRepository.save(entity);
        }
    }

    @Override
    @Transactional
    public void removeRolesFromUser(Integer userId, List<RoleDTO> roles) {
        adminLv2UserRepository.findById(userId).orElseThrow(
                () -> new ResourceNotfoundException("user is not found: " + userId));

        List<Integer> roleIds = roles.stream().map(RoleDTO::getId)
                .collect(Collectors.toList());
        for (Integer roleId : roleIds) {
            Optional<TcUserRoleEntity> tcUserRoleOpt = tcUserRoleRepository
                    .findByUserIdAndRoleId(userId, roleId);
            tcUserRoleOpt.ifPresent(e -> tcUserRoleRepository.delete(e));
        }
    }

    @Override
    public List<UserPermissionDTO> getUsersIsNotAdmin() {

        List<AdminLv2UserEntity> users = adminLv2UserRepository.findUsersIsNotRole(UserRole.ADMINISTRATOR);

        List<UserPermissionDTO> list = new ArrayList<>(users.size());

        users.forEach(entity -> {
            UserPermissionDTO user = new UserPermissionDTO();
            user.setId(entity.getAdmId());
            user.setName(entity.getAdmName());
            user.setLoginName(entity.getAdmLoginname());
            user.setMaNhanvien(entity.getAdmMa());
            list.add(user);
        });

        return list;
    }

    private PermissionDTO permissionEntityToDto(TcPermissionEntity entity) {
        PermissionDTO p = new PermissionDTO();
        p.setId(entity.getId());
        p.setName(entity.getName());
        p.setCode(entity.getCode());
        p.setEnabled(entity.getEnabled());
        p.setDescription(entity.getDescription());
        p.setType(entity.getType());
        return p;
    }

    private RoleDTO roleEntityToDto(TcRoleEntity entity) {
        RoleDTO r = new RoleDTO();
        r.setId(entity.getId());
        r.setName(entity.getName());
        r.setDescription(entity.getDescription());
        return r;
    }

}
