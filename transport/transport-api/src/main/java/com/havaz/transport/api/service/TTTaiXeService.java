package com.havaz.transport.api.service;

import com.havaz.transport.api.form.TTTaiXeForm;
import com.havaz.transport.dao.entity.TTTaiXe;

import java.time.LocalDate;

public interface TTTaiXeService {
    TTTaiXe getTTTaiXeByTaiXeId(int taiXeId);

    TTTaiXe getTTTaiXeByTaiXeIdForDieuHanh(int taiXeId, LocalDate date);

    boolean updateTrangThaiTaiXe(TTTaiXeForm ttTaiXeForm);

    boolean checkTTTaiXeSanSang(int taiXe);
}
