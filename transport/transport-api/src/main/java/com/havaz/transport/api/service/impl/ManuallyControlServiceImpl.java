package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.repository.DieuDoTempRepositoryCustom;
import com.havaz.transport.api.service.ManuallyControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ManuallyControlServiceImpl implements ManuallyControlService {

    @Autowired
    private DieuDoTempRepositoryCustom dieuDoTempRepositoryCustom;

    @Override
    public List<Integer> getListTripData(String inputDate) {
    //TODO 1: GET ALL did_id via input date
        return dieuDoTempRepositoryCustom.getListTripId();
    //TODO 2: List all ban_ve_ve
    //TODO 3: CALL API Transfer ve
//        VeActionForm veForm = new VeActionForm();
//        veForm.setAction("CAP_NHAT_VE");
//        veForm.setBvvIds(listDidIdInteger);
//        commonService.transferVeERPToVeTC(veForm);
    }
}
