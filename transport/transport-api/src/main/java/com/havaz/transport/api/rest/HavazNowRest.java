package com.havaz.transport.api.rest;

import com.havaz.transport.api.service.LocationService;
import com.havaz.transport.api.form.location.Point;
import com.havaz.transport.api.model.CanhBaoDTO;
import com.havaz.transport.api.model.NowTripDTO;
import com.havaz.transport.api.model.NowTripDetailDTO;
import com.havaz.transport.api.model.TripStationDTO;
import com.havaz.transport.api.service.HavazNowService;
import com.havaz.transport.api.service.XeTuyenService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/havaznow")
public class HavazNowRest {
    private static final Logger log = LoggerFactory.getLogger(HavazNowRest.class);

    @Autowired
    private HavazNowService havazNowService;

    @Autowired
    private XeTuyenService xeTuyenService;

    @Autowired
    private LocationService locationService;

    //TODO API: list all xe tuyến (trong 1 khoảng thời gian)
    @ApiOperation(value = "Danh sách các xe trung chuyển")
    @GetMapping("/listtrip/{beforeNow}/{afterNow}")
    public ResponseEntity<?> getTransportInfor(@ApiParam(value = "Thời gian trước hiện tại") @PathVariable int beforeNow,
                                               @ApiParam(value = "Thời gian sau hiện tại") @PathVariable int afterNow) {
        List<NowTripDTO> nowTripDTOList = havazNowService.getNowTripDTOList(beforeNow, afterNow);
        return new ResponseEntity<>(nowTripDTOList, HttpStatus.OK);
    }

    @GetMapping("/tripdetails/{tripId}/trangthai/{type}")
    public ResponseEntity<?> getTripDetail(@PathVariable("tripId") Integer tripId, @PathVariable("type") Integer type) {
        NowTripDetailDTO tripDetailDTO = havazNowService.getTripDetails(tripId, type);
        return new ResponseEntity<>(tripDetailDTO, HttpStatus.OK);
    }

    @GetMapping("tripdetails/{tripId}/trangthai/{type}/dskhach")
    public ResponseEntity getListKhachOfTrip(@PathVariable("tripId") Integer tripId, @PathVariable("type") Integer type) {
        return new ResponseEntity<>(havazNowService.getKhByTrip(tripId, type), HttpStatus.OK);
    }
    @ApiOperation(value = "Danh sách tài xế trung chuyển và Ctv")
    @GetMapping("/listTx-Ctv")
    public ResponseEntity getListTx() {
        return new ResponseEntity<>(havazNowService.getAllTxCtv(), HttpStatus.OK);
    }

    @ApiOperation(value = "Danh sách cảnh báo")
    @PostMapping("/canhbao")
    public ResponseEntity<?> getTransportInfor(@RequestBody List<Integer> listTripId) {
        List<CanhBaoDTO> canhBaoDTOS = havazNowService.getAllCanhBaoForListTrip(listTripId);
        return new ResponseEntity<>(canhBaoDTOS, HttpStatus.OK);
    }

    @ApiOperation(value = "Danh sách hub cho 1 trip")
    @GetMapping("/listhub/{tripId}")
    public ResponseEntity<?> getListHub(@PathVariable("tripId") int tripId) {
        List<TripStationDTO> tripStationDTOS = xeTuyenService.getTripStations(tripId);
        return new ResponseEntity<>(tripStationDTOS, HttpStatus.OK);
    }

    @ApiOperation(value = "Danh sách hub va cac toa do di qua cho 1 trip")
    @PostMapping("/listpoint")
    public ResponseEntity<?> getListHubDrawAble(@RequestBody List<Point> points) {
        return new ResponseEntity<>(locationService.getPointBetweenMultiplePoint(points), HttpStatus.OK);
    }

}
