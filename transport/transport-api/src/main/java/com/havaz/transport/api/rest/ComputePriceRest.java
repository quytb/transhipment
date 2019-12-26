package com.havaz.transport.api.rest;

import com.havaz.transport.api.common.ResultMessage;
import com.havaz.transport.api.form.PriceGroupForm;
import com.havaz.transport.api.form.PriceGroupSearchForm;
import com.havaz.transport.api.model.PriceDTO;
import com.havaz.transport.api.service.TcCtvPriceService;
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
@RequestMapping("/ctv-price")
public class ComputePriceRest {

    private static final Logger log = LoggerFactory.getLogger(ComputePriceRest.class);

    @Autowired
    private TcCtvPriceService priceService;

    @GetMapping("")
    public ResponseEntity getAllCtvPrice() {
        priceService.findAll();
        return new ResponseEntity<>(priceService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/delete-price/{priceStepId}")
    public ResponseEntity deletePrice(@ApiParam(value = "Id bước giá") @PathVariable Integer priceStepId) {
        priceService.deletePriceStep(priceStepId);
        return new ResponseEntity<>(new ResultMessage("success", "Xóa Thành công"), HttpStatus.OK);
    }

    @PostMapping("/create-update")
    public ResponseEntity saveOrUpdatePrice(@Valid @RequestBody PriceDTO priceDTO) {
        priceService.saveOrUpdatePriceCtv(priceDTO);
        return new ResponseEntity<>(new ResultMessage("success", "Thành công"), HttpStatus.OK);
    }

    @PostMapping("/partner/create-update")
    public ResponseEntity createOrUpdatePartner(@Valid @RequestBody PriceGroupForm priceGroupForm) {
        priceService.saveOrUpdateGroupPrice(priceGroupForm);
        return new ResponseEntity<>(new ResultMessage("success", "Thành công"), HttpStatus.OK);
    }

    @PostMapping("/partner/list")
    public ResponseEntity getListPartner(@RequestBody PriceGroupSearchForm searchForm) {
       return new ResponseEntity<>(priceService.getAllPriceGroup(searchForm) , HttpStatus.OK);
    }
}
