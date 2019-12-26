package com.havaz.transport.api.repository;

import com.havaz.transport.api.model.CanhBaoDTO;
import com.havaz.transport.api.model.NowTripDetailDTO;
import com.havaz.transport.api.model.TxCtvDTO;
import com.havaz.transport.api.model.XeTcNowDTO;

import java.util.List;

public interface HavazNowRepositoryCustom {

    List<Object[]> getListTripForHavazNow(String beforNow, String afterNow);

    List<XeTcNowDTO> getTaiXeTcByDidIdAndTrangthai(Integer didId, Integer type);

    NowTripDetailDTO getTripByTripIdAndTrangThai(Integer tripId, Integer type);

    List<CanhBaoDTO> layDanhSachDeCanhBao(List<Integer> listTripId);

    List<TxCtvDTO> getAllTxCtv();

}
