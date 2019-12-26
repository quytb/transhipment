package com.havaz.transport.api.rest;

import com.havaz.transport.api.common.ResultMessage;
import com.havaz.transport.api.form.VeActionForm;
import com.havaz.transport.api.model.TopicDTO;
import com.havaz.transport.api.model.TripDTO;
import com.havaz.transport.api.model.TripTrungChuyenDTO;
import com.havaz.transport.api.service.CommonService;
import com.havaz.transport.api.service.LenhService;
import com.havaz.transport.api.service.VeTcService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fwb")
public class IntegrationRest {
    private static final Logger log = LoggerFactory.getLogger(IntegrationRest.class);

    @Autowired
    private VeTcService veTcService;

    @Autowired
    private CommonService commonService;

//    @Autowired
//    private RabbitMQPublisher rabbitMQPublisher;
//
//    @Autowired
//    private AdminLv2UserService adminLv2UserService;
//
//    @Autowired
//    private FirebaseClientService firebaseClientService;

    @Autowired
    private LenhService lenhService;

    //TODO API: Send trip infor to Havaz
    @ApiOperation(value = "Danh sách các xe trung chuyển")
    @GetMapping("/havaz/transportlist/{tripId}/{type}")
    public ResponseEntity<?> getTransportInfor(@ApiParam(value = "Id của chuyến đi") @PathVariable int tripId,
                                               @ApiParam(value = "Constants<br/>1: Trung chuyển đón<br/>2: Trung chuyển trả") @PathVariable int type) {
        List<TripDTO> listTripDTO = new ArrayList<>();
        listTripDTO = veTcService.getListTripByTripId(tripId, type, 3);
        return new ResponseEntity<>(listTripDTO, HttpStatus.OK);
    }

    //TODO API: Lấy danh sách trung chuyển theo trip Id
    @ApiOperation(value = "Danh sách xe trung chuyển theo trip")
    @GetMapping("/danhsachxetc/{tripId}/{type}")
    public ResponseEntity<?> getDanhSachXeTc(@ApiParam(value = "Id của chuyến đi") @PathVariable int tripId,
                                             @ApiParam(value = "Constants<br/>1: Trung chuyển đón<br/>2: Trung chuyển trả") @PathVariable int type) {
        List<TripDTO> listTripDTO = new ArrayList<>();
        listTripDTO = veTcService.getListTripByTripId(tripId, type, 2);
        return new ResponseEntity<>(listTripDTO, HttpStatus.OK);
    }
    //TODO: Lấy danh sách topic theo taixeId
    @ApiOperation(value = "Danh sách topic")
    @GetMapping("/danhsachtopic/{taiXeId}")
    public ResponseEntity<?> getDanhSachTopic(@ApiParam(value = "Id tài xế trung chuyển") @PathVariable int taiXeId)  {
        List<TopicDTO> tcVeEntities = veTcService.getVeByTaiXeId(taiXeId);
        if (tcVeEntities != null && !tcVeEntities.isEmpty()) {
            return new ResponseEntity<>(tcVeEntities, HttpStatus.OK);
        } else {
            return new ResponseEntity<List<TopicDTO>>(new ArrayList<>(), HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Lấy thông tin xe tuyến")
    @GetMapping("/xetuyen/{lenhId}")
    public ResponseEntity<?> getXeTuyenInfor(@ApiParam(value = "lệnh Id") @PathVariable int lenhId)  {
        log.info("########### START: lấy thông tin các xe tuyến ##############");
        TripTrungChuyenDTO tripTrungChuyenDTO = lenhService.getXeTuyenInfor(lenhId);
        log.info("########### END: Hoàn tất lấy thông tin các xe tuyến ##############");
        return new ResponseEntity<>(tripTrungChuyenDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy thông tin vé từ ERP")
    @PostMapping("/veaction")
    public ResponseEntity<?> getActionForVe(@Valid @ApiParam(value = "Constants: TAO_MOI_VE, CAP_NHAT_VE, XUONG_XE, HUY_VE") @RequestBody VeActionForm veActionForm,
                                            BindingResult result) {
        veActionForm.validate(veActionForm, result);
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        log.info("########### Transfer body ##############");
        if (veActionForm.getBvvIds() != null && veActionForm.getBvvIds().size() > 0) {
            log.info("Transfer bvvIds:: " + veActionForm.getBvvIds().toString());
        }
        log.info("bvvIdFrom:: " + veActionForm.getBvvIdFrom());
        log.info("bvvIdTo:: " + veActionForm.getBvvIdTo());
        log.info("Action:: " + veActionForm.getAction());
        log.info("########### End body ##############");

        commonService.transferVeERPToVeTC(veActionForm);

        return new ResponseEntity<>(new ResultMessage("success", "Transfer successfully."),
                                    HttpStatus.OK);
    }
}
