package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.body.request.tcve.TaoLenhRequest;
import com.havaz.transport.api.body.response.benxe.GetBenXeReponse;
import com.havaz.transport.api.common.LenhConstants;
import com.havaz.transport.api.common.StatusCmdHub;
import com.havaz.transport.api.common.StatusTicket;
import com.havaz.transport.api.form.CmdHubToHubForm;
import com.havaz.transport.api.model.ChiTietLenhDTO;
import com.havaz.transport.api.model.NotActiveDTO;
import com.havaz.transport.api.model.ThongTinNodeDto;
import com.havaz.transport.api.repository.DieuDoTempRepositoryCustom;
import com.havaz.transport.api.repository.TcLenhRepositoryCustom;
import com.havaz.transport.api.service.TrungChuyenHubToHubService;
import com.havaz.transport.core.constant.Journey;
import com.havaz.transport.dao.entity.BenXeEntity;
import com.havaz.transport.dao.entity.TcLenhEntity;
import com.havaz.transport.dao.entity.TcVeEntity;
import com.havaz.transport.dao.query.SelectOptions;
import com.havaz.transport.dao.repository.AbstractRepository;
import com.havaz.transport.dao.repository.BenXeEntityRepository;
import com.havaz.transport.dao.repository.TcLenhRepository;
import com.havaz.transport.dao.repository.TcVeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class TrungChuyenHubToHubServiceImpl extends AbstractRepository implements TrungChuyenHubToHubService {

    private static final int CREATED = 5;
    @Autowired
    private TcLenhRepository tcLenhRepository;

    @Autowired
    private TcLenhRepositoryCustom tcLenhRepositoryCustom;

    @Autowired
    private TcVeRepository tcVeRepository;

    @Autowired
    private BenXeEntityRepository benXeEntityRepository;

    @Autowired
    private DieuDoTempRepositoryCustom dieuDoTempRepositoryCustom;


    @Override
    @Transactional
    public TcLenhEntity createdCmdHubToHub(CmdHubToHubForm form) {
        TcLenhEntity tcLenhEntity = new TcLenhEntity();
        //Create Driver Temp
        tcLenhEntity.setLaiXeId(NumberUtils.INTEGER_ZERO);
        tcLenhEntity.setKieuLenh(form.getTypeCmd());
        tcLenhEntity.setCreatedBy(form.getCreatedBy());
        tcLenhEntity.setLastUpdatedBy(form.getCreatedBy());
        tcLenhEntity.setDiemgiaokhach(form.getDiemGiaoKhach());
        tcLenhEntity.setTrangThai(CREATED);
        tcLenhRepository.save(tcLenhEntity);
        saveTicketToCmdTemp(form, tcLenhEntity);
        return tcLenhEntity;
    }

    @Override
    @Transactional
    public void updateClientToCmd(CmdHubToHubForm form, Integer cmdId) {
        Optional<TcLenhEntity> optional = tcLenhRepository.findById(cmdId);
        if (optional.isPresent()) {
            TcLenhEntity tcLenhEntity = optional.get();
            saveTicketToCmdTemp(form, tcLenhEntity);
        }
    }

    @Override
    public List<GetBenXeReponse> findAllBenxe() {
        List<BenXeEntity> allHub = benXeEntityRepository.getAllForHub();
        List<GetBenXeReponse> benXeReponses = new ArrayList<>();
        allHub.forEach(hub -> {
            GetBenXeReponse getBenXeReponse = new GetBenXeReponse();
            BeanUtils.copyProperties(hub, getBenXeReponse);
            benXeReponses.add(getBenXeReponse);
        });
        return benXeReponses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotActiveDTO> getNotActiveChieuA(Integer gioxuatben) {
        ThongTinNodeDto thongTinNodeDto = new ThongTinNodeDto();
        thongTinNodeDto.setTruocGioKhoihanh(gioxuatben);
        thongTinNodeDto.setChieuXeChay(Journey.OUTWARD);
        thongTinNodeDto.setNgayXuatBen(LocalDate.now());
        return dieuDoTempRepositoryCustom.getListNotActive(thongTinNodeDto);
    }

    @Transactional
    public void creatCmdHubToHubForWeb(TaoLenhRequest taoLenhRequest) {
        Session session = getCurrentSession();
        TcLenhEntity tcLenhEntity = new TcLenhEntity();
        tcLenhEntity.setLaiXeId(NumberUtils.INTEGER_ZERO);
        tcLenhEntity.setKieuLenh(taoLenhRequest.getTypeCmd());
        tcLenhEntity.setDiemgiaokhach(taoLenhRequest.getDiemgiaokhach());
        if(Objects.nonNull(taoLenhRequest.getTxId())) {
            tcLenhEntity.setLaiXeId(taoLenhRequest.getTxId());
        }
        if (Objects.nonNull(taoLenhRequest.getXeId())) {
            tcLenhEntity.setXeId(taoLenhRequest.getXeId());
        }
        if (Objects.nonNull(taoLenhRequest.getTxId()) && Objects.nonNull(taoLenhRequest.getXeId())
                && Objects.equals(taoLenhRequest.getTrangthaiLenh(), LenhConstants.LENH_STATUS_DA_DIEU)) {
            tcLenhEntity.setTrangThai(taoLenhRequest.getTrangthaiLenh());
        } else {
            tcLenhEntity.setTrangThai(CREATED);
        }
        tcLenhRepository.save(tcLenhEntity);
        taoLenhRequest.getKhachHubToHubForms().forEach(khachHubToHubForm -> {
            AtomicInteger atomicInteger = new AtomicInteger(0);
            List<TcVeEntity> tcVeEntities = tcVeRepository.findByBvvIds(khachHubToHubForm.getBvvIds());
            if (Objects.equals(khachHubToHubForm.getStatus(), StatusCmdHub.DA_DON.getValue())) {
                tcVeEntities.forEach(tcVeEntity -> {
                    session.detach(tcVeEntity);
                    tcVeEntity.setVersion(khachHubToHubForm.getVersions().get(atomicInteger.getAndIncrement()));
                    tcVeEntity.setTcLenhId(tcLenhEntity.getTcLenhId());
                    tcVeEntity.setTcTrangThaiDon(StatusTicket.LEN_XE.getValue());
                    tcVeRepository.save(tcVeEntity);
                });
            } else {
                tcVeEntities.forEach(tcVeEntity -> {
                    session.detach(tcVeEntity);
                    tcVeEntity.setVersion(khachHubToHubForm.getVersions().get(atomicInteger.getAndIncrement()));
                    tcVeEntity.setTcLenhId(tcLenhEntity.getTcLenhId());
                    tcVeEntity.setTcTrangThaiDon(StatusTicket.DA_DIEU.getValue());
                    tcVeRepository.save(tcVeEntity);
                });
            }
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChiTietLenhDTO> getDsLenh(Integer diemgiaokhach, LocalDate dateRequest, Pageable pageable) {
        SelectOptions selectOptions = SelectOptions.get(pageable).count();
        List<ChiTietLenhDTO> dsLenh = tcLenhRepositoryCustom.getDsLenh(diemgiaokhach, dateRequest, selectOptions);
        dsLenh.forEach(x -> {
            if (Objects.nonNull(x.getKieuLenh())) {
                x.setKieuLenhText(LenhConstants.GET_KIEU_LENH.get(x.getKieuLenh()));
            }
            if (Objects.nonNull(x.getTrangThai())) {
                x.setTrangThaiText(LenhConstants.GET_MSG_STATUS.get(x.getTrangThai()));
            }
        });
        PageImpl<ChiTietLenhDTO> page = new PageImpl<>(dsLenh
                , pageable, selectOptions.getCount());
        return page;
    }


    private void saveTicketToCmdTemp(CmdHubToHubForm form, TcLenhEntity tcLenhEntity) {
        Session session = getCurrentSession();
        List<TcVeEntity> tcVeEntities = tcVeRepository.findByBvvIds(form.getBvvIds());
        AtomicInteger i = new AtomicInteger(0);
        if (Objects.equals(form.getStatus(), StatusCmdHub.DA_DON.getValue())) {
            tcVeEntities.forEach(tcVeEntity -> {
                session.detach(tcVeEntity);
                tcVeEntity.setVersion(form.getVersions().get(i.getAndIncrement()));
                tcVeEntity.setTcLenhId(tcLenhEntity.getTcLenhId());
                tcVeEntity.setTcTrangThaiDon(StatusTicket.LEN_XE.getValue());
                tcVeRepository.save(tcVeEntity);
            });
        } else {
            tcVeEntities.forEach(tcVeEntity -> {
                session.detach(tcVeEntity);
                tcVeEntity.setVersion(form.getVersions().get(i.getAndIncrement()));
                tcVeEntity.setTcLenhId(tcLenhEntity.getTcLenhId());
                tcVeEntity.setTcTrangThaiDon(StatusTicket.DA_DIEU.getValue());
                tcVeRepository.save(tcVeEntity);
            });
        }
    }
}
