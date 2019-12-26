package com.havaz.transport.api.rest;

import com.havaz.transport.api.common.ExcelGenerator;
import com.havaz.transport.api.form.BaoCaoForm;
import com.havaz.transport.api.form.BaoCaoLenhForm;
import com.havaz.transport.api.model.BaoCaoLenhDTO;
import com.havaz.transport.api.model.BaoCaoTaiXeDTO;
import com.havaz.transport.api.service.BaoCaoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/baocaothang")
public class BaoCaoRest {

    private static final Logger LOG = LoggerFactory.getLogger(BaoCaoRest.class);

    @Autowired
    private BaoCaoService baoCaoService;

    @ApiOperation(value = "Lấy tất cả danh sách chấm công cho các tài xế")
    @PostMapping("/danhsach")
    public ResponseEntity timKiemLenh(@ApiParam(value = "Object tìm kiếm", required = true) @RequestBody BaoCaoForm baoCaoForm) {
        List<BaoCaoTaiXeDTO> dtoPageCustom;
        LOG.info("Lấy danh sách chấm công tháng cho tài xế : ");
        dtoPageCustom = baoCaoService.getAll(baoCaoForm);
        LOG.info("Đã lấy thông tin các lệnh thành công");
        return new ResponseEntity<>(dtoPageCustom, HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy tất cả danh sách chấm công cho các tài xế")
    @PostMapping(value = "/xuatbaocao")
    public ResponseEntity<InputStreamResource> excelCustomersReport(@ApiParam(value = "Object tìm kiếm", required = true) @RequestBody BaoCaoForm baoCaoForm) throws IOException {
        List<BaoCaoTaiXeDTO> customers = baoCaoService.getAll(baoCaoForm);

        ByteArrayInputStream in = ExcelGenerator.customersToExcel(customers);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                    "attachment; filename=Chamcong_" + System.currentTimeMillis() + ".xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    @ApiOperation(value = "Lấy tổng số lệnh theo tai xe")
    @PostMapping("/danhsachlenh")
    public ResponseEntity getListBaoCaoLenh(@ApiParam(value = "Object tìm kiếm", required = true) @RequestBody BaoCaoLenhForm baoCaoLenhForm) {
        List<BaoCaoLenhDTO> dtoPageCustom;
        LOG.info("Lấy danh sách tổng số lệnh theo tai xe : ");
        dtoPageCustom = baoCaoService.getAll(baoCaoLenhForm);
        LOG.info("Đã lấy thông tin các tổng số lệnh theo tai xe");
        return new ResponseEntity<>(dtoPageCustom, HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy tất cả danh sách chấm công cho các tài xế")
    @PostMapping(value = "/baocaotonghop")
    public ResponseEntity<InputStreamResource> excelCommandReport(@ApiParam(value = "Object tìm kiếm", required = true) @RequestBody BaoCaoLenhForm baoCaoLenhForm) throws IOException {
        List<BaoCaoLenhDTO>  commands = baoCaoService.getAll(baoCaoLenhForm);

        ByteArrayInputStream in = ExcelGenerator.commandToExcel(commands);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "attachment; filename=BaoCaoTongHop" + System.currentTimeMillis() + ".xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }
}
