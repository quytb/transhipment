package com.havaz.transport.api.rest;

import com.havaz.transport.api.common.LenhConstants;
import com.havaz.transport.api.common.ResultMessage;
import com.havaz.transport.api.model.DaTraDTO;
import com.havaz.transport.api.model.HuyDonDTO;
import com.havaz.transport.api.model.HuyLenhTcDTO;
import com.havaz.transport.api.model.KetThucLenhDTO;
import com.havaz.transport.api.model.KhachTrungChuyenTraDTO;
import com.havaz.transport.api.model.ThoiGianTraDTO;
import com.havaz.transport.api.model.UpdateThuTuTraDTO;
import com.havaz.transport.api.model.XacNhanLenhTraDTO;
import com.havaz.transport.api.service.DieuDoService;
import com.havaz.transport.api.service.LenhService;
import com.havaz.transport.api.service.TrungChuyenTraService;
import com.havaz.transport.api.service.VeTcService;
import com.havaz.transport.dao.entity.TcVeEntity;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/taixe")
public class TrungChuyenTraRest {
    private static final Logger log = LoggerFactory.getLogger(TrungChuyenTraRest.class);

    @Autowired
    private TrungChuyenTraService trungChuyenTraService;

    @Autowired
    private VeTcService veTcService;

    @Autowired
    private LenhService lenhService;

    @Autowired
    private DieuDoService dieuDoService;


    /*
     * //TRUNG CHUYỂN TRẢ - VŨNG TÀU 30-05-2019
     * API1: Danh sách khách hàng trả
     * API3: Xác nhận lệnh trả
     * API4: Hủy lệnh trả
     * API5: Kết thúc lệnh
     * API6: Hủy trả khách
     * API7: Trả khách xuống xe
     * API8: Thời gian ước tính sẽ trả khách
     *
     * */

    //TODO API1: Danh sách khách hàng trả
    @ApiOperation(value = "Danh sách khách hàng cần trả")
    @GetMapping("/tct/danhsachkhachtra/{taiXeId}")
    public ResponseEntity<List<KhachTrungChuyenTraDTO>> getDanhSachKhachTra(@ApiParam(value = "Id của lái xe trung chuyển trả") @PathVariable int taiXeId) {
        List<KhachTrungChuyenTraDTO> listKhachHangTC = trungChuyenTraService.getListKhachTrungChuyenTra(taiXeId);
        return new ResponseEntity<>(listKhachHangTC, HttpStatus.OK);
    }

    //TODO API1: Danh sách khách hàng trả
    @ApiOperation(value = "Danh sách khách hàng cần trả update real time thoi gian tra ")
    @GetMapping("/tct/danhsachkhachtra/{lenhId}/{lat}/{lng}")
    public ResponseEntity<List<KhachTrungChuyenTraDTO>> getDanhSachKhachTraUpdate(@ApiParam(value = "Id của lệnh") @PathVariable int lenhId,
                                                                                  @ApiParam(value = "Tọa độ lattitude của lái xe trung chuyển") @PathVariable("lat") double latitude,
                                                                                  @ApiParam(value = "Tọa độ longitude của lái xe trung chuyển") @PathVariable("lng") double longitude) {
        List<KhachTrungChuyenTraDTO> listKhachHangTC = new ArrayList<>();
        listKhachHangTC = trungChuyenTraService.getListKhachTrungChuyenTraUpdate(latitude,longitude, lenhId);
        return new ResponseEntity<>(listKhachHangTC, HttpStatus.OK);
    }

    //TODO API3: Khi tài xế nhận lệnh trả thì gọi API này, API này cập nhật lại trạng thái của lệnh/ trạng thái-thông tin của khách hàng(table TC_Ve)
    @ApiOperation(value = "Xác nhận lệnh trả")
    @PostMapping("/tct/xacnhanlenhtra")
    public ResponseEntity<ResultMessage> xacNhanLenhTra(@ApiParam(value = "Gửi thông tin lệnh/ vé để cập nhật lại", required = true) @RequestBody XacNhanLenhTraDTO requestXacNhanLenhTra) {
        boolean isXacNhanLenhTra = trungChuyenTraService.xacNhanLenhTra(requestXacNhanLenhTra);
        if (isXacNhanLenhTra) {
            return new ResponseEntity<>(new ResultMessage("success", "Đã nhận lệnh trả!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResultMessage("error", "Không có danh sách vé."), HttpStatus.OK);
    }

    //TODO API4: Tài xế muốn hủy lệnh với 1 số lý do như (Nghịch đường, xe hỏng, ốm đau, chưa sẵn sàng...)
    @ApiOperation(value = "Hủy lệnh trả")
    @PostMapping("/tct/huylenhtra")

    public ResponseEntity<ResultMessage> huyLenhTra(@ApiParam(value = "Gửi thông tin lệnh để hủy", required = true) @RequestBody HuyLenhTcDTO requestCancelLenh) {
        boolean isHuyLenh = trungChuyenTraService.huyLenhTra(requestCancelLenh);
        if (isHuyLenh) {
            return new ResponseEntity<>(new ResultMessage("success", "Đã hủy lệnh trả!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResultMessage("error", "Không có danh sách vé."), HttpStatus.OK);
    }

    //TODO API5: Kết thúc hành trình - sau khi trả khách
    @ApiOperation(value = "Kết thúc lệnh")
    @PostMapping("/tct/ketthuclenhtra")
    public ResponseEntity<ResultMessage> ketThucLenhTra(@ApiParam(value = "Gửi thông tin lệnh/ tài xế để kết thúc lệnh") @RequestBody KetThucLenhDTO requestKetThucLenh) {
        if (requestKetThucLenh.getLenhId() <= 0 || requestKetThucLenh.getTaiXeId() <= 0) {
            return new ResponseEntity<>(new ResultMessage("error", "Tham số không hợp lệ."), HttpStatus.BAD_REQUEST);
        }
        List<TcVeEntity> listTcVeEntity = veTcService.getVeByTaiXeIdAndLenhId(requestKetThucLenh.getTaiXeId(), requestKetThucLenh.getLenhId());
        if (listTcVeEntity != null && !listTcVeEntity.isEmpty()) {
            return new ResponseEntity<>(
                    new ResultMessage("error", "Không thể kết thúc lệnh do vẫn còn khách chưa trả."),
                    HttpStatus.BAD_REQUEST);
        }
        lenhService.ketThucLenh(requestKetThucLenh);
        return new ResponseEntity<>(new ResultMessage("success", "Lệnh trả đã hoàn tất!"), HttpStatus.OK);
    }

    //TODO API6: Hủy trả khách vì 1 số lý do như nghịch đường, không liên lạ được....
    @ApiOperation(value = "Hủy trả khách")
    @PostMapping("/tct/huytra")
    public ResponseEntity<ResultMessage> huyTra(@ApiParam(value = "Gửi thông tin lệnh/tài xế/ lý do hủy để hủy đón") @RequestBody HuyDonDTO requestHuyTra) {
        List<Integer> listVeId = requestHuyTra.getListBvvId();
        if (listVeId == null || listVeId.isEmpty()) {
            return new ResponseEntity<>(new ResultMessage("error", "Không có danh sách vé."), HttpStatus.OK);
        } else {
            boolean huyTra = trungChuyenTraService.huyTraKhach(requestHuyTra);
            if (huyTra) {
                return new ResponseEntity<>(new ResultMessage("success", "Đã hủy trả khách!"),
                                            HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResultMessage("error", "Không có danh sách vé."),
                                            HttpStatus.OK);
            }
        }
    }

    //TODO API7: Tài xế đã trả khách xuống xe....
    @ApiOperation(value = "Trả khách xuống xe")
    @PostMapping("/tct/trakhach")
    public ResponseEntity<ResultMessage> traKhach(@ApiParam(value = "Gửi thông tin vé để cập nhật trạng thái từ đang đi trả sang đã trả") @RequestBody DaTraDTO requestTraKhach) {
        List<Integer> listVeId = requestTraKhach.getListBvvId();
        if (listVeId != null && listVeId.size() > 0) {
            trungChuyenTraService.traKhach(requestTraKhach);
            return new ResponseEntity<>(new ResultMessage("success", "Đã trả khách!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResultMessage("error", "Không có danh sách vé."), HttpStatus.OK);
        }
    }

    //TODO API8: Cập nhật thời gian đón khách (từng khách 1 khi tài xế đổi thông tin)
    @ApiOperation(value = "Thời gian ước tính sẽ trả khách")
    @PostMapping("/tct/thoigiantra")
    public ResponseEntity<ResultMessage> thoiGianTra(@ApiParam(value = "Gửi thông tin thời gian để cập nhật lại vé") @RequestBody ThoiGianTraDTO sendThoiGianTra) {
//            if (sendThoiGianTra.getThoiGianTra() <= 0) {
//                if (sendThoiGianTra.getThoiGianTra() <= 0) {
//                    return new ResponseEntity<ResultMessage>(new ResultMessage("error", "Vui lòng điền thời gian trả khách!"), HttpStatus.BAD_REQUEST);
//                }
//            }
        List<Integer> listVeId = sendThoiGianTra.getListBvvId();
        if (listVeId != null && !listVeId.isEmpty()) {
            trungChuyenTraService.thoiGianTra(sendThoiGianTra);
            return new ResponseEntity<>(new ResultMessage("success", "Đã cập nhật thời gian trả khách!"),
                                        HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResultMessage("error", "Không có danh sách vé."), HttpStatus.OK);
        }
    }

    //TODO API Bổ sung: Cập nhật thứ tự trả sau khi xác nhận lệnh
    @ApiOperation(value = "Cập nhật thứ tự trả sau khi xác nhận lệnh")
    @PostMapping("/thututra")
    public ResponseEntity<ResultMessage> updateThuTuTra(@ApiParam(value = "Gửi danh sách vé để cập nhật lại thứ tự trả", required = true) @RequestBody UpdateThuTuTraDTO requestUpdateTttDto) {
        boolean isUpdated = lenhService.updateThuTuTra(requestUpdateTttDto);
        if (isUpdated) {
            return new ResponseEntity<>(new ResultMessage("success", "Đã cập nhật thành công thứ tự trả"),
                                        HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResultMessage("error", "Không có danh sách vé."), HttpStatus.OK);
    }

    @ApiOperation(value = "Hủy lệnh cho lái xe tuyến đi trả")
    @GetMapping("/tct/huylenhlaixetuyen")
    public ResponseEntity<?> huyLenhTraChoLaiXeTuyen(@RequestParam("taiXeId") int taiXeId, @RequestParam("bvvId") int bvvId) {
        dieuDoService.huyLenhChoLaiXeTuyen(taiXeId,bvvId, LenhConstants.LENH_TRA);
        return new ResponseEntity<>(new ResultMessage("success", "Đã hủy lệnh cho lái xe tuyến thành công"), HttpStatus.OK);
    }
}
