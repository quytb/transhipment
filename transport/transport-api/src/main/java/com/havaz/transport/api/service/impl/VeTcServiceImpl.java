package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.body.response.tcve.GetTcVeDonResponse;
import com.havaz.transport.api.common.CacheData;
import com.havaz.transport.api.common.CommonUtils;
import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.model.ConDataDTO;
import com.havaz.transport.api.model.KhachDTO;
import com.havaz.transport.api.model.KhachHavazDTO;
import com.havaz.transport.api.model.TopicDTO;
import com.havaz.transport.api.model.TripDTO;
import com.havaz.transport.api.rabbit.publisher.RabbitMQPublisher;
import com.havaz.transport.api.repository.TcVeRepositoryCustom;
import com.havaz.transport.api.service.VeTcService;
import com.havaz.transport.core.constant.VeConstants;
import com.havaz.transport.core.exception.TransportException;
import com.havaz.transport.core.utils.Strings;
import com.havaz.transport.dao.entity.TcVeEntity;
import com.havaz.transport.dao.repository.TcVeRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class VeTcServiceImpl implements VeTcService {

    private static final Logger log = LoggerFactory.getLogger(VeTcServiceImpl.class);

    @Autowired
    private TcVeRepository tcVeRepository;

    @Autowired
    private TcVeRepositoryCustom tcVeRepositoryCustom;

    @Autowired
    private RabbitMQPublisher rabbitMQPublisher;

    @Override
    public List<TcVeEntity> getVeTcByLenhId(int lenhId) {
        return tcVeRepository.getVeTcByTcLenhId(lenhId);
    }

    @Override
    public TcVeEntity getByBvvId(int bvvId) {
        return tcVeRepository.getByBvvId(bvvId);
    }

    @Override
    public List<TcVeEntity> getByTaiXeIdAndLenhId(int taixeId, int lenhId) {
        return tcVeRepositoryCustom.getByTaiXeIdAndLenhId(taixeId, lenhId);
    }

    @Override
    @Transactional
    public List<TripDTO> getListTripByTripId(int tripId, int type, int caller) {
        List<TripDTO> listTripDTO = new ArrayList<>();
        List<KhachDTO> khachDTOList = new ArrayList<>();
        SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = GregorianCalendar.getInstance();
        Date date;
        try {
            ConDataDTO conDataDTO = CacheData.THONG_TIN_NHA_XE;
            String merchantId = conDataDTO.getCon_code();
            List<Object[]> objs = tcVeRepositoryCustom.getListTripObjectTripId(tripId, type);
            if (objs == null || objs.isEmpty()) {
                return listTripDTO;
            }
            for (Object[] obj : objs) {
                TripDTO trip = new TripDTO();
                if (obj[0] != null && obj[1] != null && obj[2] != null) {
                    List<String> listPhone = Arrays.asList(String.valueOf(obj[0]).split("\\s*,\\s*"));
                    List<String> listThoiGianUpdate = Arrays.asList(String.valueOf(obj[1]).split("\\s*,\\s*"));
                    List<String> listThoiGianDon = Arrays.asList(String.valueOf(obj[2]).split("\\s*,\\s*"));
                    List<String> listThuTuDon = Arrays.asList(String.valueOf(obj[8]).split("\\s*,\\s*"));
                    List<String> listLongitude = Arrays.asList(String.valueOf(obj[9]).split("\\s*,\\s*"));
                    List<String> listLatitude = Arrays.asList(String.valueOf(obj[10]).split("\\s*,\\s*"));
                    List<String> listTrangThaiTc = Arrays.asList(String.valueOf(obj[11]).split("\\s*,\\s*"));
                    List<String> listBvvSource = Arrays.asList(String.valueOf(obj[13]).split("\\s*,\\s*"));
                    List<String> listBvvDistance = Arrays.asList(String.valueOf(obj[14]).split("\\s*,\\s*"));
                    List<String> bvvids = Arrays.asList(String.valueOf(obj[15]).split("\\s*,\\s*"));
                    List<String> hasSentMsg = Arrays.asList(String.valueOf(obj[16]).split("\\s*,\\s*"));
                    // Nếu như người gọi service này là Havaz mà trong list source toàn vé từ ERP thì thôi luôn
                    if (isNotContainVeFromHavaz(caller, listBvvSource)) continue;
                    khachDTOList = new ArrayList<>();
                    for (int i = 0; i < listPhone.size(); i++) {
                        //3: Vé từ Sàn Havaz
                        // Nếu như người gọi service này là Havaz thì không trả về những thằng != 3
                        if (caller == 3 && Integer.parseInt(listBvvSource.get(i)) != 3) {
                            continue;
                        }
                        String newTime = inputFormatter.format(calendar.getTime());
                        if (obj[7] != null && (Integer) obj[7] != 2) {
                            newTime = "";
                        } else {
                            date = inputFormatter.parse(listThoiGianUpdate.get(i));
                            calendar.setTime(date);
                            calendar.add(Calendar.MINUTE, getThoiGianDon(listThoiGianDon, i));
                            newTime = inputFormatter.format(calendar.getTime());
                        }
                        KhachDTO khachDTO = new KhachDTO();
                        KhachHavazDTO khachHavazDTO = new KhachHavazDTO();
                        khachHavazDTO.setMerchantId(merchantId);
                        khachHavazDTO.setTripId(tripId + "");
                        khachHavazDTO.setSdtXeTuyen(obj[4] != null ? String.valueOf(obj[4]) : null);
                        khachHavazDTO.setXeTrungTam(obj[6] != null ? (Integer.parseInt(obj[6].toString()) + "") : (0 + ""));
                        int laiXeIdHavaz = obj[5] != null ? (Integer) obj[5] : 0;
                        khachHavazDTO.setTopic(CommonUtils.generateTopic(merchantId, tripId, laiXeIdHavaz));
                        khachHavazDTO.setSdtKhachHang(listPhone.get(i));
                        khachHavazDTO.setGioDon(newTime);
                        khachDTO.setGioDon(newTime);
                        khachDTO.setSdtKhachHang(listPhone.get(i));
                        if (Strings.DASH.equals(listThoiGianDon.get(i))) {
                            khachDTO.setPickupTime("");
                            khachHavazDTO.setPickupTime("");
                        } else {
                            khachDTO.setPickupTime(listThoiGianDon.get(i));
                            khachHavazDTO.setPickupTime(listThoiGianDon.get(i));
                        }
                        if (Strings.DASH.equals(listThuTuDon.get(i))) {
                            khachDTO.setThuTuDon("");
                            khachHavazDTO.setThuTuDon("");
                        } else {
                            khachDTO.setThuTuDon(listThuTuDon.get(i));
                            khachHavazDTO.setThuTuDon(listThuTuDon.get(i));

                        }
                        if (Strings.DASH.equals(listLongitude.get(i))) {
                            khachDTO.setLongitude("");
                            khachHavazDTO.setLongitude("");
                        } else {
                            khachDTO.setLongitude(listLongitude.get(i));
                            khachHavazDTO.setLongitude(listLongitude.get(i));
                        }
                        if (Strings.DASH.equals(listLatitude.get(i))) {
                            khachDTO.setLatitude("");
                            khachHavazDTO.setLatitude("");
                        } else {
                            khachDTO.setLatitude(listLatitude.get(i));
                            khachHavazDTO.setLatitude(listLatitude.get(i));

                        }
                        if (Strings.DASH.equals(listTrangThaiTc.get(i))) {
                            khachDTO.setTrangThaiTc("");
                            khachHavazDTO.setTrangThaiTc("");
                        } else {
                            khachDTO.setTrangThaiTc(listTrangThaiTc.get(i));
                            khachHavazDTO.setTrangThaiTc(listTrangThaiTc.get(i));

                        }
                        if (Strings.DASH.equals(listBvvSource.get(i))) {
                            khachDTO.setTcvSource("");
                            khachHavazDTO.setTcvSource("");
                        } else {
                            khachDTO.setTcvSource(listBvvSource.get(i));
                            khachHavazDTO.setTcvSource(listBvvSource.get(i));
                        }
                        if (Strings.DASH.equals(hasSentMsg.get(i))) {
                            khachDTO.setHasSentMsg("0");
                            khachHavazDTO.setHasSentMsg("0");
                        } else {
                            khachDTO.setHasSentMsg(hasSentMsg.get(i));
                            khachHavazDTO.setHasSentMsg(hasSentMsg.get(i));
                        }

                        if (Strings.DASH.equals(listBvvDistance.get(i))) {
                            khachDTO.setTcvDistance("");
                            khachHavazDTO.setTcvDistance("");
                        } else {
                            khachDTO.setTcvDistance(listBvvDistance.get(i));
                            khachHavazDTO.setTcvDistance(listBvvDistance.get(i));
                        }

                        List<Integer> bvvId = new ArrayList<>();
                        if (!Strings.DASH.equals(bvvids.get(i))) {
                            String temp = bvvids.get(i);
                            int id = Integer.parseInt(temp);
                            bvvId.add(id);
                        }
                        if (!"".equals(khachDTO.getTcvDistance()) && khachDTO.getTcvDistance() != null && !"1".equals(khachDTO.getHasSentMsg())) {
                            if (getThoiGianDon(listThoiGianDon, i) <= 2) {
                                rabbitMQPublisher.sendMsgTrackingDriver(khachHavazDTO);
                                List<TcVeEntity> veEntities = tcVeRepository.findByBvvIds(bvvId);
                                if (veEntities != null && veEntities.size() > 0) {
                                    TcVeEntity veEntity = veEntities.get(0);
                                    if (type == 1) {
                                        veEntity.setTcIsPicked(1);
                                    } else {
                                        veEntity.setTcIsDropped(1);
                                    }
                                    tcVeRepository.save(veEntity);
                                }
                            }
                        }
                        khachDTOList.add(khachDTO);
                    }
                }
                trip.setDsKhachHang(khachDTOList);
                trip.setBks(obj[3] != null ? String.valueOf(obj[3]) : null);
                trip.setSdt(obj[4] != null ? String.valueOf(obj[4]) : null);
                trip.setXeTrungTam(obj[6] != null ? Integer.parseInt(obj[6].toString()) : 0);
                int laiXeId = obj[5] != null ? (Integer) obj[5] : 0;
                trip.setTopic(CommonUtils.generateTopic(merchantId, tripId, laiXeId));
                trip.setMerchantId(merchantId);
                trip.setTripId(tripId);
                trip.setTen(obj[12] != null ? String.valueOf(obj[12]) : null);
                listTripDTO.add(trip);
            }
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            throw new TransportException(e);
        }
        return listTripDTO;
    }

    @Override
    public List<TcVeEntity> getListVeTcById(List<Integer> listVeId) {
        return tcVeRepositoryCustom.getListVeTcByBvvId(listVeId);
    }

    @Override
    public List<TcVeEntity> getVeByTaiXeIdAndLenhId(int taixeId, int lenhId) {
        return tcVeRepositoryCustom.getVeTcByLenhIdAndTaiXeId(taixeId, lenhId);
    }

    @Override
    public List<TcVeEntity> getVeTcTraByLenhId(int lenhId) {
        return tcVeRepository.getVeTcTraByTcLenhTraId(lenhId);
    }

    @Override
    public List<TopicDTO> getVeByTaiXeId(int taixeId) {
        List<TopicDTO> topicDTOList = new ArrayList<>();
        ConDataDTO conDataDTO = CacheData.THONG_TIN_NHA_XE;
        List<TcVeEntity> tcVeEntities = tcVeRepositoryCustom.getVeTcActiveByLaiXeId(taixeId);
        if (tcVeEntities != null && !tcVeEntities.isEmpty()) {
            for (TcVeEntity tcVeEntity : tcVeEntities) {
                TopicDTO topicDTO = new TopicDTO();
                topicDTO.setComCode(conDataDTO.getCon_code());
                topicDTO.setTaiXeId(taixeId);
                topicDTO.setTripId(tcVeEntity.getDidId());
                if (!isExistTripId(tcVeEntity.getDidId(), topicDTOList)) {
                    topicDTOList.add(topicDTO);
                }
            }
        }
        return topicDTOList;
    }

    private boolean isExistTripId(int tripId, List<TopicDTO> topicDTOS) {
        List<Integer> listTrip = new ArrayList<>();
        for (TopicDTO topicDTO : topicDTOS) {
            listTrip.add(topicDTO.getTripId());
        }
        return listTrip.contains(tripId);
    }

    private int getThoiGianDon(List<String> listThoiGianDon, int index) {
        int thoiGianDon;
        try {
            thoiGianDon = Integer.parseInt(listThoiGianDon.get(index));
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            thoiGianDon = Constant.ZERO;
        }
        return thoiGianDon;
    }

    private boolean isNotContainVeFromHavaz(int caller, List<String> listBvvSource) {
        boolean containVeFromHavaz = false;
        if (caller == 3) {
            for (String s : listBvvSource) {
                if (Integer.parseInt(s) == 3) {
                    containVeFromHavaz = true;
                    break;
                }
            }
            //Không có vé nào từ Havaz
            if (!containVeFromHavaz) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<GetTcVeDonResponse> findAllByCommand(Integer cmdId) {
        List<GetTcVeDonResponse> getTcVeDonResponses = tcVeRepositoryCustom.findAllOfCommand(cmdId);
        getTcVeDonResponses.forEach(getTcVeDonResponse -> {
            getTcVeDonResponse.setBvvIds(CommonUtils.convertStringToArrayInteger(getTcVeDonResponse.getBvvIdsStr()));
            getTcVeDonResponse.setVersions(CommonUtils.convertStringToArrayInteger(getTcVeDonResponse.getVersionsStr()));
        });
        return getTcVeDonResponses;
    }



    @Override
    @Transactional
    public void removeTicketOfCommand(List<Integer> bvvIds) {
        List<TcVeEntity> tcVeEntities = tcVeRepository.findByBvvIds(bvvIds);
        tcVeEntities.forEach(tcVeEntity -> {
            tcVeEntity.setTcTrangThaiDon(VeConstants.TC_STATUS_CHUA_DIEU);
            tcVeEntity.setTcLenhId(Constant.ZERO);
            tcVeRepository.save(tcVeEntity);
        });
    }
}
