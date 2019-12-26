package com.havaz.transport.api.rest;

import com.havaz.transport.api.common.ResultMessage;
import com.havaz.transport.api.form.TTTaiXeForm;
import com.havaz.transport.api.service.TTTaiXeService;
import com.havaz.transport.dao.entity.TTTaiXe;
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

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/taixe")
public class TTTaiXeRest {
    private static final Logger log = LoggerFactory.getLogger(TTTaiXeRest.class);

    @Autowired
    private TTTaiXeService ttTaiXeService;

    @ApiOperation(value = "Lấy trạng thái của lái xe")
    @GetMapping("/gettrangthai/{taiXeId}")
    public ResponseEntity getTT(@ApiParam(value = "Lái xe id ", required = true) @PathVariable int taiXeId) {
        TTTaiXe ttTaiXe = ttTaiXeService.getTTTaiXeByTaiXeId(taiXeId);
        if (ttTaiXe != null) {
            return new ResponseEntity<>(ttTaiXe, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResultMessage("error", "Không lấy được trạng thái lái xe"), HttpStatus.OK);
    }

    @ApiOperation(value = "Cập nhật trạng thái của lái xe")
    @PostMapping("/capnhattrangthai")
    public ResponseEntity<ResultMessage> capnhat(@ApiParam(value = "Lái xe id và trạng thái <br/> 0: Ko sẵn sàng | 1: Sẵn sàng", required = true) @Valid @RequestBody TTTaiXeForm ttTaiXeForm) {
        boolean capnhat = ttTaiXeService.updateTrangThaiTaiXe(ttTaiXeForm);
        if (capnhat) {
            return new ResponseEntity<>(new ResultMessage("success", "Cập nhật trạng thái thành công!"),
                                        HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResultMessage("error", "Cập nhật không thành công."), HttpStatus.OK);
    }
}
