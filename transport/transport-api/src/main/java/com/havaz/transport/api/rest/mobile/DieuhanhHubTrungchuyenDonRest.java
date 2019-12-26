package com.havaz.transport.api.rest.mobile;

import com.havaz.transport.api.body.request.cmdhub.GetCmdHubRequest;
import com.havaz.transport.api.body.request.tcve.GetTcVeDonRequest;
import com.havaz.transport.api.body.response.benxe.GetBenXeReponse;
import com.havaz.transport.api.body.response.tcve.GetTcVeDonResponse;
import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.common.LenhConstants;
import com.havaz.transport.api.common.ResultMessage;
import com.havaz.transport.api.form.CmdHubToHubForm;
import com.havaz.transport.api.form.DieuDoForm;
import com.havaz.transport.api.form.LenhForm;
import com.havaz.transport.api.form.TicketForm;
import com.havaz.transport.api.model.NotActiveDTO;
import com.havaz.transport.api.model.TaiXeTcDTO;
import com.havaz.transport.api.model.XeTcDTO;
import com.havaz.transport.api.service.DieuDoService;
import com.havaz.transport.api.service.DieuhanhHubTrungchuyenDonService;
import com.havaz.transport.api.service.QuanLyLichTrucService;
import com.havaz.transport.api.service.TrungChuyenHubToHubService;
import com.havaz.transport.api.service.VeTcService;
import com.havaz.transport.dao.entity.TcLenhEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
public class DieuhanhHubTrungchuyenDonRest {

    @Autowired
    private DieuhanhHubTrungchuyenDonService dieuhanhHubTrungchuyenDonService;

    @Autowired
    private TrungChuyenHubToHubService hubToHubService;

    @Autowired
    private VeTcService veTcService;

    @Autowired
    private DieuDoService dieuDoService;

    @Autowired
    private QuanLyLichTrucService quanLyLichTrucService;


    @PostMapping(path = {"/dieu-hanh-hub/trung-chuyen-don/tim-kiem", "/v1/dieu-hanh-hub/trung-chuyen-don/tim-kiem"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> search(@RequestBody(required = false) GetTcVeDonRequest request, @PageableDefault Pageable pageable) {
        if (request == null) {
            request = new GetTcVeDonRequest();
        }
        Page<GetTcVeDonResponse> page = dieuhanhHubTrungchuyenDonService.search(request, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping(path = {"/dieu-hanh-hub/trung-chuyen-don/tao-lenh", "/v1/dieu-hanh-hub/trung-chuyen-don/tao-lenh"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TcLenhEntity> createCmdHubToHub(@Valid @RequestBody CmdHubToHubForm cmdHubToHubForm) {
        return new ResponseEntity<>(hubToHubService.createdCmdHubToHub(cmdHubToHubForm), HttpStatus.OK);
    }

    @PostMapping(path = {"/dieu-hanh-hub/trung-chuyen-don/cap-nhat/{lenhId}", "/v1/dieu-hanh-hub/trung-chuyen-don" +
            "/cap-nhat/{lenhId}"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultMessage> updateClientToCmd(@RequestBody @Valid CmdHubToHubForm cmdHubToHubForm,
                                                           @PathVariable("lenhId") Integer lenhId) {
        hubToHubService.updateClientToCmd(cmdHubToHubForm, lenhId);
        return new ResponseEntity<>(new ResultMessage("Success", "Thêm Vé Thành Công"), HttpStatus.OK);
    }

    @GetMapping(path = {"/dieu-hanh-hub/trung-chuyen-don/list-ticket/{cmdId}", "/v1/dieu-hanh-hub/trung-chuyen-don/list-ticket/{cmdId}"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllTicketByCommand(@PathVariable Integer cmdId) {
        List<GetTcVeDonResponse> getTcVeDonResponses = veTcService.findAllByCommand(cmdId);
        return new ResponseEntity<>(getTcVeDonResponses, HttpStatus.OK);
    }

    @PutMapping(path = {"/dieu-hanh-hub/trung-chuyen-don/remove-ticket", "/v1/dieu-hanh-hub/trung-chuyen-don/remove-ticket"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeTicketOfCommand(@RequestBody TicketForm ticketForm) {
        veTcService.removeTicketOfCommand(ticketForm.getBvvIds());
        return new ResponseEntity<>(new ResultMessage("Success", "Xóa vé thành công"), HttpStatus.OK);
    }

    @PostMapping(path = {"/dieu-hanh-hub/trung-chuyen-don/dieu-lenh", "/v1/dieu-hanh-hub/trung-chuyen-don/dieu-lenh"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> dieuLenhHub(@RequestBody GetCmdHubRequest getCmdHubRequest) {
        dieuDoService.dieuLenhHub(getCmdHubRequest);
        return new ResponseEntity<>(new ResultMessage("Success", "Điều lệnh thành công"), HttpStatus.OK);
    }

    @PostMapping(path = {"/dieu-hanh-hub/trung-chuyen-don/huy-lenh/{cmdId}", "/v1/dieu-hanh-hub/trung-chuyen-don/huy-lenh/{cmdId}"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> huyLenhHub(@PathVariable Integer cmdId) {
        dieuDoService.cancelLenhHub(cmdId);
        return new ResponseEntity<>(new ResultMessage("Success", "Hủy lệnh thành công"), HttpStatus.OK);
    }

    @GetMapping(path = {"/dieu-hanh-hub/trung-chuyen-don/danh-sach-hub", "/v1/dieu-hanh-hub/trung-chuyen-don/danh-sach-hub"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GetBenXeReponse>> getAllHub() {
        return new ResponseEntity<>(hubToHubService.findAllBenxe(), HttpStatus.OK);
    }

    @PostMapping(path = {"/dieu-hanh-hub/trung-chuyen-don/dieu-bo-sung", "/v1/dieu-hanh-hub/trung-chuyen-don/dieu-bo-sung"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> dieuBoSungDon(@RequestBody DieuDoForm dieuDoForm) {
        dieuDoForm.setAction(Constant.LUU_HOAC_DIEU_LENH_DA_CO);
        dieuDoService.dieuBoSung(dieuDoForm, LenhConstants.LENH_DON);
        return new ResponseEntity<>(new ResultMessage("success", "Đã điều bổ sung thành công"), HttpStatus.OK);
    }

    @GetMapping({"/dieu-hanh-hub/dslenhlaixe/{laiXeId}", "/v1/dieu-hanh-hub/dslenhlaixe/{laiXeId}"})
    public ResponseEntity<?> dsLaiXeVaLenhDon(@PathVariable("laiXeId") Integer laiXeId) {
        List<LenhForm> allCommand = dieuDoService.getAllLenhDaTao(laiXeId, LenhConstants.LENH_DON);
        return new ResponseEntity<>(allCommand, HttpStatus.OK);
    }

    @GetMapping(path = {"/dieu-hanh-hub/trung-chuyen-don/danh-sach-not", "/v1/dieu-hanh-hub/trung-chuyen-don/danh-sach-not"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NotActiveDTO>> getListNot(Integer gioxuatben) {
        return new ResponseEntity<>(hubToHubService.getNotActiveChieuA(gioxuatben), HttpStatus.OK);

    }

    @GetMapping(path = {"/dieu-hanh-hub/trung-chuyen-don/danh-sach-xe", "/v1/dieu-hanh-hub/trung-chuyen-don/danh-sach-xe"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> danhSachXe() {
        List<XeTcDTO> xeTcDTOS = quanLyLichTrucService
                .getDanhSachXeTrungChuyen(Constant.XE_TRUNG_CHUYEN, true);
        return new ResponseEntity<>(xeTcDTOS, HttpStatus.OK);
    }

    @GetMapping(path = {"/dieu-hanh-hub/trung-chuyen-don/danh-sach-lai-xe", "/v1/dieu-hanh-hub/trung-chuyen-don/danh-sach-lai-xe"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> danhSachTaiXe() {
        List<TaiXeTcDTO> taiXeTcDTOS = quanLyLichTrucService.getTaiXeTrungChuyen();
        return new ResponseEntity<>(taiXeTcDTOS, HttpStatus.OK);
    }

    @GetMapping(path = {"/dieu-hanh-hub/trung-chuyen-don/danh-sach-lai-xe-lenh", "/v1/dieu-hanh-hub/trung-chuyen-don/danh-sach-lai-xe-lenh"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> danhSachTaiXeLenh() {
        List<LenhForm> allCommand = dieuDoService.getAllLenh(LenhConstants.LENH_DON);
        return new ResponseEntity<>(allCommand, HttpStatus.OK);
    }

    @GetMapping(path = {"/dieu-hanh-hub/trung-chuyen-don/danh-sach-lai-xe-lenh-da-tao/{laiXeId}", "/v1/dieu-hanh-hub/trung-chuyen-don/danh-sach-lai-xe-lenh-da-tao/{laiXeId}"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> danhSachTaiXeLenh(@PathVariable("laiXeId") int laiXeId) {
        List<LenhForm> allCommand = dieuDoService.getAllLenhDaTao(laiXeId,LenhConstants.LENH_DON);
        return new ResponseEntity<>(allCommand, HttpStatus.OK);
    }

}
