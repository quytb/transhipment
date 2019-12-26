package com.havaz.transport.api.rest;

import com.havaz.transport.api.service.LenhConfigService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lenhconfig")
public class LenhConfigRest {

    @Autowired
    private LenhConfigService lenhConfigService;

    @ApiOperation(value = "Lấy status lệnh config")
    @GetMapping
    public ResponseEntity<?> getConfig() {
        int config = lenhConfigService.getStatus();
        return new ResponseEntity<>(config, HttpStatus.OK);
    }

    @ApiOperation(value = "Thay đổi status lệnh config")
    @GetMapping("/set/{status}")
    public ResponseEntity<?> setConfig(@PathVariable("status") int status) {
        lenhConfigService.changeStatus(status);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
