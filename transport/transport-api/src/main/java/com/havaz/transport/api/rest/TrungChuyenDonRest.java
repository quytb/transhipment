package com.havaz.transport.api.rest;

import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.common.LenhConstants;
import com.havaz.transport.api.common.ResultMessage;
import com.havaz.transport.api.form.DieuDoForm;
import com.havaz.transport.api.model.BksDTO;
import com.havaz.transport.api.model.ChiTietLenhDTO;
import com.havaz.transport.api.model.DaDonDTO;
import com.havaz.transport.api.model.HuyDonDTO;
import com.havaz.transport.api.model.HuyLenhTcDTO;
import com.havaz.transport.api.model.KetThucLenhDTO;
import com.havaz.transport.api.model.KhachTrungChuyenDTO;
import com.havaz.transport.api.model.LenhContainGXBDTO;
import com.havaz.transport.api.model.LenhTcDTO;
import com.havaz.transport.api.model.LichSuTrungChuyenDTO;
import com.havaz.transport.api.model.ThoiGianDonDTO;
import com.havaz.transport.api.model.UpdateThuTuDonDTO;
import com.havaz.transport.api.model.XacNhanLenhDTO;
import com.havaz.transport.api.service.DieuDoService;
import com.havaz.transport.api.service.LenhService;
import com.havaz.transport.api.service.TrungChuyenDonService;
import com.havaz.transport.api.service.VeTcService;
import com.havaz.transport.api.service.XeService;
import com.havaz.transport.dao.entity.TcVeEntity;
import com.havaz.transport.dao.entity.XeEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/taixe")
public class TrungChuyenDonRest {
    private static final Logger log = LoggerFactory.getLogger(TrungChuyenDonRest.class);

    @Autowired
    private TrungChuyenDonService trungChuyenDonService;

    @Autowired
    private VeTcService veTcService;

    @Autowired
    private LenhService lenhService;

    @Autowired
    private XeService xeService;

    @Autowired
    private DieuDoService dieuDoService;

    //TODO API1: Lấy danh sách khách hàng cần đón ( Mobile gọi lần đầu thì lệnh = 1, gọi từ lần 2 trở đi thì lệnh = 2)
    @ApiOperation(value = "Danh sách khách hàng cần đón")
    @GetMapping("/danhsachkhachdon/{taiXeId}")
    public ResponseEntity<?> getDanhSachKhachDon(@ApiParam(value = "Id của lái xe trung chuyển") @PathVariable int taiXeId) {
        List<KhachTrungChuyenDTO> listKhachHangTC = new ArrayList<>();
        listKhachHangTC = lenhService.getListKhachTrungChuyen(taiXeId);
        return new ResponseEntity<>(listKhachHangTC, HttpStatus.OK);
    }

    //TODO API1: Lấy danh sách khách hàng cần đón cập nhật real time thời gian đón
    @ApiOperation(value = "Danh sách khách hàng cần đón cập nhật real time thời gian đón")
    @GetMapping("/danhsachkhachdon/{lenhId}/{lat}/{lng}")
    public ResponseEntity<?> getDanhSachKhachDonUpdate(@ApiParam(value = "Id của lệnh") @PathVariable("lenhId") int lenhId,
                                                       @ApiParam(value = "Tọa độ lattitude của lái xe trung chuyển") @PathVariable("lat") double latitude,
                                                       @ApiParam(value = "Tọa độ longitude của lái xe trung chuyển") @PathVariable("lng") double longitude) {
        List<KhachTrungChuyenDTO> listKhachHangTCUpdate = new ArrayList<>();
        listKhachHangTCUpdate = lenhService.getListKhachTrungChuyenUpdate(latitude,longitude,lenhId);
        return new ResponseEntity<>(listKhachHangTCUpdate, HttpStatus.OK);
    }

    //TODO API3: Khi tài xế nhận lệnh thì gọi API này, API này cập nhật lại trạng thái của lệnh/ trạng thái-thông tin của khách hàng(table TC_Ve)
    @ApiOperation(value = "Xác nhận lệnh")
    @PostMapping("/xacnhanlenh")
    public ResponseEntity<ResultMessage> xacNhanLenh(@ApiParam(value = "Gửi thông tin lệnh/ vé để cập nhật lại", required = true) @RequestBody XacNhanLenhDTO requestXacNhanLenh) {
        boolean isXacNhanLenh = lenhService.xacNhanLenh(requestXacNhanLenh);
        if (isXacNhanLenh) {
            return new ResponseEntity<>(new ResultMessage("success", "Đã nhận lệnh!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResultMessage("error", "Không có danh sách vé."), HttpStatus.OK);
    }

    //TODO API4: Tài xế muốn hủy lệnh với 1 số lý do như (Nghịch đường, xe hỏng, ốm đau, chưa sẵn sàng...)
    @ApiOperation(value = "Hủy lệnh")
    @PostMapping("/huylenh")
    public ResponseEntity<ResultMessage> huyLenh(@ApiParam(value = "Gửi thông tin lệnh để hủy", required = true) @RequestBody HuyLenhTcDTO requestCancelLenh) {
       boolean isHuyLenh = lenhService.huyLenh(requestCancelLenh);
       if (isHuyLenh) {
           return new ResponseEntity<>(new ResultMessage("success", "Đã hủy lệnh!"), HttpStatus.OK);
       }
       return new ResponseEntity<>(new ResultMessage("error", "Không có danh sách vé."), HttpStatus.BAD_REQUEST);
    }

    //TODO API5: Tài xế đã đón khách và đưa về bến thành công - muốn kết thúc hành trình đón khách
    @ApiOperation(value = "Kết thúc lệnh")
    @PostMapping("/ketthuclenh")
    public ResponseEntity<ResultMessage> ketThucLenh(@ApiParam(value = "Gửi thông tin lệnh/ tài xế để kết thúc lệnh") @RequestBody KetThucLenhDTO requestKetThucLenh) {
        List<TcVeEntity> listTcVeEntity = veTcService.getByTaiXeIdAndLenhId(requestKetThucLenh.getTaiXeId(), requestKetThucLenh.getLenhId());
        if (listTcVeEntity != null && listTcVeEntity.size() > Constant.ZERO) {
            log.error("Có vé còn khách chưa đón.");
            return new ResponseEntity<>(
                    new ResultMessage("error", "Không thể kết thúc lệnh do vẫn còn khách chưa đón."),
                    HttpStatus.BAD_REQUEST);
        }
        lenhService.ketThucLenh(requestKetThucLenh);
        return new ResponseEntity<>(new ResultMessage("success", "Lệnh đã hoàn tất!"), HttpStatus.OK);
    }

    //TODO API6: Hủy đón khách vì 1 số lý do như nghịch đường, không liên lạ được....
    @ApiOperation(value = "Hủy đón khách")
    @PostMapping("/huydon")
    public ResponseEntity<ResultMessage> huyDon(@ApiParam(value = "Gửi thông tin lệnh/tài xế/ lý do hủy để hủy đón") @RequestBody HuyDonDTO requestHuyDon) {
        List<Integer> listVeId = requestHuyDon.getListBvvId();
        if(listVeId!=null && listVeId.size()>0) {
            trungChuyenDonService.huyDonKhach(requestHuyDon);
            return new ResponseEntity<>(new ResultMessage("success", "Đã hủy đón khách!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResultMessage("error", "Không có danh sách vé."), HttpStatus.OK);
        }
    }

    //TODO API7: Tài xế đã đón khách lên xe....
    @ApiOperation(value = "Đã đón khách lên xe")
    @PostMapping("/donkhach")
    public ResponseEntity<ResultMessage> donKhach(@ApiParam(value = "Gửi thông tin vé để cập nhật trạng thái từ đang đón sang đã đón") @RequestBody DaDonDTO requestDonKhach) {
        List<Integer> listVeId = requestDonKhach.getListBvvId();
        if (listVeId != null && !listVeId.isEmpty()) {
            trungChuyenDonService.donKhach(requestDonKhach);
            return new ResponseEntity<>(new ResultMessage("success", "Đã đón khách!"),
                                        HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResultMessage("error", "Không có danh sách vé."),
                                        HttpStatus.OK);
        }
    }

    //TODO API8: Cập nhật thời gian đón khách (từng khách 1 khi tài xế đổi thông tin)
    @ApiOperation(value = "Thời gian ước tính sẽ đón khách")
    @PostMapping("/thoigiandon")
    public ResponseEntity<ResultMessage> thoiGianDon(@ApiParam(value = "Gửi thông tin thời gian để cập nhật lại vé") @RequestBody ThoiGianDonDTO sendThoiGianDon) {
        List<Integer> listVeId = sendThoiGianDon.getListBvvId();
        if (listVeId != null && !listVeId.isEmpty()) {
            trungChuyenDonService.thoiGianDon(sendThoiGianDon);
            trungChuyenDonService.sendInfoTrackingToMsgMQ(listVeId.get(0));
            return new ResponseEntity<>(new ResultMessage("success", "Đã cập nhật thời gian đón!"),
                                        HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResultMessage("error", "Không có danh sách vé."),
                                        HttpStatus.OK);
        }
    }

    //TODO API9: Lấy danh sách biển kiểm soát xe để tài xế chọn
    @ApiOperation(value = "Lấy danh sách biển kiểm soát xe")
    @GetMapping("/danhsachbks")
    public ResponseEntity<?> getDanhSachBks() {
        List<BksDTO> listBksDTO = new ArrayList<>();
        List<XeEntity> listXe = xeService.getByXeTrungTam(Constant.XE_TRUNG_CHUYEN, true);
        if (listXe != null && !listXe.isEmpty()) {
            for (XeEntity xe : listXe) {
                BksDTO bksDTO = new BksDTO();
                bksDTO.setBks(xe.getXeBienKiemSoat());
                bksDTO.setXeId(xe.getXeId());
                listBksDTO.add(bksDTO);
            }
            return new ResponseEntity<>(listBksDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(listBksDTO, HttpStatus.OK);
        }
    }

    //TODO API Bổ sung: Lấy danh sách khách hàng cần đã đón và sắp xếp theo biển số xe tuyến
    @ApiOperation(value = "Danh sách khách hàng đã đón")
    @GetMapping("/khachdadon/{taiXeId}")
    public ResponseEntity<?> getKhachDaDon(@ApiParam(value = "Id của lái xe trung chuyển") @PathVariable int taiXeId) {
        List<KhachTrungChuyenDTO> listKhachHangTC = new ArrayList<>();
        listKhachHangTC = lenhService.getKhachDaDon(taiXeId);
        return new ResponseEntity<>(listKhachHangTC, HttpStatus.OK);
    }

    //TODO API Bổ sung: Lấy lịch sử trung chuyển của tài xế theo tháng
    @ApiOperation(value = "Lịch sử trung chuyển của tài xế trong tháng")
    @GetMapping("/lichsutrungchuyen/{taiXeId}/{requestDate}")
    public ResponseEntity<?> getHistoryByTaiXeId(@ApiParam(value = "Id của lái xe trung chuyển") @PathVariable int taiXeId,
                                                 @ApiParam(value = "Cú pháp: dd-MM-yyyy") @PathVariable String requestDate)  {
        List<LichSuTrungChuyenDTO> historyList = new ArrayList<>();
        historyList = lenhService.getHistoryByTaiXeId(taiXeId, requestDate);
        return new ResponseEntity<>(historyList, HttpStatus.OK);
    }

    //TODO API Bổ sung: Cập nhật thứ tự đón sau khi xác nhận lệnh
    @ApiOperation(value = "Cập nhật thứ tự đón sau khi xác nhận lệnh")
    @PostMapping("/thutudon")
    public ResponseEntity<ResultMessage> updateThuTuDon(@ApiParam(value = "Gửi danh sách vé để cập nhật lại thứ tự đón", required = true) @RequestBody UpdateThuTuDonDTO requestUpdateTtdDto) {
        boolean isUpdated = lenhService.updateThuTuDon(requestUpdateTtdDto);
        if (isUpdated) {
            return new ResponseEntity<>(new ResultMessage("success", "Đã cập nhật thành công thứ tự đón"),
                                        HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResultMessage("error", "Không có danh sách vé."), HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy thông tin chi tiết của 1 lệnh")
    @GetMapping("/chitiet/{tcLenhId}")
    public ResponseEntity chiTietLenh(@ApiParam(value = "Id của lệnh") @PathVariable int tcLenhId) {
        ChiTietLenhDTO chiTietLenhDTO = lenhService.getLenhById(tcLenhId);
        return new ResponseEntity<>(chiTietLenhDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy tất cả lệnh của tài xế theo trạng thái đã điều và đang chạy")
    @GetMapping("/dsLenh/{taiXeId}/{type}")
    public ResponseEntity<?> getDsLenh(@ApiParam(value = "Id của lái xe", required = true) @PathVariable int taiXeId,
                                       @PathVariable int type) {
        List<LenhTcDTO> lenhTcDTOs = lenhService.getListLenhByLaiXe(taiXeId, type);
        return new ResponseEntity<>(lenhTcDTOs, HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy tất cả lệnh của tài xế theo trạng thái đã điều và đang chạy update")
    @GetMapping("/dsLenh/v2/{taiXeId}/{type}")
    public ResponseEntity<?> getDsLenhUpdate(@ApiParam(value = "Id của lái xe", required = true) @PathVariable int taiXeId,
                                             @PathVariable int type) {
        List<LenhContainGXBDTO> listLenhByLaiXeupdate = lenhService.getListLenhByLaiXeupdate(taiXeId, type);
        return new ResponseEntity<>(listLenhByLaiXeupdate, HttpStatus.OK);
    }

    @ApiOperation(value = "Hủy lệnh cho lái xe tuyến đi đón")
    @GetMapping("/tcd/huylenhlaixetuyen")
    public ResponseEntity<?> huyLenhDonChoLaiXeTuyen(@RequestParam("taiXeId") int taiXeId,
                                                     @RequestParam("bvvId") int bvvId) {
        dieuDoService.huyLenhChoLaiXeTuyen(taiXeId,bvvId, LenhConstants.LENH_DON);
        return new ResponseEntity<>(
                new ResultMessage("success", "Đã hủy lệnh cho lái xe tuyến thành công"),
                HttpStatus.OK);
    }

    @ApiOperation(value = "Confirm Cmd Additional")
    @PostMapping("/tcd/cmd-additional/confirm")
    public ResponseEntity<?> confirmCmdAdditional(@Valid @RequestBody DieuDoForm dieuDoForm, BindingResult result) {
        dieuDoForm.setAction(Constant.LUU_HOAC_DIEU_LENH_DA_CO);
        dieuDoForm.validate(dieuDoForm, result);
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        dieuDoService.dieuBoSung(dieuDoForm, LenhConstants.LENH_DON);
        return new ResponseEntity<>(new ResultMessage("success", "Đã điều bổ sung thành công"), HttpStatus.OK);
    }
}
