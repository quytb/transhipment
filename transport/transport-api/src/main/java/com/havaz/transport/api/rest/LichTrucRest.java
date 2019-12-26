package com.havaz.transport.api.rest;

import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.common.ResultMessage;
import com.havaz.transport.api.configuration.MessageConfig;
import com.havaz.transport.api.form.LichTrucCNForm;
import com.havaz.transport.api.form.LichTrucForm;
import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.form.TaiXeChiNhanhForm;
import com.havaz.transport.api.model.DanhSachLichDTO;
import com.havaz.transport.api.model.TaiXeTcDTO;
import com.havaz.transport.api.model.TaoLichDTO;
import com.havaz.transport.api.model.UpdateLichDTO;
import com.havaz.transport.api.model.XeTcDTO;
import com.havaz.transport.api.service.QuanLyLichTrucService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/lichtruc")
public class LichTrucRest {

    private static final Logger LOG = LoggerFactory.getLogger(LichTrucRest.class);

    @Autowired
    private QuanLyLichTrucService quanLyLichTrucService;

    @Autowired
    private MessageConfig messageConfig;

    //Tạo lịch trực
    @ApiOperation(value = "Tạo lịch trực")
    @PostMapping("/taolich")
    public ResponseEntity<ResultMessage> taoLich(@Valid @ApiParam(value = "Thông tin để tạo lịch trực") @RequestBody List<TaoLichDTO> requestLichTcDTOS,
                                                 BindingResult result) {
        LOG.info("Tạo lịch trực cho lái xe: ");
        if (requestLichTcDTOS == null || requestLichTcDTOS.isEmpty()) {
            return new ResponseEntity<>(new ResultMessage("error", "Không có thông tin tạo lịch trực"), HttpStatus.BAD_REQUEST);
        }
        quanLyLichTrucService.taoMoiLichTruc(requestLichTcDTOS);
        LOG.info("Tạo lịch trực thành công");
        return new ResponseEntity<>(new ResultMessage("success", messageConfig.getMessage("SUCCESS.003")),
                                    HttpStatus.OK);
    }

    //Chỉnh sửa lịch trực
    @ApiOperation(value = "Chỉnh sửa lịch trực")
    @PostMapping("/sualich")
    public ResponseEntity<ResultMessage> suaLich(@ApiParam(value = "Thông tin lịch trực thay đổi") @RequestBody List<UpdateLichDTO> requestUpdateLichDTOS) {
        LOG.info("Sửa lịch trực cho tài xế: " + requestUpdateLichDTOS.get(0).getTaiXeId());
        if (requestUpdateLichDTOS.isEmpty()) {
            return new ResponseEntity<>(new ResultMessage("error", "Không có thông tin tạo lịch trực"),
                                        HttpStatus.BAD_REQUEST);
        }
        quanLyLichTrucService.updateLichTruc(requestUpdateLichDTOS);
        LOG.info("Sửa lịch trực thành công");
        return new ResponseEntity<>(new ResultMessage("success", messageConfig.getMessage("SUCCESS.004")),
                                    HttpStatus.OK);
    }

    //Tạo mới ca trực
    @ApiOperation(value = "Xóa lịch trực")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> xoaCa(@PathVariable("id") int id) {
        LOG.info("Xóa lịch trực với mã ca trực: {}", id);
        quanLyLichTrucService.xoaCaTruc(id);
        return new ResponseEntity<>(new ResultMessage("success", messageConfig.getMessage("SUCCESS.001")),
                                    HttpStatus.OK);
    }


    //Lấy toàn bộ danh sách ca trực
    @ApiOperation(value = "Lấy toàn bộ danh sách xe trung chuyển sẵn sàng")
    @GetMapping("/danhsachxe")
    public ResponseEntity<?> danhSachXe() {
        List<XeTcDTO> xeTcDTOS = quanLyLichTrucService
                .getDanhSachXeTrungChuyen(Constant.XE_TRUNG_CHUYEN, true);
        return new ResponseEntity<>(xeTcDTOS, HttpStatus.OK);
    }

    //Lấy toàn bộ danh sách tài xế xe trung chuyển sẵn sàng
    @ApiOperation(value = "Lấy toàn bộ danh sách tài xế xe trung chuyển sẵn sàng")
    @GetMapping("/danhsachtaixe")
    public ResponseEntity<?> danhSachTaiXe() {
        List<TaiXeTcDTO> taiXeTcDTOS = quanLyLichTrucService.getTaiXeTrungChuyen();
        return new ResponseEntity<>(taiXeTcDTOS, HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy toàn bộ danh sách tài xế xe trung chuyển sẵn sàng theo chi nhánh")
    @PostMapping("/danhsachtaixe/chinhanh")
    public ResponseEntity<List<TaiXeTcDTO>> danhSachTaiXeTheoChiNhanh(@RequestBody TaiXeChiNhanhForm taiXeChiNhanhForm) {
        List<TaiXeTcDTO> taiXeTcDTOS;
        if (CollectionUtils.isEmpty(taiXeChiNhanhForm.getChiNhanh())) {
            taiXeTcDTOS = quanLyLichTrucService.getTaiXeTrungChuyen();
        } else {
            taiXeTcDTOS = quanLyLichTrucService.getTaiXeTrungChuyen(taiXeChiNhanhForm.getChiNhanh());
        }
        return new ResponseEntity<>(taiXeTcDTOS, HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy toàn bộ danh sách tài xế xe trung chuyển sẵn sàng theo vùng hoạt động")
    @GetMapping("/danhsachtaixe/vunghoatdong/{vung}")
    public ResponseEntity<List<TaiXeTcDTO>> danhSachTaiXeTheoVungHoatDong(@PathVariable("vung") int vungId) {
        List<TaiXeTcDTO> taiXeTcDTOS;
        if (vungId > 0) {
            taiXeTcDTOS = quanLyLichTrucService.getTaiXeTrungChuyenTheoVung(vungId);
        } else {
            taiXeTcDTOS = quanLyLichTrucService.getTaiXeTrungChuyen();
        }
        return new ResponseEntity<>(taiXeTcDTOS, HttpStatus.OK);
    }


    //Lấy danh sách lịch trực trong khoảng ngày
    @ApiOperation(value = "Lấy danh sách lịch trực trong khoảng ngày")
    @PostMapping("/danhsachlichtruc")
    public ResponseEntity<?> danhSachLichTrucByNgayTruc(@ApiParam(value = "Tìm kiếm lịch trực trong khoảng startDate(yyyy-MM-dd) và endDate(yyyy-MM-dd), thông tin phân trang") @RequestBody LichTrucForm lichTrucForm) {
        LocalDate startDate = lichTrucForm.getStartDate();
        LocalDate endDate = lichTrucForm.getEndDate();
        LOG.debug("Lấy danh sách lịch trực từ ngày: {} đến ngày: {}", startDate, endDate);
        PageCustom<DanhSachLichDTO> dtoPage = quanLyLichTrucService.getLichTrucByNgayTruc(lichTrucForm);
        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    //Lấy danh sách lịch trực theo chi nhánh trong khoảng ngày
    @ApiOperation(value = "Lấy danh sách lịch trực theo chi nhánh trong khoảng ngày")
    @PostMapping("/dslichtrucchinhanh")
    public ResponseEntity<?> danhSachLichTrucByNgayTrucAndChiNhanh(@ApiParam(value = "Tìm kiếm lịch trực trong khoảng startDate(yyyy-MM-dd) và endDate(yyyy-MM-dd), thông tin phân trang") @RequestBody LichTrucCNForm lichTrucForm) {
        LocalDate startDate = lichTrucForm.getStartDate();
        LocalDate endDate = lichTrucForm.getEndDate();
        LOG.info("Lấy danh sách lịch trực từ ngày: " + startDate + " đến ngày: " + endDate);
        PageCustom<DanhSachLichDTO> dtoPage = quanLyLichTrucService.getLichTrucByNgayTrucAndChiNhanh(lichTrucForm);
        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

}
