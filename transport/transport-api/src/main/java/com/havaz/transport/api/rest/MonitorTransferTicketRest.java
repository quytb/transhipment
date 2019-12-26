package com.havaz.transport.api.rest;

import com.havaz.transport.api.model.TcMonitorTransferTicketDTO;
import com.havaz.transport.api.service.TcMonitorTransferTicketService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/monitor")
public class MonitorTransferTicketRest {
    private static final Logger LOG = LoggerFactory.getLogger(MonitorTransferTicketRest.class);

    @Autowired
    private TcMonitorTransferTicketService tcMonitorTransferTicketService;

    @ApiOperation(value = "Lấy danh sách những trip có số vé trung chuyển lệch nhau giữa ERP và TC")
    @GetMapping(value = "/transferTicket/{ngayKiemTra}/{trangThai}")
    public ResponseEntity<?> getListTcMonitorTransferTicket(@ApiParam(value = "Ngày cần kiểm tra hoạt động transfer vé: yyyy-MM-dd") @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ngayKiemTra,
                                                            @ApiParam(value = "Trạng thái hoạt động transfer vé: 0 = Lệch vé; 1 = Không lệch; -1 = Tất cả") @PathVariable Integer trangThai) {
        LOG.info("Start lấy danh sách monitor transfer vé từ ERP -> TC: ");
//            List<TcMonitorTransferTicketDTO> tcMonitorTransferTicketDTOs = tcMonitorTransferTicketService.getListMonitorTransferTicket(ngayKiemTra);
        List<TcMonitorTransferTicketDTO> tcMonitorTransferTicketDTOs = tcMonitorTransferTicketService.findByNgayAndTrangThai(ngayKiemTra, trangThai);
        LOG.info("Lấy danh sách monitor transfer vé từ ERP -> TC thành công: ");
        return new ResponseEntity<>(tcMonitorTransferTicketDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/updateTransferTicket/{didId}")
    public void getProductList(@ApiParam(value = "Nhập vào did_id") @PathVariable String didId) {
        LOG.info("Start lấy danh sách monitor transfer vé từ ERP -> TC: ");
        tcMonitorTransferTicketService.updateTransferTicket(didId);
        LOG.info("Lấy danh sách monitor transfer vé từ ERP -> TC thành công: ");
    }
}
