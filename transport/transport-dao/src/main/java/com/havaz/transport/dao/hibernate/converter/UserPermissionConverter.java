package com.havaz.transport.dao.hibernate.converter;

import com.havaz.transport.core.constant.UserPermission;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UserPermissionConverter implements AttributeConverter<UserPermission, String> {

    @Override
    public String convertToDatabaseColumn(UserPermission userPermission) {
        return (userPermission != null) ? userPermission.getCode() : null;
    }

    @Override
    public UserPermission convertToEntityAttribute(String code) {
        return UserPermission.fromCode(code);
    }

}