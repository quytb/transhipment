package com.havaz.transport.api.service;

import com.havaz.transport.api.form.VeForm;
import com.havaz.transport.dao.entity.TcLenhEntity;

public interface TcTripDetailService {
    void saveTcTripDetail(TcLenhEntity tcLenhEntity, VeForm veForm, int type);
}
