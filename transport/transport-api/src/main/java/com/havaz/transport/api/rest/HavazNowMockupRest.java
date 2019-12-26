package com.havaz.transport.api.rest;

import com.havaz.transport.api.body.request.tcve.GetTcVeDonRequest;
import com.havaz.transport.api.body.response.tcve.GetTcVeDonResponse;
import com.havaz.transport.api.model.KhachNowDTO;
import com.havaz.transport.api.model.NowHubDTO;
import com.havaz.transport.api.model.NowTripDTO;
import com.havaz.transport.api.model.NowTripDetailDTO;
import com.havaz.transport.api.model.TaiXeDTO;
import com.havaz.transport.api.model.XeTcNowDTO;
import com.havaz.transport.api.service.HavazNowService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/havaznowmockup")
public class HavazNowMockupRest {
    private static final Logger LOG = LoggerFactory.getLogger(HavazNowMockupRest.class);

    @Autowired
    private HavazNowService havazNowService;

    //TODO API: list all xe tuyến (trong 1 khoảng thời gian)
    @ApiOperation(value = "Danh sách các xe trung chuyển")
    @GetMapping("/listtrip/{beforeNow}/{afterNow}")
    public ResponseEntity<?> getTransportInfor(@ApiParam(value = "Thời gian trước hiện tại") @PathVariable int beforeNow,
                                               @ApiParam(value = "Thời gian sau hiện tại") @PathVariable int afterNow) {
        List<NowTripDTO> nowTripDTOList =havazNowService.getNowTripDTOList(beforeNow,afterNow);
        return new ResponseEntity<>(nowTripDTOList, HttpStatus.OK);
    }

    //TODO API: Trip detail by tripId
    @ApiOperation(value = "Danh sách các xe trung chuyển")
    @GetMapping("/tripdetail/{tripid}")
    public ResponseEntity<?> tripDetail(@ApiParam(value = "tripIdXXXX") @PathVariable("tripid") int tripId) {
        NowTripDetailDTO nowTripDetailDTO = new NowTripDetailDTO();
        nowTripDetailDTO.setTripId("1123456");
        nowTripDetailDTO.setXtBks("29F1111666");
        nowTripDetailDTO.setXtSdt("0919568888");
        nowTripDetailDTO.setXtTen("Nguyễn Trần Tuyến");
        nowTripDetailDTO.setXtLongitude("105.7748661");
        nowTripDetailDTO.setXtLatitude("21.030619799999997");
        List<XeTcNowDTO> listXeTcNowDTO = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            XeTcNowDTO x = new XeTcNowDTO();
            x.setXeId(i + 1);
            x.setXtcType(i + 1);
            x.setXtcBks("29F12122" + i);
            x.setXtcSdt("091122339" + i);
            x.setXtcTen("Trần Tấn Công" + i);
            x.setXtcLongitude("21.0" + i + "298");
            x.setXtcLatitude("105.8" + i + "739");
            x.setXtcHubId("101" + i);
            x.setXtcHubName("50" + i);
            List<KhachNowDTO> khachNowDTOList = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                KhachNowDTO y = new KhachNowDTO();
                y.setKhSdt("097899966" + j);
                y.setKhTimeDon("2" + j);
                y.setKhTimeToHub("3" + j);
                y.setKhTrangThaiTc("" + j);
                y.setKhLongitude("105." + j + "1121");
                y.setKhLatitude("21." + j + "3303");
                khachNowDTOList.add(y);
            }
            x.setDsKhachHang(khachNowDTOList);
            listXeTcNowDTO.add(x);
        }
        nowTripDetailDTO.setDsXeTc(listXeTcNowDTO);
        return new ResponseEntity<>(nowTripDetailDTO, HttpStatus.OK);
    }


    //TODO API: list hubs
    @ApiOperation(value = "Danh sách các hub")
    @GetMapping("/listhub/{tuyid}")
    public ResponseEntity<?> listhub(@ApiParam(value = "tuyid") @PathVariable("tuyid") int tuyId) {
        List<NowHubDTO> nowHubDTOList = new ArrayList<>();
        for (int j = 0; j < 9; j++) {
            NowHubDTO hub = new NowHubDTO();
            hub.setHubId("1001"+j);
            hub.setHubName("Hub No "+(j+1));
            hub.setHubLongitude("105." + j + "1123");
            hub.setHubLatitude("21." + j + "6655");
            nowHubDTOList.add(hub);
        }
        return new ResponseEntity<>(nowHubDTOList, HttpStatus.OK);
    }

    @PostMapping("/dieu-hanh-hub/trung-chuyen-don/tim-kiem")
    public ResponseEntity<Page<GetTcVeDonResponse>> getDsKhachhang(@RequestBody(required = false)
                                                   GetTcVeDonRequest request, @PageableDefault
                                                   Pageable pageable) {
        return new ResponseEntity<>(havazNowService.getTcVeResponse(), HttpStatus.OK);
    }

    //TODO API: list hubs
    @ApiOperation(value = "Danh sách cộng tác viên/ tài xế trung chuyển")
    @GetMapping("/listtaixe")
    public ResponseEntity<?> listTaiXe() {
        List<TaiXeDTO> listTx = new ArrayList<>();
        for (int j = 0; j < 9; j++) {
            TaiXeDTO tx = new TaiXeDTO();
            tx.setTaiXeId("112"+j);
            tx.setTaiXeTen("Nguyễn Huy Tùng "+j);
            tx.setType("1");
            listTx.add(tx);
        }
        TaiXeDTO tx = new TaiXeDTO();
        tx.setTaiXeId("689");
        tx.setTaiXeTen("Cộng Tác Viên");
        tx.setType("2");
        listTx.add(tx);
        return new ResponseEntity<>(listTx, HttpStatus.OK);
    }

}
