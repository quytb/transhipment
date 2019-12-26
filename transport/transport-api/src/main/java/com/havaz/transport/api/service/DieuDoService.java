package com.havaz.transport.api.service;

import com.havaz.transport.api.body.request.cmdhub.GetCmdHubRequest;
import com.havaz.transport.api.form.DieuDoForm;
import com.havaz.transport.api.form.LenhForm;
import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.model.DieuDoKhachTcDonDTO;
import com.havaz.transport.api.model.DieuDoKhachTcTraDTO;
import com.havaz.transport.api.model.DistenceCtvVtc;
import com.havaz.transport.api.model.GioDiDTO;
import com.havaz.transport.api.model.HuyLenhTcDTO;
import com.havaz.transport.api.model.NotActiveDTO;
import com.havaz.transport.api.model.ThongTinNodeDto;
import com.havaz.transport.api.model.TimGioDTO;
import com.havaz.transport.api.model.TimKhachDTO;
import com.havaz.transport.api.model.TuyenDTO;
import com.havaz.transport.api.model.XeTcDTO;
import com.havaz.transport.dao.entity.TcLenhEntity;

import java.time.LocalDate;
import java.util.List;

public interface DieuDoService {

    // Lấy danh sách tuyến
    List<TuyenDTO> getListTuyen();

    // Tìm kiếm khách trung chuyển đón
    PageCustom<DieuDoKhachTcDonDTO> getDanhSachKhachTrungChuyenDon(TimKhachDTO timKhachDTO);

    // Tìm kiếm khách trung chuyển trả
    PageCustom<DieuDoKhachTcTraDTO> getDanhSachKhachTrungChuyenTra(TimKhachDTO timKhachDTO);

    // Lấy danh sách giờ xe chạy theo tuyến và khoảng thời gian từ hiện tại tới thời gian truyền vào
    List<GioDiDTO> getListGioDi(TimGioDTO timGioDTO);

    // Lấy tất cả tuyến có trạng thái là active
    List<NotActiveDTO> getListNotActive(ThongTinNodeDto thongTinNodeDto);

    void taoLenh(DieuDoForm dieuDoForm, int type, int lenhStatus);

    //Điều lệnh tại hub
    void dieuLenhHub(GetCmdHubRequest getCmdHubRequest);

    void cancelLenhHub(Integer lenhId);

    //dieu lenh cho lai xe tuyen
    void taoLenhChoLaiXeTuyen(DieuDoForm dieuDoForm, int type, int lenhStatus);

    //lai xe tuyen co the huy lenh
    void huyLenhChoLaiXeTuyen(int idTaiXe, int bvvId, int type);

    //dieu lenh cho lai xe ngoài đi đón, trả
    void taoLenhChoLaiXeNgoai(DieuDoForm dieuDoForm, int type, int lenhStatus);

    void dieuBoSung(DieuDoForm dieuDoForm, int type);

    void luuLenh(DieuDoForm dieuDoForm, int type, int lenhStatus);

    List<LenhForm> getAllLenh(int type);

    List<LenhForm> getAllLenhDaTao(int laiXeId, int type);

    void xoaLenh(int lenhId);

    int getIdXeTuChamCongHoacPhanLich(int taiXeId, LocalDate date);

    void taoLenhCtv(DieuDoForm form, int vungId, int type, int lenhStatus,
                    DistenceCtvVtc distenceCtvVtc);

    boolean huyLenhDadieu(HuyLenhTcDTO huyLenhTcDTO);

    XeTcDTO getAttributeXe(int taixeId, LocalDate date);

    XeTcDTO getAttributeXe(int xeId);



//    public List<TcVttDTO> getListVungTrungChuyen();
//    public List<TcVttDTO> findVttByTenVung(String tenVung);

//    public void addTransportArea(VungTcDTO vungTcDTO);
//    public void updateTransportArea(UpdateVungTcDTO updateVungTcDTO);
//    public void switchStatusTransportArea(int tcVttId, int trangThai);
}
