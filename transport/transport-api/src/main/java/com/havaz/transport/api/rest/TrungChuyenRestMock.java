package com.havaz.transport.api.rest;

import com.havaz.transport.api.model.DaDonDTO;
import com.havaz.transport.api.model.HuyDonDTO;
import com.havaz.transport.api.model.HuyLenhTcDTO;
import com.havaz.transport.api.model.KetThucLenhDTO;
import com.havaz.transport.api.model.KhachTrungChuyenDTO;
import com.havaz.transport.api.model.ThoiGianDonDTO;
import com.havaz.transport.api.model.TrungChuyenResultDTO;
import com.havaz.transport.api.model.XacNhanLenhDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TrungChuyenRestMock {

    private static int lenh_status = 1;
    private static int action = 0;


    //TODO API1: Lấy danh sách khách hàng cần đón ( Mobile gọi lần đầu thì lệnh = 1, gọi từ lần 2 trở đi thì lệnh = 2)
    @ApiOperation(value = "Danh sách khách hàng cần đón")
    @GetMapping("/danhsachkhachdon/{taiXeId}")
    public ResponseEntity<List<KhachTrungChuyenDTO>> getDanhSachKhachDon(@ApiParam(value = "Id của lái xe trung chuyển") @PathVariable int taiXeId) {
        List<KhachTrungChuyenDTO> listKhach = getKhachTrungChuyenDTOS();
        if(lenh_status == 1){
            lenh_status ++;
            return new ResponseEntity<>(listKhach, HttpStatus.OK);
        }else {
            if( action == 1) {
                List<KhachTrungChuyenDTO> list = listKhach;
                for (KhachTrungChuyenDTO kh : list) {
                    kh.setTrangThaiTrungChuyen(2);
                    kh.setLenhStatus(2);
                }
                return new ResponseEntity<>(list, HttpStatus.CREATED);
            }
            if( action == 2) {
                List<KhachTrungChuyenDTO> list1 = listKhach;
                for (KhachTrungChuyenDTO kh : list1) {
                    kh.setTrangThaiTrungChuyen(4);
                    kh.setLenhStatus(3);
                    kh.setTaiXeId(0);
                }
                return new ResponseEntity<>(list1, HttpStatus.CREATED);
            }
            if( action == 3) {
                List<KhachTrungChuyenDTO> list2 = listKhach;
                for (KhachTrungChuyenDTO kh : list2) {
                    kh.setTrangThaiTrungChuyen(3);
                    kh.setLenhStatus(4);
                }
                return new ResponseEntity<>(list2, HttpStatus.CREATED);
            }
            if( action == 4) {
                //=4 là trường hợp hủy đón - đang set cho phần tử cuối cùng
                List<KhachTrungChuyenDTO> list3 = listKhach;
                list3.get(list3.size()-1).setTrangThaiTrungChuyen(4);
                list3.get(list3.size()-1).setTaiXeId(0);
                return new ResponseEntity<>(list3, HttpStatus.CREATED);
                }
            }
            if( action == 5) {
                //=5 là trường hợp đón được khách - đang set cho phần tử cuối cùng
                List<KhachTrungChuyenDTO> list4 = listKhach;
                list4.get(list4.size()-1).setTrangThaiTrungChuyen(3);
                return new ResponseEntity<>(list4, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(getKhachTrungChuyenDTOS(), HttpStatus.CREATED);
    }

    //TODO API3: Khi tài xế nhận lệnh thì gọi API này, API này cập nhật lại trạng thái của lệnh/ trạng thái-thông tin của khách hàng(table TC_Ve)
    @ApiOperation(value = "Xác nhận lệnh")
    @PostMapping("/xacnhanlenh")
    @Transactional
    public ResponseEntity<String> xacNhanLenh(@ApiParam(value ="Gửi thông tin lệnh/ vé để cập nhật lại", required = true) @RequestBody XacNhanLenhDTO requestXacNhanLenh) {
        List<KhachTrungChuyenDTO> list = getKhachTrungChuyenDTOS();
        action = 1;
        return new ResponseEntity<>("Lệnh đã được xác nhận!", HttpStatus.CREATED);
    }

    //TODO API4: Tài xế muốn hủy lệnh với 1 số lý do như (Nghịch đường, xe hỏng, ốm đau, chưa sẵn sàng...)
    @ApiOperation(value = "Hủy lệnh")
    @PostMapping("/huylenh")
    public ResponseEntity<String> huyLenh(@ApiParam(value = "Gửi thông tin lệnh để hủy", required = true) @RequestBody HuyLenhTcDTO requestCancelLenh) {
        action = 2;
        return new ResponseEntity<>("Đã hủy lệnh!", HttpStatus.CREATED);
    }

    //TODO API5: Tài xế đã đón khách và đưa về bến thành công - muốn kết thúc hành trình đón khách
    @ApiOperation(value = "Kết thúc lệnh")
    @PostMapping("/ketthuclenh")
    public ResponseEntity<String> ketThucLenh(@ApiParam(value = "Gửi thông tin lệnh/ tài xế để kết thúc lệnh") @RequestBody KetThucLenhDTO requestKetThucLenh) {
        action = 3;
        return new ResponseEntity<>("Lệnh đã hoàn tất!", HttpStatus.CREATED);
    }

    //TODO API6: Hủy đón khách vì 1 số lý do như nghịch đường, không liên lạ được....
    @ApiOperation(value = "Hủy đón khách")
    @PostMapping("/huydon")
    public ResponseEntity<String> huyDon(@ApiParam(value ="Gửi thông tin lệnh/tài xế/ lý do hủy để hủy đón") @RequestBody HuyDonDTO requestHuyDon) {
        action = 4;
        return new ResponseEntity<>("Đã hủy đón khách!", HttpStatus.OK);
    }

    //TODO API7: Tài xế đã đón khách lên xe....
    @ApiOperation(value = "Đã đón khách lên xe")
    @PostMapping("/donkhach")
    public ResponseEntity<String> donKhach(@ApiParam(value = "Gửi thông tin vé để cập nhật trạng thái từ đang đón sang đã đón") @RequestBody DaDonDTO requestDonKhach) {

            return new ResponseEntity<>("Đã đón khách!", HttpStatus.OK);
    }

    //TODO API8: Cập nhật thời gian đón khách (từng khách 1 khi tài xế đổi thông tin)
    @ApiOperation(value = "Thời gian ước tính sẽ đón khách")
    @PostMapping("/thoigiandon")
    public ResponseEntity<String> thoiGianDon(@ApiParam(value = "Gửi thông tin thời gian để cập nhật lại vé") @RequestBody ThoiGianDonDTO sendThoiGianDon) {

            return new ResponseEntity<>("Đã cập nhật thời gian đón!", HttpStatus.OK);
    }

//    //TODO API9: Lấy danh sách biển kiểm soát xe để tài xế chọn
//    @ApiOperation(value = "Lấy danh sách biển kiểm soát xe")
//    @GetMapping("/danhsachbks")
//    public ResponseEntity<List<BksDTO>> getDanhSachBks() {
//
//        return new ResponseEntity<List<BksDTO>>(listBksDTO, HttpStatus.OK);
//    }
//
//    //TODO API10: Lấy danh sách trung chuyển theo trip Id
//    @ApiOperation(value = "Danh sách xe trung chuyển theo trip")
//    @GetMapping("/danhsachxetc/{tripId}")
//    public ResponseEntity<List<TrungChuyenResultDTO>> getDanhSachXeTc(@ApiParam(value = "tripID: Mã chuyến xe") @PathVariable int tripId) throws UnsupportedEncodingException {
//        return new ResponseEntity<List<TrungChuyenResultDTO>>(listTrip, HttpStatus.OK);
//    }

    //TODO CREATE MOCKUP DATA
    @ApiOperation(value = "Mockup list xe trung chuyen")
    @GetMapping("/mockupdanhsachtriptc/{tripId}")
    public ResponseEntity<List<TrungChuyenResultDTO>> mockUpDanhSachTripTc(@ApiParam(value = "tripID: Mã chuyến xe") @PathVariable int tripId) throws UnsupportedEncodingException {
        List<TrungChuyenResultDTO> listTrip = new ArrayList<>();
        List<String> listSdt1 = new ArrayList<>();
        listSdt1.add("0933555111");
        listSdt1.add("0918222112");
        listSdt1.add("0918222113");
        listTrip.add(new TrungChuyenResultDTO(null,null,"rqye8ydx_145786_991",listSdt1));
        List<String> listSdt2 = new ArrayList<>();
        listSdt2.add("0919566114");
        listSdt2.add("0919566115");
        listSdt2.add("0919566116");
        listTrip.add(new TrungChuyenResultDTO("29LD3331","0988123456","rqye8ydx_145786_992",listSdt2));
        List<String> listSdt3 = new ArrayList<>();
        listSdt3.add("0933998117");
        listSdt3.add("0933998118");
        listSdt3.add("0933998119");
        listTrip.add(new TrungChuyenResultDTO("29LD3332","0988123457","rqye8ydx_145786_993",listSdt3));
        List<String> listSdt4 = new ArrayList<>();
        listSdt4.add("0969118211");
        listSdt4.add("0969118212");
        listSdt4.add("0969118213");
        listTrip.add(new TrungChuyenResultDTO("29LD3333","0988123458","rqye8ydx_145786_994",listSdt4));
        List<String> listSdt5 = new ArrayList<>();
        listSdt5.add("0922555214");
        listSdt5.add("0922555215");
        listSdt5.add("0922555216");
        listTrip.add(new TrungChuyenResultDTO(null,"0988123459","rqye8ydx_145786_995",listSdt5));
        return new ResponseEntity<>(listTrip, HttpStatus.OK);
    }

    private List<KhachTrungChuyenDTO> getKhachTrungChuyenDTOS() {
        List<KhachTrungChuyenDTO> listKhachHang = new ArrayList<>();
        KhachTrungChuyenDTO khachTrungChuyenDTO = new KhachTrungChuyenDTO();
        khachTrungChuyenDTO.setKhachHangMoi(0);
        List<Integer> listVe = new ArrayList<>();
        int ve1 = 9901;
        int ve2 = 9902;
        int ve3 = 9903;
        listVe.add(ve1);
        listVe.add(ve2);
        listVe.add(ve3);
        khachTrungChuyenDTO.setVeId(listVe);
        khachTrungChuyenDTO.setTenKhachDi("Nguyễn Văn C");
        khachTrungChuyenDTO.setSdtKhachDi("0947087583");
        khachTrungChuyenDTO.setDiaChiDon("133 Kim Mã - Ba Đình");
        khachTrungChuyenDTO.setSoKhach("3");
        khachTrungChuyenDTO.setSoGhe("22,26,13");
        khachTrungChuyenDTO.setThuTuDon(1);
        khachTrungChuyenDTO.setThoiGianDon(1);
        khachTrungChuyenDTO.setTrangThaiTrungChuyen(1);
        khachTrungChuyenDTO.setLenhId(2);
        khachTrungChuyenDTO.setLenhStatus(1);
        khachTrungChuyenDTO.setTaiXeId(753);
        khachTrungChuyenDTO.setTenNhaXe("CÔNG TY LIÊN DOANH VẪN CHUYỂN QUỐC TẾ HẢI VÂN");
        khachTrungChuyenDTO.setTuyenDi("Mỹ Đình → Mường Lay");
        khachTrungChuyenDTO.setGioXuatBen("19:00");
        khachTrungChuyenDTO.setBienSoXe("30H1-21299");
        khachTrungChuyenDTO.setSdtXe("0919568885");
        listKhachHang.add(khachTrungChuyenDTO);

        KhachTrungChuyenDTO khachTrungChuyenDTO1 = new KhachTrungChuyenDTO();

        khachTrungChuyenDTO1.setKhachHangMoi(0);
        List<Integer> listVe1 = new ArrayList<>();
        int ve11 = 9904;
        listVe1.add(ve11);
        khachTrungChuyenDTO1.setVeId(listVe1);
        khachTrungChuyenDTO1.setTenKhachDi("Nguyễn Văn A");
        khachTrungChuyenDTO1.setSdtKhachDi("0947087584");
        khachTrungChuyenDTO1.setDiaChiDon("203 Nguyễn Trãi, Thanh Xuân, Hà Nội");
        khachTrungChuyenDTO1.setSoKhach("1");
        khachTrungChuyenDTO1.setSoGhe("30");
        khachTrungChuyenDTO1.setThuTuDon(1);
        khachTrungChuyenDTO1.setThoiGianDon(11);
        khachTrungChuyenDTO1.setTrangThaiTrungChuyen(1);
        khachTrungChuyenDTO1.setLenhId(2);
        khachTrungChuyenDTO1.setLenhStatus(1);
        khachTrungChuyenDTO1.setTaiXeId(753);
        khachTrungChuyenDTO1.setTenNhaXe("CÔNG TY LIÊN DOANH VẪN CHUYỂN QUỐC TẾ HẢI VÂN");
        khachTrungChuyenDTO1.setTuyenDi("Mỹ Đình → Mường Lay");
        khachTrungChuyenDTO1.setGioXuatBen("19:00");
        khachTrungChuyenDTO1.setBienSoXe("30H1-21299");
        khachTrungChuyenDTO1.setSdtXe("0919568885");
        listKhachHang.add(khachTrungChuyenDTO1);

        KhachTrungChuyenDTO khachTrungChuyenDTO2 = new KhachTrungChuyenDTO();
        khachTrungChuyenDTO2.setKhachHangMoi(0);
        List<Integer> listVe2 = new ArrayList<>();
        int ve1111 = 9905;
        listVe2.add(ve1111);
        khachTrungChuyenDTO2.setVeId(listVe2);
        khachTrungChuyenDTO2.setTenKhachDi("Trần B");
        khachTrungChuyenDTO2.setSdtKhachDi("0947087585");
        khachTrungChuyenDTO2.setDiaChiDon("125 Hoàng Ngân, Thanh Xuân, Hà Nội");
        khachTrungChuyenDTO2.setSoKhach("1");
        khachTrungChuyenDTO2.setSoGhe("31");
        khachTrungChuyenDTO2.setThuTuDon(1);
        khachTrungChuyenDTO2.setThoiGianDon(30);
        khachTrungChuyenDTO2.setTrangThaiTrungChuyen(1);
        khachTrungChuyenDTO2.setLenhId(2);
        khachTrungChuyenDTO2.setLenhStatus(1);
        khachTrungChuyenDTO2.setTaiXeId(753);
        khachTrungChuyenDTO2.setTenNhaXe("CÔNG TY LIÊN DOANH VẪN CHUYỂN QUỐC TẾ HẢI VÂN");
        khachTrungChuyenDTO2.setTuyenDi("Mỹ Đình → Mường Lay");
        khachTrungChuyenDTO2.setGioXuatBen("19:00");
        khachTrungChuyenDTO2.setBienSoXe("30H1-21299");
        khachTrungChuyenDTO2.setSdtXe("0919568885");
        listKhachHang.add(khachTrungChuyenDTO1);

        KhachTrungChuyenDTO khachTrungChuyenDTO3 = new KhachTrungChuyenDTO();
        khachTrungChuyenDTO3.setKhachHangMoi(0);
        List<Integer> listVe3 = new ArrayList<>();
        int ve11111 = 9906;
        listVe3.add(ve1111);
        khachTrungChuyenDTO3.setVeId(listVe3);
        khachTrungChuyenDTO3.setTenKhachDi("Lê Bình");
        khachTrungChuyenDTO3.setSdtKhachDi("0947087586");
        khachTrungChuyenDTO3.setDiaChiDon("331 Hoàng Đạo Thúy");
        khachTrungChuyenDTO3.setSoKhach("1");
        khachTrungChuyenDTO3.setSoGhe("32");
        khachTrungChuyenDTO3.setThuTuDon(2);
        khachTrungChuyenDTO3.setThoiGianDon(45);
        khachTrungChuyenDTO3.setTrangThaiTrungChuyen(1);
        khachTrungChuyenDTO3.setLenhId(2);
        khachTrungChuyenDTO3.setLenhStatus(1);
        khachTrungChuyenDTO3.setTaiXeId(753);
        khachTrungChuyenDTO3.setTenNhaXe("CÔNG TY LIÊN DOANH VẪN CHUYỂN QUỐC TẾ HẢI VÂN");
        khachTrungChuyenDTO3.setTuyenDi("Mỹ Đình → Mường Lay");
        khachTrungChuyenDTO3.setGioXuatBen("19:00");
        khachTrungChuyenDTO3.setBienSoXe("30H1-21299");
        khachTrungChuyenDTO3.setSdtXe("0919568885");
        listKhachHang.add(khachTrungChuyenDTO3);

        KhachTrungChuyenDTO khachTrungChuyenDTO4 = new KhachTrungChuyenDTO();
        khachTrungChuyenDTO4.setKhachHangMoi(0);
        List<Integer> listVe4 = new ArrayList<>();
        int ve101 = 9907;
        listVe4.add(ve101);
        khachTrungChuyenDTO4.setVeId(listVe4);
        khachTrungChuyenDTO4.setTenKhachDi("Lê Bình Minh");
        khachTrungChuyenDTO4.setSdtKhachDi("0947087587");
        khachTrungChuyenDTO4.setDiaChiDon("111 Doãn Kế Thiện, Cầu Giấy");
        khachTrungChuyenDTO4.setSoKhach("1");
        khachTrungChuyenDTO4.setSoGhe("33");
        khachTrungChuyenDTO4.setThuTuDon(2);
        khachTrungChuyenDTO4.setThoiGianDon(60);
        khachTrungChuyenDTO4.setTrangThaiTrungChuyen(1);
        khachTrungChuyenDTO4.setLenhId(2);
        khachTrungChuyenDTO4.setLenhStatus(1);
        khachTrungChuyenDTO4.setTaiXeId(753);
        khachTrungChuyenDTO4.setTenNhaXe("CÔNG TY LIÊN DOANH VẪN CHUYỂN QUỐC TẾ HẢI VÂN");
        khachTrungChuyenDTO4.setTuyenDi("Mỹ Đình → Mường Lay");
        khachTrungChuyenDTO4.setGioXuatBen("19:00");
        khachTrungChuyenDTO4.setBienSoXe("30H1-21299");
        khachTrungChuyenDTO4.setSdtXe("0919568885");
        listKhachHang.add(khachTrungChuyenDTO4);

        KhachTrungChuyenDTO khachTrungChuyenDTO5 = new KhachTrungChuyenDTO();
        khachTrungChuyenDTO5.setKhachHangMoi(0);
        List<Integer> listVe5 = new ArrayList<>();
        int ve102 = 9908;
        listVe5.add(ve102);
        khachTrungChuyenDTO5.setVeId(listVe5);
        khachTrungChuyenDTO5.setTenKhachDi("Trần Quang Vinh");
        khachTrungChuyenDTO5.setSdtKhachDi("0947087588");
        khachTrungChuyenDTO5.setDiaChiDon("666 Xuân Thủy, Cầu Giấy");
        khachTrungChuyenDTO5.setSoKhach("1");
        khachTrungChuyenDTO5.setSoGhe("34");
        khachTrungChuyenDTO5.setThuTuDon(2);
        khachTrungChuyenDTO5.setThoiGianDon(60);
        khachTrungChuyenDTO5.setTrangThaiTrungChuyen(1);
        khachTrungChuyenDTO5.setLenhId(2);
        khachTrungChuyenDTO5.setLenhStatus(1);
        khachTrungChuyenDTO5.setTaiXeId(753);
        khachTrungChuyenDTO5.setTenNhaXe("CÔNG TY LIÊN DOANH VẪN CHUYỂN QUỐC TẾ HẢI VÂN");
        khachTrungChuyenDTO5.setTuyenDi("Mỹ Đình → Mường Lay");
        khachTrungChuyenDTO5.setGioXuatBen("19:00");
        khachTrungChuyenDTO5.setBienSoXe("30H1-21299");
        khachTrungChuyenDTO5.setSdtXe("0919568885");
        listKhachHang.add(khachTrungChuyenDTO5);
        return listKhachHang;
    }
}