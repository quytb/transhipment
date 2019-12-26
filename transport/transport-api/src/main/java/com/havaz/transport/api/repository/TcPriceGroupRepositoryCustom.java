package com.havaz.transport.api.repository;

import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.form.PriceGroupSearchForm;
import com.havaz.transport.api.model.PartnerPriceGroupDTO;
import com.havaz.transport.api.model.PriceGroupDTO;

public interface TcPriceGroupRepositoryCustom {
    PageCustom<PriceGroupDTO> findByPartnerName(PriceGroupSearchForm searchForm);

    PartnerPriceGroupDTO findPriceGroupPartner(Integer groupId);
}
