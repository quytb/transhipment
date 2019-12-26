package com.havaz.transport.api.service;

import com.havaz.transport.api.form.BaoCaoForm;
import com.havaz.transport.api.form.BaoCaoLenhForm;
import com.havaz.transport.api.model.BaoCaoLenhDTO;
import com.havaz.transport.api.model.BaoCaoTaiXeDTO;

import java.util.List;

public interface BaoCaoService {
    List<BaoCaoTaiXeDTO> getAll(BaoCaoForm thang);

    List<BaoCaoLenhDTO> getAll(BaoCaoLenhForm baoCaoLenhForm);
}
