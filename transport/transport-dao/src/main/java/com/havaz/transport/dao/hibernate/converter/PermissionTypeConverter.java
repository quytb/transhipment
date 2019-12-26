package com.havaz.transport.dao.hibernate.converter;

import com.havaz.transport.core.constant.PermissionType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PermissionTypeConverter implements AttributeConverter<PermissionType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PermissionType permissionType) {
        return (permissionType != null) ? permissionType.getCode() : null;
    }

    @Override
    public PermissionType convertToEntityAttribute(Integer code) {
        return PermissionType.fromCode(code);
    }

}
