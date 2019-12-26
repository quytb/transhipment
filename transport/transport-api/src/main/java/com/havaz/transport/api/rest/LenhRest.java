package com.havaz.transport.api.rest;

import com.havaz.transport.api.common.ResultMessage;
import com.havaz.transport.api.form.FilterLenhForm;
import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.form.XoaLenhForm;
import com.havaz.transport.api.model.ChiTietLenhDTO;
import com.havaz.transport.api.model.LenhTcDTO;
import com.havaz.transport.api.model.LuuLenhDTO;
import com.havaz.transport.api.model.MaLenhDTO;
import com.havaz.transport.api.model.TrangThaiLenhDTO;
import com.havaz.transport.api.service.DieuDoService;
import com.havaz.transport.api.service.LenhService;
import io.swagger.annotations.ApiOperation;
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

import java.util.List;

@RestController
@RequestMapping(value = "/dieudo/lenh")
public class LenhRest {
    private static final Logger LOG = LoggerFactory.getLogger(CaTrucRest.class);

    @Autowired
    private LenhService lenhService;

    @Autowired
    private DieuDoService dieuDoService;

    @ApiOperation(value = "Lấy tất cả các lệnh theo filter")
    @PostMapping("/timkiem")
    public ResponseEntity timKiemLenh(@ApiParam(value = "Object tìm kiếm", required = true) @RequestBody FilterLenhForm filterLenhForm) {
        PageCustom<ChiTietLenhDTO> chiTietLenhDTOS = new PageCustom<>();
        LOG.info("Lấy các lệnh theo filter : ");
        chiTietLenhDTOS = lenhService.getAllLenhCustom(filterLenhForm);
        LOG.info("Đã lấy thông tin các lệnh thành công");
        return new ResponseEntity<>(chiTietLenhDTOS, HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy thông tin chi tiết của 1 lệnh")
    @GetMapping("/chitiet/{tcLenhId}")
    public ResponseEntity chiTietLenh(@ApiParam(value = "Id của lệnh") @PathVariable int tcLenhId) {
        ChiTietLenhDTO chiTietLenhDTO = new ChiTietLenhDTO();
        LOG.info("Lấy thông tin chi tiết lệnh: ");
        chiTietLenhDTO = lenhService.getLenhById(tcLenhId);
        LOG.info("Đã lấy thông tin chi tiết lệnh thành công");
        return new ResponseEntity<>(chiTietLenhDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "Lưu lệnh")
    @PostMapping("/luu")
    public ResponseEntity<ResultMessage> luuLenh(@ApiParam(value = "Gửi thông tin lệnh để lưu", required = true) @RequestBody LuuLenhDTO requestLuuLenh) {
        LOG.info("Lưu lại thông tin lệnh: ");
        boolean isLuulenh = lenhService.luuLenh(requestLuuLenh);
        if (isLuulenh) {
            return new ResponseEntity<>(new ResultMessage("success", "Đã lưu lệnh!"),
                                        HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResultMessage("error", "Không thể lưu lệnh."),
                                    HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Điều lệnh")
    @PostMapping("/dieu")
    public ResponseEntity<ResultMessage> dieuLenh(@ApiParam(value = "Gửi thông tin lệnh để điều", required = true) @RequestBody LuuLenhDTO requestDieuLenh) {
        LOG.info("Điều thông tin lệnh: ");
        boolean isDieulenh = lenhService.dieuLenh(requestDieuLenh);
        if (isDieulenh) {
            return new ResponseEntity<>(new ResultMessage("success", "Đã điều lệnh!"),
                                        HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResultMessage("error", "Không thể điều lệnh."),
                                    HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Lấy tất cả trạng thái lệnh")
    @GetMapping("/listlenhstatus")
    public ResponseEntity<List<TrangThaiLenhDTO>> getLenhStatus() {
        LOG.info("Lấy tất cả trạng thái lệnh: ");
        List<TrangThaiLenhDTO> list = lenhService.getAllTrangThaiLenh();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @ApiOperation(value = "Xóa lệnh đã tạo")
    @PostMapping("/xoalenh")
    public ResponseEntity<?> xoaLenh(@RequestBody XoaLenhForm xoaLenhForm) {
            if(xoaLenhForm.getLenhId()<=0){
                return new ResponseEntity<>(new ResultMessage("error", "Lệnh không tồn tại"),
                                            HttpStatus.BAD_REQUEST);
            }
            dieuDoService.xoaLenh(xoaLenhForm.getLenhId());
            return new ResponseEntity<>(new ResultMessage("success", "Xóa lệnh thành công"),
                                        HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy tất cả mã lệnh")
    @PostMapping("/malenh")
    public ResponseEntity<List<MaLenhDTO>> getMaLenh(@ApiParam(value = "Object tìm kiếm",
                                                               required = true) @RequestBody FilterLenhForm filterLenhForm) {
        List<MaLenhDTO> lst = lenhService.getMaLenh(filterLenhForm);
        return new ResponseEntity<>(lst, HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy tất cả lệnh của tài xế theo trạng thái đã điều và đang chạy")
    @GetMapping("/dsLenh/{taiXeId}")
    public ResponseEntity<?> getDsLenh(@ApiParam(value = "Id của lái xe", required = true) @PathVariable int taiXeId) {
        List<LenhTcDTO> lenhTcDTOs = lenhService.getListLenhByLaiXe(taiXeId);
        return new ResponseEntity<>(lenhTcDTOs, HttpStatus.OK);
    }
}
