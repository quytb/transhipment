package com.havaz.transport.api.service;

import com.havaz.transport.api.body.response.tcve.GetTcVeDonResponse;
import com.havaz.transport.api.model.CanhBaoDTO;
import com.havaz.transport.api.model.NowTripDTO;
import com.havaz.transport.api.model.NowTripDetailDTO;
import com.havaz.transport.api.model.TxCtvDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HavazNowService {
    List<NowTripDTO> getNowTripDTOList(int beforeNow, int afterNow);

    NowTripDetailDTO getTripDetails(Integer tripId, Integer type);

    List<CanhBaoDTO> getAllCanhBaoForListTrip(List<Integer> listTripId);

    NowTripDetailDTO getKhByTrip(Integer tripId, Integer type);

    List<TxCtvDTO> getAllTxCtv();

    Page<GetTcVeDonResponse> getTcVeResponse();
}
