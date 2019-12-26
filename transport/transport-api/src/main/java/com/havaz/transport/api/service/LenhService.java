package com.havaz.transport.api.service;

import com.havaz.transport.api.form.FilterLenhForm;
import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.model.*;

import java.util.List;

public interface LenhService {
    //Xác nhận lệnh đón - trả
    boolean xacNhanLenh(XacNhanLenhDTO requestXacNhanLenh);

    //Két thúc lệnh đón - trả
    void ketThucLenh(KetThucLenhDTO requestKetThucLenh);

    //Hủy lệnh đón - trả
    boolean huyLenh(HuyLenhTcDTO requestCancelLenh);

    //Danh sách khách trung chuyển đón - trả
    List<KhachTrungChuyenDTO> getListKhachTrungChuyen(int taiXeId);

    //Danh sách khách trung chuyển đón - trả update real time
    List<KhachTrungChuyenDTO> getListKhachTrungChuyenUpdate(double latitude, double longitude, int lenhId);

    //Danh sách khách trung chuyển - đã đón
    List<KhachTrungChuyenDTO> getKhachDaDon(int taiXeId);

    //Lịch sử trung chuyển
    List<LichSuTrungChuyenDTO> getHistoryByTaiXeId(int taiXeId, String requestDate);

    PageCustom<ChiTietLenhDTO> getAllLenhCustom(FilterLenhForm filterLenhForm);

    //Lấy tất cả mã lệnh theo filter
    List<MaLenhDTO> getMaLenh(FilterLenhForm filterLenhForm);

    ChiTietLenhDTO getLenhById(int lenhId);

    boolean luuLenh(LuuLenhDTO requestLuuLenhDTO);

    boolean dieuLenh(LuuLenhDTO requestDieuLenhDTO);

    List<TrangThaiLenhDTO> getAllTrangThaiLenh();

    //Danh sách các xe tuyến có trong lệnh
    TripTrungChuyenDTO getXeTuyenInfor(int lenhId);

    //Cập nhật lại thứ tự đón sau khi xác nhận lệnh
    boolean updateThuTuDon(UpdateThuTuDonDTO requestUpdateTtdDto);

    //Cập nhật lại thứ tự trả sau khi xác nhận lệnh
    boolean updateThuTuTra(UpdateThuTuTraDTO requestUpdateTttDto);

    //Lấy danh sách lệnh theo tài xế có trạng thái đã điều hoặc đang chạy
    List<LenhTcDTO> getListLenhByLaiXe(int laiXeId);

    void cancelPickup(CmdHavazNowDTO cmdHavazNowDTO);

    //Lấy danh sách lệnh theo tài xế có trạng thái đã điều hoặc đang chạy (đón/trả)
    List<LenhTcDTO> getListLenhByLaiXe(int laiXeId, int type);

    //Lấy danh sách lệnh theo tài xế có trạng thái đã điều hoặc đang chạy (đón/trả) update co them gio ve ben
    List<LenhContainGXBDTO> getListLenhByLaiXeupdate(int laiXeId, int type);

    void ketthucLenhHub(int lenhId);

}
