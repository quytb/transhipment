package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.common.AudioType;
import com.havaz.transport.api.common.CacheData;
import com.havaz.transport.api.common.CommonUtils;
import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.common.LenhConstants;
import com.havaz.transport.api.common.ManagerNotificationType;
import com.havaz.transport.api.common.NotificationType;
import com.havaz.transport.api.configuration.MessageConfig;
import com.havaz.transport.api.exception.ResourceNotfoundException;
import com.havaz.transport.api.form.CustomerRankData;
import com.havaz.transport.api.form.FilterLenhForm;
import com.havaz.transport.api.form.FirebaseNotiDataForm;
import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.form.VeForm;
import com.havaz.transport.api.form.location.Point;
import com.havaz.transport.api.model.ChiTietLenhDTO;
import com.havaz.transport.api.model.CmdHavazNowDTO;
import com.havaz.transport.api.model.ConDataDTO;
import com.havaz.transport.api.model.HuyLenhTcDTO;
import com.havaz.transport.api.model.KetThucLenhDTO;
import com.havaz.transport.api.model.KhachTrungChuyenDTO;
import com.havaz.transport.api.model.LenhContainGXBDTO;
import com.havaz.transport.api.model.LenhTcDTO;
import com.havaz.transport.api.model.LichSuTrungChuyenDTO;
import com.havaz.transport.api.model.LuuLenhDTO;
import com.havaz.transport.api.model.MaLenhDTO;
import com.havaz.transport.api.model.NotifyLenhDTO;
import com.havaz.transport.api.model.TrangThaiLenhDTO;
import com.havaz.transport.api.model.TripStationDTO;
import com.havaz.transport.api.model.TripTrungChuyenDTO;
import com.havaz.transport.api.model.UpdateThuTuDonDTO;
import com.havaz.transport.api.model.UpdateThuTuDonVeTcDTO;
import com.havaz.transport.api.model.UpdateThuTuTraDTO;
import com.havaz.transport.api.model.UpdateThuTuTraVeTcDTO;
import com.havaz.transport.api.model.VeTcDTO;
import com.havaz.transport.api.model.VeTrungChuyenDTO;
import com.havaz.transport.api.model.XacNhanLenhDTO;
import com.havaz.transport.api.model.XeTuyenDTO;
import com.havaz.transport.api.rabbit.publisher.RabbitMQPublisher;
import com.havaz.transport.api.repository.TcLenhRepositoryCustom;
import com.havaz.transport.api.repository.VungTrungChuyenRepositoryCustom;
import com.havaz.transport.api.service.AdminLv2UserService;
import com.havaz.transport.api.service.BvvService;
import com.havaz.transport.api.service.CommonService;
import com.havaz.transport.api.service.FirebaseClientService;
import com.havaz.transport.api.service.LenhService;
import com.havaz.transport.api.service.LocationService;
import com.havaz.transport.api.service.NotificationService;
import com.havaz.transport.api.service.TcTaiXeVeService;
import com.havaz.transport.api.service.TrungChuyenDonService;
import com.havaz.transport.api.service.XeTuyenService;
import com.havaz.transport.api.utils.DateTimeUtils;
import com.havaz.transport.core.constant.VeConstants;
import com.havaz.transport.core.exception.TransportException;
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
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LenhServiceImpl implements LenhService {

    private static final Logger LOG = LoggerFactory.getLogger(LenhServiceImpl.class);

    @Value("${havaz.stomp.topic}")
    private String topic;

    @Autowired
    private TcLenhRepository tcLenhRepository;

    @Autowired
    private TcLenhRepositoryCustom tcLenhRepositoryCustom;

    @Autowired
    private TcVeRepository tcVeRepository;

    @Autowired
    private FirebaseClientService firebaseClientService;

    @Autowired
    private MessageConfig messageConfig;

    @Autowired
    private TcVeRepository veTcDao;

    @Autowired
    private BvvService bvvService;

    @Autowired
    private TrungChuyenDonService trungChuyenDonService;

    @Autowired
    private AdminLv2UserService adminLv2UserService;

    @Autowired
    private TcZzLogRepository ZzLogRepository;

    @Autowired
    private XeRepository xeRepository;

    @Autowired
    private VungTrungChuyenRepositoryCustom vungTrungChuyenRepositoryCustom;

    @Autowired
    private LocationService locationService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private BenXeEntityRepository benXeRepo;

    @Autowired
    private XeTuyenService xeTuyenService;

    @Autowired
    private AdminLv2UserRepository adminLv2UserRepository;

    @Autowired
    private RabbitMQPublisher rabbitMQPublisher;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TcTaiXeVeService tcTaiXeVeService;

    @Override
    @Transactional
    public boolean xacNhanLenh(XacNhanLenhDTO requestXacNhanLenh) {
        LOG.info("Start Xác Nhận Lệnh");
        //Update Ve_TC status
        List<VeTcDTO> listVeDTO = requestXacNhanLenh.getListVeTc();
        if (listVeDTO != null && !listVeDTO.isEmpty()) {
            for (VeTcDTO veTcDTO : listVeDTO) {
                if (veTcDTO.getTrangThaiTrungChuyen() == VeConstants.TC_STATUS_DA_DIEU) {
                    List<Integer> listBvvId = veTcDTO.getVeId();
                    for (int bvvId : listBvvId) {
                        TcVeEntity tcVeEntity = veTcDao.getByBvvId(bvvId);
                        if (tcVeEntity != null) {
                            tcVeEntity.setTcTrangThaiDon(VeConstants.TC_STATUS_DANG_DI_DON);
                            tcVeEntity.setThoiGianDon(0);

                            //tính tọa độ của khách hàng và tài xế
                            if (tcVeEntity.getBvvLatStart() != null && tcVeEntity.getBvvLongStart() != null && requestXacNhanLenh.getToaDoTaiXe() != null) {
                                if (tcVeEntity.getBvvLatStart() > 0 && tcVeEntity.getBvvLongStart() > 0) {
                                    Point toaDoKhachHang = new Point(tcVeEntity.getBvvLatStart(), tcVeEntity.getBvvLongStart());
                                    Point toaDoTaiXe = requestXacNhanLenh.getToaDoTaiXe();

                                    //lấy ra vùng của vé này => lấy ra tốc độ của vùng đó
                                    //double tocDo = getTocDoCuaVung(tcVeEntity.getVungTCDon());
                                    //LOG.info("Toc do cua tai xe trong vung "+tcVeEntity.getVungTCDon()+" la "+tocDo);

                                    //tính lại thoi gian don gọi API
                                    int time = getThoiGianDonFromAPI(toaDoKhachHang, requestXacNhanLenh.getToaDoTaiXe());
                                    LOG.debug("Thoi gian don cua khach hang {} la {}", tcVeEntity.getBvvPhoneDi(), time);
                                    tcVeEntity.setThoiGianDon(time);
                                    tcVeEntity.setTcDistanceDon(getDistanceFromAPI(toaDoKhachHang, toaDoTaiXe));
                                }
                            }
                            tcVeEntity.setLastUpdatedDate(LocalDateTime.now());
                            tcVeEntity.setThuTuDon(veTcDTO.getThuTuDon());

                            //luu tcVe
                            veTcDao.save(tcVeEntity);

                            //Update lai table ban_ve_ve field thuTuDon
                            bvvService.updateBvvEntity(tcVeEntity);
                        }
                    }
                    try {
                        notificationService.saveAndSendNotificationToManager(topic,
                                ManagerNotificationType.DRIVER_CONFIRMED_PICKUP.getType(), true, listBvvId);
                    } catch (Exception e) {
                        LOG.warn("Error For Send Notify To Manager ", e);
                    }

                } else {
                    throw new TransportException("Dữ liệu không hợp lệ.");
                }
            }
            //Update table Lenh
            TcLenhEntity lenhTrungChuyenEntity = tcLenhRepository.findById(requestXacNhanLenh.getLenhId()).orElse(null);
            if (lenhTrungChuyenEntity != null) {
                lenhTrungChuyenEntity.setTrangThai(LenhConstants.LENH_STATUS_DANG_CHAY);
                lenhTrungChuyenEntity.setLastUpdatedDate(LocalDateTime.now());
                tcLenhRepository.save(lenhTrungChuyenEntity);
                LOG.info("Xác nhận lệnh thành công");

               //send notify to RabbitMQ
                sendNotifyToClient(lenhTrungChuyenEntity);
                //Send message to RabbitMQ
                for (VeTcDTO veTcDTO : listVeDTO) {
                    List<Integer> listBvvId = veTcDTO.getVeId();
                    if (listBvvId != null && listBvvId.size() > 0) {
                        trungChuyenDonService.sendInfoTrackingToMsgMQ(listBvvId.get(0));
                    }
                }
                //TODO Write log
                for (VeTcDTO veDto : requestXacNhanLenh.getListVeTc()) {
                    veDto.getVeId().forEach(bvvId -> {
                        String data = "{" + "\" Xác nhận lệnh đón\":[\"0\",\"1\"]}";
                        commonService.wrieZzLog(String.valueOf(lenhTrungChuyenEntity.getLaiXeId()), bvvId, data, "XÁC NHẬN LỆNH ĐÓN");
                    });
                }
                return true;
            } else {
                LOG.error("Lỗi xác nhận lệnh");
                return false;
            }
        } else {
            LOG.error("Lỗi xác nhận lệnh");
            return false;
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

    @Transactional
    @Override
    public boolean huyLenh(HuyLenhTcDTO requestHuyLenh) {
        LOG.info("Start Hủy Lệnh: " + requestHuyLenh.getLenhId());
        //Update Ve_TC status
        List<TcVeEntity> listVeTcDto = veTcDao.getVeTcByTcLenhId(requestHuyLenh.getLenhId());
        if (listVeTcDto == null || listVeTcDto.isEmpty()) {
            LOG.info("listVeTcDto is null or empty");
            throw new ResourceNotfoundException("listVeTcDto is null or empty");
        }
        //Update table Lenh
        TcLenhEntity lenhTrungChuyenEntity = tcLenhRepository.findById(requestHuyLenh.getLenhId()).orElse(null);
        if (lenhTrungChuyenEntity == null) {
            LOG.error("lenhTrungChuyenEntity is NULL");
            return false;
        }
        lenhTrungChuyenEntity.setTrangThai(LenhConstants.LENH_STATUS_DA_BI_HUY);
        lenhTrungChuyenEntity.setLastUpdatedDate(LocalDateTime.now());
        lenhTrungChuyenEntity.setLyDoHuy(requestHuyLenh.getLyDoHuy());
        lenhTrungChuyenEntity.setCanceledBy(lenhTrungChuyenEntity.getLaiXeId());
        tcLenhRepository.save(lenhTrungChuyenEntity);

        List<ZzLogBanVeEntity> logEntities = new ArrayList<>();
        for (TcVeEntity tcVeEntity : listVeTcDto) {

            //luu bang taixeve
            saveLaiXeVe(lenhTrungChuyenEntity.getLaiXeId(),requestHuyLenh.getLenhId(),tcVeEntity.getTcVeId(),VeConstants.TC_STATUS_DA_HUY,requestHuyLenh.getLyDoHuy());

            int oldTTD = tcVeEntity.getTcTrangThaiDon();
            int laiXeId = tcVeEntity.getLaiXeIdDon();
            if (BooleanUtils.isTrue(tcVeEntity.getTcIsHavazNow())) {
                tcVeEntity.setTcTrangThaiDon(VeConstants.TC_STATUS_CHUA_DIEU);
            } else {
                tcVeEntity.setTcTrangThaiDon(VeConstants.TC_STATUS_DA_HUY);
            }
            tcVeEntity.setLaiXeIdDon(Constant.ZERO);
            tcVeEntity.setTcLenhId(Constant.ZERO);
            if (tcVeEntity.getAdminLv2UserDon() != null) {
                tcVeEntity.setLyDoTuChoiDon(tcVeEntity.getAdminLv2UserDon().getAdmName() + Strings.COLON_DOT + requestHuyLenh.getLyDoHuy());
            } else {
                tcVeEntity.setLyDoTuChoiDon(requestHuyLenh.getLyDoHuy());
            }
            tcVeEntity.setThuTuDon(Constant.ZERO);
            tcVeEntity.setLastUpdatedDate(LocalDateTime.now());
            veTcDao.save(tcVeEntity);
            //Update lai table ban_ve_ve
            bvvService.updateBvvEntity(tcVeEntity);
            //Insert history
            ZzLogBanVeEntity logEntity;
            logEntity = new ZzLogBanVeEntity();
            logEntity.setLogAdminId(laiXeId);
            logEntity.setLogRecordId(tcVeEntity.getBvvId());
            //4: đã hủy
            logEntity.setLogData("{\"Trạng thái đón\":[\"" + VeConstants.GET_MSG_STATUS.get(oldTTD) + "\",\"" + VeConstants.GET_MSG_STATUS.get(4)
                    + "\"],\"Lý do\":[\"\"," + "\"" + requestHuyLenh.getLyDoHuy() + "\"]}");
            long epoch = System.currentTimeMillis() / 1000;
            String timeS = String.valueOf(epoch);
            int timeI = Integer.valueOf(timeS);
            logEntity.setLogTime(timeI);
            logEntity.setLogType(2);//1: Insert 2: Update
            logEntity.setLogIp("HỦY LỆNH ĐÓN");
            logEntity.setLogStatus(0);//ERP said it alway is 0
            logEntities.add(logEntity);
        }
        ZzLogRepository.saveAll(logEntities);
        List<Integer> bvvIds = listVeTcDto.stream().map(TcVeEntity::getBvvId).collect(Collectors.toList());
        try {
            notificationService.saveAndSendNotificationToManager(topic,
                    ManagerNotificationType.DRIVER_CANCEL_CMD.getType(), true, bvvIds);
        } catch (Exception e) {
            LOG.warn("Error For Send Notify To Manager ", e);
        }
        LOG.info("Hủy lệnh thành công");
        return true;
    }

    @Override
    @Transactional
    public void ketThucLenh(KetThucLenhDTO requestKetThucLenh) {
        LOG.info("Start kết thúc lệnh");
        //Update table Lenh
        TcLenhEntity lenhTrungChuyenEntity = tcLenhRepository.findById(requestKetThucLenh.getLenhId()).orElse(null);
        if (lenhTrungChuyenEntity != null) {
            lenhTrungChuyenEntity.setTrangThai(LenhConstants.LENH_STATUS_DA_HOAN_TAT);
            lenhTrungChuyenEntity.setLastUpdatedDate(LocalDateTime.now());
            tcLenhRepository.save(lenhTrungChuyenEntity);
            ketthucLenhHub(lenhTrungChuyenEntity.getTcLenhId());
            List<TcVeEntity> listTcVe = tcVeRepository.findByTcLenhId(lenhTrungChuyenEntity.getTcLenhId());
            String type = lenhTrungChuyenEntity.getKieuLenh() == 1 ? "đón" : "trả";
            listTcVe.forEach(tcVeEntity -> {

                String data = "{" + "\"Kết thúc lệnh " + type + " \":[\"0\",\"1\"]}";
                commonService.wrieZzLog(String.valueOf(lenhTrungChuyenEntity.getLaiXeId()), tcVeEntity.getBvvId(), data, "KẾT THÚC LỆNH ");
            });
        }

        LOG.info("kết thúc lệnh thành công");
    }

    @Override
    public List<KhachTrungChuyenDTO> getListKhachTrungChuyen(int taiXeId) {
        List<KhachTrungChuyenDTO> listKhachHangTC = new ArrayList<>();
        List<TcLenhEntity> listLenh = tcLenhRepositoryCustom.getLenhByTaiXeId(taiXeId, LenhConstants.LENH_DON);
        List<String> listPhone = new ArrayList<>();
        if (listLenh != null && listLenh.size() > 0) {
            TcLenhEntity lenhTc = listLenh.get(Constant.FIRST_ITEM);
            int lenhId = lenhTc.getTcLenhId();
            int lenhStatus = lenhTc.getTrangThai();

            ConDataDTO conDataDTO = CacheData.THONG_TIN_NHA_XE;

            List<Object[]> Objs = tcLenhRepositoryCustom.getListKhachTrungChuyen(lenhId);
            if (Objs != null && Objs.size() > 0) {
                for (Object[] arr : Objs) {
                    KhachTrungChuyenDTO tc = new KhachTrungChuyenDTO();
                    tc.setTenKhachDi(arr[0] != null ? String.valueOf(arr[0]) : null);
                    tc.setSdtKhachDi(arr[1] != null ? String.valueOf(arr[1]) : null);
                    listPhone.add(arr[1] != null ? String.valueOf(arr[1]) : null);
                    tc.setDiaChiDon(arr[2] != null ? String.valueOf(arr[2]) : null);
                    tc.setThuTuDon(arr[3] != null ? Integer.parseInt(arr[3].toString()) : 0);
                    tc.setThoiGianDon(arr[4] != null ? Integer.parseInt(arr[4].toString()) : 0);
                    tc.setTrangThaiTrungChuyen(arr[5] != null ? Integer.parseInt(arr[5].toString()) : 0);
                    tc.setLenhId(arr[6] != null ? Integer.parseInt(arr[6].toString()) : 0);
                    tc.setTaiXeId(arr[7] != null ? Integer.parseInt(arr[7].toString()) : 0);
                    tc.setTuyenDi(arr[8] != null ? String.valueOf(arr[8]) : null);
                    tc.setGioXuatBen(arr[9] != null ? String.valueOf(arr[9]) : null);
                    tc.setBienSoXe(arr[10] != null ? String.valueOf(arr[10]) : null);
                    tc.setKhachHangMoi(arr[11] != null ? Integer.parseInt(arr[11].toString()) : 0);
                    tc.setSoGhe(arr[12] != null ? String.valueOf(arr[12]) : null);
                    tc.setSoKhach(arr[13] != null ? String.valueOf(arr[13]) : null);
                    List<Integer> listBvvId = new ArrayList<>();
                    if (arr[14] != null) {
                        listBvvId = CommonUtils.convertListStringToInt(Arrays.asList(String.valueOf(arr[14]).split("\\s*,\\s*")));
                        tc.setVeId(listBvvId);
                    }
                    if(listBvvId.size() > 0) {
                        List<TcVeEntity> listVe = tcVeRepository.findByBvvIds(listBvvId);
                        if(listVe.size() > 0) {
                            List<TripStationDTO> listHub = xeTuyenService.getTripStations(listVe.get(0).getDidId());
                            int countTime = 0;
                            for(TripStationDTO hub : listHub ) {
                                countTime = countTime + hub.getTime();
                                if (Objects.equals(hub.getId(), listVe.get(0).getTcHubDiemDon())) {
                                    tc.setTimeXtToHub(DateTimeUtils
                                                              .addTimeFormatHHMM(tc.getGioXuatBen(), countTime));
                                    break;
                                }
                            }

                        }
                    }
                    tc.setSdtXe(arr[15] != null ? String.valueOf(arr[15]) : null);
                    tc.setGioDieuHanh(arr[16] != null ? String.valueOf(arr[16]) : null);
                    tc.setGhiChu(arr[17] != null ? String.valueOf(arr[17]) : null);
                    //lat
                    tc.setLatitude(arr[19] != null ? Double.parseDouble(String.valueOf(arr[19])) : null);
                    //long
                    tc.setLongitude(arr[20] != null ? Double.parseDouble(String.valueOf(arr[20])) : null);
                    tc.setDistanceDon(arr[21] != null ? Integer.parseInt(String.valueOf(arr[21])) : null);
                    tc.setDistanceToHubDon(arr[22] != null ? Double.parseDouble(String.valueOf(arr[22])) : null);
                    tc.setThoiGianToHubDon(arr[23] != null ? Integer.parseInt(String.valueOf(arr[23])) : 0);
                    tc.setCreateTime(arr[24] != null ? String.valueOf(arr[24]) : "");
                    tc.setGioVeBen("");
                    int thoiGianChay;
                    thoiGianChay = arr[25] != null ? Integer.parseInt(String.valueOf(arr[25])) : 0;
                    if (thoiGianChay > 0) {
                        SimpleDateFormat inputFormatter = new SimpleDateFormat("HH:mm");
                        Calendar calendar = Calendar.getInstance();
                        try {
                            calendar.setTime(inputFormatter.parse(tc.getGioXuatBen()));
                        } catch (ParseException e) {
                            LOG.error(e.getMessage(), e);
                            throw new TransportException(e);
                        }
                        calendar.add(Calendar.MINUTE, thoiGianChay);
                        tc.setGioVeBen(inputFormatter.format(calendar.getTime()));
                    }

                    int hubDon;
                    hubDon = arr[26] != null ? Integer.parseInt(String.valueOf(arr[26])) : 0;
                    BenXeEntity benXe = benXeRepo.findById(hubDon).orElse(null);
                    if (benXe != null) {
                    tc.setHubTraKhach(benXe.getTen());
                    }
                    tc.setNgayXeChay(arr[27] != null ? String.valueOf(arr[27]) : "");
                    tc.setIsHavazNow(lenhTc.getIsHavazNow() + StringUtils.EMPTY);
                    tc.setTenNhaXe(conDataDTO.getCon_company_name());
                    tc.setLenhStatus(lenhStatus);
                    listKhachHangTC.add(tc);
                }
            }
        }
        listKhachHangTC = layDuLieuRank(listKhachHangTC,listPhone);
        return listKhachHangTC;
    }

    @Override
    @Transactional
    public List<KhachTrungChuyenDTO> getListKhachTrungChuyenUpdate(double latitude, double longitude, int inputLenhId) {
        List<KhachTrungChuyenDTO> listKhachHangTCUpdate = new ArrayList<>();
        List<String> listPhone = new ArrayList<>();
        try {
//            List<TcLenhEntity> listLenh = tcLenhRepository.getLenhByTaiXeId(taiXeId, LenhConstants.LENH_DON);
//            if (listLenh != null && listLenh.size() > 0) {
//            }
//            TcLenhEntity lenhTc = listLenh.get(Constant.FIRST_ITEM);

            Optional<TcLenhEntity> tcLenhEntity = tcLenhRepository.findById(inputLenhId);
            int lenhId = 0;
            int lenhStatus = 0;
            boolean isHavazNow = false;
            if (tcLenhEntity.isPresent()) {
                lenhId = tcLenhEntity.get().getTcLenhId();
                lenhStatus = tcLenhEntity.get().getTrangThai();
                isHavazNow = tcLenhEntity.get().getIsHavazNow();
            }
            Point toaDoLaiXe = new Point(latitude, longitude);
            ConDataDTO conDataDTO = CacheData.THONG_TIN_NHA_XE;

            List<Object[]> Objs = tcLenhRepositoryCustom.getListKhachTrungChuyen(lenhId);
            if (Objs != null && Objs.size() > 0) {
                for (Object[] arr : Objs) {
                    KhachTrungChuyenDTO tc = new KhachTrungChuyenDTO();
                    tc.setTenKhachDi(arr[0] != null ? String.valueOf(arr[0]) : null);
                    tc.setSdtKhachDi(arr[1] != null ? String.valueOf(arr[1]) : null);
                    listPhone.add(arr[1] != null ? String.valueOf(arr[1]) : null);
                    tc.setDiaChiDon(arr[2] != null ? String.valueOf(arr[2]) : null);
                    tc.setThuTuDon(arr[3] != null ? Integer.parseInt(arr[3].toString()) : 0);
                    tc.setThoiGianDon(arr[4] != null ? Integer.parseInt(arr[4].toString()) : 0);

                    //Tính lại thời gian đón real time
                    int time = 0;
                    int distanceDon = 0;
                    if (arr[19] != null && arr[20] != null) {
                        double latitudeKhachHang = Double.valueOf(String.valueOf(arr[19]));
                        double longitudeKhachHang = Double.valueOf(String.valueOf(arr[20]));
                        Point toaDoKhachHang = new Point(latitudeKhachHang, longitudeKhachHang);
                        time = getThoiGianDonFromAPI(toaDoLaiXe, toaDoKhachHang);
                        distanceDon = getDistanceFromAPI(toaDoLaiXe, toaDoKhachHang);
                        if (distanceDon > 0) {
                            tc.setDistanceDon(distanceDon);
                        }
                        if (time > 0)
                            tc.setThoiGianDon(time);
                    }
                    tc.setTrangThaiTrungChuyen(arr[5] != null ? Integer.parseInt(arr[5].toString()) : 0);
                    tc.setLenhId(arr[6] != null ? Integer.parseInt(arr[6].toString()) : 0);
                    tc.setTaiXeId(arr[7] != null ? Integer.parseInt(arr[7].toString()) : 0);
                    tc.setTuyenDi(arr[8] != null ? String.valueOf(arr[8]) : null);
                    tc.setGioXuatBen(arr[9] != null ? String.valueOf(arr[9]) : null);
                    tc.setBienSoXe(arr[10] != null ? String.valueOf(arr[10]) : null);
                    tc.setKhachHangMoi(arr[11] != null ? Integer.parseInt(arr[11].toString()) : 0);
                    tc.setSoGhe(arr[12] != null ? String.valueOf(arr[12]) : null);
                    tc.setSoKhach(arr[13] != null ? String.valueOf(arr[13]) : null);
                    List<Integer> listBvvId = new ArrayList<>();
                    if (arr[14] != null) {
                        listBvvId = CommonUtils.convertListStringToInt(Arrays.asList(String.valueOf(arr[14]).split("\\s*,\\s*")));
                        tc.setVeId(listBvvId);
                        if (time > 0) {
                            tcVeRepository.updateThoiGianDon(listBvvId, time, distanceDon);
                        }
                    }
                    if (!CollectionUtils.isEmpty(listBvvId)) {
                        try {
                            List<TcVeEntity> listVe = tcVeRepository.findByBvvIds(listBvvId);
                            if (!listVe.isEmpty()) {
                                List<TripStationDTO> listHub = xeTuyenService.getTripStations(listVe.get(0).getDidId());
                                int countTime = 0;
                                for (TripStationDTO hub : listHub) {
                                    countTime = countTime + hub.getTime();
                                    if (Objects.equals(hub.getId(), listVe.get(0).getTcHubDiemDon())) {
                                        tc.setTimeXtToHub(DateTimeUtils
                                                                  .addTimeFormatHHMM(tc.getGioXuatBen(), countTime));
                                        break;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            tc.setTimeXtToHub(Strings.EMPTY);
                            LOG.warn(e.getMessage(), e);
                        }
                    }
                    tc.setSdtXe(arr[15] != null ? String.valueOf(arr[15]) : null);
                    tc.setGioDieuHanh(arr[16] != null ? String.valueOf(arr[16]) : null);
                    tc.setGhiChu(arr[18] != null ? String.valueOf(arr[18]) : null);
                    //lat
                    tc.setLatitude(arr[19] != null ? Double.parseDouble(String.valueOf(arr[19])) : null);
                    //long
                    tc.setLongitude(arr[20] != null ? Double.parseDouble(String.valueOf(arr[20])) : null);
                    tc.setDistanceDon(arr[21] != null ? Integer.parseInt(String.valueOf(arr[21])) : null);
                    tc.setDistanceToHubDon(arr[22] != null ? Double.parseDouble(String.valueOf(arr[22])) : null);
                    tc.setThoiGianToHubDon(arr[23] != null ? Integer.parseInt(String.valueOf(arr[23])) : 0);
                    tc.setCreateTime(arr[24] != null ? String.valueOf(arr[24]) : Strings.EMPTY);
                    tc.setGioVeBen(Strings.EMPTY);
                    int thoiGianChay = arr[25] != null ? Integer.parseInt(String.valueOf(arr[25])) : 0;
                    if (thoiGianChay > 0) {
                        SimpleDateFormat inputFormatter = new SimpleDateFormat("HH:mm");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(inputFormatter.parse(tc.getGioXuatBen()));
                        calendar.add(Calendar.MINUTE, thoiGianChay);
                        tc.setGioVeBen(inputFormatter.format(calendar.getTime()));
                    }
                    int hubDon;
                    hubDon = arr[26] != null ? Integer.parseInt(String.valueOf(arr[26])) : 0;
                    BenXeEntity benXe = benXeRepo.findById(hubDon).orElse(null);
                    if (benXe != null) {
                        tc.setHubTraKhach(benXe.getTen());
                    }
                    tc.setNgayXeChay(arr[27] != null ? String.valueOf(arr[27]) : Strings.EMPTY);
                    tc.setIsHavazNow(isHavazNow + StringUtils.EMPTY);
                    tc.setTenNhaXe(conDataDTO.getCon_company_name());
                    tc.setLenhStatus(lenhStatus);
                    listKhachHangTCUpdate.add(tc);
                }
            }
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
            throw new TransportException(e);
        }

        listKhachHangTCUpdate = layDuLieuRank(listKhachHangTCUpdate,listPhone);
        return listKhachHangTCUpdate;
    }

    @Override
    public List<KhachTrungChuyenDTO> getKhachDaDon(int taiXeId) {
        List<KhachTrungChuyenDTO> listKhachHangTC = new ArrayList<>();
        List<TcLenhEntity> listLenh = tcLenhRepositoryCustom.getLenhDangDon(taiXeId, LenhConstants.LENH_DON);
        if (listLenh != null && !listLenh.isEmpty()) {
            TcLenhEntity lenhTc = listLenh.get(Constant.FIRST_ITEM);
            int lenhId = lenhTc.getTcLenhId();
            int lenhStatus = lenhTc.getTrangThai();

            ConDataDTO conDataDTO = CacheData.THONG_TIN_NHA_XE;

            List<Object[]> Objs = tcLenhRepositoryCustom.getListKhachDangDon(lenhId);
            if (Objs != null && Objs.size() > 0) {
                for (Object[] arr : Objs) {
                    KhachTrungChuyenDTO tc = new KhachTrungChuyenDTO();
                    tc.setTenKhachDi(arr[0] != null ? String.valueOf(arr[0]) : null);
                    tc.setSdtKhachDi(arr[1] != null ? String.valueOf(arr[1]) : null);
                    tc.setDiaChiDon(arr[2] != null ? String.valueOf(arr[2]) : null);
                    tc.setThuTuDon(arr[3] != null ? Integer.parseInt(arr[3].toString()) : 0);
                    tc.setThoiGianDon(arr[4] != null ? Integer.parseInt(arr[4].toString()) : 0);
                    tc.setTrangThaiTrungChuyen(arr[5] != null ? Integer.parseInt(arr[5].toString()) : 0);
                    tc.setLenhId(arr[6] != null ? Integer.parseInt(arr[6].toString()) : 0);
                    tc.setTaiXeId(arr[7] != null ? Integer.parseInt(arr[7].toString()) : 0);
                    tc.setTuyenDi(arr[8] != null ? String.valueOf(arr[8]) : null);
                    tc.setGioXuatBen(arr[9] != null ? String.valueOf(arr[9]) : null);
                    tc.setBienSoXe(arr[10] != null ? String.valueOf(arr[10]) : null);
                    tc.setKhachHangMoi(arr[11] != null ? Integer.parseInt(arr[11].toString()) : 0);
                    tc.setSoGhe(arr[12] != null ? String.valueOf(arr[12]) : null);
                    tc.setSoKhach(arr[13] != null ? String.valueOf(arr[13]) : null);
                    if (arr[14] != null) {
                        List<Integer> listBvvId = CommonUtils.convertListStringToInt(Arrays.asList(String.valueOf(arr[14]).split("\\s*,\\s*")));
                        tc.setVeId(listBvvId);
                    }
                    tc.setSdtXe(arr[15] != null ? String.valueOf(arr[15]) : null);
                    tc.setGioDieuHanh(arr[16] != null ? String.valueOf(arr[16]) : null);
                    tc.setTenNhaXe(conDataDTO.getCon_company_name());
                    tc.setLenhStatus(lenhStatus);
                    listKhachHangTC.add(tc);
                }
            }
        }
        return listKhachHangTC;
    }

    /**
     * @param taiXeId
     * @param requestDate Format dd-MM-yyyy
     */
    @Override
    public List<LichSuTrungChuyenDTO> getHistoryByTaiXeId(int taiXeId, String requestDate) {
        List<LichSuTrungChuyenDTO> histories = new ArrayList<>();
        // Format date from mobile is: dd-MM-yyyy
        LocalDate localDate = com.havaz.transport.core.utils.DateTimeUtils
                .convertStringToLocalDate(requestDate,
                                          com.havaz.transport.core.utils.DateTimeUtils.DATE_DD_MM_YYYY);
        Assert.notNull(localDate, "localDate cannot be null");
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        List<Object[]> Objs = tcLenhRepositoryCustom.getHistoryByTaiXeId(taiXeId, month, year);
        if (Objs != null && Objs.size() > 0) {
            for (Object[] arr : Objs) {
                LichSuTrungChuyenDTO history;
                if (arr[0] != null && !arr[0].toString().isEmpty()) {
                    histories.add(setHistoryData(LenhConstants.HST_LENH_TYPE_IS_FINISHED, arr[0].toString()));
                } else {
                    histories.add(setHistoryData(LenhConstants.HST_LENH_TYPE_IS_FINISHED, Strings.DASH));
                }
                if (arr[1] != null && !arr[1].toString().isEmpty()) {
                    histories.add(setHistoryData(LenhConstants.HST_LENH_TYPE_IS_CANCELED, arr[1].toString()));
                } else {
                    histories.add(setHistoryData(LenhConstants.HST_LENH_TYPE_IS_CANCELED, Strings.DASH));
                }
                if (arr[2] != null && !arr[2].toString().isEmpty()) {
                    histories.add(setHistoryData(LenhConstants.HST_CUST_TYPE_IS_PICKED_UP, arr[2].toString()));
                } else {
                    histories.add(setHistoryData(LenhConstants.HST_CUST_TYPE_IS_PICKED_UP, Strings.DASH));
                }
                if (arr[3] != null && !arr[3].toString().isEmpty()) {
                    histories.add(setHistoryData(LenhConstants.HST_CUST_TYPE_IS_CANCELED, arr[3].toString()));
                } else {
                    histories.add(setHistoryData(LenhConstants.HST_CUST_TYPE_IS_CANCELED, Strings.DASH));
                }
            }
        }
        return histories;
    }

    private LichSuTrungChuyenDTO setHistoryData(String historyType, String countNumber) {
        LichSuTrungChuyenDTO history = new LichSuTrungChuyenDTO();
        history.setHistoryType(historyType);
        history.setCountNumber(countNumber);
        return history;
    }

    @Override
    public PageCustom<ChiTietLenhDTO> getAllLenhCustom(FilterLenhForm filterLenhForm) {
        List<ChiTietLenhDTO> objs = tcLenhRepositoryCustom.getListChiTietLenh(filterLenhForm);
        long totalPage = tcLenhRepositoryCustom.getCountListChiTietLenh(filterLenhForm);

        for (ChiTietLenhDTO chiTietLenhDTO : objs) {
            if (chiTietLenhDTO.getKieuLenh() == LenhConstants.LENH_DON) {
                chiTietLenhDTO.setTongKhach(chiTietLenhDTO.getKhachDon().intValue());
            } else {
                chiTietLenhDTO.setTongKhach(chiTietLenhDTO.getKhachTra().intValue());
            }
            chiTietLenhDTO.setKieuLenhText(LenhConstants.GET_KIEU_LENH.get(chiTietLenhDTO.getKieuLenh()));
            chiTietLenhDTO.setTrangThaiText(LenhConstants.GET_MSG_STATUS.get(chiTietLenhDTO.getTrangThai()));
        }

        filterLenhForm.getPaging().setPage(filterLenhForm.getPaging().getPage() <= 0 ? 1 : filterLenhForm.getPaging().getPage());
        filterLenhForm.getPaging().setSize(filterLenhForm.getPaging().getSize() <= 0 ? Constant.CONFIGURATION_NO_10 : filterLenhForm.getPaging().getSize());
        return convertToPageCustom(objs,filterLenhForm, (int) totalPage);
    }

    @Override
    public List<MaLenhDTO> getMaLenh(FilterLenhForm filterLenhForm) {
        List<MaLenhDTO> list = tcLenhRepositoryCustom.getMaLenh(filterLenhForm);

        list.forEach(e -> e.setMaLenh(CommonUtils.convertMaLenh(e.getLenhId(), e.getKieuLenh())));
        return list;
    }

    @Override
    public ChiTietLenhDTO getLenhById(int lenhId) {
        ChiTietLenhDTO chiTietLenhDTO = new ChiTietLenhDTO();
        LOG.debug("Bắt đầu lấy thông tin lệnh lenhId: {}", lenhId);
        Optional<TcLenhEntity> tcLenhEntityOptional = tcLenhRepository.findById(lenhId);

        if (tcLenhEntityOptional.isPresent()) {
            TcLenhEntity tcLenhEntity = tcLenhEntityOptional.get();
            BeanUtils.copyProperties(tcLenhEntity, chiTietLenhDTO);
            chiTietLenhDTO.setKieuLenhText(LenhConstants.GET_KIEU_LENH.get(chiTietLenhDTO.getKieuLenh()));
            chiTietLenhDTO.setTrangThaiText(LenhConstants.GET_MSG_STATUS.get(chiTietLenhDTO.getTrangThai()));
            chiTietLenhDTO.setMaLenh(CommonUtils.convertMaLenh(chiTietLenhDTO.getTcLenhId(),
                                                               chiTietLenhDTO.getKieuLenh()));
            chiTietLenhDTO.setCreatedDate(tcLenhEntity.getCreatedDate());
            chiTietLenhDTO.setNguoiTao(adminLv2UserService.findAdminNameById(tcLenhEntity.getCreatedBy()));

            //Nếu là lệnh đã hủy thì lấy danh sách khách đã hủy
            if(tcLenhEntity.getTrangThai() == LenhConstants.LENH_STATUS_DA_BI_HUY){
                List<VeTrungChuyenDTO> lst = getListVeTCChoLenhBiHuy(lenhId);
                chiTietLenhDTO.setVeTrungChuyenDTOList(lst);
                return chiTietLenhDTO;
            }

            //Nếu là lệnh đón => lấy list khách đón và ngược lại:
            List<Object[]> Objs = chiTietLenhDTO.getKieuLenh() == LenhConstants.LENH_DON ?
                    tcLenhRepositoryCustom.getListKhachTrungChuyen(lenhId) : tcLenhRepositoryCustom.getListKhachTrungChuyenTra(lenhId);

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
                chiTietLenhDTO.setTongKhach(tongKhachCuaLenh);
            }
            chiTietLenhDTO.setVeTrungChuyenDTOList(list);
            LOG.debug("Kết thúc lấy thông tin lệnh lenhId: {}", lenhId);
        }
        return chiTietLenhDTO;
    }

    @Override
    @Transactional
    public boolean luuLenh(LuuLenhDTO requestLuuLenhDTO) {
        LOG.info("Start Lưu Lệnh");
        //Kiem tra lenh nay là lệnh đón hay trả để cập nhật lại
        int lenhId = requestLuuLenhDTO.getLenhId();
        int laiXeId = requestLuuLenhDTO.getTaiXeId();
        Optional<TcLenhEntity> tcLenhEntityOptional = tcLenhRepository.findById(lenhId);
        if(tcLenhEntityOptional.isPresent()){
            TcLenhEntity tcLenhEntity = tcLenhEntityOptional.get();

            //Chỉ được lưu các lệnh có trạng thái đã tạo
            if (tcLenhEntity.getTrangThai() != LenhConstants.LENH_STATUS_DA_TAO) {
                LOG.info("Không được sửa lệnh nếu ko phải lệnh đã tạo");
                throw new TransportException("Không được sửa lệnh nếu ko phải lệnh đã tạo");
            }

            //cap nhat lai lai xe trong bang lenh neu thay doi
            tcLenhRepository.updateLenhLaiXeId(laiXeId, LocalDateTime.now(),lenhId);

            //nếu là lệnh đón thì reset lại trong bảng TcVe (tc_lenh_Id = 0 và lai_xe_don = 0)
            //nếu là lệnh trả thì update lenhTra va lai_xe_tra
            if (tcLenhEntity.getKieuLenh() == LenhConstants.LENH_DON) {
                veTcDao.updateVeSetLenhDonIdZero(lenhId);
            } else {
                veTcDao.updateVeSetLenhTraIdZero(lenhId);
            }


            //Cap nhat lai tat ca bang ve voi lenh_id va ghi chu moi gui len
            List<VeForm> veForms = requestLuuLenhDTO.getDanhSachVe();
            if (veForms != null && !veForms.isEmpty()) {
                for (VeForm veForm : veForms) {
                    List<Integer> veBvvIds = veForm.getBvvIds();
                    for(int veBvvId : veBvvIds){
                        TcVeEntity tcVeEntity = veTcDao.getByBvvId(veBvvId);
                        if (tcVeEntity != null) {
                            tcVeEntity.setBvvGhiChu(veForm.getGhiChu());
                            if (tcLenhEntity.getKieuLenh() == LenhConstants.LENH_DON) {
                                tcVeEntity.setTcLenhId(lenhId);
                                tcVeEntity.setLaiXeIdDon(laiXeId);
                            } else {
                                tcVeEntity.setTcLenhTraId(lenhId);
                                tcVeEntity.setLaiXeIdTra(laiXeId);
                            }
                            veTcDao.save(tcVeEntity);
                        } else {
                            LOG.error("Lỗi lưu lệnh: không tìm thấy vé: {}", veBvvId);
                            return false;
                        }
                    }
                }
                return true;
            } else {
                LOG.error("Lỗi lưu lệnh");
                return false;
            }
        } else {
            LOG.error("Không tìm thấy lệnh lenhId: {}", lenhId);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean dieuLenh(LuuLenhDTO requestDieuLenhDTO) {
        LOG.debug("Start Lưu Lệnh");
        //Kiem tra lenh nay là lệnh đón hay trả để cập nhật lại
        int lenhId = requestDieuLenhDTO.getLenhId();
        int laiXeId = requestDieuLenhDTO.getTaiXeId();
        LocalDateTime currentTime = LocalDateTime.now();
        Optional<TcLenhEntity> tcLenhEntityOptional = tcLenhRepository.findById(lenhId);
        if(tcLenhEntityOptional.isPresent()) {
            TcLenhEntity tcLenhEntity = tcLenhEntityOptional.get();

            //Check lai xe co lenh nao da dieu hoac dang chay hay chua
            Set<Integer> trangThaiDaDieuHoacDangChay = new HashSet<>();

            trangThaiDaDieuHoacDangChay.add(LenhConstants.LENH_STATUS_DA_DIEU);
            trangThaiDaDieuHoacDangChay.add(LenhConstants.LENH_STATUS_DANG_CHAY);
            int type = tcLenhEntity.getKieuLenh();
            List<TcLenhEntity> listLenh = tcLenhRepository.getAllByLaiXeIdAAndTrangThai(laiXeId, trangThaiDaDieuHoacDangChay,type);

            if (!listLenh.isEmpty()) {
                throw new TransportException("Lái xe này đã có lệnh, yêu cầu chỉ điều bổ sung.");
            }

            // Set lại trạng thái của lệnh là đã điều
            tcLenhEntity.setTrangThai(LenhConstants.LENH_STATUS_DA_DIEU);
            tcLenhEntity.setLastUpdatedDate(currentTime);

            List<VeForm> veForms = requestDieuLenhDTO.getDanhSachVe();
            if (veForms != null && veForms.size() > 0) {
                LOG.debug("Tổng số vé trong lệnh:: {}", veForms.size());
                for (VeForm veForm : requestDieuLenhDTO.getDanhSachVe()) {
                    //Save info Ve
                    saveVeDonTra(veForm,LenhConstants.LENH_STATUS_DA_DIEU,tcLenhEntity.getTcLenhId(),laiXeId,tcLenhEntity.getKieuLenh());
                }
                LOG.debug("Update vé cho lệnh >>> DONE.");
            }
            //Send noti and data to firebase
            if (LenhConstants.LENH_STATUS_DA_DIEU == tcLenhEntity.getKieuLenh()) {
                LOG.debug("Send notification to firebase");
                FirebaseNotiDataForm firebaseNotiDataForm = new FirebaseNotiDataForm();
                firebaseNotiDataForm.setTypeNotify(NotificationType.NEW_CMD.getType());
                sendNotifyToDriver(tcLenhEntity.getLaiXeId(), type, firebaseNotiDataForm, AudioType.NEW_CMD.getValue());
            }

            return false;
        } else {
            LOG.error("Không tìm thấy lệnh lenhId: "+lenhId);
            return false;
        }
    }

    @Override
    public List<TrangThaiLenhDTO> getAllTrangThaiLenh() {
        LOG.info("Bắt đầu lấy các trạng thái lệnh");
        List<TrangThaiLenhDTO> trangThaiLenhDTOS = new ArrayList<>();
        LenhConstants.GET_MSG_STATUS.forEach((k, v) -> {
            TrangThaiLenhDTO trangThaiLenhDTO = new TrangThaiLenhDTO();
            trangThaiLenhDTO.setTrangThaiId(k);
            trangThaiLenhDTO.setTrangThaiText(v);
            trangThaiLenhDTOS.add(trangThaiLenhDTO);
        });
        return trangThaiLenhDTOS;
    }

    private PageCustom convertToPageCustom(List<ChiTietLenhDTO> content,FilterLenhForm filterLenhForm,int totalPage){
        PageCustom<ChiTietLenhDTO> pageCustom = new PageCustom<>();
        pageCustom.setContent(content);
        pageCustom.setPage(filterLenhForm.getPaging().getPage());
        pageCustom.setSize(filterLenhForm.getPaging().getSize());
        pageCustom.setTotalElement(totalPage);
        pageCustom.setSortBy(filterLenhForm.getPaging().getSortBy());
        pageCustom.setSortType(filterLenhForm.getPaging().getSortType());
        return pageCustom;
    }

    /**
     * Save data don tra
     * @param veForm
     * @param type
     * @param lenhId
     * @param taiXeId
     * @param lenhStatus
     */
    private void saveVeDonTra(VeForm veForm, int type, int lenhId, int taiXeId, int lenhStatus) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Integer> bvvIds = veForm.getBvvIds();
        int thoiGian = 3;
        if (bvvIds != null && !bvvIds.isEmpty()) {
            for (int bvvId : bvvIds) {
                TcVeEntity tcVeEntity = tcVeRepository.getByBvvId(bvvId);
                if (tcVeEntity != null) {
                    if (LenhConstants.LENH_DON == type) {
                        if (tcVeEntity.getTcLenhId() != null && tcVeEntity.getTcLenhId() > 0) {
                            LOG.error("[bvvId] Đã được gán lệnh :: {}", bvvId);
                            throw new TransportException("[bvvId] Đã được gán lệnh :: " + bvvId);
                        }
                        tcVeEntity.setLaiXeIdDon(taiXeId);
                        tcVeEntity.setGhiChuDon(veForm.getGhiChu());
//                        tcVeEntity.setThuTuDon(veForm.getThuTu());
                        tcVeEntity.setThoiGianDon(thoiGian);
                        tcVeEntity.setTcTrangThaiDon(lenhStatus);
                        tcVeEntity.setTcLenhId(lenhId);
                    } else if (LenhConstants.LENH_TRA == type) {
                        if (tcVeEntity.getTcLenhTraId() != null &&
                                tcVeEntity.getTcLenhTraId() > 0) {
                            LOG.error("[bvvId] Đã được gán lệnh :: {}", bvvId);
                            throw new TransportException("[bvvId] Đã được gán lệnh :: " + bvvId);
                        }
                        tcVeEntity.setLaiXeIdTra(taiXeId);
                        tcVeEntity.setGhiChuTra(veForm.getGhiChu());
//                        tcVeEntity.setThuTuTra(veForm.getThuTu());
                        tcVeEntity.setThoiGianTra(thoiGian);
                        tcVeEntity.setTcTrangThaiTra(lenhStatus);
                        tcVeEntity.setTcLenhTraId(lenhId);
                    }

//                            tcVeEntity.setLastUpdatedBy();
                    tcVeEntity.setLastUpdatedDate(currentTime);
                    //Save into DB
                    tcVeRepository.save(tcVeEntity);
                    LOG.info("Save done [bvvId]:: {}", bvvId);
                } else{
                    LOG.error("[bvvId] Không tồn tại :: {}", bvvId);
                    throw new TransportException("[bvvId] Không tồn tại :: " + bvvId);
                }
                thoiGian = thoiGian + 3;
            }
        }
    }

    /**
     * Send noti and data to firebase
     * @param taiXeId
     * @param type
     */
    private void sendNotiAndData(int taiXeId, int type, boolean isHavazNow) {
        String title = messageConfig.getMessage(Constant.MSG_FIREBASE_DIEU_BO_SUNG_DON);
        String topicName = Constant.FIREBASE_TOPIC_NAME + taiXeId;
        if (LenhConstants.LENH_TRA == type) {
            title = messageConfig.getMessage(Constant.MSG_FIREBASE_DIEU_BO_SUNG_TRA);
            topicName = Constant.FIREBASE_TOPIC_NAME + "TRA-" + taiXeId;
        }
//        firebaseClientService.sendNotification(taiXeId, title, title, new FirebaseNotiDataForm("", "", type, isHavazNow));
        firebaseClientService.sendDataToTopic(new Object(), topicName);
    }

    private void sendNotifyToDriver(int driverId, int type, Object data, String audio) {
        String title = messageConfig.getMessage(Constant.MSG_FIREBASE_DIEU_BO_SUNG_DON);
        String topicNameFromCache = CacheData.CONFIGURATION_DATA.get(Constant.FIREBASE_TOPIC_NAME);
        String topicName = topicNameFromCache + driverId;
        if (LenhConstants.LENH_TRA == type) {
            title = messageConfig.getMessage(Constant.MSG_FIREBASE_DIEU_BO_SUNG_TRA);
            topicName = topicNameFromCache + "TRA-" + driverId;
        }
        firebaseClientService.sendNotification(driverId, title, title, data, audio);
        firebaseClientService.sendDataToTopicFirebase(topicName, data);
    }

    @Override
    public TripTrungChuyenDTO getXeTuyenInfor(int lenhId) {
        TripTrungChuyenDTO tripTrungChuyenDTO = new TripTrungChuyenDTO();
        ConDataDTO conDataDTO = CacheData.THONG_TIN_NHA_XE;
        TcLenhEntity tcLenhEntity = tcLenhRepository.findById(lenhId)
                .orElseThrow(() -> new TransportException("ERROR: Không tồn tại lệnh: " + lenhId));
        XeEntity xeEntity = xeRepository.findById(tcLenhEntity.getXeId())
                .orElseThrow(() -> new TransportException("ERROR: Không có xe TC trong lệnh: " + lenhId));
        List<XeTuyenDTO> listXeTuyenDto = tcLenhRepositoryCustom.getXeTuyenInfor(lenhId);
        if (listXeTuyenDto.isEmpty()) {
            throw new TransportException("ERROR: Lệnh: " + lenhId + " không có xe tuyến nào!");
        }
        listXeTuyenDto.forEach(e -> e.setMachainId(conDataDTO.getCon_code()));
        tripTrungChuyenDTO.setSdtTc(xeEntity.getXeSoDienThoai());
        tripTrungChuyenDTO.setListXeTuyen(listXeTuyenDto);
        return tripTrungChuyenDTO;
    }

    @Override
    @Transactional
    public boolean updateThuTuDon(UpdateThuTuDonDTO requestUpdateTtdDto) {
        LOG.debug("Start Cập nhật lại thứ tự đón");
        //Update Ve_TC status
        List<UpdateThuTuDonVeTcDTO> listVeDTO = requestUpdateTtdDto.getListVeTc();
        if (listVeDTO != null && !listVeDTO.isEmpty()) {
            LOG.debug("listVeDTO: {}", listVeDTO);
            for (UpdateThuTuDonVeTcDTO veTcDTO : listVeDTO) {
                List<Integer> listBvvId = veTcDTO.getVeId();
                if (listBvvId == null || listBvvId.isEmpty()) {
                    LOG.debug("listBvvId in request header is NULL or empty ");
                    return false;
                }
                LOG.debug("listBvvId: {}", listBvvId);
                for (int bvvId : listBvvId) {
                    TcVeEntity tcVeEntity = veTcDao.getByBvvId(bvvId);
                    if (tcVeEntity == null) {
                        LOG.info("Không tìm thấy Vé TC tương ứng bvvId={}", bvvId);
                        return false;
                    }
                    LOG.debug("------------START UPDATE TTD cho tcVeEntity.Id: {}------------", tcVeEntity.getTcVeId());
                    tcVeEntity.setThuTuDon(veTcDTO.getThuTuDon());
                    tcVeEntity.setLastUpdatedDate(LocalDateTime.now());
                    veTcDao.save(tcVeEntity);
                    LOG.debug("------------END UPDATE TTD cho tcVeEntity.Id: {}------------", tcVeEntity.getTcVeId());
                    LOG.debug("------------START UPDATE TTD cho bvvId.Id:------------");
                    //Update lai table ban_ve_ve field thuTuDon
                    bvvService.updateBvvEntity(tcVeEntity);
                    LOG.debug("------------END UPDATE TTD cho bvvId.Id:------------");
                }
            }
            LOG.debug("End cập nhật lại thứ tự đón");
            return true;
        } else {
            LOG.debug("listVeDTO in request header is NULL or empty");
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateThuTuTra(UpdateThuTuTraDTO requestUpdateTttDto) {
        LOG.debug("Start Cập nhật lại thứ tự trả");
        //Update Ve_TC status
        List<UpdateThuTuTraVeTcDTO> listVeDTO = requestUpdateTttDto.getListVeTcUpdate();
        if (listVeDTO != null && !listVeDTO.isEmpty()) {
            LOG.debug("listVeDTO: {}", listVeDTO);
            for (UpdateThuTuTraVeTcDTO veTcDTO : listVeDTO) {
                List<Integer> listBvvId = veTcDTO.getVeId();
                if (listBvvId == null || listBvvId.isEmpty()) {
                    LOG.debug("listBvvId in request header is NULL or empty ");
                    return false;
                }
                LOG.debug("listBvvId: {}", listBvvId);
                for (int bvvId : listBvvId) {
                    TcVeEntity tcVeEntity = veTcDao.getByBvvId(bvvId);
                    if (tcVeEntity == null) {
                        LOG.debug("Không tìm thấy Vé TC tương ứng bvvId={}", bvvId);
                        return false;
                    }
                    LOG.debug("------------START UPDATE TTD cho tcVeEntity.Id: " + tcVeEntity.getTcVeId() + "------------");
                    tcVeEntity.setThuTuTra(veTcDTO.getThuTuTra());
                    tcVeEntity.setLastUpdatedDate(LocalDateTime.now());
                    veTcDao.save(tcVeEntity);
                    LOG.debug("------------END UPDATE TTT cho tcVeEntity.Id: " + tcVeEntity.getTcVeId() + "------------");
                    LOG.debug("------------START UPDATE TTT cho bvvId.Id:------------");
                    //Update lai table ban_ve_ve field thuTuDon
                    bvvService.updateBvvEntity(tcVeEntity);
                    LOG.debug("------------END UPDATE TTT cho bvvId.Id:------------");
                }
            }
            LOG.debug("End cập nhật lại thứ tự trả");
            return true;
        } else {
            LOG.debug("listVeDTO in request header is NULL or empty");
            return false;
        }
    }

    private double getTocDoCuaVung(int vungId) {
        return vungTrungChuyenRepositoryCustom.getAverageSpeedById(vungId);
    }

    private int getThoiGianDonFromAPI(Point pointStart, Point pointEnd) {
        if (pointStart.getLat() == 0 || pointStart.getLng() == 0 ||
                pointEnd.getLat() == 0 || pointEnd.getLng() == 0) {
            return 0;
        }
        double thoiGian = locationService.getDurationFromAPI(pointStart, pointEnd);
        LOG.info("Thoi gian giua 2 diem: " + pointStart + " va " + pointEnd + " la: " + thoiGian);
        return (int) Math.round(thoiGian);
    }

    private Integer getDistanceFromAPI(Point pointStart, Point pointEnd) {
        try {
            if (pointStart.getLat() == 0 || pointStart.getLng() == 0 ||
                    pointEnd.getLat() == 0 || pointEnd.getLng() == 0) {
                return 0;
            }
            double distance = locationService.getDistanFromAPI(pointStart, pointEnd);
            return (int) Math.round(distance);
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public List<LenhTcDTO> getListLenhByLaiXe(int laiXeId) {
        List<LenhTcDTO> lenhTcDTOs = new ArrayList<>();
        Set<Integer> trangThai = new HashSet<>();
        trangThai.add(LenhConstants.LENH_STATUS_DA_DIEU);
        trangThai.add(LenhConstants.LENH_STATUS_DANG_CHAY);
        List<TcLenhEntity> tcLenhEntities = tcLenhRepository.findByLaiXeIdAndTrangThai(laiXeId,trangThai);

        tcLenhEntities.forEach(tcLenhEntity -> {
            LenhTcDTO lenhTcDTO = new LenhTcDTO();
            BeanUtils.copyProperties(tcLenhEntity,lenhTcDTO);
            lenhTcDTOs.add(lenhTcDTO);
        });

        return lenhTcDTOs;
    }

    @Override
    public List<LenhTcDTO> getListLenhByLaiXe(int laiXeId, int type) {
        List<LenhTcDTO> lenhTcDTOs = new ArrayList<>();
        Set<Integer> trangThai = new HashSet<>();
        trangThai.add(LenhConstants.LENH_STATUS_DA_DIEU);
        trangThai.add(LenhConstants.LENH_STATUS_DANG_CHAY);
        List<TcLenhEntity> tcLenhEntities = tcLenhRepository.findByLaiXeIdAndTrangThaiAndKieuLenh(laiXeId, trangThai, type);

        tcLenhEntities.forEach(tcLenhEntity -> {
            LenhTcDTO lenhTcDTO = new LenhTcDTO();
            BeanUtils.copyProperties(tcLenhEntity,lenhTcDTO);
            lenhTcDTOs.add(lenhTcDTO);
        });

        return lenhTcDTOs;
    }

    @Override
    public List<LenhContainGXBDTO> getListLenhByLaiXeupdate(int laiXeId, int type) {
        return tcLenhRepositoryCustom.getListLenhCoChuaGioXuatBen(laiXeId,type);
    }

    @Override
    @Transactional
    public void cancelPickup(CmdHavazNowDTO cmdHavazNowDTO) {
        final int firstItem = 0;
        final int xacNhanLenh = 2;
        List<TcVeEntity> veEntities = veTcDao.findByBvvIds(cmdHavazNowDTO.getBvvIds());
        final int currentCmd = veEntities.get(firstItem).getTcLenhId();
        if (veEntities.get(firstItem).getTcLenhId() == null || veEntities.get(firstItem).getTcLenhId() == 0) {
            LOG.debug(" ==== HuyLenhHavazNow Không có lênh với vé: {}", veEntities.get(firstItem).getBvvId());
            return;
        }

        if (veEntities.get(firstItem).getTcTrangThaiDon() == xacNhanLenh) {
            return;
        }
        // Excute Huy Ve
        List<TcVeEntity> veEntitiesByCmd = veTcDao.findByTcLenhId(veEntities.get(0).getTcLenhId());
        final String lydoHuy = "Lái Xe Không Xác Nhận Lệnh";
        veEntities.forEach(veEntity -> {
            LOG.info("huy Lenh Ve: " + veEntity.getBvvId());
            veEntity.setLaiXeIdDon(Constant.ZERO);
            veEntity.setTcLenhId(Constant.ZERO);
            veEntity.setLyDoTuChoiDon(lydoHuy);
            veEntity.setThuTuDon(Constant.ZERO);
            veEntity.setTcTrangThaiDon(VeConstants.TC_STATUS_CHUA_DIEU);
            veTcDao.save(veEntity);
            bvvService.updateBvvEntity(veEntity);
        });

        Optional<TcLenhEntity> optional = tcLenhRepository.findById(currentCmd);
        if (optional.isPresent()) {
            TcLenhEntity tcLenhEntity = optional.get();
            final int statusDadieu = 1;
            if (tcLenhEntity.getTrangThai() != null && tcLenhEntity.getTrangThai() == statusDadieu) {// excute Huy Lenh
                tcLenhEntity.setTrangThai(LenhConstants.LENH_STATUS_DA_BI_HUY);
                tcLenhEntity.setLastUpdatedDate(LocalDateTime.now());
                tcLenhRepository.save(tcLenhEntity);
                LOG.debug("Hủy lệnh thành công: {}", tcLenhEntity.getTcLenhId());
            }

        }
    }

    // TODO refactor ket thuc lenh HUB TO HUB
    @Transactional
    @Override
    public void ketthucLenhHub(int lenhId) {
        Optional<TcLenhEntity> lenhOpt = tcLenhRepository.findById(lenhId);
        TcLenhEntity tcLenhEntity = lenhOpt
                .orElseThrow(() -> new ResourceNotfoundException("lenh is not found: " + lenhId));
        List<TcVeEntity> tcVeEntities = tcVeRepository.findByTcLenhId(lenhId);
        tcVeEntities.forEach(e -> {
            if (Objects.nonNull(tcLenhEntity.getDiemgiaokhach())) {
                e.setLastLocation(tcLenhEntity.getDiemgiaokhach());
            } else {
                e.setLastLocation(e.getTcHubDiemDon());
            }
        });
        tcVeRepository.saveAll(tcVeEntities);
    }

    private void saveLaiXeVe(int laixeId, int lenhId, int tcVeId, int statusVe, String lyDoHuy){
        tcTaiXeVeService.saveTaiXeVe(laixeId,lenhId,tcVeId,statusVe,lyDoHuy,laixeId);
    }

    private List<VeTrungChuyenDTO> getListVeTCChoLenhBiHuy(int lenhId){
        List<VeTrungChuyenDTO> lst = new ArrayList<>();
        lst = tcLenhRepositoryCustom.getListKhachChoLenhHuy(lenhId);
        return lst;
    }

    private List<KhachTrungChuyenDTO> layDuLieuRank(List<KhachTrungChuyenDTO> lst,List<String> listPhone){
        if(listPhone.size() <= 0)
            return lst;
        try{
            List<CustomerRankData> customerRankDatas = commonService.layThongTinRankTuHavaz(listPhone);
            lst = lst.stream().map(obj1 -> {
                customerRankDatas.stream().map(obj2 -> {
                    if(obj2.getPhone().equals(obj1.getSdtKhachDi())) {
                        obj1.setRank(obj2.getRank());
                    }
                    return obj1;
                }).collect(Collectors.toList());
                return obj1;
            }).collect(Collectors.toList());
            return lst;
        }catch (Exception ex){
            ex.printStackTrace();
            LOG.warn("Khong lay duoc danh sach rank tu ben san");
            return lst;
        }
    }
}
