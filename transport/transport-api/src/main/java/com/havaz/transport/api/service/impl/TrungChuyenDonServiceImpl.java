package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.common.AudioType;
import com.havaz.transport.api.common.CacheData;
import com.havaz.transport.api.common.CommonUtils;
import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.common.ManagerNotificationType;
import com.havaz.transport.api.common.NotificationType;
import com.havaz.transport.api.configuration.MessageConfig;
import com.havaz.transport.api.exception.ResourceNotfoundException;
import com.havaz.transport.api.form.FirebaseNotiDataForm;
import com.havaz.transport.api.form.location.Point;
import com.havaz.transport.api.model.ConDataDTO;
import com.havaz.transport.api.model.DaDonDTO;
import com.havaz.transport.api.model.HuyDonDTO;
import com.havaz.transport.api.model.KhachDTO;
import com.havaz.transport.api.model.ThoiGianDonDTO;
import com.havaz.transport.api.model.TripDTO;
import com.havaz.transport.api.model.UpdateHubDonDTO;
import com.havaz.transport.api.rabbit.publisher.RabbitMQPublisher;
import com.havaz.transport.api.repository.TcVeRepositoryCustom;
import com.havaz.transport.api.service.BvvService;
import com.havaz.transport.api.service.FirebaseClientService;
import com.havaz.transport.api.service.LocationService;
import com.havaz.transport.api.service.NotificationService;
import com.havaz.transport.api.service.TcTaiXeVeService;
import com.havaz.transport.api.service.TrungChuyenDonService;
import com.havaz.transport.core.constant.VeConstants;
import com.havaz.transport.core.exception.TransportException;
import com.havaz.transport.core.utils.DateTimeUtils;
import com.havaz.transport.core.utils.Strings;
import com.havaz.transport.dao.entity.AdminLv2UserEntity;
import com.havaz.transport.dao.entity.BenXeEntity;
import com.havaz.transport.dao.entity.TcLenhEntity;
import com.havaz.transport.dao.entity.TcVeEntity;
import com.havaz.transport.dao.entity.XeEntity;
import com.havaz.transport.dao.entity.ZzLogBanVeEntity;
import com.havaz.transport.dao.repository.AdminLv2UserRepository;
import com.havaz.transport.dao.repository.BenXeEntityRepository;
import com.havaz.transport.dao.repository.TcLenhRepository;
import com.havaz.transport.dao.repository.TcVeRepository;
import com.havaz.transport.dao.repository.TcZzLogRepository;
import com.havaz.transport.dao.repository.XeRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TrungChuyenDonServiceImpl implements TrungChuyenDonService {

    private static final Logger LOG = LoggerFactory.getLogger(TrungChuyenDonServiceImpl.class);

    @Value("${havaz.stomp.topic}")
    private String topic;

    @Autowired
    private TcVeRepository tcVeRepository;

    @Autowired
    private TcVeRepositoryCustom tcVeRepositoryCustom;

    @Autowired
    private TcLenhRepository tcLenhRepository;

    @Autowired
    private XeRepository xeRepository;

    @Autowired
    private BvvService bvvService;

    @Autowired
    private RabbitMQPublisher rabbitMQPublisher;

    @Autowired
    private TcZzLogRepository ZzLogRepository;

    @Autowired
    private FirebaseClientService firebaseClientService;

    @Autowired
    private AdminLv2UserRepository adminLv2UserRepository;

    @Autowired
    private MessageConfig messageConfig;

    @Autowired
    private LocationService locationService;

    @Autowired
    private BenXeEntityRepository benXeEntityRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TcTaiXeVeService tcTaiXeVeService;

    @Override
    @Transactional
    public void huyDonKhach(HuyDonDTO huyDonDTO) {
        List<Integer> bvvIds = huyDonDTO.getListBvvId();
        if (bvvIds == null || bvvIds.isEmpty()) {
            throw new TransportException("bvvIds is null or empty");
        }
        List<TcVeEntity> listVeTcEntity = tcVeRepositoryCustom.getListVeTcByBvvId(bvvIds);
        if (listVeTcEntity.isEmpty()) {
            LOG.info("Can not find any TcEntity by getListBvvId");
            throw new ResourceNotfoundException("TcEntity is not found");
        }
        List<ZzLogBanVeEntity> logEntities = new ArrayList<>();
        for (TcVeEntity tcVeEntity : listVeTcEntity) {
            int oldTTD = tcVeEntity.getTcTrangThaiDon();
            AdminLv2UserEntity adminLv2UserDon = tcVeEntity.getAdminLv2UserDon();
            if (adminLv2UserDon != null) {
                tcVeEntity.setLyDoTuChoiDon(
                        adminLv2UserDon.getAdmName() + Strings.COLON_DOT + huyDonDTO.getLyDoHuy());
            } else {
                tcVeEntity.setLyDoTuChoiDon(huyDonDTO.getLyDoHuy());
            }
            tcVeEntity.setTcTrangThaiDon(VeConstants.TC_STATUS_DA_HUY);
            tcVeEntity.setLaiXeIdDon(Constant.ZERO);
            tcVeEntity.setTcLenhId(Constant.ZERO);
            tcVeEntity.setKhachHangMoi(Constant.ZERO);
            tcVeEntity.setLastUpdatedDate(LocalDateTime.now());
            tcVeRepository.save(tcVeEntity);
            //Update table ban_ve_ve
            bvvService.updateBvvEntity(tcVeEntity);

            //insert to table laixe-ve
            saveLaiXeVe(huyDonDTO.getTaiXeId(),huyDonDTO.getLenhId(),tcVeEntity.getTcVeId(),VeConstants.TC_STATUS_DA_HUY,huyDonDTO.getLyDoHuy());

            //Insert history
            ZzLogBanVeEntity logEntity;
            logEntity = new ZzLogBanVeEntity();
            logEntity.setLogAdminId(huyDonDTO.getTaiXeId());
            logEntity.setLogRecordId(tcVeEntity.getBvvId());
            //4: đã hủy
            logEntity.setLogData("{\"Trạng thái đón\":[\"" + VeConstants.GET_MSG_STATUS
                    .get(oldTTD) + "\",\"" + VeConstants.GET_MSG_STATUS.get(4)
                                         + "\"],\"Lý do\":[\"\"," + "\""
                                         + huyDonDTO.getLyDoHuy() + "\"]}");
            long epoch = System.currentTimeMillis() / 1000;
            String timeS = String.valueOf(epoch);
            int timeI = Integer.valueOf(timeS);
            logEntity.setLogTime(timeI);
            logEntity.setLogType(2);//1: Insert 2: Update
            logEntity.setLogIp("HỦY ĐÓN KHÁCH");
            logEntity.setLogStatus(0);//ERP said it alway is 0
            logEntities.add(logEntity);
        }
        try {
            notificationService.saveAndSendNotificationToManager(topic,
                    ManagerNotificationType.DRIVER_REJECT_PICKUP.getType(), true, bvvIds);
        } catch (Exception e) {
            LOG.warn("Error For Send Notify To Manager ", e);
        }
        ZzLogRepository.saveAll(logEntities);
    }

    @Override
    @Transactional
    public void donKhach(DaDonDTO daDonDTO) {
        List<ZzLogBanVeEntity> logEntities = new ArrayList<>();
        List<Integer> listVeBvvId = daDonDTO.getListBvvId();
        for (Integer veId : listVeBvvId) {
            TcVeEntity tcVeEntity = tcVeRepository.findByBvvId(veId).orElse(null);
            if (tcVeEntity != null) {
                tcVeEntity.setTcTrangThaiDon(VeConstants.TC_STATUS_DA_DON);
                tcVeEntity.setKhachHangMoi(Constant.ZERO);
                tcVeEntity.setLastUpdatedDate(LocalDateTime.now());
                tcVeRepository.save(tcVeEntity);

                //insert to table laixe-ve
                saveLaiXeVe(daDonDTO.getTaiXeId(), tcVeEntity.getTcLenhId(), tcVeEntity.getTcVeId(), VeConstants.TC_STATUS_DA_DON, "");

                //Insert history
                ZzLogBanVeEntity logEntity;
                logEntity = new ZzLogBanVeEntity();
                logEntity.setLogAdminId(daDonDTO.getTaiXeId());
                logEntity.setLogRecordId(tcVeEntity.getBvvId());
                //4: đã đón
                logEntity.setLogData("{\"Đón khách\":[\"0\",\"1\"]}");
                long epoch = System.currentTimeMillis() / 1000;
                String timeS = String.valueOf(epoch);
                int timeI = Integer.valueOf(timeS);
                logEntity.setLogTime(timeI);
                logEntity.setLogType(2);//1: Insert 2: Update
                logEntity.setLogIp("ĐÓN KHÁCH");
                logEntity.setLogStatus(0);//ERP said it alway is 0
                logEntities.add(logEntity);
            }
        }
        ZzLogRepository.saveAll(logEntities);
    }

    @Override
    @Transactional
    public void thoiGianDon(ThoiGianDonDTO thoiGianDonDTO) {
        List<Integer> listBvvId = thoiGianDonDTO.getListBvvId();
        for (Integer bvvId : listBvvId) {
            TcVeEntity tcVeEntity = tcVeRepository.getByBvvId(bvvId);
            tcVeEntity.setThoiGianDon(thoiGianDonDTO.getThoiGianDon());
            tcVeEntity.setLastUpdatedDate(LocalDateTime.now());
            tcVeRepository.save(tcVeEntity);
        }
    }

    @Override
    public void sendInfoTrackingToMsgMQ(int bvvId) {
        try {
            LOG.info("Send info tracking to message MQ bvvId:: " + bvvId);
            ConDataDTO conDataDTO = CacheData.THONG_TIN_NHA_XE;
            String merchantId = conDataDTO.getCon_code();
            TcVeEntity tcVeEntity = tcVeRepository.getByBvvId(bvvId);
            if (tcVeEntity != null) {
                if (tcVeEntity.getBvvSource() == 3) {
//            AdminLv2UserEntity laiXeDon = adminLv2UserRepository.findById(tcVeEntity.getLaiXeIdDon()).orElse(null);
                    TcLenhEntity lenhEntity = tcLenhRepository.findById(tcVeEntity.getTcLenhId())
                            .orElse(null);
                    if (lenhEntity != null) {
                        XeEntity xeEntity = xeRepository.findById(lenhEntity.getXeId())
                                .orElse(null);
                        if (xeEntity != null) {
                            TripDTO tripDTO = new TripDTO();
                            tripDTO.setBks(xeEntity.getXeBienKiemSoat());
                            //                    if(laiXeDon!=null) {
                            //                        String phone = laiXeDon.getAdmPhone();
                            //                        if(StringUtils.isEmpty(phone)){
                            //                            phone = laiXeDon.getAdmPhone2();
                            //                        }
                            //                        tripDTO.setSdt(phone);
                            //                    }else{
                            tripDTO.setSdt(xeEntity.getXeSoDienThoai());
                            //                    }
                            String topicName = CommonUtils
                                    .generateTopic(merchantId, tcVeEntity.getDidId(),
                                                   tcVeEntity.getLaiXeIdDon());
                            tripDTO.setTopic(topicName);
                            tripDTO.setXeTrungTam(xeEntity.getXeTrungTam());
                            tripDTO.setTripId(tcVeEntity.getDidId());
                            tripDTO.setMerchantId(merchantId);
                            //Khach
                            KhachDTO khachDTO = new KhachDTO();
                            khachDTO.setSdtKhachHang(tcVeEntity.getBvvPhoneDi());
                            if (lenhEntity.getLastUpdatedDate() != null) {
                                LOG.info("Last updated date: " + lenhEntity.getLastUpdatedDate());
                                LocalDateTime time = lenhEntity.getLastUpdatedDate()
                                        .plusMinutes(tcVeEntity.getThoiGianDon());

                                khachDTO.setGioDon(
                                        DateTimeUtils.convertLocalDateTimeToString(time, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS));
                            }
                            tripDTO.getDsKhachHang().add(khachDTO);
                            //Send message to RabbitMQ
                            rabbitMQPublisher.sendMsgTrackingVehicle(tripDTO);
                        }
                    }
                }
            }
            LOG.info("Send to MQ DONE: bvvId:: " + bvvId);
        } catch (Exception e) {
            LOG.error("Error send message to RabbitMQ.", e);
        }
    }

    @Override
    @Transactional
    public void updateHubDon(UpdateHubDonDTO updateHubDonDTO) {
        LOG.info("Update hub don cho ve " + updateHubDonDTO.getVeIds());
        List<Integer> list = updateHubDonDTO.getVeIds();
        for (int bvvId : list) {
            TcVeEntity tcVeEntity = tcVeRepository.getByBvvId(bvvId);
            if (tcVeEntity != null) {
                BenXeEntity benXeEntity = benXeEntityRepository.findById(updateHubDonDTO.getHubId())
                        .orElse(null);
                Point diemKhachHang = new Point(tcVeEntity.getBvvLatStart(),
                                                tcVeEntity.getBvvLongStart());
                List<Double> lst = getDistanceAndDurationDon(diemKhachHang, benXeEntity);
                tcVeEntity.setTcHubDiemDon(updateHubDonDTO.getHubId());
                tcVeEntity.setTcDistanceToHubDon(lst != null ? lst.get(0) : (double) 0);
                tcVeEntity.setTcTimeToHubDon(lst != null ? lst.get(1).intValue() : 0);
                tcVeRepository.save(tcVeEntity);
                if (tcVeEntity.getLaiXeIdDon() != null && tcVeEntity.getLaiXeIdTra() != null) {
                    final boolean isTcDon = true;
                    sendNotifyToDriver(tcVeEntity.getLaiXeIdDon(), messageConfig
                                               .getMessage(Constant.MSG_FIREBASE_TICKET_UPDATE),
                                       tcVeEntity.getBvvPhoneDi(),
                                       tcVeEntity.getBvvTenKhachHangDi(), isTcDon, AudioType.UPDATE_CMD.getValue());
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateHubTra(UpdateHubDonDTO updateHubDonDTO) {
        List<Integer> list = updateHubDonDTO.getVeIds();
        LOG.debug("Update hub tra cho ve: {}", list);

        BenXeEntity benXeEntity = null;

        if (updateHubDonDTO.getHubId() != null) {
            benXeEntity = benXeEntityRepository.findById(updateHubDonDTO.getHubId()).orElse(null);
        }

        List<TcVeEntity> veEntities = Collections.emptyList();
        if (!CollectionUtils.isEmpty(list)) {
            veEntities = tcVeRepository.findByBvvIdIn(list);
        }

        for (TcVeEntity tcVeEntity : veEntities) {
            Point diemKhachHang = new Point(tcVeEntity.getBvvLatEnd(), tcVeEntity.getBvvLongEnd());
            List<Double> lst = getDistanceAndDurationDon(diemKhachHang, benXeEntity);
            tcVeEntity.setTcHubDiemTra(updateHubDonDTO.getHubId());
            tcVeEntity.setTcDistanceToHubTra(lst != null ? lst.get(0) : (double) 0);
            tcVeEntity.setTcTimeToHubTra(lst != null ? lst.get(1).intValue() : 0);
            tcVeRepository.save(tcVeEntity);
            if (tcVeEntity.getLaiXeIdTra() != null && tcVeEntity.getLaiXeIdTra() != null) {
                sendNotifyToDriver(tcVeEntity.getLaiXeIdTra(), messageConfig.getMessage(Constant.MSG_FIREBASE_TICKET_UPDATE),
                                   tcVeEntity.getBvvPhoneDi(),
                                   tcVeEntity.getBvvTenKhachHangDi(), false, AudioType.UPDATE_CMD.getValue());
            }
        }
    }

    private void sendNotifyToDriver(Integer driverId, String msg, String phoneDi, String tenKhachDi,
                                    boolean isTcDon, String audio) {
        String content = phoneDi + " - " + tenKhachDi;
        String FIREBASE_TOPIC_NAME = CacheData.CONFIGURATION_DATA.get(Constant.FIREBASE_TOPIC_NAME);
        AdminLv2UserEntity driverEntity = adminLv2UserRepository.findById(driverId).orElse(null);
        FirebaseNotiDataForm firebaseNotiDataForm = new FirebaseNotiDataForm();

        firebaseNotiDataForm.setTypeNotify(NotificationType.UPDATE_CMD.getType());
        if (isTcDon) {
            if (driverEntity != null) {
                firebaseNotiDataForm.setType(1);
                firebaseClientService.sendNotification(driverEntity.getAdmAppToken(), msg, content,
                        firebaseNotiDataForm, audio);
                firebaseClientService.sendDataToTopicFirebase(FIREBASE_TOPIC_NAME + driverId, firebaseNotiDataForm);
            }
        } else if (driverEntity != null) {
            firebaseNotiDataForm.setType(2);
            firebaseClientService.sendNotification(driverEntity.getAdmAppToken(), msg, content,
                    firebaseNotiDataForm, audio);
            firebaseClientService.sendDataToTopicFirebase(FIREBASE_TOPIC_NAME + "TRA-" + driverEntity, firebaseNotiDataForm );
        }

    }

    private void sendNotiToLaiXe(Integer laiXeTCDonId, Integer laiXeTCTraId, String msg, String phoneDi, String tenKhachDi){
        //Send notification to firebase
        String notiContent = phoneDi + " - " + tenKhachDi;
        String FIREBASE_TOPIC_NAME = CacheData.CONFIGURATION_DATA.get(Constant.FIREBASE_TOPIC_NAME);
        if (laiXeTCDonId != null && laiXeTCDonId > 0) {
            AdminLv2UserEntity laiXeDon = adminLv2UserRepository.findById(laiXeTCDonId)
                    .orElse(null);
            if (laiXeDon != null) {
                FirebaseNotiDataForm firebaseNotiDataForm = new FirebaseNotiDataForm();
                firebaseNotiDataForm.setType(1);
//                firebaseClientService.sendNotification(laiXeDon.getAdmAppToken(), msg, notiContent,
//                                                       firebaseNotiDataForm);
                firebaseClientService.sendDataToTopic(laiXeDon, FIREBASE_TOPIC_NAME + laiXeTCDonId);
            }
        }
        if (laiXeTCTraId != null && laiXeTCTraId > 0) {
            AdminLv2UserEntity laiXeTra = adminLv2UserRepository.findById(laiXeTCTraId).orElse(null);
            if (laiXeTra != null) {
                FirebaseNotiDataForm firebaseNotiDataForm = new FirebaseNotiDataForm();
                firebaseNotiDataForm.setType(2);
//                firebaseClientService.sendNotification(laiXeTra.getAdmAppToken(), msg, notiContent,
//                                                       firebaseNotiDataForm);
                firebaseClientService
                        .sendDataToTopic(laiXeTra, FIREBASE_TOPIC_NAME + "TRA-" + laiXeTCTraId);
            }
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
            return null;
        }
    }

    private void saveLaiXeVe(int laixeId, int lenhId, int tcVeId, int statusVe, String lyDoHuy){
        tcTaiXeVeService.saveTaiXeVe(laixeId,lenhId,tcVeId,statusVe,lyDoHuy,laixeId);
    }
}
