package com.havaz.transport.api.rest;

import com.havaz.transport.api.common.ResultMessage;
import com.havaz.transport.api.form.ChamCongForm;
import com.havaz.transport.api.model.ChamCongDTO;
import com.havaz.transport.api.model.LuuChamCongDTO;
import com.havaz.transport.api.service.QuanLyLichTrucService;
import com.havaz.transport.core.exception.TransportException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/chamcong")
public class ChamCongRest {

    private static final Logger LOG = LoggerFactory.getLogger(ChamCongRest.class);

    @Autowired
    private QuanLyLichTrucService quanLyLichTrucService;

    //Lấy toàn bộ dữ liệu chấm công theo từng chi nhánh
    @ApiOperation(value = "Lấy dữ liệu chấm công")
    @PostMapping("/taidulieu")
    public ResponseEntity<?> getDuLieuChamCong(@RequestBody ChamCongForm chamCongForm) {
        if (chamCongForm.getNgayChamCong().isAfter(LocalDate.now())) {
            throw new TransportException("Ngày chấm công phải nhỏ hơn hoặc bằng ngày hiện tại!");
        }
        LOG.debug("Lấy dữ liệu chấm công ngày : {}", chamCongForm.getNgayChamCong());
        List<ChamCongDTO> chamCongDTOS = quanLyLichTrucService.getDuLieuChamCong(chamCongForm);
        return new ResponseEntity<>(chamCongDTOS, HttpStatus.OK);
    }

    //Lấy toàn bộ dữ liệu chấm công
    @ApiOperation(value = "Lưu dữ liệu chấm công")
    @PostMapping("/luudulieu")
    public ResponseEntity<ResultMessage> saveDuLieuChamCong(@ApiParam(value = "Dữ liệu chấm công") @RequestBody LuuChamCongDTO luuChamCongDTO) {
        LOG.debug("Start lưu dữ liệu chấm công ngày : {}", luuChamCongDTO.getNgayChamCong());
        quanLyLichTrucService.saveDuLieuChamCong(luuChamCongDTO);
        return new ResponseEntity<>(new ResultMessage("success", "Thành công"), HttpStatus.OK);
    }

}
