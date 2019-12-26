package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.body.request.cmdhub.GetCmdHubRequest;
import com.havaz.transport.api.common.AudioType;
import com.havaz.transport.api.common.CacheData;
import com.havaz.transport.api.common.CommonUtils;
import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.common.LenhConstants;
import com.havaz.transport.api.common.NotificationType;
import com.havaz.transport.api.common.VtcCtvConstants;
import com.havaz.transport.api.configuration.MessageConfig;
import com.havaz.transport.api.exception.ResourceNotfoundException;
import com.havaz.transport.api.form.DieuDoForm;
import com.havaz.transport.api.form.FirebaseNotiDataForm;
import com.havaz.transport.api.form.LenhForm;
import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.form.VeForm;
import com.havaz.transport.api.model.DieuDoKhachTcDonDTO;
import com.havaz.transport.api.model.DieuDoKhachTcTraDTO;
import com.havaz.transport.api.model.DistenceCtvVtc;
import com.havaz.transport.api.model.GioDiDTO;
import com.havaz.transport.api.model.HuyLenhTcDTO;
import com.havaz.transport.api.model.NotActiveDTO;
import com.havaz.transport.api.model.NotifyLenhDTO;
import com.havaz.transport.api.model.ThongTinNodeDto;
import com.havaz.transport.api.model.TimGioDTO;
import com.havaz.transport.api.model.TimKhachDTO;
import com.havaz.transport.api.model.TuyenDTO;
import com.havaz.transport.api.model.VeTrungChuyenDTO;
import com.havaz.transport.api.model.XeTcDTO;
import com.havaz.transport.api.rabbit.publisher.RabbitMQPublisher;
import com.havaz.transport.api.repository.DieuDoTempRepositoryCustom;
import com.havaz.transport.api.repository.QuanLyLichTrucRepositoryCustom;
import com.havaz.transport.api.repository.TcLenhRepositoryCustom;
import com.havaz.transport.api.service.BvvService;
import com.havaz.transport.api.service.CommonService;
import com.havaz.transport.api.service.DieuDoService;
import com.havaz.transport.api.service.FirebaseClientService;
import com.havaz.transport.api.service.LenhConfigService;
import com.havaz.transport.api.service.TrungChuyenDonService;
import com.havaz.transport.api.utils.SecurityUtils;
import com.havaz.transport.core.constant.TrangThaiVe;
import com.havaz.transport.core.constant.VeConstants;
import com.havaz.transport.core.exception.TransportException;
import com.havaz.transport.core.utils.Strings;
import com.havaz.transport.dao.entity.AdminLv2UserEntity;
import com.havaz.transport.dao.entity.DieuDoTempEntity;
import com.havaz.transport.dao.entity.TcChamCongEntity;
import com.havaz.transport.dao.entity.TcLenhEntity;
import com.havaz.transport.dao.entity.TcLichTrucEntity;
import com.havaz.transport.dao.entity.TcVeEntity;
import com.havaz.transport.dao.entity.TcVtcCtvEntity;
import com.havaz.transport.dao.entity.TuyenEntity;
import com.havaz.transport.dao.entity.XeEntity;
import com.havaz.transport.dao.entity.ZzLogBanVeEntity;
import com.havaz.transport.dao.repository.AdminLv2UserRepository;
import com.havaz.transport.dao.repository.DieuDoTempRepository;
import com.havaz.transport.dao.repository.TcChamCongRepository;
import com.havaz.transport.dao.repository.TcLenhRepository;
import com.havaz.transport.dao.repository.TcLichTrucRepository;
import com.havaz.transport.dao.repository.TcVeRepository;
import com.havaz.transport.dao.repository.TcVtcCtvRepository;
import com.havaz.transport.dao.repository.TcZzLogRepository;
import com.havaz.transport.dao.repository.TuyenRepository;
import com.havaz.transport.dao.repository.XeRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class DieuDoServiceImpl implements DieuDoService {

    private static final Logger LOG = LoggerFactory.getLogger(DieuDoServiceImpl.class);

    @Value("${havaz.stomp.topic}")
    private String topic;

    @Autowired
    private QuanLyLichTrucRepositoryCustom quanLyLichTrucRepositoryCustom;

    @Autowired
    private DieuDoTempRepository dieuDoTempRepository;

    @Autowired
    private DieuDoTempRepositoryCustom dieuDoTempRepositoryCustom;

    @Autowired
    private TcLenhRepository tcLenhRepository;

    @Autowired
    private TcVeRepository tcVeRepository;

    @Autowired
    private TuyenRepository tuyenRepository;

    @Autowired
    private FirebaseClientService firebaseClientService;

    @Autowired
    private MessageConfig messageConfig;

    @Autowired
    private AdminLv2UserRepository adminLv2UserRepository;

    @Autowired
    private TcLichTrucRepository tcLichTrucRepository;

    @Autowired
    private TcChamCongRepository tcChamCongRepository;

    @Autowired
    private TcVtcCtvRepository vtcCtvRepository;

    @Autowired
    private TcLenhRepository lenhRepository;

    @Autowired
    private TcVeRepository veRepository;

    @Autowired
    private BvvService bvvService;

    @Autowired
    private TcZzLogRepository ZzLogRepository;

    @Autowired
    private TcZzLogRepository logRepository;

    @Autowired
    private LenhConfigService lenhConfigService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private XeRepository xeRepository;

    @Autowired
    private RabbitMQPublisher rabbitMQPublisher;

    @Autowired
    private TrungChuyenDonService trungChuyenDonService;

    @Autowired
    private TcLenhRepositoryCustom tcLenhRepositoryCustom;

    @Override
    public List<TuyenDTO> getListTuyen() {
        List<TuyenDTO> tuyenDTOS = new ArrayList<>();
        List<TuyenEntity> tuyenEntities = tuyenRepository.getListTuyen();
        if (tuyenEntities != null && tuyenEntities.size() > 0) {
            for (TuyenEntity tuyenEntity : tuyenEntities) {
                TuyenDTO tuyenDTO = new TuyenDTO();
                tuyenDTO.setTenTuyen(tuyenEntity.getTuyTen());
                tuyenDTO.setTuyId(tuyenEntity.getTuyId());
                tuyenDTOS.add(tuyenDTO);
            }
        }
        return tuyenDTOS;
    }

    @Override
    public List<GioDiDTO> getListGioDi(TimGioDTO timGioDTO) {
        List<GioDiDTO> gioDiDTOS = new ArrayList<>();
        List<Integer> didIds = new ArrayList<>();

        if (timGioDTO != null) {
            didIds = timGioDTO.getDidIds();
            int khoangThoiGian = timGioDTO.getKhoangThoiGian();

            List<Object[]> objects = dieuDoTempRepository.getListGioDi(didIds, CommonUtils.getSeconds(khoangThoiGian));
            if (objects != null && objects.size() > 0) {
                for (Object[] obj : objects) {
                    GioDiDTO gioDiDTO = new GioDiDTO();
                    gioDiDTO.setDidId(Integer.parseInt(obj[0].toString()));
                    gioDiDTO.setGioDi(obj[1] != null ? String.valueOf(obj[1]) : "");
                    gioDiDTOS.add(gioDiDTO);
                }
            }
        }
        return gioDiDTOS;
    }

    @Override
    public PageCustom<DieuDoKhachTcDonDTO> getDanhSachKhachTrungChuyenDon(TimKhachDTO timKhachDTO) {
        String sortBy = "thuTuDon";
        String sortType = Constant.ASC;
        setOrder(timKhachDTO, sortBy, sortType);
        Page<DieuDoKhachTcDonDTO> dtoPage = quanLyLichTrucRepositoryCustom.getDanhSachKhachTrungChuyenDon(timKhachDTO);
        return CommonUtils.convertPageImplToPageCustom(dtoPage, timKhachDTO.getSortBy(), timKhachDTO.getSortType());
    }

    @Override
    public PageCustom<DieuDoKhachTcTraDTO> getDanhSachKhachTrungChuyenTra(TimKhachDTO timKhachDTO) {
        String sortBy = "thuTuTra";
        String sortType = Constant.ASC;
        setOrder(timKhachDTO, sortBy, sortType);
        Page<DieuDoKhachTcTraDTO> dtoPage = quanLyLichTrucRepositoryCustom.getDanhSachKhachTrungChuyenTra(timKhachDTO);
        return CommonUtils.convertPageImplToPageCustom(dtoPage, timKhachDTO.getSortBy(), timKhachDTO.getSortType());
    }

    private void setOrder(TimKhachDTO timKhachDTO, String sortBy, String sortType) {
        if (!StringUtils.isEmpty(timKhachDTO.getSortBy())) {
            try {
                sortBy = timKhachDTO.getSortBy();
            } catch (Exception e) {
                LOG.error("Field not existing !");
            }
        }
        if (!StringUtils.isEmpty(timKhachDTO.getSortType())) {
            if (Constant.ASC.equalsIgnoreCase(timKhachDTO.getSortType()) || Constant.DESC.equalsIgnoreCase(timKhachDTO.getSortType())) {
                sortType = timKhachDTO.getSortType();
            }
        }
        timKhachDTO.setSortBy(sortBy);
        timKhachDTO.setSortType(sortType);
    }

    @Override
    public List<NotActiveDTO> getListNotActive(ThongTinNodeDto thongTinNodeDto) {
        if (thongTinNodeDto == null) {
            return Collections.emptyList();
        }
        return dieuDoTempRepositoryCustom.getListNotActive(thongTinNodeDto);
    }

    @Override
    @Transactional
    public void taoLenh(DieuDoForm dieuDoForm, int type, int lenhStatus) {
        LOG.debug("Lenh ID: {}, Action: {}, lenh-type: {}, lenh-status: {}",
                  dieuDoForm.getLenhId(), dieuDoForm.getAction(), type, lenhStatus);
        int taiXeId = dieuDoForm.getTaiXeId();

        if (LenhConstants.LENH_STATUS_DA_DIEU == lenhStatus) {
            //Check lai xe co lenh nao da dieu hoac dang chay hay chua
            Set<Integer> trangThai = new HashSet<>();
            trangThai.add(LenhConstants.LENH_STATUS_DA_DIEU);
            trangThai.add(LenhConstants.LENH_STATUS_DANG_CHAY);

            List<TcLenhEntity> listLenh = tcLenhRepository.getAllByLaiXeIdAAndTrangThai(taiXeId, trangThai, type);

            if (listLenh != null && !listLenh.isEmpty() && lenhConfigService.getStatus() != 1) {
                throw new TransportException("Không thể gán nhiều lệnh cho lái xe cùng lúc. Vui lòng cài đặt điều hành nhiều lệnh");
            }
        }
        TcLenhEntity tcLenhEntity = new TcLenhEntity();
        tcLenhEntity.setLaiXeId(dieuDoForm.getTaiXeId());
        tcLenhEntity.setTrangThai(lenhStatus);
        tcLenhEntity.setKieuLenh(type);

//        int currentUser = SecurityUtils.getCurrentUserLogin();
        int currentUser = 1;
        tcLenhEntity.setCreatedBy(currentUser);
        //check tai xe da duoc phan lich chua va lay xe id
        tcLenhEntity.setXeId(checkDieuKienPhanLichCuaLaiXe(taiXeId, dieuDoForm));
        //Save into DB
        tcLenhRepository.save(tcLenhEntity);
        List<VeForm> veForms = dieuDoForm.getDanhSachVe();
        if (veForms != null && veForms.size() > 0) {
            LOG.debug("Tổng số vé trong lệnh:: {}", veForms.size());
            for (VeForm veForm : dieuDoForm.getDanhSachVe()) {
                //Save info Ve
                saveVeDonTra(veForm, type, tcLenhEntity.getTcLenhId(), taiXeId, lenhStatus, 0);

                // save Info TcTripDetail
//                tripDetailService.saveTcTripDetail(tcLenhEntity, veForm, type);
            }
            LOG.info("Update vé cho lệnh >>> DONE.");
        }
        //TODO: WRITE LOG
        long epoch = System.currentTimeMillis() / 1000;
        String timeS = String.valueOf(epoch);
        int timeI = Integer.valueOf(timeS);
        String kieuLenh = type == 1 ? "Điều lệnh đón" : "Điều lệnh trả";
        String tx = "";
        try {
            tx = adminLv2UserRepository.getOne(taiXeId).getAdmName();
        } catch (Exception e) {
            tx = "Unknown";
        }
        String txName = tx;

        if (veForms != null) {
            for (VeForm veForm : veForms) {
                veForm.getBvvIds().forEach(bvvId -> {
                    LOG.info("ZzLOG: START write history");
                    String data = "{\"" + kieuLenh + " \":[\"0\",\"1\"],\"LX nhận lệnh\":[\"0\",\"" + txName + "\"]}";
                    ZzLogBanVeEntity logEntity = new ZzLogBanVeEntity();
                    int user;
                    if (tcLenhEntity.getCreatedBy() == null) {
                        user = 1;
                    } else {
                        user = tcLenhEntity.getCreatedBy();
                    }
                    logEntity.setLogAdminId(user);
                    logEntity.setLogRecordId(bvvId);
                    logEntity.setLogData(data);
                    logEntity.setLogTime(timeI);
                    logEntity.setLogType(2);//1: Insert 2: Update
                    logEntity.setLogIp("TẠO LỆNH");
                    logEntity.setLogStatus(0);
                    ZzLogRepository.save(logEntity);
                    LOG.info("ZzLOG: END write history");
                });
            }
        }
        //Send noti and data to firebase
        if (LenhConstants.LENH_STATUS_DA_DIEU == lenhStatus) {
            FirebaseNotiDataForm firebaseNotiDataForm = new FirebaseNotiDataForm();
            firebaseNotiDataForm.setTypeNotify(NotificationType.NEW_CMD.getType());
            sendNotifyToDriver(taiXeId, type, firebaseNotiDataForm, AudioType.NEW_CMD.getValue());
        }
    }

    @Override
    @Transactional
    public void taoLenhChoLaiXeTuyen(DieuDoForm dieuDoForm, int type, int lenhStatus) {
        LOG.debug("Lenh ID: {}, Action: {}, lenh-type: {}, lenh-status: {}",
                  dieuDoForm.getLenhId(), dieuDoForm.getAction(), type, lenhStatus);
        for (VeForm veForm: dieuDoForm.getDanhSachVe()) {
            saveVeDonTraKhiDieuXeTuyen(veForm, type, 0, 0, 0, 0);
        }
    }

    @Override
    @Transactional
    public void huyLenhChoLaiXeTuyen(int idLaiXe, int bvvId, int type) {
        LOG.info("Lái xe tuyến hủy đón khách. laixeId={}", idLaiXe);
        if (idLaiXe <= 0) {
            throw new ResourceNotfoundException("Không tìm thấy lái xe idLaixe=" + idLaiXe);
        }
        TcVeEntity byBvvId = tcVeRepository.getByBvvId(bvvId);
        if (byBvvId != null) {
            if (byBvvId.getTcTrangThaiDon() != VeConstants.TC_STATUS_XE_TUYEN_DON) {
                throw new TransportException("Trạng thái vé không hợp lệ");
            }
            String adminNameById = adminLv2UserRepository.findAdminNameById(idLaiXe);
            if (LenhConstants.LENH_DON == type) {
                byBvvId.setLyDoTuChoiDon(adminNameById + ": từ chối đón");
                byBvvId.setTcTrangThaiDon(VeConstants.TC_STATUS_XE_TUYEN_HUY_DON);
            } else {
                byBvvId.setLyDoTuChoiTra(adminNameById + ": từ chối trả");
                byBvvId.setTcTrangThaiTra(VeConstants.TC_STATUS_XE_TUYEN_HUY_TRA);
            }
            tcVeRepository.save(byBvvId);
        } else {
            throw new ResourceNotfoundException("Không tìm thấy vé id=" + bvvId);
        }
    }

    @Override
    @Transactional
    public void taoLenhChoLaiXeNgoai(DieuDoForm dieuDoForm, int type, int lenhStatus) {
        LOG.debug("Lenh ID: {}, Action: {}, lenh-type: {}, lenh-status: {}",
                  dieuDoForm.getLenhId(), dieuDoForm.getAction(), type, lenhStatus);
        for (VeForm veForm: dieuDoForm.getDanhSachVe()) {
            saveVeDonTraKhiDieuXeNgoai(veForm, type, 0, 0, 0, 0);
        }
    }

    @Override
    @Transactional
    public void taoLenhCtv(DieuDoForm form, int vungId, int type, int lenhStatus, DistenceCtvVtc distenceCtvVtc) {
        TcLenhEntity tcLenhEntity = new TcLenhEntity();
        tcLenhEntity.setLaiXeId(form.getTaiXeId());
        tcLenhEntity.setTrangThai(lenhStatus);
        tcLenhEntity.setKieuLenh(type);
        Optional<TcVtcCtvEntity> vtcCtvEntity = vtcCtvRepository.findByTcVttIdAndTcCtvIdAndStatus(vungId, form.getTaiXeId(), VtcCtvConstants.STATUS_ACTIVE);
        if (vtcCtvEntity.isPresent()) {
            tcLenhEntity.setIsHavazNow(true);
            tcLenhEntity.setXeId(vtcCtvEntity.get().getXeId());
            tcLenhEntity = tcLenhRepository.save(tcLenhEntity);
            List<VeForm> veForms = form.getDanhSachVe();
            if (veForms != null && veForms.size() > 0) {
                LOG.info("Tổng số vé trong lệnh:: {}", veForms.size());
                for (VeForm veForm : veForms) {
                    //Save info Ve
                    saveVeDonCtv(veForm, tcLenhEntity.getTcLenhId(), vtcCtvEntity.get().getTcCtvId(), lenhStatus, 0, distenceCtvVtc);
                }
                LOG.info("Update vé cho lệnh >>> DONE.");
            }
            //Send noti and data to firebase
            if (LenhConstants.LENH_STATUS_DA_DIEU == lenhStatus) {
                FirebaseNotiDataForm firebaseNotiDataForm = new FirebaseNotiDataForm();
                firebaseNotiDataForm.setTypeNotify(NotificationType.NEW_CMD_NOW.getType());
                sendNotifyToDriver(tcLenhEntity.getLaiXeId(), type, firebaseNotiDataForm, AudioType.NEW_CMD.getValue());
            }
            //TODO: WRITE LOG
            long epoch = System.currentTimeMillis() / 1000;
            String timeS = String.valueOf(epoch);
            int timeI = Integer.valueOf(timeS);
            String kieuLenh = type == 1 ? "TC: Điều lệnh đón (Auto)" : "TC: Điều lệnh trả (Auto)";
            Integer id = tcLenhEntity.getCreatedBy();

            if (veForms == null) {
                return;
            }

            for (VeForm veForm : veForms) {
                veForm.getBvvIds().forEach(bvvId -> {
                    LOG.info("ZzLOG: START write history");
                    String data = "{\"" + kieuLenh + "\":[\"0\",\"1\"]}";
                    ZzLogBanVeEntity logEntity = new ZzLogBanVeEntity();
                    int user;
                    if (id == null) {
                        user = 1;
                    } else {
                        user = id;
                    }
                    logEntity.setLogAdminId(user);
                    logEntity.setLogRecordId(bvvId);
                    logEntity.setLogData(data);
                    logEntity.setLogTime(timeI);
                    logEntity.setLogType(2);//1: Insert 2: Update
                    logEntity.setLogIp("TẠO LỆNH-AUTO");
                    logEntity.setLogStatus(0);
                    ZzLogRepository.save(logEntity);
                    LOG.info("ZzLOG: END write history");
                });
            }
        }
    }

    @Override
    @Transactional
    public boolean huyLenhDadieu(HuyLenhTcDTO huyLenhTcDTO) {
        Optional<TcLenhEntity> oplenhEntity = lenhRepository.findById(huyLenhTcDTO.getLenhId());
        int currentUser = SecurityUtils.getCurrentUserLogin();
        if (oplenhEntity.isPresent()) {
            TcLenhEntity tcLenhEntity = oplenhEntity.get();
            if (tcLenhEntity.getKieuLenh() == TrangThaiVe.DON.getTrangThai()) {
                List<TcVeEntity> veEntities = tcVeRepository.getVeTcByTcLenhId(tcLenhEntity.getTcLenhId());
                AtomicInteger count = new AtomicInteger(0);
                veEntities.forEach(veEntity -> {
                    if (veEntity.getTcTrangThaiDon() != 3) {
                        LOG.info("huy Lenh Ve: " + veEntity.getBvvId());
                        count.getAndIncrement();
                        int oldTTD = veEntity.getTcTrangThaiDon();
                        int laiXeId = veEntity.getLaiXeIdDon();
                        veEntity.setTcTrangThaiDon(VeConstants.TC_STATUS_DA_HUY);
                        veEntity.setLaiXeIdDon(Constant.ZERO);
                        veEntity.setTcLenhId(Constant.ZERO);
                        veEntity.setLyDoTuChoiDon(huyLenhTcDTO.getLyDoHuy());
                        veEntity.setThuTuDon(Constant.ZERO);
                        veEntity.setLastUpdatedDate(LocalDateTime.now());
                        veRepository.save(veEntity);
                        //Update lai table ban_ve_ve
                        bvvService.updateBvvEntity(veEntity);

                        //save Zzlog
                        ZzLogBanVeEntity logEntity;
                        logEntity = new ZzLogBanVeEntity();
                        logEntity.setLogAdminId(laiXeId);
                        logEntity.setLogRecordId(veEntity.getBvvId());
                        //4: đã hủy
                        logEntity.setLogData("{\"Trạng thái đón\":[\"" + VeConstants.GET_MSG_STATUS.get(oldTTD) + "\",\"" + VeConstants.GET_MSG_STATUS.get(4)
                                + "\"],\"Lý do\":[\"\"," + "\"" + huyLenhTcDTO.getLyDoHuy() + "\"]}");
                        long epoch = System.currentTimeMillis() / 1000;
                        String timeS = String.valueOf(epoch);
                        int timeI = Integer.valueOf(timeS);
                        logEntity.setLogTime(timeI);
                        logEntity.setLogType(2);//1: Insert 2: Update
                        logEntity.setLogIp("HỦY LỆNH ĐÓN");
                        logEntity.setLogStatus(0);//ERP said it alway is 0
                        logRepository.save(logEntity);

                    }
                });
                //excute send notify to Driver
                FirebaseNotiDataForm firebaseNotiDataForm = new FirebaseNotiDataForm();
                firebaseNotiDataForm.setTypeNotify(NotificationType.UPDATE_CMD.getType());
                sendNotifyToDriverTicketCancel(tcLenhEntity.getLaiXeId(), tcLenhEntity.getKieuLenh(),
                        firebaseNotiDataForm, AudioType.CANCEL_TICKET.getValue());

                if (count.get() == veEntities.size()) {
                    tcLenhEntity.setTrangThai(LenhConstants.LENH_STATUS_DA_BI_HUY);
                    tcLenhEntity.setLastUpdatedDate(LocalDateTime.now());
                    tcLenhEntity.setCanceledBy(currentUser);
                    tcLenhEntity.setLyDoHuy(huyLenhTcDTO.getLyDoHuy());
                    tcLenhRepository.save(tcLenhEntity);
                    LOG.info("huy Lenh: " + tcLenhEntity.getTcLenhId());
                    return true;
                } else {
                    return false;
                }
            } else if (tcLenhEntity.getKieuLenh() == TrangThaiVe.TRA.getTrangThai()) {
                List<TcVeEntity> veEntities = tcVeRepository.getVeTcTraByTcLenhTraId(tcLenhEntity.getTcLenhId());
                AtomicInteger count = new AtomicInteger(0);
                veEntities.forEach(veEntity -> {
                    if (veEntity.getTcTrangThaiDon() != 3) {
                        LOG.info("huy Lenh Ve: " + veEntity.getBvvId());
                        count.getAndIncrement();
                        int oldTTT = veEntity.getTcTrangThaiTra();
                        int laiXeId = veEntity.getLaiXeIdTra();
                        veEntity.setTcTrangThaiTra(VeConstants.TC_STATUS_DA_HUY);
                        veEntity.setLaiXeIdTra(Constant.ZERO);
                        veEntity.setTcLenhTraId(Constant.ZERO);
                        veEntity.setLyDoTuChoiTra(huyLenhTcDTO.getLyDoHuy());
                        veEntity.setThuTuTra(Constant.ZERO);
                        veEntity.setLastUpdatedDate(LocalDateTime.now());
                        veRepository.save(veEntity);
                        //Update lai table ban_ve_ve
                        bvvService.updateBvvEntity(veEntity);
                        //save to zZlog
                        ZzLogBanVeEntity logEntity;
                        logEntity = new ZzLogBanVeEntity();
                        logEntity.setLogAdminId(laiXeId);
                        logEntity.setLogRecordId(veEntity.getBvvId());
                        //4: đã hủy
                        logEntity.setLogData("{\"TC: Trạng thái trả\":[\"" + VeConstants.GET_MSG_STATUS_TRA.get(oldTTT) + "\",\"" + VeConstants.GET_MSG_STATUS_TRA.get(4)
                                + "\"],\"Lý do từ chối trả\":[\"\"," + "\"" + huyLenhTcDTO.getLyDoHuy() + "\"]}");
                        long epoch = System.currentTimeMillis() / 1000;
                        String timeS = String.valueOf(epoch);
                        int timeI = Integer.valueOf(timeS);
                        logEntity.setLogTime(timeI);
                        logEntity.setLogType(2);//1: Insert 2: Update
                        logEntity.setLogIp("API-Hủy-Lệnh-Trả");
                        logEntity.setLogStatus(0);//ERP said it alway is 0
                        logRepository.save(logEntity);
                    }
                });
                //excute send notify to Driver
                FirebaseNotiDataForm firebaseNotiDataForm = new FirebaseNotiDataForm();
                firebaseNotiDataForm.setTypeNotify(NotificationType.UPDATE_CMD.getType());
                sendNotifyToDriverTicketCancel(tcLenhEntity.getLaiXeId(), tcLenhEntity.getKieuLenh(),
                        firebaseNotiDataForm, AudioType.CANCEL_TICKET.getValue());

                if (count.get() == veEntities.size()) {
                    tcLenhEntity.setTrangThai(LenhConstants.LENH_STATUS_DA_BI_HUY);
                    tcLenhEntity.setLastUpdatedDate(LocalDateTime.now());
                    tcLenhEntity.setLyDoHuy(huyLenhTcDTO.getLyDoHuy());
                    tcLenhEntity.setCanceledBy(currentUser);
                    tcLenhRepository.save(tcLenhEntity);
                    LOG.info("huy Lenh: " + tcLenhEntity.getTcLenhId());
                    return true;
                } else {
                    return false;
                }
            }

        } else {
            throw new ResourceNotfoundException("Không tìm thấy lệnh!" + huyLenhTcDTO.getLenhId());
        }
        return false;
    }


    @Override
    @Transactional
    public void dieuBoSung(DieuDoForm dieuDoForm, int type) {
        LOG.info("Lệnh ID: {}, type: {}", dieuDoForm.getLenhId(), type);
        int taiXeId = dieuDoForm.getTaiXeId();
        TcLenhEntity tcLenhEntity = tcLenhRepository.findById(dieuDoForm.getLenhId()).orElse(null);
        if (tcLenhEntity != null) {
            int lenhStatus = tcLenhEntity.getTrangThai();
            if (LenhConstants.LENH_STATUS_DA_BI_HUY != lenhStatus
                    && LenhConstants.LENH_STATUS_DA_HOAN_TAT != lenhStatus) {
                for (VeForm veForm : dieuDoForm.getDanhSachVe()) {
                    //save info Ve
                    saveVeDonTra(veForm, type, tcLenhEntity.getTcLenhId(), taiXeId, lenhStatus, 1);
                    if (!CollectionUtils.isEmpty(veForm.getBvvIds())) {
                        trungChuyenDonService.sendInfoTrackingToMsgMQ(veForm.getBvvIds().get(0));
                    }

                }
                //Send noti and data
                if (LenhConstants.LENH_STATUS_DA_DIEU == lenhStatus || LenhConstants.LENH_STATUS_DANG_CHAY == lenhStatus) {
                    FirebaseNotiDataForm firebaseNotiDataForm = new FirebaseNotiDataForm();
                    firebaseNotiDataForm.setTypeNotify(NotificationType.ADD_CMD.getType());
                    sendNotifyToDriver(taiXeId, type, firebaseNotiDataForm, AudioType.CMD_DBS.getValue());
                }

            } else {
                LOG.error("Lệnh đã hoàn tất hoặc bị hủy.");
                throw new TransportException("Lệnh đã hoàn tất hoặc bị hủy.");
            }
            if (dieuDoForm.getCmdAdditional() != null && dieuDoForm.getCmdAdditional()) {
                tcLenhEntity.setIsHavazNow(true);
                tcLenhRepository.save(tcLenhEntity);
            }

        } else {
            LOG.error("Lệnh không tồn tại.");
            throw new TransportException("Lệnh không tồn tại.");
        }
    }

    private void sendNotifyToClient(TcLenhEntity tcLenhEntity) {
        Optional<AdminLv2UserEntity> adminLv2UserEntity = adminLv2UserRepository.findById(tcLenhEntity.getLaiXeId());
        Optional<XeEntity> optionalXeEntity = xeRepository.findById(tcLenhEntity.getXeId());
        NotifyLenhDTO notifyLenhDTO = new NotifyLenhDTO();
        if (adminLv2UserEntity.isPresent()) {
            notifyLenhDTO.setTaixeName(adminLv2UserEntity.get().getAdmName());
        }
        if (optionalXeEntity.isPresent()) {
            notifyLenhDTO.setXeBks(optionalXeEntity.get().getXeBienKiemSoat());
            notifyLenhDTO.setLoaiXe(optionalXeEntity.get().getXeLoai());
        }
        List<TcVeEntity> veEntities = tcVeRepository.findByTcLenhId(tcLenhEntity.getTcLenhId());
        if (veEntities.size() > 0) {
            notifyLenhDTO.setClientNumberPhone(veEntities.get(0).getBvvPhoneDi());
        }
        notifyLenhDTO.setMerchantId(CacheData.THONG_TIN_NHA_XE.getCon_code());
        rabbitMQPublisher.sendMsgDriverConfirmLenh(notifyLenhDTO);
    }

    @Override
    @Transactional
    public void luuLenh(DieuDoForm dieuDoForm, int type, int lenhStatus) {
        LOG.info("Lệnh ID: {}, type: {}, status: {}", dieuDoForm.getLenhId(), type, lenhStatus);
        LocalDateTime currentTime = LocalDateTime.now();
        int taiXeId = dieuDoForm.getTaiXeId();
        List<VeForm> danhSachVe = dieuDoForm.getDanhSachVe();
        TcLenhEntity tcLenhEntity = tcLenhRepository.findById(dieuDoForm.getLenhId()).orElse(null);
        if (tcLenhEntity != null) {
            //Reset data
            List<TcVeEntity> veTcByTcLenhId = tcVeRepository.getVeTcByTcLenhId(dieuDoForm.getLenhId());
            if (!veTcByTcLenhId.isEmpty()) {
                for (TcVeEntity tcVeEntity : veTcByTcLenhId) {
                    if (LenhConstants.LENH_DON == type) {
                        tcVeEntity.setTcLenhId(0);
                        tcVeEntity.setLaiXeIdDon(0);
                        tcVeEntity.setThuTuDon(0);
                        tcVeEntity.setGhiChuDon(null);
                        tcVeEntity.setTcTrangThaiDon(0);
                    } else if (LenhConstants.LENH_TRA == type) {
                        tcVeEntity.setTcLenhTraId(0);
                        tcVeEntity.setLaiXeIdTra(0);
                        tcVeEntity.setThuTuTra(0);
                        tcVeEntity.setGhiChuTra(null);
                        tcVeEntity.setTcTrangThaiTra(0);
                    }
                    tcVeEntity.setLastUpdatedDate(currentTime);

                    tcVeRepository.save(tcVeEntity);
                }
            }
            //Update new data
            if (danhSachVe != null && danhSachVe.size() > 0) {
                for (VeForm veForm : danhSachVe) {
                    saveVeDonTra(veForm, type, dieuDoForm.getLenhId(), dieuDoForm.getTaiXeId(), tcLenhEntity.getTrangThai(), 0);
                }
            }
            if (LenhConstants.LENH_STATUS_DA_DIEU == lenhStatus) {
                FirebaseNotiDataForm firebaseNotiDataForm = new FirebaseNotiDataForm();
                firebaseNotiDataForm.setTypeNotify(NotificationType.NEW_CMD.getType());
                sendNotifyToDriver(taiXeId, type, firebaseNotiDataForm, AudioType.NEW_CMD.getValue());
            }
        } else {
            LOG.error("Lệnh không tồn tại.");
            throw new TransportException("Lệnh không tồn tại.");
        }
    }

    @Override
    public List<LenhForm> getAllLenh(int type) {
        List<LenhForm> lenhForms = new ArrayList<>();
        Set<Integer> trangThai = new HashSet<>();
        trangThai.add(LenhConstants.LENH_STATUS_DA_BI_HUY);
        trangThai.add(LenhConstants.LENH_STATUS_DA_HOAN_TAT);
        List<TcLenhEntity> commands = tcLenhRepository.getAllByKieuLenhAndTrangThaiNotIn(type, trangThai);
        if (!commands.isEmpty()) {
            Set<Integer> laiXeIdSet = commands.stream().map(TcLenhEntity::getLaiXeId)
                    .collect(Collectors.toSet());
            List<AdminLv2UserEntity> allUser = adminLv2UserRepository.findAllByAdmIdIn(laiXeIdSet);
            for (TcLenhEntity tcLenhEntity : commands) {
                if (tcLenhEntity.getLaiXeId() != null && tcLenhEntity.getLaiXeId() != 0) {
                    AdminLv2UserEntity adminLv2User = allUser.stream()
                            .filter(e -> tcLenhEntity.getLaiXeId().equals(e.getAdmId()))
                            .findFirst().orElse(new AdminLv2UserEntity());
                    LenhForm lenhForm = new LenhForm();
                    lenhForm.setLenhId(tcLenhEntity.getTcLenhId());
                    lenhForm.setLaiXeId(tcLenhEntity.getLaiXeId());
                    lenhForm.setLaiXeTen(adminLv2User.getAdmName());
                    lenhForm.setTrangThai(LenhConstants.GET_MSG_STATUS.get(tcLenhEntity.getTrangThai()));
                    lenhForm.setIdXe(tcLenhEntity.getXeId());

                    lenhForms.add(lenhForm);
                }
            }
        }
        return lenhForms;
    }

    @Override
    public List<LenhForm> getAllLenhDaTao(int laiXeId, int type) {
        List<LenhForm> lenhForms = new ArrayList<>();
        Set<Integer> trangThai = new HashSet<>();
        List<TcLenhEntity> commands = new ArrayList<>();
        if(laiXeId == 0) {
            trangThai.add(LenhConstants.LENH_STATUS_DA_TAO);
            commands = tcLenhRepository.getAllByKieuLenhAndTrangThaiIn(type, trangThai);
        }else {
            trangThai.add(LenhConstants.LENH_STATUS_DA_TAO);
            trangThai.add(LenhConstants.LENH_STATUS_DANG_CHAY);
            trangThai.add(LenhConstants.LENH_STATUS_DA_DIEU);
            commands = tcLenhRepository.getAllByLaiXeIdAAndTrangThai(laiXeId, trangThai,type);
        }
        if (!commands.isEmpty()) {
            Set<Integer> xeIds = commands.stream().map(TcLenhEntity::getXeId)
                    .collect(Collectors.toSet());
            List<XeEntity> allXe = xeRepository.getAllByXeIdIn(xeIds);
            for (TcLenhEntity tcLenhEntity : commands) {
                XeEntity xeEntity = allXe.stream()
                        .filter(e -> tcLenhEntity.getXeId() != null && tcLenhEntity.getXeId().equals(e.getXeId()))
                        .findFirst().orElse(new XeEntity());
                LenhForm lenhForm = new LenhForm();
                lenhForm.setLenhId(tcLenhEntity.getTcLenhId());
                lenhForm.setLaiXeId(tcLenhEntity.getLaiXeId());
                lenhForm.setBks(xeEntity.getXeBienKiemSoat() != null ? xeEntity.getXeBienKiemSoat() : "");
                lenhForm.setSeats(xeEntity.getXeSoCho() != null ? xeEntity.getXeSoCho() : 0);
                lenhForm.setTrangThai(LenhConstants.GET_MSG_STATUS.get(tcLenhEntity.getTrangThai()));
                lenhForm.setIdXe(tcLenhEntity.getXeId() != null ? xeEntity.getXeId() : 0);

                //Nếu là lệnh đón => lấy list khách đón và ngược lại:
                List<Object[]> Objs = tcLenhEntity.getKieuLenh() == LenhConstants.LENH_DON ?
                        tcLenhRepositoryCustom.getListKhachTrungChuyen(tcLenhEntity.getTcLenhId()) : tcLenhRepositoryCustom.getListKhachTrungChuyenTra(tcLenhEntity.getTcLenhId());

                int tongKhachCuaLenh = 0;
                List<VeTrungChuyenDTO> list = new ArrayList<>();
                if (Objs != null && Objs.size() > 0) {
                    for (Object[] arr : Objs) {
                        VeTrungChuyenDTO veTC = new VeTrungChuyenDTO();
                        veTC.setDiaChi(arr[2] != null ? String.valueOf(arr[2]) : null);
                        veTC.setSdt(arr[1] != null ? String.valueOf(arr[1]) : null);
                        veTC.setTen(arr[0] != null ? String.valueOf(arr[0]) : null);
                        veTC.setKhachHangMoi(arr[11] != null ? arr[11].toString() : "");
                        veTC.setSoGhe(arr[12] != null ? String.valueOf(arr[12]) : null);
                        veTC.setSoKhach(arr[13] != null ? Integer.valueOf(String.valueOf(arr[13])) : null);
                        veTC.setTrangThaiTrungChuyen(arr[5] != null ? Integer.parseInt(arr[5].toString()) : 0);
                        veTC.setThoiGianDon(arr[4] != null ? Integer.parseInt(arr[4].toString()) : 0);
                        veTC.setThuTuDon(arr[3] != null ? Integer.parseInt(arr[3].toString()) : 0);
                        if (arr[14] != null) {
                            List<Integer> listBvvId = CommonUtils.convertListStringToInt(Arrays.asList(String.valueOf(arr[14]).split("\\s*,\\s*")));
                            veTC.setListBvvId(listBvvId);
                        }
                        veTC.setGhiChuVe(arr[17] != null ? String.valueOf(arr[17]) : null);
                        veTC.setGhiChuVeBvv(arr[18] != null ? String.valueOf(arr[18]) : null);
                        tongKhachCuaLenh += Integer.valueOf(String.valueOf(arr[13]));
                        list.add(veTC);
                    }
                }
                lenhForm.setSoKhach(tongKhachCuaLenh);
                lenhForm.setVeTrungChuyenDTOList(list);
                lenhForms.add(lenhForm);
            }
        }
        return lenhForms;
    }

    @Override
    @Transactional
    public void xoaLenh(int lenhId) {
        TcLenhEntity tcLenhEntity = tcLenhRepository.findById(lenhId).orElse(null);
        if (tcLenhEntity != null) {
            if (tcLenhEntity.getTrangThai() == 5) {
                int kieuLenh = tcLenhEntity.getKieuLenh();
                LOG.info("Xoa lenh lenhId:: " + lenhId);
                //Xoa data lenh
                List<TcVeEntity> veTc = null;
                if (LenhConstants.LENH_DON == kieuLenh) {
                    veTc = tcVeRepository.getVeTcByTcLenhId(lenhId);
                } else if (LenhConstants.LENH_TRA == kieuLenh) {
                    veTc = tcVeRepository.getVeTcTraByTcLenhTraId(lenhId);
                }
                if (!CollectionUtils.isEmpty(veTc)) {
                    for (TcVeEntity tcVeEntity : veTc) {
                        if (LenhConstants.LENH_DON == kieuLenh) {
                            tcVeEntity.setTcLenhId(0);
                            tcVeEntity.setLaiXeIdDon(0);
                            tcVeEntity.setThuTuDon(0);
                            tcVeEntity.setGhiChuDon(null);
                            tcVeEntity.setTcTrangThaiDon(0);
                            tcVeEntity.setThoiGianDon(0);
                        } else if (LenhConstants.LENH_TRA == kieuLenh) {
                            tcVeEntity.setTcLenhTraId(0);
                            tcVeEntity.setLaiXeIdTra(0);
                            tcVeEntity.setThuTuTra(0);
                            tcVeEntity.setGhiChuTra(null);
                            tcVeEntity.setTcTrangThaiTra(0);
                            tcVeEntity.setThoiGianTra(0);
                        }
                        tcVeRepository.save(tcVeEntity);
                    }
                }
                tcLenhRepository.delete(tcLenhEntity);
            } else {
                LOG.error("Lệnh này đã điều hoặc đang chạy.");
                throw new TransportException("Lệnh này đã điều hoặc đang chạy.");
            }
        } else {
            LOG.error("Lệnh không tồn tại.");
            throw new TransportException("Lệnh không tồn tại.");
        }
    }

    /**
     * Save data don tra
     *
     * @param veForm
     * @param type
     * @param lenhId
     * @param taiXeId
     * @param lenhStatus
     * @param kieu
     */
    private void saveVeDonTra(VeForm veForm, int type, int lenhId, int taiXeId, int lenhStatus, int kieu) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Integer> bvvIds = veForm.getBvvIds();
        int thoiGian = 3;
        if (bvvIds != null && bvvIds.size() > 0) {
            for (int bvvId : bvvIds) {
                TcVeEntity tcVeEntity = tcVeRepository.getByBvvId(bvvId);
                if (tcVeEntity != null) {
                    if (LenhConstants.LENH_DON == type) {
                        if (tcVeEntity.getTcLenhId() != null && tcVeEntity.getTcLenhId() > 0) {
                            LOG.info("[bvvId] Đã được gán lệnh :: {}", bvvId);
                            throw new TransportException("[bvvId] Đã được gán lệnh :: " + bvvId);
                        }
                        tcVeEntity.setLaiXeIdDon(taiXeId);
                        tcVeEntity.setGhiChuDon(veForm.getGhiChu());
                        tcVeEntity.setThuTuDon(veForm.getThuTu());
                        tcVeEntity.setThoiGianDon(thoiGian);
                        tcVeEntity.setTcTrangThaiDon(lenhStatus);
                        tcVeEntity.setTcLenhId(lenhId);
                        tcVeEntity.setLyDoTuChoiDon(Strings.EMPTY);
                    } else if (LenhConstants.LENH_TRA == type) {
                        if (tcVeEntity.getTcLenhTraId() != null && tcVeEntity.getTcLenhTraId() > 0) {
                            LOG.error("[bvvId] Đã được gán lệnh :: " + bvvId);
                            throw new TransportException("[bvvId] Đã được gán lệnh :: " + bvvId);
                        }
                        tcVeEntity.setLaiXeIdTra(taiXeId);
                        tcVeEntity.setGhiChuTra(veForm.getGhiChu());
//                        tcVeEntity.setThuTuTra(veForm.getThuTu());
                        tcVeEntity.setThoiGianTra(thoiGian);
                        tcVeEntity.setTcTrangThaiTra(lenhStatus);
                        tcVeEntity.setTcLenhTraId(lenhId);
                        tcVeEntity.setLyDoTuChoiTra("");
                    }
                    //neu truong hop dieu bo sung thi la khach hang moi va nguoc lai
                    tcVeEntity.setKhachHangMoi(kieu == 1 ? 1 : 0);
                    //Save into DB
                    tcVeRepository.save(tcVeEntity);
                    bvvService.updateBvvEntity(tcVeEntity);
                    LOG.info("Save done [bvvId]:: " + bvvId);
                    if (kieu == 1) {
                        String typeX = type == LenhConstants.LENH_TRA ? "trả" : "đón";
                        String data = "{\"Điều bổ sung " + typeX + " \"" + ":[\"0\",\"1\"]}";
                        commonService.wrieZzLog(String.valueOf(SecurityUtils.getCurrentUserLogin()), tcVeEntity.getBvvId(), data, "ĐIỀU BỔ SUNG");
                    }

                } else {
                    LOG.error("[bvvId] Không tồn tại :: " + bvvId);
                    throw new TransportException("[bvvId] Không tồn tại :: " + bvvId);
                }
                thoiGian = thoiGian + 3;
            }
        }
    }

    private void saveVeDonTraKhiDieuXeTuyen(VeForm veForm, int type, int lenhId, int taiXeId, int lenhStatus, int kieu) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Integer> bvvIds = veForm.getBvvIds();
        if (!CollectionUtils.isEmpty(bvvIds)) {
            for (int bvvId : bvvIds) {
                TcVeEntity tcVeEntity = tcVeRepository.getByBvvId(bvvId);
                if (tcVeEntity != null) {
                    if (LenhConstants.LENH_DON == type) {
                        if (tcVeEntity.getTcLenhId() != null && tcVeEntity.getTcLenhId() > 0) {
                            LOG.info("[bvvId] Đã được gán lệnh :: {}", bvvId);
                            throw new TransportException("[bvvId] Đã được gán lệnh :: " + bvvId);
                        }
                        tcVeEntity.setLaiXeIdDon(0);
                        tcVeEntity.setGhiChuDon(Strings.EMPTY);
                        tcVeEntity.setThuTuDon(0);
                        tcVeEntity.setThoiGianDon(0);
                        tcVeEntity.setTcTrangThaiDon(VeConstants.TC_STATUS_XE_TUYEN_DON);
                        tcVeEntity.setTcLenhId(lenhId);
                        tcVeEntity.setLyDoTuChoiDon(Strings.EMPTY);
                    } else if (LenhConstants.LENH_TRA == type) {
                        if (tcVeEntity.getTcLenhTraId() != null && tcVeEntity.getTcLenhTraId() > 0) {
                            LOG.info("[bvvId] Đã được gán lệnh :: {}", bvvId);
                            throw new TransportException("[bvvId] Đã được gán lệnh :: " + bvvId);
                        }
                        tcVeEntity.setLaiXeIdTra(taiXeId);
                        tcVeEntity.setGhiChuTra("");
//                        tcVeEntity.setThuTuTra(veForm.getThuTu());
                        tcVeEntity.setThoiGianTra(0);
                        tcVeEntity.setTcTrangThaiTra(VeConstants.TC_STATUS_XE_TUYEN_TRA);
                        tcVeEntity.setTcLenhTraId(0);
                        tcVeEntity.setLyDoTuChoiTra(Strings.EMPTY);
                    }
                    //neu truong hop dieu bo sung thi la khach hang moi va nguoc lai
                    tcVeEntity.setKhachHangMoi(kieu == 1 ? 1 : 0);
                    //Save into DB
                    tcVeRepository.save(tcVeEntity);
                    if (kieu == 1) {
                        String typeX = type == LenhConstants.LENH_TRA ? "trả" : "đón";
                        String data = "{\"Điều xe tuyến " + typeX + " \"" + ":[\"0\",\"1\"]}";
                        commonService.wrieZzLog(String.valueOf(SecurityUtils.getCurrentUserLogin()), tcVeEntity.getBvvId(), data, "ĐIỀU XE TUYẾN");
                    }

                } else {
                    LOG.info("[bvvId] Không tồn tại :: {}", bvvId);
                    throw new TransportException("[bvvId] Không tồn tại :: " + bvvId);
                }
            }
        }
    }

    private void saveVeDonTraKhiDieuXeNgoai(VeForm veForm, int type, int lenhId, int taiXeId, int lenhStatus, int kieu) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Integer> bvvIds = veForm.getBvvIds();
        if (bvvIds != null && bvvIds.size() > 0) {
            for (int bvvId : bvvIds) {
                TcVeEntity tcVeEntity = tcVeRepository.getByBvvId(bvvId);
                if (tcVeEntity != null) {
                    if (LenhConstants.LENH_DON == type) {
                        if (tcVeEntity.getTcLenhId() != null && tcVeEntity.getTcLenhId() > 0) {
                            LOG.info("[bvvId] Đã được gán lệnh :: {}", bvvId);
                            throw new TransportException("[bvvId] Đã được gán lệnh :: " + bvvId);
                        }
                        tcVeEntity.setLaiXeIdDon(0);
                        tcVeEntity.setGhiChuDon(Strings.EMPTY);
                        tcVeEntity.setThuTuDon(0);
                        tcVeEntity.setThoiGianDon(0);
                        tcVeEntity.setTcTrangThaiDon(VeConstants.TC_STATUS_XE_NGOAI_DON);
                        tcVeEntity.setTcLenhId(lenhId);
                        tcVeEntity.setLyDoTuChoiDon(Strings.EMPTY);
                    } else if (LenhConstants.LENH_TRA == type) {
                        if (tcVeEntity.getTcLenhTraId() != null && tcVeEntity.getTcLenhTraId() > 0) {
                            LOG.info("[bvvId] Đã được gán lệnh :: {}", bvvId);
                            throw new TransportException("[bvvId] Đã được gán lệnh :: " + bvvId);
                        }
                        tcVeEntity.setLaiXeIdTra(taiXeId);
                        tcVeEntity.setGhiChuTra(Strings.EMPTY);
                        tcVeEntity.setThoiGianTra(0);
                        tcVeEntity.setTcTrangThaiTra(VeConstants.TC_STATUS_XE_NGOAI_TRA);
                        tcVeEntity.setTcLenhTraId(0);
                        tcVeEntity.setLyDoTuChoiTra(Strings.EMPTY);
                    }
                    //neu truong hop dieu bo sung thi la khach hang moi va nguoc lai
                    tcVeEntity.setKhachHangMoi(kieu == 1 ? 1 : 0);
                    //Save into DB
                    tcVeRepository.save(tcVeEntity);
                    if (kieu == 1) {
                        String typeX = type == LenhConstants.LENH_TRA ? "trả" : "đón";
                        String data = "{\"Điều xe ngoài " + typeX + " \"" + ":[\"0\",\"1\"]}";
                        commonService.wrieZzLog(String.valueOf(SecurityUtils.getCurrentUserLogin()), tcVeEntity.getBvvId(), data, "ĐIỀU XE NGOÀI");
                    }

                } else {
                    LOG.info("[bvvId] Không tồn tại :: {}", bvvId);
                    throw new TransportException("[bvvId] Không tồn tại :: " + bvvId);
                }
            }
        }
    }

    private void saveVeDonCtv(VeForm veForm, int lenhId, int taiXeId, int lenhStatus, int kieu, DistenceCtvVtc distenceCtvVtc) {
        List<Integer> bvvIds = veForm.getBvvIds();
        if (!CollectionUtils.isEmpty(bvvIds)) {
            for (int bvvId : bvvIds) {
                TcVeEntity tcVeEntity = tcVeRepository.getByBvvId(bvvId);
                if (tcVeEntity != null) {
                    tcVeEntity.setLaiXeIdDon(taiXeId);
                    tcVeEntity.setGhiChuDon(veForm.getGhiChu());
                    tcVeEntity.setThuTuDon(veForm.getThuTu());
                    tcVeEntity.setThoiGianDon((int) (Math.round(distenceCtvVtc.getDisctence())));
                    tcVeEntity.setTcTrangThaiDon(lenhStatus);
                    tcVeEntity.setTcLenhId(lenhId);
                    tcVeEntity.setLyDoTuChoiDon(Strings.EMPTY);
                    tcVeEntity.setKhachHangMoi(kieu == 1 ? 1 : 0);
                    //Save into DB
                    tcVeRepository.save(tcVeEntity);
                } else {
                    throw new TransportException("Chưa Tồn Tại vé BvvID:" + bvvId);
                }
            }
        }
    }

    /**
     * Send noti and data to firebase
     *
     * @param driverId
     * @param type
     */

    private void sendNotifyToDriver(int driverId, int type, Object data, String audio) {
        String title = messageConfig.getMessage(Constant.MSG_FIREBASE_DIEU_BO_SUNG_DON);
        String topicNameFromCache = getDefaultTopic();;
        String topicName = topicNameFromCache + driverId;
        if (LenhConstants.LENH_TRA == type) {
            title = messageConfig.getMessage(Constant.MSG_FIREBASE_DIEU_BO_SUNG_TRA);
            topicName = topicNameFromCache + "TRA-" + driverId;
        }
        firebaseClientService.sendNotification(driverId, title, title, data, audio);
        firebaseClientService.sendDataToTopicFirebase(topicName, data);
    }

    private String getDefaultTopic() {
        return CacheData.CONFIGURATION_DATA.get(Constant.FIREBASE_TOPIC_NAME);
    }

    private void sendNotiAndData(int taiXeId, int type, boolean isHavazNow) {
        String title = messageConfig.getMessage(Constant.MSG_FIREBASE_DIEU_BO_SUNG_DON);
        String topicNameFromCache = getDefaultTopic();
        String topicName = topicNameFromCache + taiXeId;
        if (LenhConstants.LENH_TRA == type) {
            title = messageConfig.getMessage(Constant.MSG_FIREBASE_DIEU_BO_SUNG_TRA);
            topicName = topicNameFromCache + "TRA-" + taiXeId;
        }
//        firebaseClientService.sendNotification(taiXeId, title, title, new FirebaseNotiDataForm("", "", type, isHavazNow));
        firebaseClientService.sendDataToTopic(new Object(), topicName);
    }

    private void sendNotifyToDriverTicketCancel(int driverId, int type, Object data, String audio) {
        String title = messageConfig.getMessage(Constant.MSG_FIREBASE_TICKET_CANCEL);
        String topicNameFromCache = getDefaultTopic();
        String topicName = topicNameFromCache + driverId;
        if (LenhConstants.LENH_TRA == type) {
            title = messageConfig.getMessage(Constant.MSG_FIREBASE_DIEU_BO_SUNG_TRA);
            topicName = topicNameFromCache + "TRA-" + driverId;
        }
        firebaseClientService.sendNotification(driverId, title, title, data, audio);
        firebaseClientService.sendDataToTopicFirebase(topicName, data);
    }
    private int checkDieuKienPhanLichCuaLaiXe(int laiXeId, DieuDoForm dieuDoForm) {

        //Kiem tra lai xe da duoc phan lich cho xe nao chua
        int xeId = getXeIdForLaiXeId(laiXeId, dieuDoForm.getDanhSachVe());
        if (xeId > 0) {
            return xeId;
        } else {
            throw new TransportException("Lái xe này chưa được phân lịch cho xe nào, Vui lòng xem lại trong phân lịch hoặc chấm công");
        }
    }

    private int getXeIdForLaiXeId(int taiXeId, List<VeForm> veForms) {
        try {
            //Lấy ngày chạy lệnh từ danh sách vé
            if (veForms != null && veForms.size() > 0) {
                DieuDoTempEntity dieuDoTempEntity = dieuDoTempRepository.getByTcVeId(veForms.get(0).getBvvIds().get(0));
                if (dieuDoTempEntity != null) {
                    LocalDate date =
                            LocalDateTime.ofInstant(Instant.ofEpochSecond(dieuDoTempEntity.getDidTimeXuatBen()),
                                                    TimeZone.getDefault().toZoneId()).toLocalDate();
                    // Lấy xeId của tài xế trong ngày được phân chạy từ bảng phân lịch
                    return getIdXeTuChamCongHoacPhanLich(taiXeId, date);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return 0;
        }
        return 0;
    }

    @Override
    public int getIdXeTuChamCongHoacPhanLich(int taiXeId, LocalDate date) {
        TcChamCongEntity tcChamCongEntity = tcChamCongRepository.findTopByTaiXeIdAndNgayChamCongOrderByCreatedDateDesc(taiXeId, date);
        List<TcChamCongEntity> tcChamCongEntities = tcChamCongRepository.findByTaiXeIdAndNgayChamCongAndCaTruc(taiXeId,date);
        if(CollectionUtils.isEmpty(tcChamCongEntities)){
            if (tcChamCongEntity != null) {
                int xeId = tcChamCongEntity.getXeId();
                if (xeId > 0) {
                    return xeId;
                }
            } else {
                TcLichTrucEntity tcLichTrucEntity = tcLichTrucRepository.findTopByNgayTrucAndTaiXeId(date + "", taiXeId);
                if (tcLichTrucEntity != null) {
                    int xeId = tcLichTrucEntity.getXeId();
                    if (xeId > 0) {
                        return xeId;
                    }
                }
            }
        }else {
            int xeId = tcChamCongEntities.get(0).getXeId();
            if (xeId > 0) {
                return xeId;
            }
        }
        return 0;
    }

    @Override
    public XeTcDTO getAttributeXe(int taixeId, LocalDate date) {
        int xeId = getIdXeTuChamCongHoacPhanLich(taixeId,date);
        if(xeId != 0){
            XeTcDTO xeTcDTO = new XeTcDTO();
            Optional<XeEntity> xeEntityOptional = xeRepository.findById(xeId);
            if(xeEntityOptional.isPresent()){
                XeEntity xeEntity = xeEntityOptional.get();               ;
                xeTcDTO.setXeId(xeEntity.getXeId());
                xeTcDTO.setSdtXe(xeEntity.getXeSoDienThoai());
                xeTcDTO.setBks(xeEntity.getXeBienKiemSoat());
                xeTcDTO.setSeats(xeEntity.getXeSoCho());
            }
            return xeTcDTO;
        } else {
            return null;
        }

    }

    @Override
    @Transactional
    public void dieuLenhHub(GetCmdHubRequest getCmdHubRequest) {
        Integer lenhId = getCmdHubRequest.getLenhId();
        Integer taixeId = getCmdHubRequest.getTaixeId();
        Integer xeId = getCmdHubRequest.getXeId();
        //Set trang thái lệnh
        Optional<TcLenhEntity> optionalTcLenhEntity = tcLenhRepository.findByTcLenhIdAndTrangThai(lenhId,
                LenhConstants.LENH_STATUS_DA_TAO);
        if (optionalTcLenhEntity.isPresent()) {
            TcLenhEntity tcLenhEntity = optionalTcLenhEntity.get();
            tcLenhEntity.setLaiXeId(taixeId);
            tcLenhEntity.setXeId(xeId);
            tcLenhEntity.setTrangThai(LenhConstants.LENH_STATUS_DA_DIEU);
            tcLenhRepository.save(tcLenhEntity);
            List<TcVeEntity> tcVeEntities = tcVeRepository.findByTcLenhId(lenhId);
            tcVeEntities.forEach(tcVeEntity -> {
                tcVeEntity.setLaiXeIdDon(taixeId);
                tcVeRepository.save(tcVeEntity);
            });
        } else {
            throw new ResourceNotfoundException("Không tìm thấy lệnh id = " + lenhId);
        }
    }

    @Override
    @Transactional
    public void cancelLenhHub(Integer lenhId) {
        Optional<TcLenhEntity> optionalTcLenhEntity = tcLenhRepository.findByTcLenhIdAndTrangThai(lenhId,
                LenhConstants.LENH_STATUS_DA_TAO);
        if (optionalTcLenhEntity.isPresent()) {
            TcLenhEntity tcLenhEntity = optionalTcLenhEntity.get();
            tcLenhEntity.setTrangThai(LenhConstants.LENH_STATUS_DA_BI_HUY);
            tcLenhRepository.save(tcLenhEntity);
            List<TcVeEntity> tcVeEntities = tcVeRepository.findByTcLenhId(lenhId);
            tcVeEntities.forEach(tcVeEntity -> {
                tcVeEntity.setTcLenhId(Constant.ZERO);
                tcVeEntity.setTcTrangThaiDon(VeConstants.TC_STATUS_CHUA_DIEU);
                tcVeRepository.save(tcVeEntity);
            });
        } else {
            throw new ResourceNotfoundException("Không tìm thấy lênh id = "+ lenhId);
        }
    }
    public XeTcDTO getAttributeXe(int xeId) {
        if(xeId != 0){
            XeTcDTO xeTcDTO = new XeTcDTO();
            Optional<XeEntity> xeEntityOptional = xeRepository.findById(xeId);
            if(xeEntityOptional.isPresent()){
                XeEntity xeEntity = xeEntityOptional.get();
                xeTcDTO.setXeId(xeEntity.getXeId());
                xeTcDTO.setSdtXe(xeEntity.getXeSoDienThoai());
                xeTcDTO.setBks(xeEntity.getXeBienKiemSoat());
                xeTcDTO.setSeats(xeEntity.getXeSoCho());
            }
            return xeTcDTO;
        } else {
            return null;
        }

    }
    //    //API VUNG TRUNG CHUYEN
//
//    //API 1 - Lấy danh sách vùng trung chuyển
//    @Override
//    public List<TcVttDTO> getListVungTrungChuyen() {
//        try {
//            List<TcVungTrungChuyenEntity> tcVungTrungChuyenEntities = tcVungTrungChuyenRepository.findAll(Sort.by(Sort.Direction.ASC, "tcVttId"));
//            List<TcVttDTO> tcVttDTOS = new ArrayList<>();
//            convertTcVttTcVungTrungChuyenEntityToTcVttDTO(tcVungTrungChuyenEntities, tcVttDTOS);
//
////            File fXmlFile = new File("C:/Users/havaz/Downloads/Havaz.kml");
////            Coordinate[] coordinates = CommonUtils.getCoordinate(fXmlFile);
////
////            GeometryFactory geometryFactory = new GeometryFactory();
////            LinearRing linear = new GeometryFactory().createLinearRing(coordinates);
////            Polygon poly = new Polygon(linear, null, geometryFactory);
////            TcVungTrungChuyenEntity tcVungTrungChuyenEntity = new TcVungTrungChuyenEntity();
////            tcVungTrungChuyenEntity.setTcVttName("HaNoi");
////            tcVungTrungChuyenEntity.setTcVttCode("HN");
////            tcVungTrungChuyenEntity.setTcVttContent(poly);
////            tcVungTrungChuyenRepository.save(tcVungTrungChuyenEntity);
//            return tcVttDTOS;
//        } catch (Exception e) {
//            throw new TransportException(e.getMessage());
//        }
//    }
//
//    //API 2 - Tìm kiếm vùng trung chuyển theo tên vùng
//    @Override
//    public List<TcVttDTO> findVttByTenVung(String tenVung) {
//        try {
//            List<TcVungTrungChuyenEntity> tcVungTrungChuyenEntities = tcVungTrungChuyenRepository.findByTcVttNameContainingOrderByTcVttIdAsc(tenVung);
//            List<TcVttDTO> tcVttDTOS = new ArrayList<>();
//            convertTcVttTcVungTrungChuyenEntityToTcVttDTO(tcVungTrungChuyenEntities, tcVttDTOS);
//            return tcVttDTOS;
//        } catch (BeansException e) {
//            throw new TransportException(e.getMessage());
//        }
//    }
//
//    private void convertTcVttTcVungTrungChuyenEntityToTcVttDTO(List<TcVungTrungChuyenEntity> tcVungTrungChuyenEntities, List<TcVttDTO> tcVttDTOs) {
//        if (tcVungTrungChuyenEntities != null && tcVungTrungChuyenEntities.size() > 0) {
//            for (TcVungTrungChuyenEntity tcVungTrungChuyenEntity : tcVungTrungChuyenEntities) {
//                TcVttDTO tcVttDTO = new TcVttDTO();
//                BeanUtils.copyProperties(tcVungTrungChuyenEntity, tcVttDTO);
//                tcVttDTO.setTcVttContent(tcVungTrungChuyenEntity.getTcVttContent().toString());
//                tcVttDTO.setCreatedDate(new Date(tcVungTrungChuyenEntity.getCreatedDate().getTime()));
//                tcVttDTOs.add(tcVttDTO);
//            }
//        }
//    }
//
//    //API 3 - Tạo mới vùng trung chuyển
//    @Override
//    public void addTransportArea(VungTcDTO vungTcDTO) {
//        try {
//            File file = new File(vungTcDTO.getTcVttContent());
////            Coordinate[] coordinates = CommonUtils.getCoordinate(file);
////            GeometryFactory geometryFactory = new GeometryFactory();
////            LinearRing linear = new GeometryFactory().createLinearRing(coordinates);
////            Polygon poly = new Polygon(linear, null, geometryFactory);
//
//
////            Path path = Paths.get(vungTcDTO.getPathFileContent().getOriginalFilename());
////            Path file = Files.write(path,bytes);
//            Polygon poly = createPolygon(file);
//            TcVungTrungChuyenEntity tcVungTrungChuyenEntity = new TcVungTrungChuyenEntity();
//            tcVungTrungChuyenEntity.setTcVttName(vungTcDTO.getTcVttName());
//            tcVungTrungChuyenEntity.setTcVttCode(vungTcDTO.getTcVttCode());
//            tcVungTrungChuyenEntity.setCreatedBy("");
//
//            LocalDateTime createdDate = LocalDateTime.now();
//            tcVungTrungChuyenEntity.setCreatedDate(createdDate);
//            tcVungTrungChuyenEntity.setTcVttContent(poly);
//            tcVungTrungChuyenEntity.setLastUpdatedBy("");
//            tcVungTrungChuyenEntity.setLastUpdatedDate(createdDate);
//            tcVungTrungChuyenEntity.setStatus(vungTcDTO.getStatus());
//            tcVungTrungChuyenRepository.save(tcVungTrungChuyenEntity);
//        } catch (Exception e) {
//            throw new TransportException(e.getMessage());
//        }
//    }
//
//    private Polygon createPolygon(File file) {
//        try {
////            File file = new File(filePath);
//            Coordinate[] coordinates = CommonUtils.getCoordinate(file);
//            GeometryFactory geometryFactory = new GeometryFactory();
//            LinearRing linear = new GeometryFactory().createLinearRing(coordinates);
//            Polygon poly = new Polygon(linear, null, geometryFactory);
//            return poly;
//        } catch (Exception e) {
//            throw new TransportException(e.getMessage());
//        }
//    }
//
//    //API 4 - Cập nhật vùng trung chuyển
//    @Override
//    public void updateTransportArea(UpdateVungTcDTO updateVungTcDTO) {
//        try {
//            File file = new File(String.valueOf(updateVungTcDTO.getTcVttContent()));
////            Coordinate[] coordinates = CommonUtils.getCoordinate(file);
////            GeometryFactory geometryFactory = new GeometryFactory();
////            LinearRing linear = new GeometryFactory().createLinearRing(coordinates);
////            Polygon poly = new Polygon(linear, null, geometryFactory);
//            Polygon poly = createPolygon(file);
//            Optional<TcVungTrungChuyenEntity> optionalTcVungTrungChuyenEntity = tcVungTrungChuyenRepository.findById(updateVungTcDTO.getTcVttId());
//            if (optionalTcVungTrungChuyenEntity.isPresent()) {
//                TcVungTrungChuyenEntity tcVungTrungChuyenEntity = optionalTcVungTrungChuyenEntity.get();
//                BeanUtils.copyProperties(updateVungTcDTO, tcVungTrungChuyenEntity);
//                tcVungTrungChuyenEntity.setTcVttName(updateVungTcDTO.getTcVttName());
//                tcVungTrungChuyenEntity.setTcVttCode(updateVungTcDTO.getTcVttCode());
////                tcVungTrungChuyenEntity.setTcVttLocale(updateVungTcDTO.getLocale());
//                LocalDateTime createdDate = LocalDateTime.now();
////                tcVungTrungChuyenEntity.setTcVttContent(poly);
//                tcVungTrungChuyenEntity.setLastUpdatedBy("");
//                tcVungTrungChuyenEntity.setLastUpdatedDate(createdDate);
////                tcVungTrungChuyenEntity.setStatus(updateVungTcDTO.getStatus());
//                tcVungTrungChuyenEntity.setTcVttNote(updateVungTcDTO.getTcVttNote());
//                tcVungTrungChuyenRepository.save(tcVungTrungChuyenEntity);
//            } else {
//                LOG.info("Không tồn tại vùng trung chuyển: " + updateVungTcDTO.getTcVttName() + "trong database");
//            }
//        } catch (Exception e) {
//            throw new TransportException(e.getMessage());
//        }
//    }
//
//    //API 5 - Đổi trạng thái vùng trung chuyển
//    @Override
//    public void switchStatusTransportArea(int tcVttId, int trangThai) {
//        try {
//            Optional<TcVungTrungChuyenEntity> optionalTcVungTrungChuyenEntity = tcVungTrungChuyenRepository.findById(tcVttId);
//            if (optionalTcVungTrungChuyenEntity.isPresent()) {
//                TcVungTrungChuyenEntity tcVungTrungChuyenEntity = optionalTcVungTrungChuyenEntity.get();
//
//                LocalDateTime createdDate = LocalDateTime.now();
//                tcVungTrungChuyenEntity.setLastUpdatedBy("");
//                tcVungTrungChuyenEntity.setLastUpdatedDate(createdDate);
////                tcVungTrungChuyenEntity.setStatus(trangThai);
//                tcVungTrungChuyenRepository.save(tcVungTrungChuyenEntity);
//            } else {
//                LOG.info("Không tồn tại vùng trung chuyển có mã vùng: " + tcVttId + "trong database");
//            }
//        } catch (Exception e) {
//            throw new TransportException(e.getMessage());
//        }
//    }
}
