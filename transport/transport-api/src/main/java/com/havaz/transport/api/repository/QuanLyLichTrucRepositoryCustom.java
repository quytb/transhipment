package com.havaz.transport.api.repository;

import com.havaz.transport.api.model.CaPagingDTO;
import com.havaz.transport.api.model.DieuDoKhachTcDonDTO;
import com.havaz.transport.api.model.DieuDoKhachTcTraDTO;
import com.havaz.transport.api.model.LichTrucDTO;
import com.havaz.transport.api.model.TaiXeTcDTO;
import com.havaz.transport.api.model.TimKhachDTO;
import com.havaz.transport.dao.entity.TcCaEntity;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface QuanLyLichTrucRepositoryCustom  {
    List<TaiXeTcDTO> getTaiXeTrungChuyen();

    List<TaiXeTcDTO> getTaiXeTrungChuyenByChiNhanh(List<Integer> chiNhanhId);

    List<TaiXeTcDTO> getTaiXeTrungChuyenByVung(int vungId);

    Page<TcCaEntity> findCaTruc(CaPagingDTO caPagingDTO);

    Page<DieuDoKhachTcDonDTO> getDanhSachKhachTrungChuyenDon(TimKhachDTO timKhachDTO);

    Page<DieuDoKhachTcTraDTO> getDanhSachKhachTrungChuyenTra(TimKhachDTO timKhachDTO);

    List<LichTrucDTO> getLichTrucByNgayTruc(int taixeId, LocalDate startDate, LocalDate endDate, String sortBy, String sortType);
//    public List<Object[]> getDuLieuChamCong(Date ngayChamCong);

//    public List<Object[]> getLichTrucByNgayTruc(Date startDate,Date endDate,int taixeId);
//    public List<Integer> getTaiXeByNgayTruc(Date startDate,Date endDate);

}
