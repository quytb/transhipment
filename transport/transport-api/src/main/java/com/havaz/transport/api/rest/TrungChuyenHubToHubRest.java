package com.havaz.transport.api.rest;

import com.havaz.transport.api.body.request.cmdhub.GetCmdHubRequest;
import com.havaz.transport.api.body.request.tcve.TaoLenhRequest;
import com.havaz.transport.api.common.ResultMessage;
import com.havaz.transport.api.model.ChiTietLenhDTO;
import com.havaz.transport.api.service.DieuDoService;
import com.havaz.transport.api.service.TrungChuyenHubToHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/trung-chuyen-hub")
public class TrungChuyenHubToHubRest {

    @Autowired
    private TrungChuyenHubToHubService hubToHubService;
    @Autowired
    private DieuDoService dieuDoService;

    @PostMapping("/tao-lenh")
    public ResponseEntity taoLenhTemp(@Valid @RequestBody TaoLenhRequest taoLenhRequest) {
        hubToHubService.creatCmdHubToHubForWeb(taoLenhRequest);
        return new ResponseEntity("success", HttpStatus.OK);
    }

    @PostMapping("/xac-nhan-lenh")
    public ResponseEntity<?> dieuLenhHub(@RequestBody GetCmdHubRequest getCmdHubRequest) {
        dieuDoService.dieuLenhHub(getCmdHubRequest);
        return new ResponseEntity<>(new ResultMessage("Success", "Điều lệnh thành công"), HttpStatus.OK);
    }

    @GetMapping("/huy-lenh/{cmdId}")
    public ResponseEntity<?> huyLenhHub(@PathVariable Integer cmdId) {
        dieuDoService.cancelLenhHub(cmdId);
        return new ResponseEntity<>(new ResultMessage("Success", "Hủy thành công"), HttpStatus.OK);
    }

    @GetMapping("ds-lenh")
    public ResponseEntity<Page<ChiTietLenhDTO>> getdsLenhTao(Integer diemgiaokhach,
                                                             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateRequest,
                                                             @PageableDefault Pageable pageable) {
        return new ResponseEntity<>(hubToHubService.getDsLenh(diemgiaokhach, dateRequest, pageable), HttpStatus.OK);
    }


}
