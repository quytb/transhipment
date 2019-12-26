package com.havaz.transport.api.service;

import com.havaz.transport.api.form.location.Point;
import com.havaz.transport.api.model.TripStationDTO;

import java.util.List;

public interface XeTuyenService {
    List<TripStationDTO> getTripStations(int tripId);

    int getBestHub(int tripId, Point toaDoKhachHang);
}
