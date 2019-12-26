package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.body.response.tcve.GetTcVeDonResponse;
import com.havaz.transport.api.common.CalculateHubCommon;
import com.havaz.transport.api.common.TrangThaiTc;
import com.havaz.transport.api.exception.ResourceNotfoundException;
import com.havaz.transport.api.form.location.Point;
import com.havaz.transport.api.model.CanhBaoDTO;
import com.havaz.transport.api.model.KhachNowDTO;
import com.havaz.transport.api.model.NowTripDTO;
import com.havaz.transport.api.model.NowTripDetailDTO;
import com.havaz.transport.api.model.TxCtvDTO;
import com.havaz.transport.api.model.XeTcNowDTO;
import com.havaz.transport.api.repository.HavazNowRepositoryCustom;
import com.havaz.transport.api.repository.VungTrungChuyenRepositoryCustom;
import com.havaz.transport.api.service.HavazNowService;
import com.havaz.transport.api.service.LocationService;
import com.havaz.transport.core.constant.TrangThaiVe;
import com.havaz.transport.core.constant.VeConstants;
import com.havaz.transport.core.exception.TransportException;
import com.havaz.transport.core.utils.Strings;
import com.havaz.transport.dao.entity.BenXeEntity;
import com.havaz.transport.dao.entity.TcVeEntity;
import com.havaz.transport.dao.entity.TcVungTrungChuyenEntity;
import com.havaz.transport.dao.repository.BenXeEntityRepository;
import com.havaz.transport.dao.repository.TcVeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional(readOnly = true)
public class HavazNowServiceImpl implements HavazNowService {

    @Value("${havaz.time-delay}")
    private int TIME_DELAY;

    @Autowired
    private HavazNowRepositoryCustom havazNowRepositoryCustom;

    @Autowired
    private TcVeRepository veRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private VungTrungChuyenRepositoryCustom vungTrungChuyenRepositoryCustom;

    @Autowired
    private CalculateHubCommon calculateHubCommon;

    @Autowired
    private BenXeEntityRepository benXeEntityRepository;

    @Override
    public List<NowTripDTO> getNowTripDTOList(int beforeNow, int afterNow) {
        List<NowTripDTO> nowTripDTOList = new ArrayList<>();
        try {
            SimpleDateFormat inputFormatter = new SimpleDateFormat("HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, -beforeNow);
            String beNow = inputFormatter.format(calendar.getTime());
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, afterNow);
            String afNow = inputFormatter.format(calendar.getTime());
            List<Object[]> objs = havazNowRepositoryCustom.getListTripForHavazNow(beNow, afNow);
            for (Object[] obj : objs) {
                NowTripDTO nowTripDTO = new NowTripDTO();
                nowTripDTO.setTripId(obj[0] != null ? String.valueOf(obj[0]) : null);
                nowTripDTO.setTuyenId(obj[1] != null ? String.valueOf(obj[1]) : null);
                nowTripDTO.setTuyenTen(convertTenTuyen(String.valueOf(obj[2]), Integer.valueOf(obj[8]+"")));
                nowTripDTO.setXeTuyenBKS(obj[3] != null ? String.valueOf(obj[3]) : null);
                nowTripDTO.setSdt(obj[4] != null ? String.valueOf(obj[4]) : null);
                nowTripDTO.setGioXuatBen(obj[5] != null ? String.valueOf(obj[5]) : null);
                nowTripDTO.setTaiXeId(obj[6] != null ? String.valueOf(obj[6]) : null);
                nowTripDTO.setTaiXeTen(obj[7] != null ? String.valueOf(obj[7]) : null);
                nowTripDTO.setNotChieuDi(obj[8] != null ? Integer.valueOf(obj[8]+"") : 0);
                nowTripDTOList.add(nowTripDTO);

            }
            return nowTripDTOList;
        } catch (Exception e) {
            throw new TransportException(e.getMessage(), e);
        }
    }

    @Override
    public NowTripDetailDTO getTripDetails(Integer tripId, Integer type) {
        NowTripDetailDTO tripDetailDTO = havazNowRepositoryCustom.getTripByTripIdAndTrangThai(tripId, type);
        if (tripDetailDTO == null) {
            throw new ResourceNotfoundException("Không Tìm Thấy Trip");
        }
        Point currentXtPoint = redisService.getLocationDriver(tripDetailDTO.getLxTuyenId() + StringUtils.EMPTY);
        if (currentXtPoint != null) {
            tripDetailDTO.setXtLatitude(currentXtPoint.getLat() + StringUtils.EMPTY);
            tripDetailDTO.setXtLongitude(currentXtPoint.getLng() + StringUtils.EMPTY);
        }
        // type = 1 => ve don type =2 => ve tra
        List<XeTcNowDTO> txTcHavazDTOS = havazNowRepositoryCustom.getTaiXeTcByDidIdAndTrangthai(tripId, type);
        txTcHavazDTOS.forEach(txTcHavazDTO -> {
            //todo check don
            Point currentTcPoint = redisService.getLocationDriver(txTcHavazDTO.getTxId() + StringUtils.EMPTY);
            if (currentTcPoint != null) {
                txTcHavazDTO.setXtcLatitude(currentTcPoint.getLat() + StringUtils.EMPTY);
                txTcHavazDTO.setXtcLongitude(currentTcPoint.getLng() + StringUtils.EMPTY);
            }
            List<TcVeEntity> veEntities = new ArrayList<>();
            List<KhachNowDTO> khachNowDTOS = new ArrayList<>();
            List<Integer> status = Arrays.asList(1, 2, 3);
            if (type == TrangThaiVe.DON.getTrangThai()) {
                veEntities = veRepository.findByDidIdAndLaiXeIdDon(tripId, status, txTcHavazDTO.getTxId(), type);
                veEntities.forEach(veEntity -> {
                    KhachNowDTO khachNowDTO = new KhachNowDTO();
                    khachNowDTO.setKhSdt(veEntity.getBvvPhoneDi());
                    khachNowDTO.setKhLatitude(veEntity.getBvvLatStart() + "");
                    khachNowDTO.setKhLongitude(veEntity.getBvvLongStart() + "");
                    khachNowDTO.setKhTrangThaiTc(veEntity.getTcTrangThaiDon() + "");
                    khachNowDTO.setHubTuyen(veEntity.getTcHubDiemDon());
                    khachNowDTOS.add(khachNowDTO);
                });
            }
            if (type == TrangThaiVe.TRA.getTrangThai()) {
                veEntities = veRepository.findByDidIdAndLaiXeIdTra(tripId, status, txTcHavazDTO.getTxId(), type);
                veEntities.forEach(veEntity -> {
                    KhachNowDTO khachNowDTO = new KhachNowDTO();
                    khachNowDTO.setKhSdt(veEntity.getBvvPhoneDi());
                    khachNowDTO.setKhLatitude(veEntity.getBvvLatEnd() + "");
                    khachNowDTO.setKhLongitude(veEntity.getBvvLongEnd() + "");
                    khachNowDTO.setKhTrangThaiTc(veEntity.getTcTrangThaiTra() + "");
                    khachNowDTO.setHubTuyen(veEntity.getTcHubDiemTra());
                    khachNowDTOS.add(khachNowDTO);
                });
            }
            txTcHavazDTO.setDsKhachHang(khachNowDTOS);

        });
        tripDetailDTO.setDsXeTc(txTcHavazDTOS);
        calculateHubCommon.getHubCommon(tripDetailDTO);
        return tripDetailDTO;
    }

    @Override
    public List<CanhBaoDTO> getAllCanhBaoForListTrip(List<Integer> listTripId) {
        List<CanhBaoDTO> objs = havazNowRepositoryCustom.layDanhSachDeCanhBao(listTripId);
        Point toaDoKhach = new Point();
        Point toaDoHub = new Point();
        Point toaDoTaiXeTC = new Point();
        Point toaDoTaiXeTuyen = new Point();

        Iterator listIterator = objs.iterator();

        while (listIterator.hasNext()) {
            CanhBaoDTO canhBaoDTO = (CanhBaoDTO) listIterator.next();
            if (canhBaoDTO.getLat() != null && canhBaoDTO.getLng() != null && canhBaoDTO.getHubLat() != null && canhBaoDTO.getHubLng() != null) {
                if (canhBaoDTO.getLat() > 0 && canhBaoDTO.getLng() > 0 && canhBaoDTO.getHubLat() > 0 && canhBaoDTO.getHubLng() > 0) {
                    toaDoKhach.setLat(canhBaoDTO.getLat());
                    toaDoKhach.setLng(canhBaoDTO.getLng());
                    toaDoHub.setLat(canhBaoDTO.getHubLat());
                    toaDoHub.setLng(canhBaoDTO.getHubLng());
                    toaDoTaiXeTC = redisService.getLocationDriver(canhBaoDTO.getTaiXeId() + "");
                    toaDoTaiXeTuyen = redisService.getLocationDriver(canhBaoDTO.getTaiXeXeTCId() + "");
                    int timeTraKhachRaHub = (int) locationService.getDurationFromAPI(toaDoKhach, toaDoHub);
                    int timeDonKhach = toaDoTaiXeTC == null ? 0 :  (int) locationService.getDurationFromAPI(toaDoKhach, toaDoTaiXeTC);
                    int timeXeTuyen = toaDoTaiXeTuyen == null ? 0 : (int) locationService.getDurationFromAPI(toaDoTaiXeTuyen, toaDoHub);

                    if (canhBaoDTO.getTrangThaiDon() == VeConstants.TC_STATUS_DA_HUY) {
                        canhBaoDTO.setCung("Đón Khách");
                        canhBaoDTO.setKieuCanhBao(1);
                        canhBaoDTO.setKieuCanhBaoText("CTV hủy");
                    } else if (timeDonKhach + timeTraKhachRaHub + TIME_DELAY > timeXeTuyen) {
                        canhBaoDTO.setCung("Đón Khách");
                        canhBaoDTO.setKieuCanhBao(1);
                        canhBaoDTO.setKieuCanhBaoText("Đã trễ");
                    } else if (timeDonKhach + timeTraKhachRaHub + TIME_DELAY + 5 > timeXeTuyen) {
                        canhBaoDTO.setCung("Đón Khách");
                        canhBaoDTO.setKieuCanhBao(2);
                        canhBaoDTO.setKieuCanhBaoText("Nguy cơ trễ");
                    } else {
                        listIterator.remove();
                    }

                }
            }
        }
        return objs;
    }

    @Override
    public NowTripDetailDTO getKhByTrip(Integer tripId, Integer type) {
        NowTripDetailDTO tripDetailDTO = havazNowRepositoryCustom.getTripByTripIdAndTrangThai(tripId, type);
        if (tripDetailDTO == null) {
            throw new ResourceNotfoundException("Không Tìm Thấy Trip");
        }
        List<XeTcNowDTO> txTcHavazDTOS = havazNowRepositoryCustom.getTaiXeTcByDidIdAndTrangthai(tripId, type);
        txTcHavazDTOS.forEach(txTcHavazDTO -> {
            //todo check don
            Point currentTcPoint = redisService.getLocationDriver(txTcHavazDTO.getTxId() + StringUtils.EMPTY);
            if (currentTcPoint != null) {
                txTcHavazDTO.setXtcLatitude(currentTcPoint.getLat() + StringUtils.EMPTY);
                txTcHavazDTO.setXtcLongitude(currentTcPoint.getLng() + StringUtils.EMPTY);
            }
            List<TcVeEntity> veEntities = new ArrayList<>();
            List<KhachNowDTO> khachNowDTOS = new ArrayList<>();
            List<Integer> status = Arrays.asList(1, 2, 3);
            if (type == TrangThaiVe.DON.getTrangThai()) {
                veEntities = veRepository.findByDidIdAndLaiXeIdDon(tripId, status, txTcHavazDTO.getTxId(), type);
                veEntities.forEach(veEntity -> {
                    KhachNowDTO khachNowDTO = new KhachNowDTO();
                    updateInfoClientDon(veEntity, khachNowDTO);
                    updateTrangThaiDon(veEntity, khachNowDTO);
                    khachNowDTO.setKhSdt(veEntity.getBvvPhoneDi());
                    khachNowDTO.setKhLatitude(veEntity.getBvvLatStart() + "");
                    khachNowDTO.setKhLongitude(veEntity.getBvvLongStart() + "");
                    khachNowDTO.setHubTuyen(veEntity.getTcHubDiemDon());
                    khachNowDTO.setBbvId(veEntity.getBvvId() + StringUtils.EMPTY);
                    khachNowDTO.setTenKh(veEntity.getBvvTenKhachHangDi());
                    updateLocationHub(khachNowDTO, veEntity, true);
                    khachNowDTOS.add(khachNowDTO);
                });
            }
            if (type == TrangThaiVe.TRA.getTrangThai()) {
                veEntities = veRepository.findByDidIdAndLaiXeIdTra(tripId, status, txTcHavazDTO.getTxId(), type);
                veEntities.forEach(veEntity -> {
                    KhachNowDTO khachNowDTO = new KhachNowDTO();
                    updateTrangThaiTra(veEntity, khachNowDTO);
                    updateInfoClientTra(veEntity, khachNowDTO);
                    khachNowDTO.setKhSdt(veEntity.getBvvPhoneDi());
                    khachNowDTO.setKhLatitude(veEntity.getBvvLatEnd() + "");
                    khachNowDTO.setKhLongitude(veEntity.getBvvLongEnd() + "");
                    khachNowDTO.setHubTuyen(veEntity.getTcHubDiemTra());
                    khachNowDTO.setTenKh(veEntity.getBvvTenKhachHangDi());
                    khachNowDTO.setBbvId(veEntity.getBvvId() + StringUtils.EMPTY);
                    updateLocationHub(khachNowDTO, veEntity, false);
                    khachNowDTOS.add(khachNowDTO);
                });
            }
            txTcHavazDTO.setDsKhachHang(khachNowDTOS);

        });
        //todo Client chua duoc Trung Chuyen
        XeTcNowDTO xeTcNowDTO = new XeTcNowDTO();
        List<TcVeEntity> listClient = new ArrayList<>();
        List<KhachNowDTO> clients = new ArrayList<>();
        if (type == TrangThaiVe.DON.getTrangThai()) {
            listClient = veRepository.findByDidIdAndTcLenhIdIsNullOrDidIdAndTcLenhId(tripId, tripId, 0);
            listClient.forEach(veEntity -> {
                KhachNowDTO khachNowDTO = new KhachNowDTO();
                updateInfoClientDon(veEntity, khachNowDTO);
                updateTrangThaiDon(veEntity, khachNowDTO);
                khachNowDTO.setKhSdt(veEntity.getBvvPhoneDi());
                khachNowDTO.setKhLatitude(veEntity.getBvvLatStart() + StringUtils.EMPTY);
                khachNowDTO.setKhLongitude(veEntity.getBvvLongStart() + StringUtils.EMPTY);
                khachNowDTO.setHubTuyen(veEntity.getTcHubDiemTra());
                khachNowDTO.setTenKh(veEntity.getBvvTenKhachHangDi());
                khachNowDTO.setBbvId(veEntity.getBvvId() + StringUtils.EMPTY);
                updateLocationHub(khachNowDTO, veEntity, true);
                clients.add(khachNowDTO);
            });
        }

        if (type == TrangThaiVe.TRA.getTrangThai()) {
            listClient = veRepository.findByDidIdAndTcLenhTraIdIsNullOrDidIdAndTcLenhTraId(tripId, tripId, 0);
            listClient.forEach(veEntity -> {
                KhachNowDTO khachNowDTO = new KhachNowDTO();
                updateInfoClientTra(veEntity, khachNowDTO);
                updateTrangThaiTra(veEntity, khachNowDTO);
                khachNowDTO.setKhSdt(veEntity.getBvvPhoneDi());
                khachNowDTO.setKhLatitude(veEntity.getBvvLatEnd() + "");
                khachNowDTO.setKhLongitude(veEntity.getBvvLongEnd() + "");
                khachNowDTO.setHubTuyen(veEntity.getTcHubDiemTra());
                khachNowDTO.setTenKh(veEntity.getBvvTenKhachHangDi());
                khachNowDTO.setBbvId(veEntity.getBvvId() + StringUtils.EMPTY);
                updateLocationHub(khachNowDTO, veEntity, false);
                clients.add(khachNowDTO);
            });
        }
        xeTcNowDTO.setDsKhachHang(clients);
        txTcHavazDTOS.add(xeTcNowDTO);
        tripDetailDTO.setDsXeTc(txTcHavazDTOS);
        return tripDetailDTO;
    }

    @Override
    public List<TxCtvDTO> getAllTxCtv() {
        return havazNowRepositoryCustom.getAllTxCtv();
    }

    @Override
    public Page<GetTcVeDonResponse> getTcVeResponse() {
        Random random = new Random();
        List<GetTcVeDonResponse> tcVeDonResponses = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            GetTcVeDonResponse tcVeDonResponse = new GetTcVeDonResponse();
            tcVeDonResponse.setBvvIds(Arrays.asList(random.nextInt(1000),random.nextInt(1000)));
            tcVeDonResponse.setVersions(Arrays.asList(random.nextInt(10),random.nextInt(10)));
            tcVeDonResponse.setBks("29LD02279");
            tcVeDonResponse.setCurrentHub("Mỹ Dình");
            tcVeDonResponse.setCurrentHubId(1);
            tcVeDonResponse.setSoluongKhach(2l);
            tcVeDonResponse.setGiodieuhanh("19:30");
            tcVeDonResponse.setDiachiHanhkhach(random.nextInt(1000)+ "-Hoàng Ngân");
            tcVeDonResponse.setTuyen("Mỹ Đình - Lào Cai");
            tcVeDonResponse.setTenHanhkhach("test v-" + random.nextInt(1000));
            tcVeDonResponse.setStatus(1);
            tcVeDonResponse.setSdtHanhkhach("0123456789");
            tcVeDonResponses.add(tcVeDonResponse);

        }
        Page<GetTcVeDonResponse> page = new PageImpl<>(tcVeDonResponses);
        return page;
    }

    private void updateInfoClientDon(TcVeEntity veEntity, KhachNowDTO khachNowDTO) {
        if (veEntity.getVungTCDon() != null) {
            TcVungTrungChuyenEntity tcVungTrungChuyenEntity = vungTrungChuyenRepositoryCustom.findByIdVtc(veEntity.getVungTCDon());
            if (tcVungTrungChuyenEntity != null) {
                khachNowDTO.setTenVtc(tcVungTrungChuyenEntity.getTcVttName());
            }
        }
    }

    private void updateInfoClientTra(TcVeEntity veEntity, KhachNowDTO khachNowDTO) {
        if (veEntity.getVungTCTra() != null) {
            TcVungTrungChuyenEntity tcVungTrungChuyenEntity = vungTrungChuyenRepositoryCustom.findByIdVtc(veEntity.getVungTCTra());
            if (tcVungTrungChuyenEntity != null) {
                khachNowDTO.setTenVtc(tcVungTrungChuyenEntity.getTcVttName());
            }
        }
    }
    private void updateTrangThaiDon(TcVeEntity veEntity, KhachNowDTO khachNowDTO) {
        switch (veEntity.getTcTrangThaiDon()) {
            case 1:
                khachNowDTO.setKhTrangThaiTc(TrangThaiTc.DADIEU.getTrangThai());
                break;
            case 2:
                khachNowDTO.setKhTrangThaiTc(TrangThaiTc.DANGDON.getTrangThai());
                break;
            case 3:
                khachNowDTO.setKhTrangThaiTc(TrangThaiTc.DADON.getTrangThai());
                break;
            case 4:
                khachNowDTO.setKhTrangThaiTc(TrangThaiTc.DAHUY.getTrangThai());
                break;
            default:
                khachNowDTO.setKhTrangThaiTc(TrangThaiTc.CHUADIEU.getTrangThai());

        }
    }

    private void updateTrangThaiTra(TcVeEntity veEntity, KhachNowDTO khachNowDTO) {
        switch (veEntity.getTcTrangThaiTra()) {
            case 1:
                khachNowDTO.setKhTrangThaiTc(TrangThaiTc.DADIEU.getTrangThai());
                break;
            case 2:
                khachNowDTO.setKhTrangThaiTc(TrangThaiTc.DANGTRA.getTrangThai());
                break;
            case 3:
                khachNowDTO.setKhTrangThaiTc(TrangThaiTc.DATRA.getTrangThai());
                break;
            case 4:
                khachNowDTO.setKhTrangThaiTc(TrangThaiTc.DAHUY.getTrangThai());
                break;
            default:
                khachNowDTO.setKhTrangThaiTc(TrangThaiTc.CHUADIEU.getTrangThai());

        }
    }

    private void updateLocationHub(KhachNowDTO khachNowDTO, TcVeEntity veEntity, boolean isDon) {
        if (isDon) {
            Optional<BenXeEntity> benXeEntity = benXeEntityRepository.findById(veEntity.getTcHubDiemDon());
            if (benXeEntity.isPresent()) {
                khachNowDTO.setLatHub(benXeEntity.get().getBenxeLat());
                khachNowDTO.setLongHub(benXeEntity.get().getBenxeLong());
                khachNowDTO.setHubName(benXeEntity.get().getTen());
            }
        } else {
            Optional<BenXeEntity> benXeEntity = benXeEntityRepository.findById(veEntity.getTcHubDiemTra());
            if (benXeEntity.isPresent()) {
                khachNowDTO.setLatHub(benXeEntity.get().getBenxeLat());
                khachNowDTO.setLongHub(benXeEntity.get().getBenxeLong());
                khachNowDTO.setHubName(benXeEntity.get().getTen());
            }
        }
    }

    private String convertTenTuyen(String tenTuyen, int chieu) {
        if (tenTuyen == null) {
            return Strings.EMPTY;
        }
        List<String> lst = Arrays.asList(tenTuyen.split(Strings.DASH));
        if (chieu == 2) {
            Collections.reverse(lst);
        }
        return String.join(" - ", lst);

    }
}

