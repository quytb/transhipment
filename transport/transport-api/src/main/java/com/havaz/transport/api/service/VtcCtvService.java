package com.havaz.transport.api.service;

import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.form.VtcCtvForm;
import com.havaz.transport.api.model.VtcCtvDTO;

public interface VtcCtvService {
    PageCustom<VtcCtvDTO> getList(Integer offset, Integer limit, String ctvName);

    String createOrUpdate(VtcCtvForm vtcCtvForm);
}
