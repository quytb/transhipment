package com.havaz.transport.api.rest;

import com.havaz.transport.api.common.CacheData;
import com.havaz.transport.api.model.HavazDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/public")
public class PublicRest {

    private static final Logger LOG = LoggerFactory.getLogger(PublicRest.class);

    //Lấy thông tin token
    @ApiOperation(value = "Lấy thông tin token")
    @GetMapping("/getToken/{appId}")
    public ResponseEntity<HavazDTO> getToken(@ApiParam(value = "Thông tin appId") @PathVariable String appId) {
        LOG.info("Start lấy thông tin token appId: " + appId);
        HavazDTO havazDTO = new HavazDTO();
        havazDTO.setAppId(appId);
        havazDTO.setAppToken(CacheData.CONFIGURATION_DATA.get(appId));
        LOG.info("Kết thúc lấy thông tin token");
        return new ResponseEntity<>(havazDTO, HttpStatus.OK);
    }

}
