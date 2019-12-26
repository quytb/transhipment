package com.havaz.transport.api.service;

import com.havaz.transport.api.body.response.tcve.GetTcVeDonResponse;
import com.havaz.transport.api.model.TopicDTO;
import com.havaz.transport.api.model.TripDTO;
import com.havaz.transport.dao.entity.TcVeEntity;

import java.util.List;

public interface VeTcService {
    List<TcVeEntity> getVeTcByLenhId(int lenhId);

    TcVeEntity getByBvvId(int bvvId);

    List<TcVeEntity> getByTaiXeIdAndLenhId(int taixeId, int lenhId);

    List<TripDTO> getListTripByTripId(int tripId, int type, int caller);

    //Get list ticket for route hub->hub
    List<GetTcVeDonResponse> findAllByCommand(Integer cmdId);

    void removeTicketOfCommand(List<Integer> bvvIds);


    // TRUNG CHUYỂN TRẢ
    // Get list ve by lenh Id
    List<TcVeEntity> getVeByTaiXeIdAndLenhId(int taixeId, int lenhId);

    // get list ve entity by list VeId
    List<TcVeEntity> getListVeTcById(List<Integer> listVeId);

    // get list ve by lenh id
    List<TcVeEntity> getVeTcTraByLenhId(int lenhTraId);

    //get list TC_VE by taiXeId
    List<TopicDTO> getVeByTaiXeId(int taixeId);
}
