package com.havaz.transport.api.rest;

import com.havaz.transport.api.form.VtcCtvForm;
import com.havaz.transport.api.model.CtvDTO;
import com.havaz.transport.api.model.XeDTO;
import com.havaz.transport.api.service.AdminLv2UserService;
import com.havaz.transport.api.service.VtcCtvService;
import com.havaz.transport.api.service.VungTrungChuyenService;
import com.havaz.transport.api.service.XeService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/vtc-ctv")
public class VtcCtvRest {

    private static final Logger log = LoggerFactory.getLogger(VtcCtvRest.class);

    @Autowired
    private VtcCtvService vtcCtvService;

    @Autowired
    private AdminLv2UserService adminLv2UserService;

    @Autowired
    private VungTrungChuyenService vungTrungChuyenService;

    @Autowired
    private XeService xeService;

    @ApiOperation(value = "List Ctv theo Vùng")
    @GetMapping("/list")
    public ResponseEntity<?> getListVctCtv(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size,
                                           @RequestParam(value = "ctvName", defaultValue = "", required = false) String ctvName ) {
        return new ResponseEntity<>(vtcCtvService.getList(page, size, ctvName), HttpStatus.OK);
    }

    @ApiOperation(value = "List Ctv ")
    @GetMapping("/ctv-list")
    public ResponseEntity<List<CtvDTO>> getAllCtv() {
        return new ResponseEntity<>(adminLv2UserService.getAllCtv(), HttpStatus.OK);
    }

    @ApiOperation(value = "Create or update CTV-VTC ")
    @PostMapping("/create-update")
    public ResponseEntity updateVtcCtv(@RequestBody @Valid VtcCtvForm form) {
        return new ResponseEntity<>(vtcCtvService.createOrUpdate(form), HttpStatus.OK);
    }

    @ApiOperation(value = "List Vung ")
    @GetMapping("/vtc-list")
    public ResponseEntity<?> getAllVung() {
        return new ResponseEntity<>(vungTrungChuyenService.findAllByStatus(1), HttpStatus.OK);
    }

    @ApiOperation(value = "List Xe Ctv ")
    @GetMapping("/xe-list")
    public ResponseEntity<List<XeDTO>> getAllXeCtv() {
        return new ResponseEntity<>(xeService.getXeCtvVtc(), HttpStatus.OK);
    }
}
