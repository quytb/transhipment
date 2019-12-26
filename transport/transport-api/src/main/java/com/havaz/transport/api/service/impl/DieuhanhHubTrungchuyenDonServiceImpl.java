package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.body.request.tcve.GetTcVeDonRequest;
import com.havaz.transport.api.body.response.tcve.GetTcVeDonResponse;
import com.havaz.transport.api.form.CustomerRankData;
import com.havaz.transport.api.repository.TcVeRepositoryCustom;
import com.havaz.transport.api.service.CommonService;
import com.havaz.transport.api.service.DieuhanhHubTrungchuyenDonService;
import com.havaz.transport.dao.query.SelectOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DieuhanhHubTrungchuyenDonServiceImpl implements DieuhanhHubTrungchuyenDonService {
    @Autowired
    private TcVeRepositoryCustom tcVeRepositoryCustom;

    @Autowired
    private CommonService commonService;

    @Override
    public Page<GetTcVeDonResponse> search(GetTcVeDonRequest request, Pageable pageable) {
        SelectOptions options = SelectOptions.get(pageable).count();

        if (request.getNgayXuatben() == null) {
            request.setNgayXuatben(LocalDate.now());
        }

        List<GetTcVeDonResponse> list = tcVeRepositoryCustom
                .findAllByGetTcVeDonRequest(request, options);
        List<String> listPhone = list.stream().map(x-> x.getSdtHanhkhach()).collect(Collectors.toList());
        List<GetTcVeDonResponse> listTicket = getRankCustomerHavazTCD(list,listPhone);

        listTicket.forEach(getTcVeDonResponse -> {
            getTcVeDonResponse.setBvvIds(convertStringToArrayInteger(getTcVeDonResponse.getBvvIdsStr()));
            getTcVeDonResponse.setVersions(convertStringToArrayInteger(getTcVeDonResponse.getVersionsStr()));
        });
        return new PageImpl<>(listTicket, pageable, options.getCount());
    }

    private List<Integer> convertStringToArrayInteger(String arr) {
        if (Strings.isEmpty(arr)) {
            return new ArrayList<>();
        }
        return Arrays.stream(Stream.of(arr.split(",")).mapToInt(Integer::parseInt).toArray()).boxed()
                .collect(Collectors.toList());
    }

    private List<GetTcVeDonResponse> getRankCustomerHavazTCD(List<GetTcVeDonResponse> lstObject, List<String> lstString){
        if(lstString.size() <= 0)
            return lstObject;
        try{
            List<CustomerRankData> customerRankDatas = commonService.layThongTinRankTuHavaz(lstString);
            lstObject = lstObject.stream().map(obj1 -> {
                customerRankDatas.stream().map(obj2 -> {
                    if(obj2.getPhone().equals(obj1.getSdtHanhkhach())) {
                        obj1.setRank(obj2.getRank());
                        obj1.setIcon(obj2.getIcon());
                    }
                    return obj1;
                }).collect(Collectors.toList());
                return obj1;
            }).collect(Collectors.toList());
            return lstObject;
        }catch (Exception ex){
            ex.printStackTrace();
            log.warn("Khong lay duoc danh sach rank tu ben san");
            return lstObject;
        }
    }

}
