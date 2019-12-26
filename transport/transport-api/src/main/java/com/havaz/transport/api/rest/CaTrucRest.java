package com.havaz.transport.api.rest;

import com.havaz.transport.api.common.ResultMessage;
import com.havaz.transport.api.configuration.MessageConfig;
import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.form.PagingForm;
import com.havaz.transport.api.model.CaPagingDTO;
import com.havaz.transport.api.model.CaTcDTO;
import com.havaz.transport.api.model.DanhSachCaDTO;
import com.havaz.transport.api.model.TaoCaDTO;
import com.havaz.transport.api.service.QuanLyLichTrucService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/catruc")
public class CaTrucRest {
    private static final Logger log = LoggerFactory.getLogger(CaTrucRest.class);

    @Autowired
    private QuanLyLichTrucService quanLyLichTrucService;

    @Autowired
    private MessageConfig messageConfig;

    // Tạo mới ca trực
    @ApiOperation(value = "Tạo mới ca trực")
    @PostMapping("/taomoi")
    public ResponseEntity<?> taoCa(@Validated @ApiParam(value = "Thông tin về ca trực để tạo mới ca trực") @RequestBody TaoCaDTO requestTaoCaDTO) {
        log.debug("Tạo ca trực mới mã ca trực: {}, tên ca trực: {}", requestTaoCaDTO.getMaCa(), requestTaoCaDTO.getTenCa());
        quanLyLichTrucService.taoMoiCaTruc(requestTaoCaDTO);
        return new ResponseEntity<>(new ResultMessage("success", messageConfig.getMessage("SUCCESS.001")),
                                    HttpStatus.OK);
    }

    //Lấy toàn bộ danh sách ca trực
    @ApiOperation(value = "Lấy toàn bộ danh sách ca trực")
    @PostMapping("/danhsach")
    public ResponseEntity<?> danhSachCa(@ApiParam(value = "Thông tin phân trang") @RequestBody PagingForm pagingForm) {
        PageCustom<DanhSachCaDTO> dtoPage = quanLyLichTrucService.getDanhSachCaTruc(pagingForm);
        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    //Lấy toàn bộ danh sách ca trực không phân trang
    @ApiOperation(value = "Lấy toàn bộ danh sách ca trực không sử dụng phân trang")
    @GetMapping("/allca")
    public ResponseEntity<List<DanhSachCaDTO>> allCa() {
        List<DanhSachCaDTO> danhSachCaDTOS = quanLyLichTrucService.getAllCaTruc();
        return new ResponseEntity<>(danhSachCaDTOS, HttpStatus.OK);
    }

    // Lấy danh sách ca trực active
    @ApiOperation(value = "Lấy danh sách ca trực active")
    @GetMapping("/activeca")
    public ResponseEntity<List<CaTcDTO>> activeCa() {
        List<CaTcDTO> caTcDTOS = quanLyLichTrucService.getActiveCa();
        return new ResponseEntity<>(caTcDTOS, HttpStatus.OK);
    }

    // Lấy thông tin chi tiết ca trực
    @ApiOperation(value = "Lấy thông tin chi tiết ca trực")
    @GetMapping("/{tcCaId}")
    public ResponseEntity<CaTcDTO> chiTietCa(@ApiParam(value = "Id của ca trực") @PathVariable int tcCaId) {
        CaTcDTO caTcDTO = quanLyLichTrucService.getCaTruc(tcCaId);
        return new ResponseEntity<>(caTcDTO, HttpStatus.OK);
    }

    // Tạo mới ca trực
    @ApiOperation(value = "Cập nhật ca trực")
    @PostMapping("/capnhat")
    public ResponseEntity<ResultMessage> capNhatCa(@ApiParam(value = "Thông tin thay đổi của ca trực") @RequestBody CaTcDTO requestCaTcDTO) {
        log.debug("Cập nhật ca trực mã ca trực: {}, tên ca trực: {}", requestCaTcDTO.getMaCa(), requestCaTcDTO.getTenCa());
        quanLyLichTrucService.updateCaTruc(requestCaTcDTO);
        return new ResponseEntity<>(new ResultMessage("success", messageConfig.getMessage("SUCCESS.002")),
                                    HttpStatus.OK);
    }

    //Thay đổi trạng thái ca trực
    @ApiOperation(value = "Thay đổi trạng thái ca trực")
    @GetMapping("/trangthaica/{tcCaId}/{active}")
    public ResponseEntity<CaTcDTO> changeStatusCa(@ApiParam(value = "Id của ca trực") @PathVariable int tcCaId,
                                                  @ApiParam(value = "Trạng thái của ca trực: 0- inactive; 1- active") @PathVariable int active) {
        CaTcDTO caTcDTO = quanLyLichTrucService.changeStatusCaTruc(tcCaId, active == 1);
        return new ResponseEntity<>(caTcDTO, HttpStatus.OK);
    }

    //Tìm kiếm ca trực
    @ApiOperation(value = "Tìm kiếm ca trực")
    @PostMapping("/timkiem")
    public ResponseEntity<?> findCa(@ApiParam(value = "Thông tin tìm ca trực của ca trực") @RequestBody CaPagingDTO caPagingDTO) {
        log.info("Start tìm ca trực: {}, tên ca trực: {}", caPagingDTO.getMaCa(), caPagingDTO.getTenCa());
        PageCustom<DanhSachCaDTO> dtoPage = quanLyLichTrucService.findCaTruc(caPagingDTO);
        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }
}
