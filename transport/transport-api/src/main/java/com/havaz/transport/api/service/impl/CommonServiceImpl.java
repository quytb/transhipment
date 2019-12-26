package com.havaz.transport.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.havaz.transport.api.common.AudioType;
import com.havaz.transport.api.common.CacheData;
import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.common.LenhConstants;
import com.havaz.transport.api.common.ManagerNotificationType;
import com.havaz.transport.api.common.NotificationType;
import com.havaz.transport.api.common.VtcCtvConstants;
import com.havaz.transport.api.configuration.MessageConfig;
import com.havaz.transport.api.form.CustomerRank;
import com.havaz.transport.api.form.CustomerRankData;
import com.havaz.transport.api.form.CustomerRankForm;
import com.havaz.transport.api.form.DieuDoForm;
import com.havaz.transport.api.form.FirebaseNotiDataForm;
import com.havaz.transport.api.form.VeActionForm;
import com.havaz.transport.api.form.VeForm;
import com.havaz.transport.api.form.location.Point;
import com.havaz.transport.api.model.ClientHavazNowDTO;
import com.havaz.transport.api.model.CmdHavazNowDTO;
import com.havaz.transport.api.model.ConDataDTO;
import com.havaz.transport.api.model.DistenceCtvVtc;
import com.havaz.transport.api.model.HuyLenhTcDTO;
import com.havaz.transport.api.model.TripStationDTO;
import com.havaz.transport.api.repository.TcLenhRepositoryCustom;
import com.havaz.transport.api.service.CommonService;
import com.havaz.transport.api.service.DieuDoService;
import com.havaz.transport.api.service.FirebaseClientService;
import com.havaz.transport.api.service.LenhService;
import com.havaz.transport.api.service.LocationService;
import com.havaz.transport.api.service.NotificationService;
import com.havaz.transport.api.service.VungTrungChuyenService;
import com.havaz.transport.api.service.XeTuyenService;
import com.havaz.transport.api.utils.DateTimeUtils;
import com.havaz.transport.api.utils.SecurityUtils;
import com.havaz.transport.core.constant.VeConstants;
import com.havaz.transport.core.exception.TransportException;
import com.havaz.transport.core.utils.Strings;
import com.havaz.transport.dao.entity.AdminLv2UserEntity;
import com.havaz.transport.dao.entity.BanVeVeEntity;
import com.havaz.transport.dao.entity.BenXeEntity;
import com.havaz.transport.dao.entity.ConfigurationEntity;
import com.havaz.transport.dao.entity.TcConfigurationEntity;
import com.havaz.transport.dao.entity.TcLenhEntity;
import com.havaz.transport.dao.entity.TcVeEntity;
import com.havaz.transport.dao.entity.TcVtcCtvEntity;
import com.havaz.transport.dao.entity.ZzLogBanVeEntity;
import com.havaz.transport.dao.repository.AdminLv2UserRepository;
import com.havaz.transport.dao.repository.BanVeVeRepository;
import com.havaz.transport.dao.repository.BenXeEntityRepository;
import com.havaz.transport.dao.repository.ConfigRepository;
import com.havaz.transport.dao.repository.TcConfigRepository;
import com.havaz.transport.dao.repository.TcLenhRepository;
import com.havaz.transport.dao.repository.TcVeRepository;
import com.havaz.transport.dao.repository.TcVtcCtvRepository;
import com.havaz.transport.dao.repository.TcZzLogRepository;
import com.havaz.transport.dao.repository.TuyenDiemDonTraRepository;
import com.havaz.transport.dao.repository.TuyenRepository;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Service
@Transactional(readOnly = true)
public class CommonServiceImpl implements CommonService {

    private static final Logger log = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Value("${havaz.number-driver-limit}")
    private Integer numberDriver;

    @Value("${havaz.number-minute-limit}")
    private Integer limitTime;

    @Value("${havaz.stomp.topic}")
    private String stompTopic;

    @Value("${havaz.firebase.cmd.havaznow.topic}")
    private String topicHavazNow;

    @Value("${havaz.stomp.topic}")
    private String topic;

    @Value("${havaz.url-san}")
    private String URL_HAVAZ;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private TcConfigRepository tcConfigRepository;

    @Autowired
    private BanVeVeRepository banVeVeRepository;

    @Autowired
    private TcVeRepository tcVeRepository;

    @Autowired
    private TcLenhRepository tcLenhRepository;

    @Autowired
    private TcLenhRepositoryCustom tcLenhRepositoryCustom;

    @Autowired
    private AdminLv2UserRepository adminLv2UserRepository;

    @Autowired
    private FirebaseClientService firebaseClientService;

    @Autowired
    private MessageConfig messageConfig;

    @Autowired
    private TuyenRepository tuyenRepository;

    @Autowired
    private TuyenDiemDonTraRepository tuyenDiemDonTraRepository;

    @Autowired
    private VungTrungChuyenService vungTrungChuyenService;

    @Autowired
    private TcVtcCtvRepository vtcCtvRepository;

    @Autowired
    private DieuDoService dieuDoService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private LenhService lenhService;

    @Autowired
    private TcZzLogRepository ZzLogRepository;

    @Autowired
    private XeTuyenService xeTuyenService;

    @Autowired
    private BenXeEntityRepository benXeEntityRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public ConDataDTO layThongTinNhaXe() {
        log.debug("Lấy thông tin nhà xe từ DB.");
        ConDataDTO conDataDTO = new ConDataDTO();
        ConfigurationEntity configById = configRepository.findById(Constant.CONFIGURATION_NO_1).orElse(null);
        if (configById != null) {
            String conData = configById.getConData();
            String conDataDecode = new String(Base64.decodeBase64(conData), StandardCharsets.UTF_8);
            try {
                conDataDTO = objectMapper.readValue(conDataDecode, ConDataDTO.class);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
        }
        log.debug("Đã lấy thông tin nhà xe thành công.");
        return conDataDTO;
    }

    @Override
    @Transactional
    public void transferVeERPToVeTC(VeActionForm veActionForm) {
        log.debug("Transfer Action: {}", veActionForm);
        if (VeConstants.VE_ACTION.get(VeConstants.CHUYEN_CHO).equals(veActionForm.getAction())) {
            //Process move ve
            transferVeMove(veActionForm);
        } else if (VeConstants.VE_ACTION.get(VeConstants.HUY_VE).equals(veActionForm.getAction())) {
            //Process delete ve
            transferVeDelete(veActionForm);
        } else if (VeConstants.VE_ACTION.get(VeConstants.XUONG_XE).equals(veActionForm.getAction())) {
            //Process xuong xe
            transferVeXuongXe(veActionForm);
        } else if (VeConstants.VE_ACTION.get(VeConstants.TAO_MOI_VE).equals(veActionForm.getAction()) ||
                VeConstants.VE_ACTION.get(VeConstants.CAP_NHAT_VE).equals(veActionForm.getAction())) {
            //Process create or update
            transferVe(veActionForm);
        }
        log.info("Transfer Ve from ERP done.");
   }

    @Override
    @Transactional
    public void updateTransferVeERPToVeTC(VeActionForm veActionForm) {
        List<Integer> bvvIds = veActionForm.getBvvIds();
        if (!CollectionUtils.isEmpty(bvvIds)) {
            this.updateTcVe(bvvIds);
            //Auto create command for driver
            final int isCreate = 1;
            List<TcVeEntity> listVe = tcVeRepository.findByBvvIds(bvvIds);
            if (!listVe.isEmpty() && listVe.get(0).getVeAction() == isCreate
                    && BooleanUtils.isTrue(listVe.get(0).getTcIsHavazNow())) {
                autoCreateLenh(veActionForm, false);
            }
        }
    }

    @Override
    public Map<String, String> getAllConfig() {
        log.info("Lấy configuration.");
        Map<String, String> allConfig = new HashMap<>();
        List<TcConfigurationEntity> tcConfigDaoAll = tcConfigRepository.findAll();
        if (!tcConfigDaoAll.isEmpty()) {
            for (TcConfigurationEntity config : tcConfigDaoAll) {
                allConfig.put(config.getConfigKey(), config.getConfigValue());
            }
            log.info("Đã lấy được configuration.");
        } else {
            log.info("Không có configuration.");
        }
        return allConfig;
    }

    private void transferVeMove(VeActionForm veActionForm) {
        int bvvIdFrom = veActionForm.getBvvIdFrom();
        int bvvIdTo = veActionForm.getBvvIdTo();
        log.debug("Move bvvIdFrom: {} --> bvvIdTo: {}", bvvIdFrom, bvvIdTo);
        BanVeVeEntity banVeVeEntity = banVeVeRepository.findById(bvvIdTo).orElse(null);
        log.debug("banVeVeEntity={}", banVeVeEntity);

        if (banVeVeEntity != null) {
            int laiXeIdDon = 0;
            int laiXeIdTra = 0;
            String phoneDi = Strings.EMPTY;
            String khachDi = Strings.EMPTY;
            int didId = 0;
            //Update new bvvId
            TcVeEntity tcVeEntity = tcVeRepository.getByBvvId(bvvIdFrom);
            if (tcVeEntity != null) {
                if (tcVeEntity.getDidId() != null) {
                    didId = tcVeEntity.getDidId();
                }
                laiXeIdDon = tcVeEntity.getLaiXeIdDon();
                laiXeIdTra = tcVeEntity.getLaiXeIdTra();
                phoneDi = tcVeEntity.getBvvPhoneDi();
                khachDi = tcVeEntity.getBvvTenKhachHangDi();
                if (!Objects.equals(tcVeEntity.getDidId(), banVeVeEntity.getBvvBvnId())) {
                    //Ket thuc lenh khi khong co ve nao
                    checkDataAndCancelLenh(tcVeEntity, VeConstants.CHUYEN_CHO);
                }
                processUpdateVe(tcVeEntity, banVeVeEntity);
            } else {
                processCreateVe(banVeVeEntity);
            }
            //Send notification to firebase
            TcVeEntity tcVeEntityNew = tcVeRepository.getByBvvId(bvvIdTo);
            if (tcVeEntityNew != null && didId != tcVeEntityNew.getDidId()) {
                sendNotifyToDriver(laiXeIdDon, laiXeIdTra,
                        messageConfig.getMessage(Constant.MSG_FIREBASE_TICKET_MOVE),
                        phoneDi, khachDi, NotificationType.UPDATE_CMD.getType(), AudioType.CANCEL_TICKET.getValue());
                try {
                    List<Integer> bvvIds = Arrays.asList(bvvIdTo);
                    notificationService.saveAndSendNotificationToManager(topic,
                            ManagerNotificationType.CLIENT_UPDATE_INFO.getType(), true, bvvIds);
                } catch (Exception e) {
                    log.warn("Error For Send Notify To Manager ", e);
                }
            }
        } else {
            log.info("Do not get data from [ban_ve_ve] with bvvId: {}", bvvIdTo);
        }
    }

    private void transferVeDelete(VeActionForm veActionForm) {
        List<Integer> bvvIds = veActionForm.getBvvIds();
        if (bvvIds != null && !bvvIds.isEmpty()) {
            int laiXeIdDon = 0;
            int laiXeIdTra = 0;
            String phoneDi = "";
            String khachDi = "";
            for (int bvvId : bvvIds) {
                TcVeEntity tcVeEntity = tcVeRepository.getByBvvId(bvvId);
                if (tcVeEntity != null) {
                    laiXeIdDon = tcVeEntity.getLaiXeIdDon();
                    laiXeIdTra = tcVeEntity.getLaiXeIdTra();
                    phoneDi = tcVeEntity.getBvvPhoneDi();
                    khachDi = tcVeEntity.getBvvTenKhachHangDi();
                    //Ket thuc lenh khi khong co ve nao
                    checkDataAndCancelLenh(tcVeEntity, VeConstants.HUY_VE);
                    tcVeRepository.delete(tcVeEntity);
                }
                log.debug("transferVeDelete:: {}", bvvId);
            }
            sendNotifyToDriver(laiXeIdDon, laiXeIdTra,
                    messageConfig.getMessage(Constant.MSG_FIREBASE_TICKET_CANCEL),
                    phoneDi, khachDi, NotificationType.CANCEL_CMD.getType(), AudioType.CANCEL_TICKET.getValue());
            try {
                List<Integer> arrEmpty = new ArrayList<>();
                notificationService.saveAndSendNotificationToManager(topic,
                        ManagerNotificationType.CLIENT_CANCEL_TRANSPORT.getType(), true, arrEmpty);
            } catch (Exception e) {
                log.warn("Error For Send Notify To Manager ", e);
            }
        } else {
            log.info("transferVeDelete:: bvvIds is empty.");
        }
    }

    private void transferVeXuongXe(VeActionForm veActionForm) {
        List<Integer> bvvIds = veActionForm.getBvvIds();
        if (bvvIds != null && !bvvIds.isEmpty()) {
            log.info("transferVeXuongXe:: {}", bvvIds);
            Set<Integer> setBvvIds = new HashSet<>(bvvIds);
            tcVeRepository.updateVeActionByBvvId(VeConstants.XUONG_XE, setBvvIds);
        } else {
            log.info("transferVeXuongXe:: bvvIds is empty.");
        }
    }

    private void transferVe(VeActionForm veActionForm) {
        List<Integer> bvvIds = veActionForm.getBvvIds();
        int laiXeIdDon = 0;
        int laiXeIdTra = 0;
        String phoneDi = Strings.EMPTY;
        String khachDi = Strings.EMPTY;
        final int firstItem = 0;
        boolean isCreate = true;
        boolean isSendNotification = false;
        if (!CollectionUtils.isEmpty(bvvIds)) {

            List<TcVeEntity> veEntities = tcVeRepository.findByBvvIds(bvvIds);
            if (veEntities.size() > 0) {
                entityManager.detach(veEntities.get(0));
                isSendNotification = true;
            }

            log.debug("transferVe:: {}", bvvIds);
            for (int bvvId : bvvIds) {
                BanVeVeEntity banVeVeEntity = banVeVeRepository.findById(bvvId).orElse(null);
                log.debug("{}", banVeVeEntity);

                if (banVeVeEntity != null) {
                    TcVeEntity tcVeEntity = tcVeRepository.getByBvvId(bvvId);
                    //Check ve la moi hay da co san
                    if (tcVeEntity != null) {
                        laiXeIdDon = tcVeEntity.getLaiXeIdDon();
                        laiXeIdTra = tcVeEntity.getLaiXeIdTra();
                        phoneDi = tcVeEntity.getBvvPhoneDi();
                        khachDi = tcVeEntity.getBvvTenKhachHangDi();
                        processUpdateVe(tcVeEntity, banVeVeEntity);
                    } else{
                        processCreateVe(banVeVeEntity);
                    }

                } else {
                    log.info("Do not get data from [ban_ve_ve] with bvvId: {}", bvvId);
                }
            }
            TcVeEntity tcVeEntity = tcVeRepository.getByBvvId(bvvIds.get(firstItem));
            try {
                if (tcVeEntity == null && isSendNotification) {
                    sendNotifyToDriver(laiXeIdDon, laiXeIdTra, messageConfig.getMessage(Constant.MSG_FIREBASE_TICKET_UPDATE),
                            phoneDi, khachDi, NotificationType.UPDATE_CMD.getType(), AudioType.CANCEL_TICKET.getValue());
                    List<Integer> arr = new ArrayList<>();
                    notificationService.saveAndSendNotificationToManager(topic,
                            ManagerNotificationType.CLIENT_CANCEL_TRANSPORT.getType(), true, arr);
                }
                if (!veEntities.isEmpty() && tcVeEntity != null) {
                    TcVeEntity oldTcVeEntity = veEntities.get(firstItem);
                    isCreate = false;
                    final String audio = AudioType.UPDATE_CMD.getValue();
                    if (oldTcVeEntity.getIsTcDon() != null && tcVeEntity.getIsTcDon() != null &&
                            !Objects.equals(oldTcVeEntity.getIsTcDon(), tcVeEntity.getIsTcDon())) {
                        if (tcVeEntity.getIsTcDon()) {
                            notificationService.saveAndSendNotificationToManager(topic,
                                    ManagerNotificationType.CLIENT_UPDATE_INFO.getType(), true, bvvIds);
                        } else {
                            notificationService.saveAndSendNotificationToManager(topic,
                                    ManagerNotificationType.CLIENT_CANCEL_TRANSPORT.getType(), true, bvvIds);
                        }
                        sendNotifyToDriver(laiXeIdDon, laiXeIdTra,
                                messageConfig.getMessage(Constant.MSG_FIREBASE_TICKET_UPDATE), phoneDi, khachDi, NotificationType.UPDATE_CMD.getType(), audio);
                    } else if (oldTcVeEntity.getIsTcTra() != null && tcVeEntity.getIsTcTra() != null
                            && !Objects.equals(oldTcVeEntity.getIsTcTra(), tcVeEntity.getIsTcTra())) {
                        sendNotifyToDriver(laiXeIdDon, laiXeIdTra, messageConfig.getMessage(Constant.MSG_FIREBASE_TICKET_UPDATE),
                                phoneDi, khachDi, NotificationType.UPDATE_CMD.getType(), audio);
                        if(tcVeEntity.getIsTcTra()) {
                            notificationService.saveAndSendNotificationToManager(topic,
                                    ManagerNotificationType.CLIENT_UPDATE_INFO.getType(), false, bvvIds);
                        } else {
                            notificationService.saveAndSendNotificationToManager(topic,
                                    ManagerNotificationType.CLIENT_CANCEL_TRANSPORT.getType(), false, bvvIds);
                        }

                    } else if (StringUtils.isNotEmpty(tcVeEntity.getBvvPhoneDi()) && StringUtils.isNotEmpty(oldTcVeEntity.getBvvPhoneDi())
                            && !oldTcVeEntity.getBvvPhoneDi().equalsIgnoreCase(tcVeEntity.getBvvPhoneDi())) {
                        sendNotifyToDriver(laiXeIdDon, laiXeIdTra,
                                messageConfig.getMessage(Constant.MSG_FIREBASE_TICKET_UPDATE), phoneDi, khachDi,
                                NotificationType.UPDATE_CMD.getType(), audio);
                        notificationService.saveAndSendNotificationToManager(topic,
                                ManagerNotificationType.CLIENT_UPDATE_INFO.getType(), true, bvvIds);
                    } else if (StringUtils.isNotEmpty(oldTcVeEntity.getBvvTenKhachHangDi()) && StringUtils.isNotEmpty(tcVeEntity.getBvvTenKhachHangDi())
                            && !oldTcVeEntity.getBvvTenKhachHangDi().equalsIgnoreCase(tcVeEntity.getBvvTenKhachHangDi())) {
                        sendNotifyToDriver(laiXeIdDon, laiXeIdTra,
                                messageConfig.getMessage(Constant.MSG_FIREBASE_TICKET_UPDATE), phoneDi, khachDi,
                                NotificationType.UPDATE_CMD.getType(), audio);
                        notificationService.saveAndSendNotificationToManager(topic,
                                ManagerNotificationType.CLIENT_UPDATE_INFO.getType(), true, bvvIds);

                    } else if (StringUtils.isNotEmpty(tcVeEntity.getBvvDiemDonKhach()) && StringUtils.isNotEmpty(oldTcVeEntity.getBvvDiemDonKhach())
                            && !oldTcVeEntity.getBvvDiemDonKhach().equalsIgnoreCase(tcVeEntity.getBvvDiemDonKhach()) && BooleanUtils.isTrue(tcVeEntity.getIsTcDon())) {
                        sendNotifyToDriver(laiXeIdDon, laiXeIdTra,
                                messageConfig.getMessage(Constant.MSG_FIREBASE_TICKET_UPDATE), phoneDi, khachDi,
                                NotificationType.UPDATE_CMD.getType(), audio);
                        notificationService.saveAndSendNotificationToManager(topic,
                                ManagerNotificationType.CLIENT_UPDATE_INFO.getType(), true, bvvIds);
                    } else if (StringUtils.isNotEmpty(tcVeEntity.getBvvDiemTraKhach()) && StringUtils.isNotEmpty(oldTcVeEntity.getBvvDiemTraKhach())
                            && !oldTcVeEntity.getBvvDiemTraKhach().equalsIgnoreCase(tcVeEntity.getBvvDiemTraKhach()) && BooleanUtils.isTrue(tcVeEntity.getIsTcTra())) {
                        sendNotifyToDriver(laiXeIdDon, laiXeIdTra,
                                messageConfig.getMessage(Constant.MSG_FIREBASE_TICKET_UPDATE), phoneDi, khachDi,
                                NotificationType.UPDATE_CMD.getType(), audio);
                        notificationService.saveAndSendNotificationToManager(topic,
                                ManagerNotificationType.CLIENT_UPDATE_INFO.getType(), false, bvvIds);
                    } else if (StringUtils.isNotEmpty(oldTcVeEntity.getBvvGhiChu()) && StringUtils.isNotEmpty(tcVeEntity.getBvvGhiChu())
                            && !oldTcVeEntity.getBvvGhiChu().equalsIgnoreCase(tcVeEntity.getBvvGhiChu())) {
                        sendNotifyToDriver(laiXeIdDon, laiXeIdTra,
                                messageConfig.getMessage(Constant.MSG_FIREBASE_TICKET_UPDATE), phoneDi, khachDi,
                                NotificationType.UPDATE_CMD.getType(), audio);
                        notificationService.saveAndSendNotificationToManager(topic,
                                ManagerNotificationType.CLIENT_UPDATE_INFO.getType(), true, bvvIds);
                    }
                }
            } catch (Exception e) {
                log.warn("Error For Send Notify To Manager", e);
            }

            // isCreate Ve
            if (isCreate) {
                createVe(veActionForm);
            }

        } else {
            log.info("transferVeXuongXe:: bvvIds is empty.");
        }
    }

    private void createVe(VeActionForm veActionForm) {
        List<Integer> bvvIds = veActionForm.getBvvIds();
        List<TcVeEntity> listVe = tcVeRepository.findByBvvIds(bvvIds);
        if (!listVe.isEmpty()) {
            TcVeEntity tcVeEntityFirst = listVe.get(0);
            try {
                if (tcVeEntityFirst.getIsTcDon()) {
                    notificationService.saveAndSendNotificationToManager(topic,
                                                                         ManagerNotificationType.NEW_TICKET.getType(), true, bvvIds);
                } else {
                    notificationService.saveAndSendNotificationToManager(topic,
                                                                         ManagerNotificationType.NEW_TICKET.getType(), false, bvvIds);
                }
            } catch (Exception e) {
                log.warn("Error For Send Notify To Manager", e);
            }
        }
    }
    private synchronized void autoCreateLenh(VeActionForm veActionForm, boolean isTra) {
        //Todo Auto Create Lenh
        DieuDoForm form = new DieuDoForm();
        VeForm veForm = new VeForm();
        veForm.setGhiChu("Tự động tạo lệnh");
        form.setAction("Tự động tạo lệnh");
        veForm.setBvvIds(veActionForm.getBvvIds());

        List<VeForm> veForms = new ArrayList<>();
        veForms.add(veForm);
        form.setDanhSachVe(veForms);
        //loại lái Xe Đã được điều lệnh hoặc Đang thực hiện lenh
        final Integer cmdDaDieu = 1;
        final Integer cmdDangDon = 2;
        List<Integer> cmdStatus = Arrays.asList(cmdDangDon);
        List<TcVeEntity> veEntities = tcVeRepository.findByBvvIds(veActionForm.getBvvIds());
        List<TcVtcCtvEntity> listVtcCtv;
        List<TcVtcCtvEntity> listDriverRunning;
        final boolean isCmdNow = true;
        if (!veEntities.isEmpty()) {
            TcVeEntity tcVeEntityFirst = veEntities.get(0);
            if (isTra) {
                listVtcCtv = vtcCtvRepository.findVtcDriverNotBusy(tcVeEntityFirst.getVungTCTra(),
                                                                   VtcCtvConstants.STATUS_ACTIVE, cmdStatus);
                listDriverRunning = vtcCtvRepository.findByDriverByCmdRunning(
                        tcVeEntityFirst.getVungTCTra(), cmdDangDon, isCmdNow, VtcCtvConstants.STATUS_ACTIVE);
            } else {
                listVtcCtv = vtcCtvRepository.findVtcDriverNotBusy(tcVeEntityFirst.getVungTCDon(),
                                                                   VtcCtvConstants.STATUS_ACTIVE, cmdStatus);
                listDriverRunning = vtcCtvRepository.findByDriverByCmdRunning(
                        tcVeEntityFirst.getVungTCDon(), cmdDangDon, isCmdNow, VtcCtvConstants.STATUS_ACTIVE);
            }
            if (!listDriverRunning.isEmpty()) {
                listVtcCtv.addAll(listDriverRunning);
            }
            SortedSet<DistenceCtvVtc> distenceCtvVtcs = new TreeSet<>();
            listVtcCtv.forEach(tcVtcCtvEntity -> {
                DistenceCtvVtc distenceCtvVtc = new DistenceCtvVtc();
                distenceCtvVtc.setTcVtcCtvEntity(tcVtcCtvEntity);
                distenceCtvVtc.setDisctence(locationService.getDurationFromAPI(
                        new Point(tcVeEntityFirst.getBvvLatStart(), tcVeEntityFirst
                                .getBvvLongStart()),
                        new Point(tcVtcCtvEntity.getCoordinateLat(), tcVtcCtvEntity.getCoordinateLong())));
                distenceCtvVtcs.add(distenceCtvVtc);
            });
            int i = 0;
            for (DistenceCtvVtc distenceCtvVtc : distenceCtvVtcs) {
                if (i < numberDriver) {

                    //create cmd with driver no cmd
                    taskScheduler.schedule(new Runnable() {
                        @Override
                        public void run() {
                            form.setTaiXeId(distenceCtvVtc.getTcVtcCtvEntity().getTcCtvId());
                            createLenh(form, distenceCtvVtc, isTra);
                        }

                    }, LocalDateTime.now().plus(i * limitTime, ChronoField.MILLI_OF_DAY.getBaseUnit()).atZone(ZoneId.systemDefault()).toInstant());
                }
                i++;
                // Tạo Schedule cho thằng cuối cùng nếu quá 30 không confirm  sẽ hủy lệnh

                if (i == distenceCtvVtcs.size()) {
                    taskScheduler.schedule(new Runnable() {
                        @Override
                        public void run() {
                            final int firstItem = 0;
                            CmdHavazNowDTO cmdHavazNowDTO = new CmdHavazNowDTO();
                            cmdHavazNowDTO.setBvvIds(form.getDanhSachVe().get(firstItem).getBvvIds());
                            lenhService.cancelPickup(cmdHavazNowDTO);
                            notificationService.sendNotificationToWebSocket(stompTopic, form.getDanhSachVe().get(firstItem).getBvvIds().toString());
                        }
                    }, LocalDateTime.now().plus(i * limitTime, ChronoField.MILLI_OF_DAY.getBaseUnit()).atZone(ZoneId.systemDefault()).toInstant());
                } else {
                    if (i == numberDriver) {
                        taskScheduler.schedule(new Runnable() {
                            @Override
                            public void run() {
                                final int firstItem = 0;
                                CmdHavazNowDTO cmdHavazNowDTO = new CmdHavazNowDTO();
                                cmdHavazNowDTO.setBvvIds(form.getDanhSachVe().get(firstItem).getBvvIds());
                                lenhService.cancelPickup(cmdHavazNowDTO);
                                notificationService.sendNotificationToWebSocket(stompTopic, form.getDanhSachVe().get(firstItem).getBvvIds().toString());
                            }
                        }, LocalDateTime.now().plus(i * limitTime, ChronoField.MILLI_OF_DAY.getBaseUnit()).atZone(ZoneId.systemDefault()).toInstant());
                        break;
                    }
                }

            }
        } else {
            throw new TransportException("Không Tìm Thấy Vé");
        }
    }

    private synchronized void createLenh(DieuDoForm form, DistenceCtvVtc distenceCtvVtc, boolean isTra) {
        final int isRunning = 2;
        final boolean isHavazNow = true;
        final List<Integer> trangthai = Arrays.asList(1, 3);
        final int firstItem = 0;
        List<TcVeEntity> veEntities = tcVeRepository.findByBvvIds(form.getDanhSachVe().get(0).getBvvIds());
        List<TcLenhEntity> tcLenhEntities = tcLenhRepository.findByLaiXeIdAndTrangThaiAndIsHavazNowIsNot(form.getTaiXeId(), isRunning, isHavazNow);
        if (!tcLenhEntities.isEmpty()) {
            // only create cmd additional
            CmdHavazNowDTO cmdHavazNowDTO = new CmdHavazNowDTO();
            cmdHavazNowDTO.setBvvIds(form.getDanhSachVe().get(firstItem).getBvvIds());
            lenhService.cancelPickup(cmdHavazNowDTO);
            form.setLenhId(tcLenhEntities.get(firstItem).getTcLenhId());
            createCmdAdditional(form);
            return;
        }

        if (veEntities != null) {

            TcVeEntity tcVeEntityFirst = veEntities.get(0);
            if (!isTra) {
                if (tcVeEntityFirst.getTcTrangThaiDon() == null ||
                        tcVeEntityFirst.getTcTrangThaiDon() == 0 ||
                        tcVeEntityFirst.getTcTrangThaiDon() == 1 ||
                        tcVeEntityFirst.getTcTrangThaiDon() == 4) {

                    CmdHavazNowDTO havazNowDTO = new CmdHavazNowDTO();
                    havazNowDTO.setBvvIds(form.getDanhSachVe().get(firstItem).getBvvIds());
                    lenhService.cancelPickup(havazNowDTO);
                    dieuDoService.taoLenhCtv(form, tcVeEntityFirst.getVungTCDon(),
                                             LenhConstants.LENH_DON,
                                             LenhConstants.LENH_STATUS_DA_DIEU, distenceCtvVtc);
                }
            } else {
                // create lenh tra
                if (tcVeEntityFirst.getTcTrangThaiTra() == null ||
                        tcVeEntityFirst.getTcTrangThaiTra() == 0 ||
                        tcVeEntityFirst.getTcTrangThaiTra() == 1 ||
                        tcVeEntityFirst.getTcTrangThaiTra() == 4) {

                    if (tcVeEntityFirst.getTcLenhId() == null) {
                        dieuDoService.taoLenhCtv(form, tcVeEntityFirst.getVungTCTra(),
                                                 LenhConstants.LENH_TRA,
                                                 LenhConstants.LENH_STATUS_DA_DIEU, distenceCtvVtc);
                        return;
                    }
                    Optional<TcLenhEntity> lenhEntity = tcLenhRepository.findByTcLenhIdAndTrangThaiIn(
                            tcVeEntityFirst.getTcLenhId(), trangthai);
                    if (lenhEntity.isPresent()) {
                        HuyLenhTcDTO huyLenhTcDTO = new HuyLenhTcDTO();
                        huyLenhTcDTO.setLenhId(lenhEntity.get().getTcLenhId());
                        if (lenhEntity.get().getTrangThai() == 1) {
                            huyLenhTcDTO.setLyDoHuy("Không Xác Nhận Lệnh!");
                            CmdHavazNowDTO cmdHavazNowDTO = new CmdHavazNowDTO();
                            cmdHavazNowDTO.setBvvIds(form.getDanhSachVe().get(firstItem).getBvvIds());
                            lenhService.cancelPickup(cmdHavazNowDTO);
                        }
                        dieuDoService.taoLenhCtv(form, tcVeEntityFirst.getVungTCDon(),
                                                 LenhConstants.LENH_TRA,
                                                 LenhConstants.LENH_STATUS_DA_DIEU, distenceCtvVtc);
                    } else {
                        dieuDoService.taoLenhCtv(form, tcVeEntityFirst.getVungTCDon(),
                                                 LenhConstants.LENH_TRA,
                                                 LenhConstants.LENH_STATUS_DA_DIEU, distenceCtvVtc);
                    }
                }
            }
        } else {
            throw new TransportException("Khong tim thay ve");
        }
    }

    private void createCmdAdditional(DieuDoForm dieuDoForm) {
        final int firstItem = 0;

        List<ClientHavazNowDTO> clients = tcLenhRepositoryCustom.getClientAdditionalHavazNow(dieuDoForm.getDanhSachVe().get(firstItem).getBvvIds().get(firstItem));
        if (clients.size() > 0) {
            dieuDoForm.setCmdAdditional(Boolean.TRUE);
            ClientHavazNowDTO clientHavazNowDTO = clients.get(firstItem);
            clientHavazNowDTO.setDieuDoForm(dieuDoForm);
            clientHavazNowDTO.setTypeNotify(NotificationType.ADD_CMD_NOW.getType());
            List<TripStationDTO> listHub = xeTuyenService.getTripStations(clientHavazNowDTO.getTripId());
            int countTime = 0;
            for (TripStationDTO hub : listHub) {
                countTime = countTime + hub.getTime();
                if (Objects.equals(hub.getId(), clientHavazNowDTO.getHubTraKhach())) {
                    clientHavazNowDTO.setTimeXtToHub(DateTimeUtils
                            .addTimeFormatHHMM(clientHavazNowDTO.getGioXuatBen(), countTime));
                    clientHavazNowDTO.setDiaChiTra(hub.getStation());
                    break;
                }
            }
            Optional<AdminLv2UserEntity> opAdmLv2 = adminLv2UserRepository.findById(dieuDoForm.getTaiXeId());
            if (opAdmLv2.isPresent()) {
                firebaseClientService.sendNotification(opAdmLv2.get().getAdmAppToken(),
                                                       "DBS Havaz Now !!! ",
                                                       "Có vé HavazNow Điều bổ sung",
                                                       clientHavazNowDTO,
                                                       AudioType.CMD_DBS.getValue());
                firebaseClientService.sendDataToTopicFirebase(topicHavazNow + dieuDoForm.getTaiXeId(), clientHavazNowDTO);
            }

        }

    }

    private void sendNotifyToDriver(Integer laiXeTCDonId, Integer laiXeTCTraId, String msg, String phoneDi,
                                    String tenKhachDi, Integer notificationType, String audio) {
        String notiContent = phoneDi + " - " + tenKhachDi;
        String FIREBASE_TOPIC_NAME = CacheData.CONFIGURATION_DATA.get(Constant.FIREBASE_TOPIC_NAME);
        if (laiXeTCDonId != null && laiXeTCDonId > 0) {
            AdminLv2UserEntity laiXeDon = adminLv2UserRepository.findById(laiXeTCDonId).orElse(null);
            if (laiXeDon != null) {
                FirebaseNotiDataForm firebaseNotiDataForm = new FirebaseNotiDataForm();
                firebaseNotiDataForm.setType(1);
                firebaseNotiDataForm.setTypeNotify(notificationType);
                firebaseClientService.sendNotification(laiXeDon.getAdmAppToken(), msg, notiContent,
                        firebaseNotiDataForm, audio);
                firebaseClientService.sendDataToTopicFirebase(FIREBASE_TOPIC_NAME + laiXeTCDonId, firebaseNotiDataForm);
            }
        }
        if (laiXeTCTraId != null && laiXeTCTraId > 0) {
            AdminLv2UserEntity laiXeTra = adminLv2UserRepository.findById(laiXeTCTraId).orElse(null);
            if (laiXeTra != null) {
                FirebaseNotiDataForm firebaseNotiDataForm = new FirebaseNotiDataForm();
                firebaseNotiDataForm.setType(2);
                firebaseNotiDataForm.setTypeNotify(notificationType);
                firebaseClientService.sendNotification(laiXeTra.getAdmAppToken(), msg, notiContent,
                        firebaseNotiDataForm, audio);
                firebaseClientService.sendDataToTopicFirebase(FIREBASE_TOPIC_NAME + "TRA-" + laiXeTCTraId,
                        firebaseNotiDataForm);
            }
        }
    }

    private void processCreateOrUpdateVe(TcVeEntity tcVeEntity, BanVeVeEntity banVeVeEntity) {
        if (tcVeEntity != null) {
            log.debug("Update ve in tc_ve.");
            checkIsTrungChuyenForBVVEntity(banVeVeEntity, tcVeEntity);
            if (tcVeEntity.getIsTcDon() || tcVeEntity.getIsTcTra()) {
                //Convert BanVeVe to TcVe
                convertBanVeVeToVeTC(banVeVeEntity, tcVeEntity, VeConstants.CAP_NHAT_VE);
                //Save TcVe into DB
                tcVeRepository.save(tcVeEntity);
            } else {
                checkDataAndCancelLenh(tcVeEntity, VeConstants.HUY_VE);
                tcVeRepository.delete(tcVeEntity);
            }
        } else {
            log.debug("Create new ve in tc_ve.");
            tcVeEntity = checkIsTrungChuyenForBVVEntity(banVeVeEntity, new TcVeEntity());
            if (tcVeEntity.getIsTcDon() || tcVeEntity.getIsTcTra()) {
                //Convert BanVeVe to TcVe
                convertBanVeVeToVeTC(banVeVeEntity, tcVeEntity, VeConstants.TAO_MOI_VE);
                //Save TcVe into DB
                tcVeRepository.save(tcVeEntity);
                log.debug("ZzLOG: START write history");
                this.createLogBvv(banVeVeEntity.getBvvId(), VeConstants.VE_ACTION.get(VeConstants.TAO_MOI_VE), 2);
                log.debug("ZzLOG: END write history");
            } else {
                log.info("Ve khong co don tra");
            }
            log.info("TC ve: {}", tcVeEntity);
            log.info("BVV ve: {}", banVeVeEntity);
        }
    }
    private void processUpdateVe(TcVeEntity tcVeEntity, BanVeVeEntity banVeVeEntity) {
        log.info("Update ve in tc_ve.");
        tcVeEntity = checkIsTrungChuyenForBVVEntity(banVeVeEntity, tcVeEntity);
        if (tcVeEntity.getIsTcDon() || tcVeEntity.getIsTcTra()) {
            //Convert BanVeVe to TcVe
            convertBanVeVeToVeTC(banVeVeEntity, tcVeEntity, VeConstants.CAP_NHAT_VE);
            //Save TcVe into DB
            tcVeRepository.save(tcVeEntity);
        } else {
            checkDataAndCancelLenh(tcVeEntity, VeConstants.HUY_VE);
            tcVeRepository.delete(tcVeEntity);
        }
    }

    private void processCreateVe(BanVeVeEntity banVeVeEntity) {
        TcVeEntity tcVeEntity = new TcVeEntity();
        checkIsTrungChuyenForBVVEntity(banVeVeEntity, tcVeEntity);
        // Kiem tra ve co trung chuyen hay khong
        if (tcVeEntity.getIsTcDon() || tcVeEntity.getIsTcTra()) {
            //Convert BanVeVe to TcVe
            convertBanVeVeToVeTC(banVeVeEntity, tcVeEntity, VeConstants.TAO_MOI_VE);
            tcVeRepository.save(tcVeEntity);
            this.createLogBvv(banVeVeEntity.getBvvId(), VeConstants.VE_ACTION.get(VeConstants.TAO_MOI_VE), 2);
        } else {
            log.info("Vé không có trung chuyển: {}", banVeVeEntity);
        }
    }

    private void checkDataAndCancelLenh(TcVeEntity tcVeEntity, int kieuVe) {
        log.debug("Kiem tra ve co phai la ve cuoi cung cua lenh ko.");
        int lenhDonId = tcVeEntity.getTcLenhId() != null ? tcVeEntity.getTcLenhId() : 0;
        int lenhTraId = tcVeEntity.getTcLenhTraId() != null ? tcVeEntity.getTcLenhTraId() : 0;
        //Neu la truong hop cap nhat ve thi kiem tra cho tung truong hop don va tra
        if (kieuVe == VeConstants.CAP_NHAT_VE) {
            //Neu khach hang van can trung chuyen don thi ko duoc xoa lenh don
            lenhDonId = tcVeEntity.getIsTcDon() ? Constant.ZERO : lenhDonId;
            //Neu khach hang van can trung chuyen tra thi ko duoc xoa lenh tra
            lenhTraId = tcVeEntity.getIsTcTra() ? Constant.ZERO : lenhTraId;
        }
        if (lenhDonId > 0) {
            List<TcVeEntity> veTcByTcLenhId = tcVeRepository.findByTcLenhId(lenhDonId);
            if (veTcByTcLenhId.size() <= 1) {
                cancelLenh(lenhDonId);
                log.info("Lenh :: " + lenhDonId + " > " + veTcByTcLenhId.size() + " ve.");

            }
        }
        if (lenhTraId > 0) {
            List<TcVeEntity> veTcByTcLenhId = tcVeRepository.findByTcLenhTraId(lenhTraId);
            if (veTcByTcLenhId.size() <= 1) {
                cancelLenh(lenhTraId);
            }
            log.info("Lenh :: " + lenhTraId + " > " + veTcByTcLenhId.size() + " ve.");
        }
    }

    private void cancelLenh(int lenhId){
        TcLenhEntity tcLenhEntity = tcLenhRepository.findById(lenhId).orElse(null);
        if (tcLenhEntity != null) {
            log.info("Huy lenh vi khong co ve nao trong lenh nay.");
            tcLenhEntity.setTrangThai(3);
            tcLenhEntity.setGhiChu(messageConfig.getMessage("integration.transfer.command.cancel"));
            tcLenhRepository.save(tcLenhEntity);
        }
    }

    private void convertBanVeVeToVeTC(BanVeVeEntity banVeVeEntity, TcVeEntity tcVeEntity, int action) {
        if (banVeVeEntity != null) {
            tcVeEntity.setBvvId(banVeVeEntity.getBvvId());
            tcVeEntity.setBvvTenKhachHangDi(banVeVeEntity.getBvvTenKhachHang());
            if (!StringUtils.isEmpty(banVeVeEntity.getBvvTenKhachHangDi())) {
                tcVeEntity.setBvvTenKhachHangDi(banVeVeEntity.getBvvTenKhachHangDi());
            }
            tcVeEntity.setBvvPhoneDi(banVeVeEntity.getBvvPhone());
            if (!StringUtils.isEmpty(banVeVeEntity.getBvvPhoneDi())) {
                tcVeEntity.setBvvPhoneDi(banVeVeEntity.getBvvPhoneDi());
            }
            tcVeEntity.setTcIsHavazNow(banVeVeEntity.getBvvFast());
            if (action == VeConstants.TAO_MOI_VE
                    || !Objects.equals(tcVeEntity.getDidId(), banVeVeEntity.getBvvBvnId())) {
                processUpdateStatusTicket(tcVeEntity);
            }
            tcVeEntity.setDidId(banVeVeEntity.getBvvBvnId());

            tcVeEntity.setBvvSource(banVeVeEntity.getBvvSource());
            tcVeEntity.setBvvMaGhe(banVeVeEntity.getBvvNumberName());
            tcVeEntity.setBvvGhiChu(banVeVeEntity.getBvvGhiChu());
            tcVeEntity.setVeAction(action);

            tcVeEntity.setLastUpdatedDate(LocalDateTime.now());
            //check lại để xóa lệnh nếu vé đó ko tick trung chuyển
            if (action != VeConstants.TAO_MOI_VE) {
                checkDataAndCancelLenh(tcVeEntity, action);
                resetVe(tcVeEntity);
            }
            //Reset data trung chuyen khi tao ve moi va chuyen cho khac chuyen
            if (action == VeConstants.TAO_MOI_VE
                    || (Objects.equals(tcVeEntity.getDidId(), banVeVeEntity.getBvvBvnId()))) {
                tcVeEntity.setCreatedDate(LocalDateTime.now());
                resetVe(tcVeEntity);
            }

        }
    }
    private void processUpdateStatusTicket(TcVeEntity tcVeEntity) {
        tcVeEntity.setTcLenhId(Constant.ZERO);
        tcVeEntity.setTcTrangThaiDon(Constant.ZERO);
        tcVeEntity.setLyDoTuChoiDon(StringUtils.EMPTY);
        tcVeEntity.setThuTuDon(Constant.ZERO);
        tcVeEntity.setThoiGianDon(Constant.ZERO);
        tcVeEntity.setLaiXeIdDon(Constant.ZERO);

        //TC tra
        tcVeEntity.setTcLenhTraId(Constant.ZERO);
        tcVeEntity.setTcTrangThaiTra(Constant.ZERO);
        tcVeEntity.setLyDoTuChoiTra(StringUtils.EMPTY);
        tcVeEntity.setThuTuTra(Constant.ZERO);
        tcVeEntity.setThoiGianTra(Constant.ZERO);
        tcVeEntity.setLaiXeIdTra(Constant.ZERO);
        tcVeEntity.setKhachHangMoi(Constant.ZERO);

        if (BooleanUtils.isTrue(tcVeEntity.getIsTcDon())) {
            tcVeEntity.setTcLenhId(Constant.ZERO);
            tcVeEntity.setTcTrangThaiDon(Constant.ZERO);
            tcVeEntity.setLyDoTuChoiDon(StringUtils.EMPTY);
            tcVeEntity.setThuTuDon(Constant.ZERO);
            tcVeEntity.setThoiGianDon(Constant.ZERO);
            tcVeEntity.setLaiXeIdDon(Constant.ZERO);
            tcVeEntity.setGhiChuDon(StringUtils.EMPTY);
        }
        if (BooleanUtils.isTrue(tcVeEntity.getIsTcTra())) {
            tcVeEntity.setTcLenhTraId(Constant.ZERO);
            tcVeEntity.setTcTrangThaiTra(Constant.ZERO);
            tcVeEntity.setLyDoTuChoiTra(StringUtils.EMPTY);
            tcVeEntity.setThuTuTra(Constant.ZERO);
            tcVeEntity.setThoiGianTra(Constant.ZERO);
            tcVeEntity.setLaiXeIdTra(Constant.ZERO);
            tcVeEntity.setGhiChuTra(StringUtils.EMPTY);
            tcVeEntity.setKhachHangMoi(Constant.ZERO);
        }

    }

    private TcVeEntity checkIsTrungChuyenForBVVEntity(BanVeVeEntity banVeVeEntity, TcVeEntity tcVeEntity) {
        //TODO Kiểm tra xem có thay đổi địa chỉ đón không, nếu thay đổi thì ghi log
        writeHistory(banVeVeEntity, tcVeEntity);
        tcVeEntity.setTcBvvBexIdA(banVeVeEntity.getBvvBexIdA());
        tcVeEntity.setTcBvvBexIdB(banVeVeEntity.getBvvBexIdB());
        tcVeEntity.setTcHubDiemDon(banVeVeEntity.getBvvBenTrungChuyenA());
        tcVeEntity.setTcHubDiemTra(banVeVeEntity.getBvvBenTrungChuyenB());
        //Neu ko chon trung chuyen don thi phai check lai xem ben xe do co phai trung chuyen ko
        tcVeEntity.setIsTcDon(banVeVeEntity.getBvvTrungChuyenA());
        tcVeEntity.setBvvDiemDonKhach(banVeVeEntity.getBvvDiemDonKhach());
        tcVeEntity.setBvvLongStart(banVeVeEntity.getBvvLongStart());
        tcVeEntity.setBvvLatStart(banVeVeEntity.getBvvLatStart());

        tcVeEntity.setVungTCDon(vungTrungChuyenService.checkExistsPoint(tcVeEntity.getBvvLatStart(), tcVeEntity.getBvvLongStart()));
        if (StringUtils.isEmpty(tcVeEntity.getGhiChuDon()))
            tcVeEntity.setGhiChuDon("");
        if (!banVeVeEntity.getBvvTrungChuyenA()) {
            //check lai ben xe co phai la diem can trung chuyen ko
            //lay tuyen id, benXeId tu bvvId
            Object[] tuyenDiemDonTraEntity = getTuyenDiemDonTraEntity(banVeVeEntity, banVeVeEntity.getBvvBexIdA());
            if (tuyenDiemDonTraEntity != null && tuyenDiemDonTraEntity.length > 0) {
                int isTrungChuyen = Integer.valueOf(String.valueOf(tuyenDiemDonTraEntity[1]));
                if (isTrungChuyen == 1) {
                    tcVeEntity.setIsTcDon(true);
                    tcVeEntity.setBvvDiemDonKhach(tuyenDiemDonTraEntity[2] == null ? "" : tuyenDiemDonTraEntity[2] + "");
                    tcVeEntity.setGhiChuDon(tuyenDiemDonTraEntity[3] == null ? "" : Constant.GHI_CHU_TRUNG_CHUYEN + tuyenDiemDonTraEntity[3]);
                }
            }
        }
        tcVeEntity.setIsTcTra(banVeVeEntity.getBvvTrungChuyenB());
        tcVeEntity.setBvvDiemTraKhach(banVeVeEntity.getBvvDiemTraKhach());
        tcVeEntity.setBvvLongEnd(banVeVeEntity.getBvvLongEnd());
        tcVeEntity.setBvvLatEnd(banVeVeEntity.getBvvLatEnd());
        tcVeEntity.setVungTCTra(vungTrungChuyenService.checkExistsPoint(tcVeEntity.getBvvLatEnd(), tcVeEntity.getBvvLongEnd()));
        if (StringUtils.isEmpty(tcVeEntity.getGhiChuTra())) {
            tcVeEntity.setGhiChuTra(StringUtils.EMPTY);
        }

        if (!banVeVeEntity.getBvvTrungChuyenB()) {
            //check lai ben xe co phai la diem can trung chuyen ko
            //lay tuyen id, benXeId tu bvvId
            Object[] tuyenDiemDonTraEntity = getTuyenDiemDonTraEntity(banVeVeEntity, banVeVeEntity.getBvvBexIdB());
            if (tuyenDiemDonTraEntity != null && tuyenDiemDonTraEntity.length > 0) {
                int isTrungChuyen = Integer.valueOf(String.valueOf(tuyenDiemDonTraEntity[1]));
                if (isTrungChuyen == 1) {
                    tcVeEntity.setIsTcTra(true);
                    tcVeEntity.setBvvDiemTraKhach(tuyenDiemDonTraEntity[2] == null ? "" : tuyenDiemDonTraEntity[2] + "");
                    tcVeEntity.setGhiChuTra(tuyenDiemDonTraEntity[3] == null ? "" : Constant.GHI_CHU_TRUNG_CHUYEN + tuyenDiemDonTraEntity[3]);
                }
            }
        }

        return tcVeEntity;
    }

    private void writeHistory(BanVeVeEntity banVeVeEntity, TcVeEntity tcVeEntity) {
        boolean bvvDon = banVeVeEntity.getBvvTrungChuyenA();
        boolean tcDon = tcVeEntity.getIsTcDon() == null ? false : tcVeEntity.getIsTcDon();
        String user;
        try {
            user = String.valueOf(SecurityUtils.getCurrentUserLogin());
            if ("0".equals(user)) {
                user = "1";
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            user = "1";
        }
        if (tcDon && !bvvDon) {
            //TODO GHI LOG: Bỏ tick trung chuyển đón
            String data = "{\"Yêu cầu đón \"" + ":[\"1\",\"0\"]}";
            wrieZzLog(user, banVeVeEntity.getBvvId(), data, "HỦY ĐÓN");
        }
//        if (tcDon == 0 && bvvDon == 1){
//            //TODO GHI LOG: tick trung chuyển đón
//            String data = "{\"TC: Yêu cầu đón \""+":[\"0\",\"1\"]}";
//            wrieZzLog(user, banVeVeEntity.getBvvId(), data, "TICK TC ĐÓN");
//        }
        boolean bvvTra = banVeVeEntity.getBvvTrungChuyenB();
        boolean tcTra = tcVeEntity.getIsTcTra() == null ? false : tcVeEntity.getIsTcTra();
        if (tcTra && !bvvTra) {
            //TODO GHI LOG: Bỏ tick trung chuyển trả
            String data = "{\"Yêu cầu trả \"" + ":[\"1\",\"0\"]}";
            wrieZzLog(user, banVeVeEntity.getBvvId(), data, "HỦY TRẢ");
        }
    }

    @Override
    public void wrieZzLog(String id, int bvvId, String data, String ip) {
        long epoch = System.currentTimeMillis() / 1000;
        String timeS = String.valueOf(epoch);
        int timeI = Integer.valueOf(timeS);
        log.info("ZzLOG: START write history");
        ZzLogBanVeEntity logEntity = new ZzLogBanVeEntity();
        int user;
        if (StringUtils.isEmpty(id)) {
            user = 1;
        } else {
            user = Integer.parseInt(id);
        }
        logEntity.setLogAdminId(user);
        logEntity.setLogRecordId(bvvId);
        logEntity.setLogData(data);
        logEntity.setLogTime(timeI);
        logEntity.setLogType(2);//1: Insert 2: Update
        logEntity.setLogIp(ip);
        logEntity.setLogStatus(0);
        ZzLogRepository.save(logEntity);
        log.info("ZzLOG: END write history");
    }

    private Object[] getTuyenDiemDonTraEntity(BanVeVeEntity banVeVeEntity, int BenXeId) {
        try {
            int tuyenId = tuyenRepository.getTuyIdByBvvBvnId(banVeVeEntity.getBvvBvnId());
            List<Object[]> lst = tuyenDiemDonTraRepository.getByTuyenIdAndBenId(tuyenId, BenXeId);
            Object[] result = null;
            if (lst != null && lst.size() > 0) {
                for (Object[] arr : lst) {
                    result = arr;
                }
            }
            return result;
        } catch (Exception e) {
            log.warn("Không tìm thấy dữ liệu" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean createLogBvv(int bvvId, String action, int type) {
        ZzLogBanVeEntity logEntity;
        logEntity = new ZzLogBanVeEntity();
        logEntity.setLogAdminId(1);
        logEntity.setLogRecordId(bvvId);
        logEntity.setLogData("{\"TC: Vé ERP -> TC \"" + ":[\"0\",\"1\"]}");
        long epoch = System.currentTimeMillis() / 1000;
        String timeS = String.valueOf(epoch);
        int timeI = Integer.valueOf(timeS);
        logEntity.setLogTime(timeI);
        logEntity.setLogType(type);//1: Insert 2: Update
        logEntity.setLogIp(action);
        logEntity.setLogStatus(0);
        ZzLogRepository.save(logEntity);
        return true;
    }

    private void resetVe(TcVeEntity tcVeEntity){
        if (BooleanUtils.isFalse(tcVeEntity.getIsTcDon())) {
            tcVeEntity.setTcLenhId(0);
            tcVeEntity.setTcTrangThaiDon(0);
            tcVeEntity.setLyDoTuChoiDon(Strings.EMPTY);
            tcVeEntity.setThuTuDon(0);
            tcVeEntity.setThoiGianDon(0);
            tcVeEntity.setLaiXeIdDon(0);
            tcVeEntity.setGhiChuDon(Strings.EMPTY);
            tcVeEntity.setBvvLongStart((double) 0);
            tcVeEntity.setBvvLatStart((double) 0);
        }
        if (BooleanUtils.isFalse(tcVeEntity.getIsTcTra())) {
            tcVeEntity.setTcLenhTraId(0);
            tcVeEntity.setTcTrangThaiTra(0);
            tcVeEntity.setLyDoTuChoiTra(Strings.EMPTY);
            tcVeEntity.setThuTuTra(0);
            tcVeEntity.setThoiGianTra(0);
            tcVeEntity.setLaiXeIdTra(0);
            tcVeEntity.setGhiChuTra("");
            tcVeEntity.setKhachHangMoi(0);
            tcVeEntity.setBvvLongEnd((double) 0);
            tcVeEntity.setBvvLatEnd((double) 0);
        }

    }

    private void calculateTimeAndDistanceDonTra(TcVeEntity tcVeEntity, int kieuLenh ){
        if(kieuLenh == LenhConstants.LENH_DON){
            if (tcVeEntity.getTcHubDiemDon() > 0) {
                Point diemDon = new Point(tcVeEntity.getBvvLatStart(), tcVeEntity.getBvvLongStart());
                BenXeEntity benXeEntity = benXeEntityRepository.findById(tcVeEntity.getTcHubDiemDon()).orElse(null);
                List<Double> lst = getDistanceAndDurationDon(diemDon, benXeEntity);
                tcVeEntity.setTcDistanceToHubDon(lst != null ? lst.get(0) : (double) 0);
                tcVeEntity.setTcTimeToHubDon(lst != null ? lst.get(1).intValue() : 0);
            }

        } else if(kieuLenh == LenhConstants.LENH_TRA){
            if (tcVeEntity.getTcHubDiemTra() > 0) {
                Point diemTra = new Point(tcVeEntity.getBvvLatEnd(), tcVeEntity.getBvvLongEnd());
                BenXeEntity benXeEntity = benXeEntityRepository.findById(tcVeEntity.getTcHubDiemTra()).orElse(null);
                List<Double> lst = getDistanceAndDurationDon(diemTra, benXeEntity);
                tcVeEntity.setTcDistanceToHubTra(lst != null ? lst.get(0) : (double) 0);
                tcVeEntity.setTcTimeToHubTra(lst != null ? lst.get(1).intValue() : 0);
            }
        }

    }

    private int getHubdonTra(TcVeEntity tcVeEntity, int kieuLenh) {
        try {
            if (tcVeEntity.getTcHubDiemDon() != null && tcVeEntity.getTcHubDiemDon() > 0
                    && kieuLenh == LenhConstants.LENH_DON) {
                return tcVeEntity.getTcHubDiemDon();
            } else if (tcVeEntity.getTcHubDiemTra() != null &&tcVeEntity.getTcHubDiemTra() > 0
                    && kieuLenh == LenhConstants.LENH_TRA) {
                return tcVeEntity.getTcHubDiemTra();
            }
            Point diemKhachHang;
            if (kieuLenh == LenhConstants.LENH_DON) {
                diemKhachHang = new Point(tcVeEntity.getBvvLatStart(), tcVeEntity.getBvvLongStart());
            } else {
                diemKhachHang = new Point(tcVeEntity.getBvvLatEnd(), tcVeEntity.getBvvLongEnd());
            }
            return xeTuyenService.getBestHub(tcVeEntity.getDidId(), diemKhachHang);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return 0;
        }
    }

    private List<Double> getDistanceAndDurationDon(Point diemKhachHang, BenXeEntity benXeEntity) {
        try {
            if (benXeEntity == null || diemKhachHang.getLng() == 0 || diemKhachHang.getLat() == 0) {
                return null;
            } else {
                Point diemHub = new Point(benXeEntity.getBenxeLat(), benXeEntity.getBenxeLong());
                return locationService.getDistanceAndDurationFromAPI(diemKhachHang, diemHub);
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    private void updateTcVe(List<Integer> bvvIds) {
        for (int bvvId : bvvIds) {
            Optional<TcVeEntity> tcVeEntityOpt = tcVeRepository.findByBvvId(bvvId);
            tcVeEntityOpt.ifPresent(e -> {
                if (Objects.isNull((e.getTcHubDiemDon()))) {
                    e.setTcHubDiemDon(getHubdonTra(e, LenhConstants.LENH_DON));
                }
                //nếu bên erp ko gửi hub TC đón sang thì tự gợi ý hub
                //Nếu có hub thì tình khoảng cách đến hub lưu db;
                calculateTimeAndDistanceDonTra(e, LenhConstants.LENH_DON);
                if (Objects.isNull(e.getTcHubDiemTra())) {
                    e.setTcHubDiemTra(getHubdonTra(e, LenhConstants.LENH_TRA));
                }
                //Nếu có hub thì tình khoảng cách đến hub lưu db;
                calculateTimeAndDistanceDonTra(e, LenhConstants.LENH_TRA);
                tcVeRepository.save(e);
            });
        }
    }

    @Override
    public List<CustomerRankData> layThongTinRankTuHavaz(List<String> listPhone) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        CustomerRankForm customerRankForm = new CustomerRankForm(listPhone.toArray(new String[0]));
        HttpEntity<CustomerRankForm> entity = new HttpEntity<>(customerRankForm,headers);
        ResponseEntity<CustomerRank> dataResponse =
                restTemplate.exchange(URL_HAVAZ+"/api/v3/customer/rank", HttpMethod.POST, entity,
                        CustomerRank.class);
        CustomerRank data = dataResponse.getBody();
        return data != null && data.getData() != null ? data.getData() : new ArrayList<>();
    }
}
