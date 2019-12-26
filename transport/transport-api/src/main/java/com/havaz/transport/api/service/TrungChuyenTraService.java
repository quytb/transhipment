package com.havaz.transport.api.service;

import com.havaz.transport.api.model.DaTraDTO;
import com.havaz.transport.api.model.HuyDonDTO;
import com.havaz.transport.api.model.HuyLenhTcDTO;
import com.havaz.transport.api.model.KhachTrungChuyenTraDTO;
import com.havaz.transport.api.model.ThoiGianTraDTO;
import com.havaz.transport.api.model.XacNhanLenhTraDTO;

import java.util.List;

public interface TrungChuyenTraService {
    void traKhach(DaTraDTO daTraDTO);

    List<KhachTrungChuyenTraDTO> getListKhachTrungChuyenTra(int taiXeId);

    List<KhachTrungChuyenTraDTO> getListKhachTrungChuyenTraUpdate(double latitude, double longitude, int lenhId);

    //Xác nhận lệnh trả
    boolean xacNhanLenhTra(XacNhanLenhTraDTO requestXacNhanLenhTra);

    //Hủy lệnh trả
    boolean huyLenhTra(HuyLenhTcDTO requestCancelLenh);

    boolean huyTraKhach(HuyDonDTO huyDonDTO);

    void thoiGianTra(ThoiGianTraDTO thoiGianTraDTO);
}
