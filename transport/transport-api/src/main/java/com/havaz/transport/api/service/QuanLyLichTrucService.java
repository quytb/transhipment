package com.havaz.transport.api.service;

import com.havaz.transport.api.form.*;
import com.havaz.transport.api.model.CaPagingDTO;
import com.havaz.transport.api.model.CaTcDTO;
import com.havaz.transport.api.model.ChamCongDTO;
import com.havaz.transport.api.model.DanhSachCaDTO;
import com.havaz.transport.api.model.DanhSachLichDTO;
import com.havaz.transport.api.model.LuuChamCongDTO;
import com.havaz.transport.api.model.TaiXeTcDTO;
import com.havaz.transport.api.model.TaoCaDTO;
import com.havaz.transport.api.model.TaoLichDTO;
import com.havaz.transport.api.model.UpdateLichDTO;
import com.havaz.transport.api.model.XeTcDTO;

import java.time.LocalDate;
import java.util.List;

public interface QuanLyLichTrucService {
    //1. API QUẢN LÝ CA TRỰC

    // Tạo mới ca trực
    void taoMoiCaTruc(TaoCaDTO taoCaDTO);

    // Lấy toàn bộ danh sách ca trực
    PageCustom<DanhSachCaDTO> getDanhSachCaTruc(PagingForm pagingForm);

    List<DanhSachCaDTO> getAllCaTruc();

    // Lấy danh sách ca trực active
    List<CaTcDTO> getActiveCa();

    // Lấy thông tin chi tiết của một ca trực
    CaTcDTO getCaTruc(int tcCaId);

    // Cập nhật thông tin ca trực
    void updateCaTruc(CaTcDTO caTcDTO);

    // Thay đổi trạng thái ca trực
    CaTcDTO changeStatusCaTruc(int tcCaId, boolean active);

    // Tìm kiếm ca trực
    PageCustom<DanhSachCaDTO> findCaTruc(CaPagingDTO caPagingDTO);

    Boolean checkCaUnique(String maCa, Float gioBatDau, Float gioKetThuc);

    //    2. API QUẢN LÝ LỊCH TRỰC

    //Tạo mới lịch trực
    void taoMoiLichTruc(List<TaoLichDTO> lichTcDTOS);

    //Cập nhật lịch trực
    void updateLichTruc(List<UpdateLichDTO> updateLichDTOS);

    // Lấy danh sách xe trung chuyển sẵn sàng hoạt động
    List<XeTcDTO> getDanhSachXeTrungChuyen(int xeTrungTam, boolean xeActive);

    // Lấy danh sách tài xế xe trung chuyển sẵn sàng
    List<TaiXeTcDTO> getTaiXeTrungChuyen();

    // Lấy danh sách tài xế xe trung chuyển sẵn sàng theo chi nhánh được chọn
    List<TaiXeTcDTO> getTaiXeTrungChuyen(List<Integer> chiNhanhId);

    // Lấy danh sách tài xế xe trung chuyển sẵn sàng theo vùng hoạt động được chọn
    List<TaiXeTcDTO> getTaiXeTrungChuyenTheoVung(int vungId);

    // Lấy danh sách lịch trực từ ngày tới ngày
    PageCustom<DanhSachLichDTO> getLichTrucByNgayTruc(LichTrucForm lichTrucForm);

    // Lấy danh sách lịch trực từ ngày tới ngày theo từng chi nhánh
    PageCustom<DanhSachLichDTO> getLichTrucByNgayTrucAndChiNhanh(LichTrucCNForm lichTrucCNForm);

    // Lấy dữ liệu chấm công cho lái xe
    List<ChamCongDTO> getDuLieuChamCong(ChamCongForm chamCongForm);

    // Lưu dữ liệu chấm công cho lái xe
    void saveDuLieuChamCong(LuuChamCongDTO luuChamCongDTO);

    // Xóa ca trực
    void xoaCaTruc(int id);

}
