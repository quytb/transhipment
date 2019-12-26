package com.havaz.transport.api.service;

import com.havaz.transport.api.model.CtvDTO;
import com.havaz.transport.dao.entity.AdminLv2UserEntity;

import java.util.List;

public interface AdminLv2UserService {

    AdminLv2UserEntity getByAdmId(int admId);

    String findAdminNameById(int admId);

//    LoginResponse authentication(AuthenticationForm authenticationForm);

    List<CtvDTO> getAllCtv();

    AdminLv2UserEntity findByUsername(String username);
}
