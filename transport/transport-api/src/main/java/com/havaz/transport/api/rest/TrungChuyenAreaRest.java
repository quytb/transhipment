package com.havaz.transport.api.rest;

import com.havaz.transport.api.form.VungTrungChuyenForm;
import com.havaz.transport.api.model.DistanceDTO;
import com.havaz.transport.api.model.TcDspVungTrungChuyenDTO;
import com.havaz.transport.api.service.VungTrungChuyenService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/vungtrungchuyen")
public class TrungChuyenAreaRest {

    private static final Logger log = LoggerFactory.getLogger(TrungChuyenAreaRest.class);

    @Autowired
    public VungTrungChuyenService vungTrungChuyenSv;

    @ApiOperation(value = "Khai báo vùng trung chuyển")
    @PostMapping("/add")
    public ResponseEntity add(@RequestParam("file") MultipartFile file,
                              @RequestParam("code") String code,
                              @RequestParam("averageSpeed") String averageSpeed,
                              @RequestParam("status") int status,
                              @RequestParam("ten") String ten,
                              @RequestParam(value = "note", required = false) String note,
                              @RequestParam(value = "confirmedTime", required = false) Integer confirmedTime) {
        log.info("Start tạo vùng trung chuyển");
        VungTrungChuyenForm vtt = new VungTrungChuyenForm();
        vtt.setCode(code);
        vtt.setFileContent(file);
        vtt.setStatus(status);
        vtt.setAverageSpeed(averageSpeed);
        vtt.setTen(ten);
        vtt.setNote(note);
        vtt.setConfirmedTime(confirmedTime);
        vungTrungChuyenSv.createOrUpdate(vtt);
        log.info("End tạo vùng trung chuyển");
        return new ResponseEntity<>("Thêm mới vùng trung chuyển thành công", HttpStatus.OK);
    }

    @ApiOperation(value = "Cập nhật vùng trung chuyển")
    @PostMapping("/update")
    public ResponseEntity update(@RequestParam("file") Optional<MultipartFile> file,
                                 @RequestParam("code") String code,
                                 @RequestParam("averageSpeed") String averageSpeed,
                                 @RequestParam("status") int status,
                                 @RequestParam("ten") String ten,
                                 @RequestParam("id") int id,
                                 @RequestParam(value = "note", required = false) String note,
                                 @RequestParam(value = "confirmedTime", required = false) Integer confirmedTime) {
        log.info("Start cập nhật vùng trung chuyển");
        log.info("Cập nhật vùng có ID:" + id);
        VungTrungChuyenForm vtt = new VungTrungChuyenForm();
        vtt.setCode(code);
        if (file.isPresent()) {
            vtt.setFileContent(file.get());
        } else {
            vtt.setFileContent(null);
        }

        vtt.setStatus(status);
        vtt.setAverageSpeed(averageSpeed);
        vtt.setId(id);
        vtt.setTen(ten);
        vtt.setNote(note);
        vtt.setConfirmedTime(confirmedTime);
        vungTrungChuyenSv.createOrUpdate(vtt);
        log.info("End cập nhật vùng trung chuyển");
        return new ResponseEntity<>("cập nhật vùng trung chuyển thành công", HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy danh sách vùng trung chuyển")
    @GetMapping("/list")
    public ResponseEntity listall(@RequestParam(value = "tenVung", defaultValue = "") String tenVung) {
        log.info("Start get all vùng trung chuyển");
        List<TcDspVungTrungChuyenDTO> listVung = new ArrayList<>();
        listVung = vungTrungChuyenSv.getDataByName(tenVung);
        log.info("END get all vùng trung chuyển");
        return new ResponseEntity<>(listVung, HttpStatus.OK);
    }

    @ApiOperation(value = "check exist point in polygon or not")
    @PostMapping("/checkExistsPoint")
    public ResponseEntity checkExistsPoint(@RequestParam("latX") Double latX, @RequestParam("longX") Double longX) {
        log.info("Start check exist point in polygon");
        int isExists =  vungTrungChuyenSv.checkExistsPoint(latX, longX);
        log.info("End check exist point in polygon");
        return new ResponseEntity<>(isExists, HttpStatus.OK);
    }

    @ApiOperation(value = "get the distance between 2 points")
    @PostMapping("/getDistance")
    public ResponseEntity getDistance(@RequestParam("latX") String latX, @RequestParam("longX") String longX,
                                      @RequestParam("latY") String latY, @RequestParam("longY") String longY) {
        log.info("Start get distance");
        DistanceDTO distance =  vungTrungChuyenSv.getDistance(latX, longX, latY, longY);
        log.info("End get distance");
        return new ResponseEntity<>(distance, HttpStatus.OK);
    }
}
