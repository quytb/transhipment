package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.model.CtvDTO;
import com.havaz.transport.api.service.AdminLv2UserService;
import com.havaz.transport.dao.entity.AdminLv2UserEntity;
import com.havaz.transport.dao.entity.TcRoleEntity;
import com.havaz.transport.dao.repository.AdminLv2UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class AdminLv2UserServiceImpl implements AdminLv2UserService {

    private static final Logger log = LoggerFactory.getLogger(AdminLv2UserServiceImpl.class);

    @Autowired
    private AdminLv2UserRepository adminLv2UserRepository;

    @Override
    public AdminLv2UserEntity getByAdmId(int admId) {
        return adminLv2UserRepository.findById(admId).orElse(null);
    }

    @Override
    public List<CtvDTO> getAllCtv() {
        List<AdminLv2UserEntity> ctvLv2UserEntities = adminLv2UserRepository
                .findAllByCtvOrLxTc(true, Constant.ADM_IS_CTV, Constant.ADM_IS_LXTC);
        List<CtvDTO> result = new ArrayList<>();
        ctvLv2UserEntities.forEach(ctv -> {
            CtvDTO ctvDTO = new CtvDTO();
            ctvDTO.setId(ctv.getAdmId());
            ctvDTO.setName(ctv.getAdmName());
            result.add(ctvDTO);
        });
        return result;
    }

    @Override
    public String findAdminNameById(int admId) {
        String adminName = adminLv2UserRepository.findAdminNameById(admId);
        return !StringUtils.isEmpty(adminName) ? adminName : StringUtils.EMPTY;
    }

    @Override
    public AdminLv2UserEntity findByUsername(String username) {
        AdminLv2UserEntity user = adminLv2UserRepository
                .findTopByAdmLoginnameEquals(username);
        int size;
        if (user != null) {
            // Lazy-loading
            Set<TcRoleEntity> roles = user.getRoles();
            for (TcRoleEntity role : roles) {
                size = role.getPermissions().size();
                if (log.isDebugEnabled()) {
                    log.debug("role permissions size: {}", size);
                }
            }
            size = user.getPermissions().size();
            if (log.isDebugEnabled()) {
                log.debug("user permissions size: {}", size);
            }

            size = user.getGroups().size();
            if (log.isDebugEnabled()) {
                log.debug("user groups size: {}", size);
            }

            user.getGroups().forEach(group -> {
                group.getModules().size();
                group.getAdmRoles().size();
            });

            size = user.getAdmRoles().size();
            if (log.isDebugEnabled()) {
                log.debug("user admRoles size: {}", size);
            }

        }
        return user;
    }
}
