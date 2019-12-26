package com.havaz.transport.dao.hibernate.converter;

import com.havaz.transport.core.constant.Journey;
import com.havaz.transport.core.constant.PermissionType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class JourneyConverter implements AttributeConverter<Journey, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Journey journey) {
        return (journey != null) ? journey.getCode() : null;
    }

    @Override
    public Journey convertToEntityAttribute(Integer code) {
        return Journey.of(code);
    }

}
