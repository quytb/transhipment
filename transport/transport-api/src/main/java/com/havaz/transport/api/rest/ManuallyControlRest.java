package com.havaz.transport.api.rest;

import com.havaz.transport.api.service.ManuallyControlService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/manuallycontrol")
public class ManuallyControlRest {

    @Autowired
    public ManuallyControlService manuallyControlService;

    //TODO API: Send trip infor to Havaz
    @ApiOperation(value = "Manually control to update ban_ve_ve to tc_ve table")
    @GetMapping("/updatetcv")
    public ResponseEntity getListTripData(@RequestParam("inputDate") String inputDate) {
        manuallyControlService.getListTripData(inputDate);
        return new ResponseEntity<>("TRANSFER DONE", HttpStatus.OK);
    }
}
