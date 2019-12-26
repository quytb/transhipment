package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.common.CommonUtils;
import com.havaz.transport.api.form.XeTuyenTripStationForm;
import com.havaz.transport.api.form.location.Point;
import com.havaz.transport.api.model.TripStationDTO;
import com.havaz.transport.api.service.XeTuyenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class XeTuyenServiceImpl implements XeTuyenService {

    private static final Logger LOG = LoggerFactory.getLogger(XeTuyenServiceImpl.class);

    @Value("${havaz.url-erp}")
    private String url;

    @Value("${havaz.url-erp-token}")
    private String token;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<TripStationDTO> getTripStations(int tripId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "trip-detail-time")
                .queryParam("tripId", tripId)
                .queryParam("token", token);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<XeTuyenTripStationForm> dataResponse =
                restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
                                      XeTuyenTripStationForm.class);
        XeTuyenTripStationForm data = dataResponse.getBody();
        return data.getData();
    }

    @Override
    public int getBestHub(int tripId, Point toaDoKhachHang) {
        if (toaDoKhachHang == null || toaDoKhachHang.getLat() == null ||
                toaDoKhachHang.getLat() == 0 || toaDoKhachHang.getLng() == null ||
                toaDoKhachHang.getLng() == 0) {
            return 0;
        }

        List<TripStationDTO> tripStationDTOS = getTripStations(tripId);
        if (!tripStationDTOS.isEmpty()) {
            double min = 1000000000;
            TripStationDTO tripStationDTO = null;
            for (TripStationDTO e : tripStationDTOS) {
                if (e.getIsBusiness() == 0 && e.getIsPoint() == 0) {
                    continue;
                }
                if (e.getLat() == null || e.getLng() == null) {
                    continue;
                }
                try {
                    Point toaDoHub = new Point(e.getLat(), e.getLng());
                    double khoangCach = CommonUtils.distance(toaDoHub, toaDoKhachHang);
                    if (khoangCach < min && khoangCach != 0) {
                        min = khoangCach;
                        tripStationDTO = e;
                    }
                } catch (NullPointerException ex) {
                    LOG.warn(ex.getMessage(), ex);
                }
            }

            return tripStationDTO != null ? tripStationDTO.getId() : 0;
        }
        return 0;
    }
}
