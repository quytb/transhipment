package com.havaz.transport.api.service;

import com.havaz.transport.api.body.request.tcve.TaoLenhRequest;
import com.havaz.transport.api.body.response.benxe.GetBenXeReponse;
import com.havaz.transport.api.form.CmdHubToHubForm;
import com.havaz.transport.api.model.ChiTietLenhDTO;
import com.havaz.transport.api.model.NotActiveDTO;
import com.havaz.transport.dao.entity.TcLenhEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TrungChuyenHubToHubService {
    TcLenhEntity createdCmdHubToHub(CmdHubToHubForm form);

    void updateClientToCmd(CmdHubToHubForm form, Integer cmdId);

    List<GetBenXeReponse> findAllBenxe();

    List<NotActiveDTO> getNotActiveChieuA( Integer gioxuatben);

    void creatCmdHubToHubForWeb(TaoLenhRequest taoLenhRequest);

    Page<ChiTietLenhDTO> getDsLenh(Integer diemgiaokhach, LocalDate dateRequest, Pageable pageable);

}
