package com.havaz.transport.api.repository;

import com.havaz.transport.api.form.BaoCaoLenhForm;
import com.havaz.transport.api.form.FilterLenhForm;
import com.havaz.transport.api.model.*;
import com.havaz.transport.dao.entity.TcLenhEntity;
import com.havaz.transport.dao.query.SelectOptions;

import java.time.LocalDate;
import java.util.List;

public interface TcLenhRepositoryCustom {
    List<TcLenhEntity> getLenhByTaiXeId(int taiXeId, int kieuLenh);

    List<Object[]> getListKhachTrungChuyen(int lenhId);

    List<Object[]> getListKhachTrungChuyenTra(int lenhId);

    List<TcLenhEntity> getLenhDangDon(int taiXeId, int kieuLenh);

    List<Object[]> getListKhachDangDon(int lenhId);

    List<Object[]> getHistoryByTaiXeId(int taixeId, int month, int year);

    List<ChiTietLenhDTO> getListChiTietLenh(FilterLenhForm filterLenhForm);

    long getCountListChiTietLenh(FilterLenhForm filterLenhForm);

    List<MaLenhDTO> getMaLenh(FilterLenhForm filterLenhForm);

    //Get thông tin các xe tuyến theo lệnh
    List<XeTuyenDTO> getXeTuyenInfor(int lenhId);

    //Get tong so lenh cua tai xe.
    List<Object[]> getTongSoLenh(BaoCaoLenhForm baoCaoLenhForm);

    List<Object[]> getTongSoKhach(BaoCaoLenhForm baoCaoLenhForm);

    List<BaoCaoLenhDTO> getReports(BaoCaoLenhForm baoCaoLenhForm);

    List<ClientHavazNowDTO> getClientAdditionalHavazNow(Integer bvvId);

    List<Object> findLenhIdsByLaiXeIdAndLastUpdatedDate(int laiXeId, LocalDate lastUpdatedDate);

    List<Object> findChamCongByLaiXeIdAndLastUpdatedDate(int laiXeId, LocalDate lastUpdatedDate);

    List<VeTrungChuyenDTO> getListKhachChoLenhHuy(int lenhId);

    List<LenhContainGXBDTO> getListLenhCoChuaGioXuatBen(int laiXeId, int type);

    List<ChiTietLenhDTO> getDsLenh(Integer diemgiaokhach,LocalDate dateRequest ,SelectOptions selectOptions);
}
