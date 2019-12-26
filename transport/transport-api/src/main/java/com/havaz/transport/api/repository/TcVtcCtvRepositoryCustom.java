package com.havaz.transport.api.repository;

import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.form.PagingForm;
import com.havaz.transport.api.model.VtcCtvDTO;

public interface TcVtcCtvRepositoryCustom {
    PageCustom<VtcCtvDTO> getListVtcCtv(PagingForm pagingForm, String ctvName);
}

