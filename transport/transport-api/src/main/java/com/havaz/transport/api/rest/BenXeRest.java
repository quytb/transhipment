package com.havaz.transport.api.rest;

import com.havaz.transport.api.model.TripStationDTO;
import com.havaz.transport.api.service.BenXeService;
import com.havaz.transport.api.service.XeTuyenService;
import com.havaz.transport.dao.entity.BenXeEntity;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/benxe")
public class BenXeRest {
    private static final Logger LOG = LoggerFactory.getLogger(BenXeRest.class);

    @Autowired
    private BenXeService benXeService;

    @Autowired
    private XeTuyenService xeTuyenService;

    @ApiOperation(value = "Lấy toàn bộ danh sách hub")
    @GetMapping("/danhsach")
    public ResponseEntity<?> danhSachBenXe(@RequestParam(required = false, value = "diemdontra",
                                                         defaultValue = "0") int diemdontra) {
        LOG.info("Lấy toàn bộ danh sách hub: ");
        List<BenXeEntity> benXeEntities = benXeService.getAll(diemdontra);
        LOG.info("Đã lấy danh sách hub thành công");
        return new ResponseEntity<>(benXeEntities, HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy toàn bộ danh sách hub theo tripId")
    @GetMapping("/danhsachtheotrip/{tripId}")
    public ResponseEntity<?> danhSachBenXeTheoId(@PathVariable("tripId") int tripId) {
        LOG.info("Lấy toàn bộ danh sách hub: ");
        List<TripStationDTO> tripStationDTOS = xeTuyenService.getTripStations(tripId);
        LOG.info("Đã lấy danh sách hub thành công");
        return new ResponseEntity<>(tripStationDTOS, HttpStatus.OK);
    }
}
