package com.havaz.transport.api.service;

import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.form.PriceGroupForm;
import com.havaz.transport.api.form.PriceGroupSearchForm;
import com.havaz.transport.api.model.PriceDTO;
import com.havaz.transport.api.model.PriceGroupDTO;

import java.util.List;

public interface TcCtvPriceService {
    List<PriceDTO> findAll();

    void saveOrUpdatePriceCtv(PriceDTO priceDTO);

    void saveOrUpdateGroupPrice(PriceGroupForm priceGroupForm);

    PageCustom<PriceGroupDTO> getAllPriceGroup(PriceGroupSearchForm searchForm);

    void deletePriceStep(Integer priceStepId);

}
