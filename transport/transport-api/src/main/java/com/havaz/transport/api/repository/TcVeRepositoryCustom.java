package com.havaz.transport.api.repository;

import com.havaz.transport.api.body.request.tcve.GetTcVeDonRequest;
import com.havaz.transport.api.body.response.tcve.GetTcVeDonResponse;
import com.havaz.transport.dao.entity.TcVeEntity;
import com.havaz.transport.dao.query.SelectOptions;

import java.util.List;

public interface TcVeRepositoryCustom {
    // Lấy danh sách trip theo tripId
    List<Object[]> getListTripObjectTripId(int tripId, int type);

    void traKhach(List<Integer> listVe);

    // get list ve by list ve id
    List<TcVeEntity> getListVeTcByBvvId(List<Integer> listBvvId);

    List<TcVeEntity> getByTaiXeIdAndLenhId(int taixeId, int lenhId);

    // Get vé trung chuyển theo lệnh ID và tài xế Id
    List<TcVeEntity> getVeTcByLenhIdAndTaiXeId(int taixeId, int lenhId);

    List<TcVeEntity> getVeTcActiveByLaiXeId(int taixeId);

    List<GetTcVeDonResponse> findAllByGetTcVeDonRequest(GetTcVeDonRequest getTcVeDonRequest, SelectOptions options);

    List<GetTcVeDonResponse> findAllOfCommand(Integer lenhId);
}
