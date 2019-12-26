package com.havaz.transport.api.rest;

import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.common.LenhConstants;
import com.havaz.transport.api.common.ResultMessage;
import com.havaz.transport.api.form.DieuDoForm;
import com.havaz.transport.api.form.LenhForm;
import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.model.ChiTietLenhDTO;
import com.havaz.transport.api.model.DieuDoKhachTcDonDTO;
import com.havaz.transport.api.model.DieuDoKhachTcTraDTO;
import com.havaz.transport.api.model.HuyLenhTcDTO;
import com.havaz.transport.api.model.NotActiveDTO;
import com.havaz.transport.api.model.ThongTinNodeDto;
import com.havaz.transport.api.model.TimKhachDTO;
import com.havaz.transport.api.model.TuyenDTO;
import com.havaz.transport.api.model.UpdateHubDonDTO;
import com.havaz.transport.api.model.XeTcDTO;
import com.havaz.transport.api.service.DieuDoService;
import com.havaz.transport.api.service.LenhService;
import com.havaz.transport.api.service.TTTaiXeService;
import com.havaz.transport.api.service.TrungChuyenDonService;
import com.havaz.transport.core.exception.TransportException;
import com.havaz.transport.dao.entity.TTTaiXe;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
@RequestMapping("/dieudo")
public class DieuDoRest {

    private static final Logger log = LoggerFactory.getLogger(DieuDoRest.class);

    @Autowired
    private DieuDoService dieuDoService;

    @Autowired
    private LenhService lenhService;

    @Autowired
    private TTTaiXeService ttTaiXeService;

    @Autowired
    private TrungChuyenDonService trungChuyenDonService;

    //Lấy danh sách tuyến hoạt động
    @ApiOperation(value = "Lấy danh sách các tuyến đang hoạt động ")
    @GetMapping("/dstuyen")
    public ResponseEntity<?> dsTuyen() {
        List<TuyenDTO> tuyenDTOS = dieuDoService.getListTuyen();
        return new ResponseEntity<>(tuyenDTOS, HttpStatus.OK);

    }

    //Lấy danh sách khách trung chuyển đón
    @ApiOperation(value = "Lấy danh sách khách trung chuyển đón")
    @PostMapping("/tcd/timkiem")
    public ResponseEntity<?> getDanhSachKhachTrungChuyenDon(@ApiParam(value = "Thông tin tìm kiếm khách trung chuyển đón") @RequestBody TimKhachDTO timKhachDTO) {
        PageCustom<DieuDoKhachTcDonDTO> danhSachKhachTrungChuyenDon = dieuDoService.getDanhSachKhachTrungChuyenDon(timKhachDTO);
        return new ResponseEntity<>(danhSachKhachTrungChuyenDon, HttpStatus.OK);
    }

    //Lấy danh sách khách trung chuyển trả
    @ApiOperation(value = "Lấy danh sách khách trung chuyển trả")
    @PostMapping("/tct/timkiem")
    public ResponseEntity<?> getDanhSachKhachTrungChuyenTra(@ApiParam(value = "Thông tin tìm kiếm khách trung chuyển trả") @RequestBody TimKhachDTO timKhachDTO) {
        PageCustom<DieuDoKhachTcTraDTO> danhSachKhachTrungChuyenTra = dieuDoService.getDanhSachKhachTrungChuyenTra(timKhachDTO);
        return new ResponseEntity<>(danhSachKhachTrungChuyenTra, HttpStatus.OK);
    }


    @ApiOperation(value = "Lấy danh sách lệnh của lái xe")
    @GetMapping("/tcd/dslenhlaixe")
    public ResponseEntity<?> dsLaiXeVaLenhDon() {
        List<LenhForm> allCommand = dieuDoService.getAllLenh(LenhConstants.LENH_DON);
        return new ResponseEntity<>(allCommand, HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy danh sách lệnh và vé trong lệnh đang tạo nhưng ko có lái xe")
    @GetMapping("/tcd/dslenhlaixedatao/{laiXeId}")
    public ResponseEntity<?> dsLenhDonChuaCoLaiXe(@PathVariable("laiXeId") Integer laiXeId) {
        List<LenhForm> allCommand = dieuDoService.getAllLenhDaTao(laiXeId,LenhConstants.LENH_DON);
        return new ResponseEntity<>(allCommand, HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy danh sách lệnh của lái xe")
    @GetMapping("/tcd/dskhachdangdon/{lenhId}")
    public ResponseEntity<?> dsKhachDangDon(@PathVariable int lenhId) {
        ChiTietLenhDTO lenhById = lenhService.getLenhById(lenhId);
        return new ResponseEntity<>(lenhById, HttpStatus.OK);
    }

    @ApiOperation(value = "Điều lệnh cho lái xe")
    @PostMapping("/tcd/dieulenh")
    public ResponseEntity<?> dieuLenhDon(@Valid @RequestBody DieuDoForm dieuDoForm, BindingResult result) {
        dieuDoForm.setAction(Constant.TAO_HOAC_DIEU_LENH_MOI);
        dieuDoForm.validate(dieuDoForm, result);
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        dieuDoService.taoLenh(dieuDoForm, LenhConstants.LENH_DON, LenhConstants.LENH_STATUS_DA_DIEU);
        return new ResponseEntity<>(new ResultMessage("success", "Đã điều lệnh thành công"), HttpStatus.OK);
    }

    @ApiOperation(value = "Điều lệnh cho lái xe tuyến đi đón")
    @PostMapping("/tcd/dieulenhlaixetuyen")
    public ResponseEntity<?> dieuLenhDonChoLaiXeTuyen(@Valid @RequestBody DieuDoForm dieuDoForm, BindingResult result) {
        dieuDoForm.setAction(Constant.TAO_HOAC_DIEU_LENH_MOI);
        dieuDoService.taoLenhChoLaiXeTuyen(dieuDoForm, LenhConstants.LENH_DON, LenhConstants.LENH_STATUS_DA_DIEU);
        return new ResponseEntity<>(new ResultMessage("success", "Đã điều lệnh cho lái xe tuyến thành công"), HttpStatus.OK);
    }

    @ApiOperation(value = "Điều lệnh cho lái xe ngoài (grab, ...) đi đón")
    @PostMapping("/tcd/dieulenhngoai")
    public ResponseEntity<?> dieuLenhDonChoLaiXeNgoai(@Valid @RequestBody DieuDoForm dieuDoForm, BindingResult result) {
        try {
            dieuDoForm.setAction(Constant.TAO_HOAC_DIEU_LENH_MOI);
            dieuDoService.taoLenhChoLaiXeNgoai(dieuDoForm, LenhConstants.LENH_DON, LenhConstants.LENH_STATUS_DA_DIEU);
            return new ResponseEntity<>(new ResultMessage("success", "Đã điều lệnh cho lái xe tuyến thành công"), HttpStatus.OK);
        } catch (TransportException e) {
            return new ResponseEntity<>(new ResultMessage("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Lưu lệnh cho lái xe")
    @PostMapping("/tcd/luu")
    public ResponseEntity<?> luuLenhDon(@Valid @RequestBody DieuDoForm dieuDoForm, BindingResult result) {
        dieuDoForm.setAction(Constant.TAO_HOAC_DIEU_LENH_MOI);
        dieuDoForm.validate(dieuDoForm, result);
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        dieuDoService.taoLenh(dieuDoForm, LenhConstants.LENH_DON, LenhConstants.LENH_STATUS_DA_TAO);
        return new ResponseEntity<>(new ResultMessage("success", "Đã lưu lệnh thành công"), HttpStatus.OK);
    }

    @ApiOperation(value = "Điều bổ sung cho lái xe")
    @PostMapping("/tcd/dieubosung")
    public ResponseEntity<?> dieuBoSungDon(@Valid @RequestBody DieuDoForm dieuDoForm, BindingResult result) {
        dieuDoForm.setAction(Constant.LUU_HOAC_DIEU_LENH_DA_CO);
        dieuDoForm.validate(dieuDoForm, result);
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        dieuDoService.dieuBoSung(dieuDoForm, LenhConstants.LENH_DON);
        return new ResponseEntity<>(new ResultMessage("success", "Đã điều bổ sung thành công"), HttpStatus.OK);
    }

    // ############################ TRUNG CHUYEN TRA #########################################
    // API cho trung chuyen tra
    @ApiOperation(value = "Điều lệnh cho lái xe")
    @PostMapping("/tct/dieulenh")
    public ResponseEntity<?> dieuLenhTra(@Valid @RequestBody DieuDoForm dieuDoForm, BindingResult result) {
        dieuDoForm.setAction(Constant.TAO_HOAC_DIEU_LENH_MOI);
        dieuDoForm.validate(dieuDoForm, result);
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        dieuDoService.taoLenh(dieuDoForm, LenhConstants.LENH_TRA, LenhConstants.LENH_STATUS_DA_DIEU);
        return new ResponseEntity<>(new ResultMessage("success", "Đã điều lệnh thành công"), HttpStatus.OK);
    }

    @ApiOperation(value = "Điều lệnh cho lái xe tuyến đi trả")
    @PostMapping("/tct/dieulenhlaixetuyen")
    public ResponseEntity<?> dieuLenhTraChoLaiXeTuyen(@Valid @RequestBody DieuDoForm dieuDoForm, BindingResult result) {
        dieuDoForm.setAction(Constant.TAO_HOAC_DIEU_LENH_MOI);
        dieuDoService.taoLenhChoLaiXeTuyen(dieuDoForm, LenhConstants.LENH_TRA, LenhConstants.LENH_STATUS_DA_DIEU);
        return new ResponseEntity<>(
                new ResultMessage("success", "Đã điều lệnh cho lái xe tuyến thành công"),
                HttpStatus.OK);
    }

    @ApiOperation(value = "Điều lệnh cho lái xe ngoài (grab, ...) đi trả")
    @PostMapping("/tct/dieulenhngoai")
    public ResponseEntity<?> dieuLenhTraChoLaiXeNgoai(@Valid @RequestBody DieuDoForm dieuDoForm, BindingResult result) {
        dieuDoForm.setAction(Constant.TAO_HOAC_DIEU_LENH_MOI);
        dieuDoService.taoLenhChoLaiXeNgoai(dieuDoForm, LenhConstants.LENH_TRA, LenhConstants.LENH_STATUS_DA_DIEU);
        return new ResponseEntity<>(new ResultMessage("success", "Đã điều lệnh cho lái xe ngoài thành công"), HttpStatus.OK);
    }

    @ApiOperation(value = "Lưu lệnh cho lái xe")
    @PostMapping("/tct/luu")
    public ResponseEntity<?> luuLenhTra(@Valid @RequestBody DieuDoForm dieuDoForm, BindingResult result) {
        dieuDoForm.setAction(Constant.TAO_HOAC_DIEU_LENH_MOI);
        dieuDoForm.validate(dieuDoForm, result);
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        dieuDoService.taoLenh(dieuDoForm, LenhConstants.LENH_TRA, LenhConstants.LENH_STATUS_DA_TAO);
        return new ResponseEntity<>(new ResultMessage("success", "Đã lưu lệnh thành công"), HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy danh sách lệnh của lái xe")
    @GetMapping("/tct/dslenhlaixe")
    public ResponseEntity<?> dsLaiXeVaLenhTra() {
        List<LenhForm> allCommand = dieuDoService.getAllLenh(LenhConstants.LENH_TRA);
        return new ResponseEntity<>(allCommand, HttpStatus.OK);
    }

    //Lấy danh sách nốt tuyến có trạng thái là hoạt động
    @ApiOperation(value = "Lấy danh sách nốt có trạng thái là đang hoạt động ")
    @PostMapping("/dsnottuyen")
    public ResponseEntity<?> dsNotTuyen(@ApiParam(value = "") @RequestBody ThongTinNodeDto thongTinNodeDto) {
        log.debug("Danh sách các tham số truyền vào: {}", thongTinNodeDto);
        List<NotActiveDTO> notActivedDto = dieuDoService.getListNotActive(thongTinNodeDto);
        return new ResponseEntity<>(notActivedDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Điều bổ sung cho lái xe")
    @PostMapping("/tct/dieubosung")
    public ResponseEntity<?> dieuBoSungTra(@Valid @RequestBody DieuDoForm dieuDoForm, BindingResult result) {
        dieuDoForm.setAction(Constant.LUU_HOAC_DIEU_LENH_DA_CO);
        dieuDoForm.validate(dieuDoForm, result);
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        dieuDoService.dieuBoSung(dieuDoForm, LenhConstants.LENH_TRA);
        return new ResponseEntity<>(new ResultMessage("success", "Đã điều bổ sung thành công"), HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy danh sách khách của lệnh")
    @GetMapping("/tcd/dskhachdangtra/{lenhId}")
    public ResponseEntity<?> dsKhachDangTra(@PathVariable int lenhId) {
        ChiTietLenhDTO lenhById = lenhService.getLenhById(lenhId);
        return new ResponseEntity<>(lenhById, HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy trạng thái của lái xe cho điều hành")
    @GetMapping("/gettrangthai/{taiXeId}/{date}")
    public ResponseEntity getTT(@ApiParam(value = "Lái xe id ", required = true) @PathVariable int taiXeId,
                                @ApiParam(value = "Ngày tìm kiếm", required = true) @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        TTTaiXe ttTaiXe = ttTaiXeService.getTTTaiXeByTaiXeIdForDieuHanh(taiXeId, date);
        if (ttTaiXe != null) {
            if (ttTaiXe.getTrangThai() == 1) {
                return new ResponseEntity<>("Tài xế này đang sẵn sàng nhận lệnh.", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ResultMessage("error",
                                                      "Tài xế đang có trạng thái chưa sẵn sàng nhận lệnh, bạn có chắc muốn điều lệnh?"),
                                    HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Lấy trạng thái của lái xe cho điều hành")
    @GetMapping("/xetrungchuyen/{taiXeId}/{date}")
    public ResponseEntity<?> getXeTc(@ApiParam(value = "Lái xe id ", required = true) @PathVariable int taiXeId,
                                @ApiParam(value = "Ngày tìm kiếm", required = true) @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        XeTcDTO xeTcDTO = dieuDoService.getAttributeXe(taiXeId,date);
        if (xeTcDTO != null) {
                return new ResponseEntity<XeTcDTO>(xeTcDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResultMessage("error",
                    "Không có thông tin về xe trung chuyển cho tài xế: "+ taiXeId),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Lấy trạng thái của xe cho điều hành v2")
    @GetMapping("/xetrungchuyen/v2/{xeId}")
    public ResponseEntity<?> getXeTc(@ApiParam(value = "xe id ", required = true) @PathVariable int xeId) {

        XeTcDTO xeTcDTO = dieuDoService.getAttributeXe(xeId);
        if (xeTcDTO != null) {
            return new ResponseEntity<>(xeTcDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResultMessage("error",
                    "Không có thông tin về xe trung chuyển cho xe : "+ xeId),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/huylenhdadieu")
    public ResponseEntity huyLenh(@RequestBody HuyLenhTcDTO huyLenhDTO) {
        if (dieuDoService.huyLenhDadieu(huyLenhDTO)) {
            return new ResponseEntity<>(new ResultMessage("success", "Đã hủy lệnh thành công"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResultMessage("error", "Đã hủy lệnh với khách chưa đón"), HttpStatus.OK);
        }
    }


    //TODO API Bổ sung: Cập nhật hub đón
    @ApiOperation(value = "Cập nhật hub đón")
    @PostMapping("/updatehub/tcd")
    public ResponseEntity<ResultMessage> updatehubdon(@ApiParam(value = "Gửi danh sách vé để cập nhật lại hub đón", required = true) @RequestBody UpdateHubDonDTO updateHubDonDTO) {
        trungChuyenDonService.updateHubDon(updateHubDonDTO);
        return new ResponseEntity<>(new ResultMessage("success", "Đã cập nhật thành công hub"), HttpStatus.OK);
    }

    //TODO API Bổ sung: Cập nhật hub trả
    @ApiOperation(value = "Cập nhật hub trả")
    @PostMapping("/updatehub/tct")
    public ResponseEntity<ResultMessage> updateHubTra(@ApiParam(value = "Gửi danh sách vé để cập nhật lại hub trả", required = true) @RequestBody UpdateHubDonDTO updateHubDonDTO) {
        trungChuyenDonService.updateHubTra(updateHubDonDTO);
        return new ResponseEntity<>(new ResultMessage("success", "Đã cập nhật thành công hub"), HttpStatus.OK);
    }
//
//    //Lấy danh sách vùng trung chuyển
//    @ApiOperation(value = "Lấy danh sách vùng trung chuyển ")
//    @GetMapping("/vungtrungchuyen/list")
//    public ResponseEntity<?> getListVungTrungChuyen() {
//        List<TcVttDTO> tcVttDTOS = new ArrayList<TcVttDTO>();
//        try {
//            LOG.info("Lấy danh sách vùng trung chuyển: ");
//            tcVttDTOS = dieuDoService.getListVungTrungChuyen();
//
//            LOG.info("Đã lấy danh sách vùng trung chuyển thành công");
//            return new ResponseEntity<List<TcVttDTO>>(tcVttDTOS, HttpStatus.OK);
//        } catch (TransportException e) {
//            LOG.error("Lấy danh sách tuyến active bị lỗi " + e.getMessage());
//            return new ResponseEntity<ResultMessage>(new ResultMessage("error", messageConfig.getMessage("ERROR.003")), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @ApiOperation(value = "Tạo mới vùng trung chuyển")
//    @PostMapping("/vungtrungchuyen/add")
//    public ResponseEntity<?> addVungTrungChuyen(@ApiParam(value = "Thông tin tạo mới vùng trung chuyển") @RequestBody VungTcDTO vungTcDTO) {
//
//        try {
//            LOG.info("Start tạo mới vùng trung chuyển: ");
//            dieuDoService.addTransportArea(vungTcDTO);
//
//            LOG.info("Tạo mới vùng trung chuyển thành công");
//            return new ResponseEntity<ResultMessage>(new ResultMessage("success", "Tạo mới vùng trung chuyển thành công"), HttpStatus.OK);
//        } catch (TransportException e) {
//            LOG.error("Tạo mới vùng trung chuyển bị lỗi " + e.getMessage());
//            return new ResponseEntity<ResultMessage>(new ResultMessage("error", messageConfig.getMessage("ERROR.003")), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @ApiOperation(value = "Upadate vùng trung chuyển")
//    @PostMapping("/vungtrungchuyen/update")
//    public ResponseEntity<?> updateVungTrungChuyen(@ApiParam(value = "Thông tin tạo update vùng trung chuyển") @RequestBody UpdateVungTcDTO updateVungTcDTO) {
//
//        try {
//            LOG.info("Start update vùng trung chuyển: ");
//            dieuDoService.updateTransportArea(updateVungTcDTO);
//
//            LOG.info("Update vùng trung chuyển thành công");
//            return new ResponseEntity<ResultMessage>(new ResultMessage("success", "Update vùng trung chuyển thành công"), HttpStatus.OK);
//        } catch (TransportException e) {
//            LOG.error("Update vùng trung chuyển bị lỗi " + e.getMessage());
//            return new ResponseEntity<ResultMessage>(new ResultMessage("error", messageConfig.getMessage("ERROR.003")), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @ApiOperation(value = "Thay đổi trạng thái vùng trung chuyển ")
//    @GetMapping("/vungtrungchuyen/switchstatus/{tcVttId}/{status}")
//    public ResponseEntity<?> changeStatusVungTrunChuyen(@ApiParam(value = "Id vùng trung chuyển") @PathVariable int tcVttId,
//                                                        @ApiParam(value = "Trạng thái vùng trung chuyển") @PathVariable int status) {
//        List<TcVttDTO> tcVttDTOS = new ArrayList<TcVttDTO>();
//        try {
//            LOG.info("Start thay đổi trạng thái vùng trung chuyển: ");
//            dieuDoService.switchStatusTransportArea(tcVttId,status);
//
//            LOG.info("Thay đổi trạng thái vùng trung chuyển thành công");
//            return new ResponseEntity<ResultMessage>(new ResultMessage("success", "Thay đổi trạng thái vùng trung chuyển thành công"), HttpStatus.OK);
//        } catch (TransportException e) {
//            LOG.error("Thay đổi trạng thái vùng trung chuyển bị lỗi " + e.getMessage());
//            return new ResponseEntity<ResultMessage>(new ResultMessage("error", messageConfig.getMessage("ERROR.003")), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @ApiOperation(value = "Tìm kiếm vùng trung chuyển ")
//    @GetMapping("/vungtrungchuyen/search/{tenVung}")
//    public ResponseEntity<?> findVungTrunChuyen(@ApiParam(value = "Nhập vào tên vùng trung chuyển để tìm kiếm") @PathVariable String tenVung) {
//        List<TcVttDTO> tcVttDTOS = new ArrayList<TcVttDTO>();
//        try {
//            LOG.info("Start tìm kiếm vùng trung chuyển: ");
//            tcVttDTOS = dieuDoService.findVttByTenVung(tenVung);
//
//            LOG.info("Tìm kiếm vùng trung chuyển thành công");
//            return new ResponseEntity<List<TcVttDTO>>(tcVttDTOS, HttpStatus.OK);
//        } catch (TransportException e) {
//            LOG.error("Tìm kiếm vùng trung chuyển bị lỗi " + e.getMessage());
//            return new ResponseEntity<ResultMessage>(new ResultMessage("error", messageConfig.getMessage("ERROR.003")), HttpStatus.BAD_REQUEST);
//        }
//    }
}
