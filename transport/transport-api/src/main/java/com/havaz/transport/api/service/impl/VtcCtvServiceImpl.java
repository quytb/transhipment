package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.form.PagingForm;
import com.havaz.transport.api.form.VtcCtvForm;
import com.havaz.transport.api.model.VtcCtvDTO;
import com.havaz.transport.api.repository.TcVtcCtvRepositoryCustom;
import com.havaz.transport.api.service.VtcCtvService;
import com.havaz.transport.api.utils.SecurityUtils;
import com.havaz.transport.dao.entity.TcVtcCtvEntity;
import com.havaz.transport.dao.repository.TcVtcCtvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class VtcCtvServiceImpl implements VtcCtvService {

    @Autowired
    private TcVtcCtvRepositoryCustom tcVtcCtvRepositoryCustom;

    @Autowired
    private TcVtcCtvRepository tcVtcCtvRepository;

    @Override
    public PageCustom<VtcCtvDTO> getList(Integer offset, Integer limit, String ctvName) {
        offset = offset - 1;
        PagingForm pagingForm = new PagingForm();
        pagingForm.setPage(offset);
        pagingForm.setSize(limit);
        return tcVtcCtvRepositoryCustom.getListVtcCtv(pagingForm, ctvName);
    }

    @Override
    @Transactional
    public String createOrUpdate(VtcCtvForm vtcCtvForm) {
        TcVtcCtvEntity tcVtcCtvEntity = new TcVtcCtvEntity();
        if (vtcCtvForm.getVtcCtvId() != null) {
            Optional<TcVtcCtvEntity> entity = tcVtcCtvRepository.findById(vtcCtvForm.getVtcCtvId());
            if (entity.isPresent()) {
                tcVtcCtvEntity = tcVtcCtvRepository.findById(vtcCtvForm.getVtcCtvId()).get();
            } else {
                tcVtcCtvEntity.setCreateDate(LocalDateTime.now());
            }
        } else {
            tcVtcCtvEntity.setCreateDate(LocalDateTime.now());
        }
        tcVtcCtvEntity.setTcCtvId(vtcCtvForm.getCtvId());
        tcVtcCtvEntity.setCreatedBy(SecurityUtils.getCurrentUserLogin());
        tcVtcCtvEntity.setCoordinateLat(vtcCtvForm.getCoordinateLat());
        tcVtcCtvEntity.setCoordinateLong(vtcCtvForm.getCoordinateLong());
        tcVtcCtvEntity.setXeId(vtcCtvForm.getXeId());
        tcVtcCtvEntity.setTcVttId(vtcCtvForm.getVtcId());
        tcVtcCtvEntity.setNote(vtcCtvForm.getNote());
        tcVtcCtvEntity.setStatus(vtcCtvForm.getStatus());
        tcVtcCtvEntity.setLastUpdateDate(LocalDateTime.now());
        tcVtcCtvRepository.save(tcVtcCtvEntity);
        return "Lưu Cộng Tác Viên Trung Chuyển Thành Công";
    }
}
