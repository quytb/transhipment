package com.havaz.transport.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.havaz.transport.api.form.location.DataLocationServiceDistanceForm;
import com.havaz.transport.api.form.location.LocationRequest;
import com.havaz.transport.api.form.location.Point;
import com.havaz.transport.api.service.LocationService;
import com.havaz.transport.core.utils.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

    private static final String URL_LOCATION_SERVICE_V2 = "http://maps.havaz.vn/api/v2/distances";
    private static final String URL_LOCATION_SERVICE_V1 = "http://maps.havaz.vn/api/v1/distances";
    private static final String URL_LOCATION_SERVICE_TOKEN = "34fe3ce5f63cfa23757a751286817742";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public double getDistanFromAPI(Point pointStart, Point pointEnd) {
        HttpHeaders headers = createHeaders();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                URL_LOCATION_SERVICE_V2 + "?from=" + pointStart.getLat() + "," + pointStart
                        .getLng() + "&to=" + pointEnd.getLat() + "," + pointEnd.getLng());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<DataLocationServiceDistanceForm> dataResponse =
                restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
                                      DataLocationServiceDistanceForm.class);
        DataLocationServiceDistanceForm data = dataResponse.getBody();
        return data.getData().getTotal().getDistance().getValue();
    }

    @Override
    public double getDurationFromAPI(Point pointStart, Point pointEnd) {
        HttpHeaders headers = createHeaders();
        String sb = URL_LOCATION_SERVICE_V2 + "?from=" +
                pointStart.getLat() + Strings.COMMA +
                pointStart.getLng() + "&to=" +
                pointEnd.getLat() + Strings.COMMA +
                pointEnd.getLng();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sb);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<DataLocationServiceDistanceForm> dataResponse =
                restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
                                      DataLocationServiceDistanceForm.class);
        DataLocationServiceDistanceForm data = dataResponse.getBody();
        return data.getData().getTotal().getDuration().getValue();
    }

    @Override
    public List<Double> getDistanceAndDurationFromAPI(Point pointStart, Point pointEnd) { ;
        HttpHeaders headers = createHeaders();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                URL_LOCATION_SERVICE_V2 + "?from=" + pointStart.getLat() + "," + pointStart
                        .getLng() + "&to=" + pointEnd.getLat() + "," + pointEnd.getLng());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<DataLocationServiceDistanceForm> dataResponse =
                restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
                                      DataLocationServiceDistanceForm.class);
        DataLocationServiceDistanceForm data = dataResponse.getBody();
        List<Double> lst = new ArrayList<>();
        lst.add(data.getData().getTotal().getDistance().getValue());
        lst.add(data.getData().getTotal().getDuration().getValue());
        return lst;
    }

    private List<Point> getPointBetween2Point(Point pointStart, Point pointEnd) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setFrom(pointStart);
        locationRequest.setTo(pointEnd);
        String jsonString = StringUtils.EMPTY;
        try {
            jsonString = objectMapper.writeValueAsString(locationRequest);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }

        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);

        ResponseEntity<DataLocationServiceDistanceForm> dataResponse =
                restTemplate.exchange(URL_LOCATION_SERVICE_V1, HttpMethod.GET, entity,
                                      DataLocationServiceDistanceForm.class);
        DataLocationServiceDistanceForm data = dataResponse.getBody();
        List<Point> lst = new ArrayList<>();
        lst = data.getData().getPoints().stream().map(temp->{
            Point point = new Point();
            point.setLat(temp.getLocation().get(1));
            point.setLng(temp.getLocation().get(0));
            return point;
        }).collect(Collectors.toList());

        return lst;
    }

    @Override
    public List<Point> getPointBetweenMultiplePoint(List<Point> points) {
        List<Point> pointList = new ArrayList<>();
        for (int i = 0; i < points.size(); i++){
            if (i + 1 == points.size()) {
                continue;
            }
            pointList.addAll(getPointBetween2Point(points.get(i), points.get(i + 1)));
        }
        return pointList;
    }

    private HttpHeaders createHeaders() {
        return new HttpHeaders() {
            {
                set("Authorization", URL_LOCATION_SERVICE_TOKEN);
                set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            }
        };
    }
}
