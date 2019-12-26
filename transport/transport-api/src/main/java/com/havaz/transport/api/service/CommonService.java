package com.havaz.transport.api.service;

import com.havaz.transport.api.form.CustomerRankData;
import com.havaz.transport.api.form.VeActionForm;
import com.havaz.transport.api.model.ConDataDTO;
import com.havaz.transport.api.rabbit.message.TransferTicketMessage;

import java.util.List;
import java.util.Map;

public interface CommonService {
    ConDataDTO layThongTinNhaXe();

    void transferVeERPToVeTC(VeActionForm message);

    void updateTransferVeERPToVeTC(VeActionForm message);

    Map<String, String> getAllConfig();

    boolean createLogBvv(int bvvId, String action, int type);

    void wrieZzLog(String id, int bvvId, String data, String ip);

    List<CustomerRankData> layThongTinRankTuHavaz(List<String> listPhone);
}
