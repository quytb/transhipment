package com.havaz.transport.api.rest;

import com.havaz.transport.api.service.ChiNhanhService;
import com.havaz.transport.dao.entity.ChiNhanhEntity;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/chinhanh")
public class ChiNhanhRest {

    @Autowired
    private ChiNhanhService chiNhanhService;

    @ApiOperation(value = "Lấy tất cả danh sách các chi nhanh")
    @GetMapping("/danhsach")
    public ResponseEntity getAll() {
        List<ChiNhanhEntity> chiNhanhs = chiNhanhService.getAll();
        return new ResponseEntity<>(chiNhanhs, HttpStatus.OK);
    }
}
