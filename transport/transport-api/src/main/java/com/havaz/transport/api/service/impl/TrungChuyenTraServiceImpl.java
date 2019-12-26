package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.common.CacheData;
import com.havaz.transport.api.common.CommonUtils;
import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.common.LenhConstants;
import com.havaz.transport.api.form.CustomerRankData;
import com.havaz.transport.api.model.*;
import com.havaz.transport.api.service.*;
import com.havaz.transport.api.common.ManagerNotificationType;
import com.havaz.transport.api.form.location.Point;
import com.havaz.transport.api.repository.TcLenhRepositoryCustom;
import com.havaz.transport.api.repository.TcVeRepositoryCustom;
import com.havaz.transport.core.constant.VeConstants;
import com.havaz.transport.core.exception.TransportException;
import com.havaz.transport.core.utils.Strings;
import com.havaz.transport.dao.entity.AdminLv2UserEntity;
import com.havaz.transport.dao.entity.BenXeEntity;
import com.havaz.transport.dao.entity.TcLenhEntity;
import com.havaz.transport.dao.entity.TcVeEntity;
import com.havaz.transport.dao.entity.ZzLogBanVeEntity;
import com.havaz.transport.dao.repository.BenXeEntityRepository;
import com.havaz.transport.dao.repository.TcLenhRepository;
import com.havaz.transport.dao.repository.TcVeRepository;
import com.havaz.transport.dao.repository.TcZzLogRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TrungChuyenTraServiceImpl implements TrungChuyenTraService {

    private static final Logger LOG = LoggerFactory.getLogger(TrungChuyenTraServiceImpl.class);

    @Value("${havaz.stomp.topic}")
    private String topic;

    @Autowired
    private TcVeRepository tcVeRepository;

    @Autowired
    private TcVeRepositoryCustom tcVeRepositoryCustom;

    @Autowired
    private TcLenhRepository tcLenhRepository;

    @Autowired
    private TcLenhRepositoryCustom tcLenhRepositoryCustom;

    @Autowired
    private BvvService bvvService;

    @Autowired
    private TcZzLogRepository ZzLogRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private BenXeEntityRepository benXeRepo;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TcTaiXeVeService tcTaiXeVeService;

    @Override
    @Transactional
    public void traKhach(DaTraDTO daTraDTO) {
        List<Integer> listVeId = daTraDTO.getListBvvId();
        tcVeRepositoryCustom.traKhach(listVeId);
        //TODO WRITE HISTORY
        List<ZzLogBanVeEntity> logEntities = new ArrayList<>();
        listVeId.forEach(bvvId -> {
            //save vao bang laixeve
            TcVeEntity tcVeEntity = tcVeRepository.getByBvvId(bvvId);
            if(tcVeEntity != null){
                saveTaiXeVe(daTraDTO.getTaixeId(),tcVeEntity.getTcLenhTraId(),tcVeEntity.getTcVeId(),VeConstants.TC_STATUS_DA_TRA,"");
            }

            //Insert history
            ZzLogBanVeEntity logEntity;
            logEntity = new ZzLogBanVeEntity();
            logEntity.setLogAdminId(daTraDTO.getTaixeId());
            logEntity.setLogRecordId(bvvId);
            //4: đã trả
            logEntity.setLogData("{\"TC Trạng thái trả\":[\"0\",\"1\"]}");
            long epoch = System.currentTimeMillis() / 1000;
            String timeS = String.valueOf(epoch);
            int timeI = Integer.valueOf(timeS);
            logEntity.setLogTime(timeI);
            logEntity.setLogType(2);//1: Insert 2: Update
            logEntity.setLogIp("API-Trả-Khách");
            logEntity.setLogStatus(0);//ERP said it alway is 0
            logEntities.add(logEntity);
        });
        ZzLogRepository.saveAll(logEntities);
    }

    @Override
    public List<KhachTrungChuyenTraDTO> getListKhachTrungChuyenTra(int taiXeId) {
        List<KhachTrungChuyenTraDTO> listKhachHangTC = new ArrayList<>();
        List<String> listPhone = new ArrayList<>();
        List<TcLenhEntity> listLenh = tcLenhRepositoryCustom.getLenhByTaiXeId(taiXeId, LenhConstants.LENH_TRA);
        if (listLenh != null && !listLenh.isEmpty()) {
            TcLenhEntity tcLenhEntity = listLenh.get(Constant.FIRST_ITEM);
            int lenhId = tcLenhEntity.getTcLenhId();
            int lenhStatus = tcLenhEntity.getTrangThai();

            try {
                ConDataDTO conDataDTO = CacheData.THONG_TIN_NHA_XE;
                List<Object[]> Objs = tcLenhRepositoryCustom.getListKhachTrungChuyenTra(lenhId);
                for (Object[] arr : Objs) {
                    KhachTrungChuyenTraDTO tc = new KhachTrungChuyenTraDTO();
                    tc.setTenKhachTra(arr[0] != null ? String.valueOf(arr[0]) : null);
                    tc.setSdtKhachTra(arr[1] != null ? String.valueOf(arr[1]) : null);
                    listPhone.add(arr[1] != null ? String.valueOf(arr[1]) : null);
                    tc.setDiaChiTra(arr[2] != null ? String.valueOf(arr[2]) : null);
                    tc.setThuTuTra(arr[3] != null ? Integer.parseInt(arr[3].toString()) : 0);
                    tc.setThoiGianTra(arr[4] != null ? Integer.parseInt(arr[4].toString()) : 0);
                    tc.setTrangThaiTrungChuyenTra(arr[5] != null ? Integer.parseInt(arr[5].toString()) : 0);
                    tc.setLenhIdTra(arr[6] != null ? Integer.parseInt(arr[6].toString()) : 0);
                    tc.setTaiXeTraId(arr[7] != null ? Integer.parseInt(arr[7].toString()) : 0);
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
                    tc.setGhiChu(arr[17] != null ? String.valueOf(arr[17]) : null);
                    tc.setLatitude(arr[19] != null ? Double.parseDouble(String.valueOf(arr[19])) : null);
                    tc.setLongitude(arr[20] != null ? Double.parseDouble(String.valueOf(arr[20])) : null);
                    tc.setDistanceTra(arr[21] != null ? Integer.parseInt(String.valueOf(arr[21])) : null);
                    tc.setDistanceToHubTra(arr[22] != null ? Double.parseDouble(String.valueOf(arr[22])) : null);
                    tc.setThoiGianToHubTra(arr[23] != null ? Integer.parseInt(String.valueOf(arr[23])) : 0);
                    tc.setCreateTime(arr[24] != null ? String.valueOf(arr[24]) : "");
                    tc.setGioVeBen(StringUtils.EMPTY);
                    int thoiGianChay;
                    thoiGianChay = arr[25] != null ? Integer.parseInt(String.valueOf(arr[25])) : 0;
                    if (thoiGianChay > 0) {
                        SimpleDateFormat inputFormatter = new SimpleDateFormat("HH:mm");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(inputFormatter.parse(tc.getGioXuatBen()));
                        calendar.add(Calendar.MINUTE, thoiGianChay);
                        tc.setGioVeBen(inputFormatter.format(calendar.getTime()));
                    }

                    int hubTra;
                    hubTra = arr[26] != null ? Integer.parseInt(String.valueOf(arr[26])) : 0;
                    BenXeEntity benXe = benXeRepo.findById(hubTra).orElse(null);
                    if (benXe != null) {
                        tc.setHubDonKhach(benXe.getTen());
                    }

                    tc.setTenNhaXe(conDataDTO.getCon_company_name());
                    tc.setLenhTraStatus(lenhStatus);
                    listKhachHangTC.add(tc);
                }
            } catch (ParseException e) {
                LOG.warn("Lỗi lấy thông tin trung chuyển : " + e.getMessage(), e);
            }
        }
        listKhachHangTC = layDuLieuRank(listKhachHangTC,listPhone);
        return listKhachHangTC;

    }

    @Override
    @Transactional
    public List<KhachTrungChuyenTraDTO> getListKhachTrungChuyenTraUpdate(double latitude, double longitude, int inputLenhId) {
        List<KhachTrungChuyenTraDTO> listKhachHangTCUpdate = new ArrayList<>();
        List<String> listPhone = new ArrayList<>();
//        List<TcLenhEntity> listLenh = tcLenhRepository.getLenhByTaiXeId(taiXeId, LenhConstants.LENH_TRA);
        Optional<TcLenhEntity> tcLenhEntity = tcLenhRepository.findById(inputLenhId);
        int lenhId = 0;
        int lenhStatus = 0;
        boolean isHavazNow = false;
        if (tcLenhEntity.isPresent()) {
            lenhId = tcLenhEntity.get().getTcLenhId();
            lenhStatus = tcLenhEntity.get().getTrangThai();
            isHavazNow = tcLenhEntity.get().getIsHavazNow();
        }
//        TcLenhEntity tcLenhEntity = listLenh.get(Constant.FIRST_ITEM);
//        int lenhId = tcLenhEntity.getTcLenhId();
//        int lenhStatus = tcLenhEntity.getTrangThai();
        Point toaDoLaiXe = new Point(latitude, longitude);

        try {
            ConDataDTO conDataDTO = CacheData.THONG_TIN_NHA_XE;
            List<Object[]> Objs = tcLenhRepositoryCustom.getListKhachTrungChuyenTra(lenhId);
            for (Object[] arr : Objs) {
                KhachTrungChuyenTraDTO tc = new KhachTrungChuyenTraDTO();
                tc.setTenKhachTra(arr[0] != null ? String.valueOf(arr[0]) : null);
                tc.setSdtKhachTra(arr[1] != null ? String.valueOf(arr[1]) : null);
                listPhone.add(arr[1] != null ? String.valueOf(arr[1]) : null);
                tc.setDiaChiTra(arr[2] != null ? String.valueOf(arr[2]) : null);
                tc.setThuTuTra(arr[3] != null ? Integer.parseInt(arr[3].toString()) : 0);
                tc.setThoiGianTra(arr[4] != null ? Integer.parseInt(arr[4].toString()) : 0);

                //Tính lại thời gian trả real time
                int time = 0;
                int distance = 0;
                if (arr[19] != null && arr[20] != null) {
                    double latitudeKhachHang = Double.valueOf(String.valueOf(arr[19]));
                    double longitudeKhachHang = Double.valueOf(String.valueOf(arr[20]));
                    Point toaDoKhachHang = new Point(latitudeKhachHang, longitudeKhachHang);
                    time = getThoiGianDonFromAPI(toaDoLaiXe, toaDoKhachHang);
                    distance = getDistanceFromAPI(toaDoKhachHang, toaDoLaiXe);
                    if (distance > 0) {
                        tc.setDistanceTra(distance);
                    }
                    if (time > 0)
                        tc.setThoiGianTra(time);
                }

                tc.setTrangThaiTrungChuyenTra(arr[5] != null ? Integer.parseInt(arr[5].toString()) : 0);
                tc.setLenhIdTra(arr[6] != null ? Integer.parseInt(arr[6].toString()) : 0);
                tc.setTaiXeTraId(arr[7] != null ? Integer.parseInt(arr[7].toString()) : 0);
                tc.setTuyenDi(arr[8] != null ? String.valueOf(arr[8]) : null);
                tc.setGioXuatBen(arr[9] != null ? String.valueOf(arr[9]) : null);
                tc.setBienSoXe(arr[10] != null ? String.valueOf(arr[10]) : null);
                tc.setKhachHangMoi(arr[11] != null ? Integer.parseInt(arr[11].toString()) : 0);
                tc.setSoGhe(arr[12] != null ? String.valueOf(arr[12]) : null);
                tc.setSoKhach(arr[13] != null ? String.valueOf(arr[13]) : null);
                if (arr[14] != null) {
                    List<Integer> listBvvId = CommonUtils.convertListStringToInt(Arrays.asList(String.valueOf(arr[14]).split("\\s*,\\s*")));
                    tc.setVeId(listBvvId);
                    if (time > 0) {
                        tcVeRepository.updateThoiGianTra(listBvvId, time, distance);
                    }
                }
                tc.setSdtXe(arr[15] != null ? String.valueOf(arr[15]) : null);
                tc.setGioDieuHanh(arr[16] != null ? String.valueOf(arr[16]) : null);
                tc.setGhiChu(arr[18] != null ? String.valueOf(arr[18]) : null);
                tc.setLatitude(arr[19] != null ? Double.parseDouble(String.valueOf(arr[19])) : null);
                tc.setLongitude(arr[20] != null ? Double.parseDouble(String.valueOf(arr[20])) : null);
                tc.setDistanceTra(arr[21] != null ? Integer.parseInt(String.valueOf(arr[21])) : null);
                tc.setDistanceToHubTra(arr[22] != null ? Double.parseDouble(String.valueOf(arr[22])) : null);
                tc.setThoiGianToHubTra(arr[23] != null ? Integer.parseInt(String.valueOf(arr[23])) : 0);
                tc.setCreateTime(arr[24] != null ? String.valueOf(arr[24]) : "");
                tc.setGioVeBen("");
                int thoiGianChay;
                thoiGianChay = arr[25] != null ? Integer.parseInt(String.valueOf(arr[25])) : 0;
                if (thoiGianChay > 0) {
                    SimpleDateFormat inputFormatter = new SimpleDateFormat("HH:mm");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(inputFormatter.parse(tc.getGioXuatBen()));
                    calendar.add(Calendar.MINUTE, thoiGianChay);
                    tc.setGioVeBen(inputFormatter.format(calendar.getTime()));
                }

                int hubTra;
                hubTra = arr[26] != null ? Integer.parseInt(String.valueOf(arr[26])) : 0;
                BenXeEntity benXe = benXeRepo.findById(hubTra).orElse(null);
                if (benXe != null) {
                    tc.setHubDonKhach(benXe.getTen());
                }

                tc.setTenNhaXe(conDataDTO.getCon_company_name());
                tc.setLenhTraStatus(lenhStatus);
                tc.setIsHavazNow(isHavazNow + StringUtils.EMPTY);
                listKhachHangTCUpdate.add(tc);
            }

        } catch (ParseException e) {
            LOG.warn("Lỗi lấy thông tin trung chuyển : " + e.getMessage(), e);
        }
        listKhachHangTCUpdate = layDuLieuRank(listKhachHangTCUpdate,listPhone);
        return listKhachHangTCUpdate;
    }

    @Override
    @Transactional
    public boolean xacNhanLenhTra(XacNhanLenhTraDTO requestXacNhanLenhTra) {
        LOG.info("Start xác nhận lệnh trả");
        //Update Ve_TC status
        List<VeTcTraDTO> listVeTraDTO = requestXacNhanLenhTra.getListVeTcUpdate();
        if (listVeTraDTO != null && !listVeTraDTO.isEmpty()) {
            for (VeTcTraDTO veTcTraDTO : listVeTraDTO) {
                if (veTcTraDTO.getTrangThaiTrungChuyenTra() == VeConstants.TC_STATUS_DA_DIEU) {
                    List<Integer> listVeTcId = veTcTraDTO.getVeId();
                    for (int bvvId : listVeTcId) {
                        TcVeEntity tcVeEntity = tcVeRepository.getByBvvId(bvvId);
                        tcVeEntity.setTcTrangThaiTra(VeConstants.TC_STATUS_DANG_DI_TRA);
                        tcVeEntity.setThoiGianTra(0);
                        //tính tọa độ của khách hàng và tài xế
                        if(tcVeEntity.getBvvLatEnd() != null && tcVeEntity.getBvvLongEnd() != null && requestXacNhanLenhTra.getToaDoTaiXe() != null) {
                            if(tcVeEntity.getBvvLatEnd() > 0 && tcVeEntity.getBvvLongEnd() > 0) {
                                Point toaDoKhachHang = new Point(tcVeEntity.getBvvLatEnd(), tcVeEntity.getBvvLongEnd());
                                Point toaDoTaiXe = requestXacNhanLenhTra.getToaDoTaiXe();
                                //tính lại thoi gian tra gọi API
                                int time = getThoiGianDonFromAPI(toaDoKhachHang, toaDoTaiXe);
                                int distance = getDistanceFromAPI(toaDoKhachHang, toaDoTaiXe);
                                tcVeEntity.setTcDistanceTra(distance);
                                LOG.info("Thoi gian don cua khach hang "+tcVeEntity.getBvvPhoneDi()+" la "+time);
                                tcVeEntity.setThoiGianTra(time);
                            }
                        }
                        tcVeEntity.setThuTuTra(veTcTraDTO.getThuTuTra());
                        tcVeEntity.setLastUpdatedDate(LocalDateTime.now());
                        tcVeRepository.save(tcVeEntity);

                        //Update lai table ban_ve_ve field thuTuDon
                        bvvService.updateBvvEntity(tcVeEntity);
                    }
                    try {
                        notificationService.saveAndSendNotificationToManager(topic,
                                ManagerNotificationType.DRIVER_CONFIRMED_PICKUP.getType(), false, listVeTcId);
                    } catch (Exception e) {
                        LOG.warn("Error For Send Notify To Manager ", e);
                    }
                }
            }
            //Update table Lenh
            TcLenhEntity lenhTrungChuyenEntity = tcLenhRepository.findById(requestXacNhanLenhTra.getLenhId()).orElse(null);
            if (lenhTrungChuyenEntity != null) {
                lenhTrungChuyenEntity.setTrangThai(LenhConstants.LENH_STATUS_DANG_CHAY);
                lenhTrungChuyenEntity.setLastUpdatedDate(LocalDateTime.now());
                tcLenhRepository.save(lenhTrungChuyenEntity);
                LOG.info("Xác nhận lệnh trả thành công");
                //TODO Write log
                for (VeTcTraDTO veDto : requestXacNhanLenhTra.getListVeTcUpdate()) {
                    veDto.getVeId().forEach(bvvId -> {
                        String data = "{" + "\"Xác nhận lệnh trả\":[\"0\",\"1\"]}";
                        commonService.wrieZzLog(String.valueOf(lenhTrungChuyenEntity.getLaiXeId()), bvvId, data, "XÁC NHẬN LỆNH TRẢ");
                    });
                }
                return true;
            } else {
                LOG.error("Lỗi xác nhận lệnh trả");
                return false;
            }
        } else {
            throw new TransportException("Dữ liệu không hợp lệ.");
        }
    }

    @Transactional
    @Override
    public boolean huyLenhTra(HuyLenhTcDTO requestHuyLenhTra) {
        LOG.info("Start Hủy Lệnh Trả:: " + requestHuyLenhTra.getLenhId());
        //Update Ve_TC status
        List<TcVeEntity> listVeTcDto = tcVeRepository.getVeTcTraByTcLenhTraId(requestHuyLenhTra.getLenhId());
        if (listVeTcDto == null || listVeTcDto.isEmpty()) {
            LOG.info("listVeTcDto is null or empty");
            throw new TransportException("listVeTcDto is null or empty");
        }
        List<ZzLogBanVeEntity> logEntities = new ArrayList<>();
        for (TcVeEntity tcVeEntity : listVeTcDto) {

            //insert to table laixe-ve before reset ve
            saveTaiXeVe(tcVeEntity.getLaiXeIdTra(),requestHuyLenhTra.getLenhId(),tcVeEntity.getTcVeId(),VeConstants.TC_STATUS_DA_HUY,requestHuyLenhTra.getLyDoHuy());

            int laiXeId = tcVeEntity.getLaiXeIdTra();
            int oldTTT = tcVeEntity.getTcTrangThaiTra();
            tcVeEntity.setTcTrangThaiTra(VeConstants.TC_STATUS_DA_HUY_TRA);
            tcVeEntity.setLaiXeIdTra(Constant.ZERO);
            tcVeEntity.setTcLenhTraId(Constant.ZERO);
            if (tcVeEntity.getAdminLv2UserTra() != null) {
                tcVeEntity.setLyDoTuChoiTra(tcVeEntity.getAdminLv2UserTra().getAdmName() + Strings.COLON_DOT + requestHuyLenhTra.getLyDoHuy());
            } else {
                tcVeEntity.setLyDoTuChoiTra(requestHuyLenhTra.getLyDoHuy());
            }
            tcVeEntity.setLastUpdatedDate(LocalDateTime.now());
            tcVeRepository.save(tcVeEntity);

            //Update lai table ban_ve_ve
            bvvService.updateBvvEntity(tcVeEntity);
            //Insert history
            ZzLogBanVeEntity logEntity;
            logEntity = new ZzLogBanVeEntity();
            logEntity.setLogAdminId(laiXeId);
            logEntity.setLogRecordId(tcVeEntity.getBvvId());
            //4: đã hủy
            logEntity.setLogData("{\"TC: Trạng thái trả\":[\"" + VeConstants.GET_MSG_STATUS_TRA.get(oldTTT) + "\",\"" + VeConstants.GET_MSG_STATUS_TRA.get(4)
                    + "\"],\"Lý do từ chối trả\":[\"\"," + "\"" + requestHuyLenhTra.getLyDoHuy() + "\"]}");
            long epoch = System.currentTimeMillis() / 1000;
            String timeS = String.valueOf(epoch);
            int timeI = Integer.valueOf(timeS);
            logEntity.setLogTime(timeI);
            logEntity.setLogType(2);//1: Insert 2: Update
            logEntity.setLogIp("API-Hủy-Lệnh-Trả");
            logEntity.setLogStatus(0);//ERP said it alway is 0
            logEntities.add(logEntity);
        }
        List<Integer> bvvIds = listVeTcDto.stream().map(TcVeEntity::getBvvId).collect(Collectors.toList());
        try {
            notificationService.saveAndSendNotificationToManager(topic,
                    ManagerNotificationType.DRIVER_CANCEL_CMD.getType(), false, bvvIds);
        } catch (Exception e) {
            LOG.warn("Error For Send Notify To Manager ", e);
        }
        ZzLogRepository.saveAll(logEntities);
        //Update table Lenh
        TcLenhEntity lenhTrungChuyenEntity = tcLenhRepository.findById(requestHuyLenhTra.getLenhId()).orElse(null);
        if (lenhTrungChuyenEntity != null) {
            lenhTrungChuyenEntity.setTrangThai(LenhConstants.LENH_STATUS_DA_BI_HUY);
            lenhTrungChuyenEntity.setLastUpdatedDate(LocalDateTime.now());
            lenhTrungChuyenEntity.setLyDoHuy(requestHuyLenhTra.getLyDoHuy());
            lenhTrungChuyenEntity.setCanceledBy(lenhTrungChuyenEntity.getLaiXeId());
            tcLenhRepository.save(lenhTrungChuyenEntity);
            LOG.info("Hủy lệnh trả thành công");
            return true;
        } else {
            LOG.error("Lỗi hủy lệnh trả");
            return false;
        }
    }

    @Override
    @Transactional
    public boolean huyTraKhach(HuyDonDTO huyDonDTO) {
        boolean result = false;
        List<TcVeEntity> listVeTcEntity = tcVeRepositoryCustom.getListVeTcByBvvId(huyDonDTO.getListBvvId());
        if (listVeTcEntity != null && !listVeTcEntity.isEmpty()) {
            List<ZzLogBanVeEntity> logEntities = new ArrayList<>();
            for (TcVeEntity tcVeEnt : listVeTcEntity) {
                int oldTTT = tcVeEnt.getTcTrangThaiTra();
                AdminLv2UserEntity adminLv2UserTra = tcVeEnt.getAdminLv2UserTra();
                if (adminLv2UserTra != null) {
                    tcVeEnt.setLyDoTuChoiTra(
                            adminLv2UserTra.getAdmName() + Strings.COLON_DOT + huyDonDTO.getLyDoHuy());
                } else {
                    tcVeEnt.setLyDoTuChoiTra(huyDonDTO.getLyDoHuy());
                }
                tcVeEnt.setTcTrangThaiTra(VeConstants.TC_STATUS_DA_HUY_TRA);
                tcVeEnt.setLaiXeIdTra(Constant.ZERO);
                tcVeEnt.setTcLenhTraId(Constant.ZERO);
                tcVeEnt.setKhachHangMoi(Constant.ZERO);
                tcVeEnt.setLastUpdatedDate(LocalDateTime.now());
                tcVeRepository.save(tcVeEnt);
                //Update table ban_ve_ve
                bvvService.updateBvvEntity(tcVeEnt);

                //insert to table laixe-ve
                saveTaiXeVe(huyDonDTO.getTaiXeId(),huyDonDTO.getLenhId(),tcVeEnt.getTcVeId(),VeConstants.TC_STATUS_DA_HUY,huyDonDTO.getLyDoHuy());

                //Insert history
                ZzLogBanVeEntity logEntity;
                logEntity = new ZzLogBanVeEntity();
                logEntity.setLogAdminId(huyDonDTO.getTaiXeId());
                logEntity.setLogRecordId(tcVeEnt.getBvvId());
                //4: đã hủy
                logEntity.setLogData("{\"TC: Trạng thái trả\":[\"" + VeConstants.GET_MSG_STATUS_TRA
                        .get(oldTTT) + "\",\"" + VeConstants.GET_MSG_STATUS_TRA.get(4)
                                             + "\"],\"Lý do từ chối trả\":[\"\"," + "\""
                                             + huyDonDTO.getLyDoHuy() + "\"]}");
                long epoch = System.currentTimeMillis() / 1000;
                String timeS = String.valueOf(epoch);
                int timeI = Integer.valueOf(timeS);
                logEntity.setLogTime(timeI);
                logEntity.setLogType(2);//1: Insert 2: Update
                logEntity.setLogIp("API-Hủy-Trả-Khách");
                logEntity.setLogStatus(0);//ERP said it alway is 0
                logEntities.add(logEntity);
            }
            ZzLogRepository.saveAll(logEntities);
            result = true;
            try {
                notificationService.saveAndSendNotificationToManager(topic,
                        ManagerNotificationType.DRIVER_REJECT_PICKUP.getType(), false, huyDonDTO.getListBvvId());
            } catch (Exception e) {
                LOG.warn("Error For Send Notify To Manager ", e);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public void thoiGianTra(ThoiGianTraDTO thoiGianTraDTO) {
        for (Integer bvvId : thoiGianTraDTO.getListBvvId()) {
            TcVeEntity tcVeEntity = tcVeRepository.getByBvvId(bvvId);
            tcVeEntity.setThoiGianTra(thoiGianTraDTO.getThoiGianTra());
            tcVeEntity.setLastUpdatedDate(LocalDateTime.now());
            tcVeRepository.save(tcVeEntity);
        }
    }

    private int getThoiGianDonFromAPI(Point pointStart, Point pointEnd) {
        if (pointStart.getLat() == 0 || pointStart.getLng() == 0 ||
                pointEnd.getLat() == 0 || pointEnd.getLng() == 0) {
            return 0;
        }
        double thoiGian = locationService.getDurationFromAPI(pointStart, pointEnd);
        LOG.info("Thoi gian giua 2 diem: {} va {} la: {}", pointStart, pointEnd, thoiGian);
        return (int) thoiGian;
    }

    private int getDistanceFromAPI(Point pointStart, Point pointEnd) {
        if (pointStart.getLat() == 0 || pointStart.getLng() == 0 || pointEnd.getLat() == 0 || pointEnd.getLng() == 0) {
            return 0;
        }
        double distance = locationService.getDistanFromAPI(pointStart, pointEnd) * 1000;
        return (int) distance;
    }

    private void saveTaiXeVe(int laixeId, int lenhId, int tcVeId, int statusVe, String lyDoHuy){
        tcTaiXeVeService.saveTaiXeVe(laixeId,lenhId,tcVeId,statusVe,lyDoHuy,laixeId);
    }

    private List<KhachTrungChuyenTraDTO> layDuLieuRank(List<KhachTrungChuyenTraDTO> lst, List<String> listPhone){

        if(listPhone.size() <= 0)
            return lst;
        try{
            List<CustomerRankData> customerRankDatas = commonService.layThongTinRankTuHavaz(listPhone);
            lst = lst.stream().map(obj1 -> {
                customerRankDatas.stream().map(obj2 -> {
                    if(obj2.getPhone().equals(obj1.getSdtKhachTra())) {
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
