package com.havaz.transport.api.common;

import com.havaz.transport.api.form.location.Point;
import com.havaz.transport.api.model.KhachNowDTO;
import com.havaz.transport.api.model.NowTripDetailDTO;
import com.havaz.transport.api.model.TripStationDTO;
import com.havaz.transport.api.service.LocationService;
import com.havaz.transport.api.service.XeTuyenService;
import com.havaz.transport.core.exception.TransportException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CalculateHubCommon {

    private static final Logger log = LoggerFactory.getLogger(CalculateHubCommon.class);

    @Autowired
    private LocationService locationService;

    @Autowired
    private XeTuyenService xeTuyenService;

    public void getHubCommon(NowTripDetailDTO xtDetailDTO)  {

        xtDetailDTO.getDsXeTc().forEach(xeTcNowDTO -> {
            //todo set duration for khach
            xeTcNowDTO.getDsKhachHang().forEach(khachNowDTO -> {
                if ("1".equals(khachNowDTO.getKhTrangThaiTc()) ||
                        "2".equals(khachNowDTO.getKhTrangThaiTc())) {
                    double timeTxToKh = locationService.getDurationFromAPI(
                            new Point(Double.parseDouble(xeTcNowDTO.getXtcLatitude()),
                                      Double.parseDouble(xeTcNowDTO.getXtcLongitude())),
                            new Point(Double.parseDouble(khachNowDTO.getKhLatitude()),
                                      Double.parseDouble(khachNowDTO.getKhLongitude())));
                    khachNowDTO.setDuration(timeTxToKh);
                    khachNowDTO.setKhTimeDon(Math.round(timeTxToKh) + StringUtils.EMPTY);
                } else {
                    khachNowDTO.setDuration(0d);
                }

            });
            //todo sort duration gan nhat len truoc
            xeTcNowDTO.setDsKhachHang(xeTcNowDTO.getDsKhachHang()
                    .stream()
                    .sorted(Comparator.comparingDouble(KhachNowDTO::getDuration)).collect(Collectors.toList()));

            //todo check toan bo client da len xe
            int count = 0;
            for(KhachNowDTO khachNowDTO : xeTcNowDTO.getDsKhachHang()) {
                if( "3".equals(khachNowDTO.getKhTrangThaiTc())) {
                    count ++;
                }
            }
            if (count == xeTcNowDTO.getDsKhachHang().size() && xeTcNowDTO.getDsKhachHang().size() > 0) {
                xeTcNowDTO.getDsKhachHang().forEach(khachNowDTO -> {
                    List<TripStationDTO> tripStationDTOs = xeTuyenService.getTripStations(Integer.parseInt(xtDetailDTO.getTripId()))
                            .stream()
                            .filter(tripStationDTO ->
                                            Objects.equals(tripStationDTO.getId(),
                                                           xeTcNowDTO.getDsKhachHang()
                                                                   .get(xeTcNowDTO.getDsKhachHang().size() - 1)
                                                                   .getHubTuyen()))
                            .collect(Collectors.toList());
                    TripStationDTO tripStationDTO = new TripStationDTO();
                    if (tripStationDTOs.size() >= 1) {
                        tripStationDTO = tripStationDTOs.get(0);
                    }
                    try {
                        khachNowDTO.setKhTimeToHub(locationService.getDurationFromAPI(
                                new Point(Double.parseDouble(xeTcNowDTO.getXtcLatitude()),
                                          Double.parseDouble(xeTcNowDTO.getXtcLongitude())),
                                new Point(tripStationDTO.getLat(),
                                          tripStationDTO.getLng())) + StringUtils.EMPTY);
                    } catch (TransportException e) {
                        log.error(e.getMessage(), e);
                    }
                });
                return;
            }

            //todo tinh toan thoi gian don khach
            if (xeTcNowDTO.getDsKhachHang().size() > 1) {
                for (int i = 0; i < xeTcNowDTO.getDsKhachHang().size() - 1; i++) {
                    if ("1".equals(xeTcNowDTO.getDsKhachHang().get(i).getKhTrangThaiTc()) || "2".equals(xeTcNowDTO.getDsKhachHang().get(i).getKhTrangThaiTc())) {
                        try {
                            Double timeKhToKh = locationService.getDurationFromAPI(new Point(Double.parseDouble(xeTcNowDTO.getDsKhachHang().get(i).getKhLatitude())
                                            , Double.parseDouble(xeTcNowDTO.getDsKhachHang().get(i).getKhLongitude()))
                                    , new Point(Double.parseDouble(xeTcNowDTO.getDsKhachHang().get(i + 1).getKhLatitude()),
                                            Double.parseDouble(xeTcNowDTO.getDsKhachHang().get(i + 1).getKhLongitude())));
                            xeTcNowDTO.getDsKhachHang().get(i + 1).setKhTimeDon(Math.round(xeTcNowDTO.getDsKhachHang().get(i).getDuration() + timeKhToKh) + StringUtils.EMPTY);
                            xeTcNowDTO.getDsKhachHang().get(i + 1).setDuration(timeKhToKh + xeTcNowDTO.getDsKhachHang().get(i).getDuration());
                        } catch (TransportException e) {
                            log.error(e.getMessage(), e);
                            xeTcNowDTO.getDsKhachHang().get(i + 1).setKhTimeDon(StringUtils.EMPTY);
                        }
                    }
                }
            } else if (xeTcNowDTO.getDsKhachHang().size() == 1) {
                xeTcNowDTO.getDsKhachHang().get(0).setKhTimeDon(xeTcNowDTO.getDsKhachHang().get(0).getDuration() + StringUtils.EMPTY);
            }
            Double totalTimeDon = 0d;
            for (KhachNowDTO khachNowDTO : xeTcNowDTO.getDsKhachHang()) {
                if ("1".equals(khachNowDTO.getKhTrangThaiTc()) || "2".equals(khachNowDTO.getKhTrangThaiTc())) {
                    totalTimeDon = totalTimeDon + khachNowDTO.getDuration();
                }
            }

            //todo calculator time to hub from last client
//            xeTcNowDTO.getDsKhachHang().get(l)
            double durationTohub = 0d;
            if (xeTcNowDTO.getDsKhachHang().size() > 0) {
                KhachNowDTO clientTcLatest = xeTcNowDTO.getDsKhachHang().get(xeTcNowDTO.getDsKhachHang().size() - 1);
                List<TripStationDTO> tripStationDTOs = xeTuyenService.getTripStations(Integer.parseInt(xtDetailDTO.getTripId()))
                        .stream()
                        .filter(tripStationDTO -> Objects.equals(tripStationDTO.getId(), clientTcLatest.getHubTuyen()))
                        .collect(Collectors.toList());
                TripStationDTO tripStationDTO = new TripStationDTO();
                if (!tripStationDTOs.isEmpty()) {
                    tripStationDTO = tripStationDTOs.get(0);
                }
                try {
                    durationTohub = locationService.getDurationFromAPI(new Point(Double.parseDouble(clientTcLatest.getKhLatitude()),
                            Double.parseDouble(clientTcLatest.getKhLongitude())), new Point(tripStationDTO.getLat(), tripStationDTO.getLng()));
                } catch (TransportException e) {
                    log.error(e.getMessage(), e);
                }
            }

            for (KhachNowDTO khachNowDTO : xeTcNowDTO.getDsKhachHang()) {
                khachNowDTO.setKhTimeToHub(Math.round(durationTohub + xeTcNowDTO.getDsKhachHang()
                        .get(xeTcNowDTO.getDsKhachHang().size() - 1)
                        .getDuration()) + StringUtils.EMPTY);
            }

        });
    }

}
