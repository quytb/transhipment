package com.havaz.transport.api.service;

import com.havaz.transport.api.model.DaDonDTO;
import com.havaz.transport.api.model.HuyDonDTO;
import com.havaz.transport.api.model.ThoiGianDonDTO;
import com.havaz.transport.api.model.UpdateHubDonDTO;

public interface TrungChuyenDonService {
    void huyDonKhach(HuyDonDTO huyDonDTO);

    void donKhach(DaDonDTO daDonDTO);

    void thoiGianDon(ThoiGianDonDTO thoiGianDonDTO);

    void sendInfoTrackingToMsgMQ(int bvvId);

    void updateHubDon(UpdateHubDonDTO updateHubDonDTO);

    void updateHubTra(UpdateHubDonDTO updateHubDonDTO);
}
