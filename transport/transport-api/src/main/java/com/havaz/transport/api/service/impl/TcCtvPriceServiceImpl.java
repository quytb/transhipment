package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.common.PriceType;
import com.havaz.transport.api.exception.ResourceNotfoundException;
import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.form.PriceGroupForm;
import com.havaz.transport.api.form.PriceGroupSearchForm;
import com.havaz.transport.api.model.PriceByDistanceDTO;
import com.havaz.transport.api.model.PriceByStepDTO;
import com.havaz.transport.api.model.PriceDTO;
import com.havaz.transport.api.model.PriceGroupDTO;
import com.havaz.transport.api.repository.TcPriceGroupRepositoryCustom;
import com.havaz.transport.api.service.TcCtvPriceService;
import com.havaz.transport.dao.entity.TcCtvPriceByDistanceEntity;
import com.havaz.transport.dao.entity.TcCtvPriceByStepEntity;
import com.havaz.transport.dao.entity.TcCtvPriceEntity;
import com.havaz.transport.dao.entity.TcCtvPriceGroupEntity;
import com.havaz.transport.dao.entity.TcPartnerEntity;
import com.havaz.transport.dao.repository.TcCtvPriceByDistanceRepository;
import com.havaz.transport.dao.repository.TcCtvPriceByStepRepository;
import com.havaz.transport.dao.repository.TcCtvPriceRepository;
import com.havaz.transport.dao.repository.TcCtvPriceStepRepository;
import com.havaz.transport.dao.repository.TcPartnerRepository;
import com.havaz.transport.dao.repository.TcPriceGroupRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class TcCtvPriceServiceImpl implements TcCtvPriceService {

    @Autowired
    private TcCtvPriceRepository priceRepository;

    @Autowired
    private TcCtvPriceByStepRepository byStepRepository;

    @Autowired
    private TcCtvPriceByDistanceRepository byDistanceRepository;

    @Autowired
    private TcPriceGroupRepository priceGroupRepository;

    @Autowired
    private TcPriceGroupRepositoryCustom tcPriceGroupRepositoryCustom;

    @Autowired
    private TcCtvPriceStepRepository stepRepository;

    @Autowired
    private TcPartnerRepository partnerRepository;

    @Override
    public List<PriceDTO> findAll() {
        List<TcCtvPriceEntity> priceEntities = priceRepository.findAll();
        List<PriceDTO> priceDTOS = new ArrayList<>();
        priceEntities.forEach(tcCtvPriceEntity -> {
            PriceDTO priceDTO = new PriceDTO();
            BeanUtils.copyProperties(tcCtvPriceEntity, priceDTO);
            priceDTOS.add(priceDTO);
        });
        if (priceDTOS.size() > 0) {
            priceDTOS.forEach(price -> {
                if (price.getType() == PriceType.BY_DISTANCE.getType()) {
                    Optional<TcCtvPriceByDistanceEntity> optionalPrice = byDistanceRepository.findByPriceId(price.getPriceId());
                    if (optionalPrice.isPresent()) {
                        PriceByDistanceDTO byDistanceDTO = new PriceByDistanceDTO();
                        BeanUtils.copyProperties(optionalPrice.get(), byDistanceDTO);
                        price.setPriceByDistanceDTO(byDistanceDTO);
                    }
                } else {
                    List<TcCtvPriceByStepEntity> byStepEntities = byStepRepository.findByPriceId(price.getPriceId());
                    List<PriceByStepDTO> byStepDTOS = new ArrayList<>();
                    byStepEntities.forEach(tcCtvPriceByStepEntity -> {
                        PriceByStepDTO priceByStepDTO = new PriceByStepDTO();
                        BeanUtils.copyProperties(tcCtvPriceByStepEntity, priceByStepDTO);
                        byStepDTOS.add(priceByStepDTO);
                    });
                    price.setPriceByStepDTOS(byStepDTOS);
                }
            });
        }
        return priceDTOS;
    }

    @Override
    @Transactional
    public void saveOrUpdatePriceCtv(PriceDTO priceDTO) {
        TcCtvPriceEntity priceEntity = new TcCtvPriceEntity();
        BeanUtils.copyProperties(priceDTO, priceEntity);
        priceEntity = priceRepository.save(priceEntity);
        if (priceDTO.getPriceByDistanceDTO() != null && priceEntity.getType() == PriceType.BY_DISTANCE.getType()) {
            TcCtvPriceByDistanceEntity priceByDistanceEntity = new TcCtvPriceByDistanceEntity();
            BeanUtils.copyProperties(priceDTO.getPriceByDistanceDTO(), priceByDistanceEntity);
            priceByDistanceEntity.setPriceId(priceEntity.getPriceId());
            byDistanceRepository.save(priceByDistanceEntity);
        }
        if (priceDTO.getPriceByStepDTOS() != null && PriceType.BY_STEP.getType() == priceEntity.getType()) {
            TcCtvPriceEntity finalPriceEntity = priceEntity;
            Set<TcCtvPriceByStepEntity> tcVtcCtvEntities = new HashSet<>();
            priceDTO.getPriceByStepDTOS().forEach(priceByStepDTO -> {
                TcCtvPriceByStepEntity tcCtvPriceByStepEntity = new TcCtvPriceByStepEntity();
                BeanUtils.copyProperties(priceByStepDTO, tcCtvPriceByStepEntity);
                tcCtvPriceByStepEntity.setPriceId(finalPriceEntity.getPriceId());
                byStepRepository.save(tcCtvPriceByStepEntity);
                tcVtcCtvEntities.add(tcCtvPriceByStepEntity);
            });
            List<TcCtvPriceByStepEntity> byStepEntities = byStepRepository.findByPriceId(priceEntity.getPriceId());
            // Todo Action Delete PriceStep
            byStepEntities.forEach(tcCtvPriceByStepEntity -> {
                if (!tcVtcCtvEntities.contains(tcCtvPriceByStepEntity)) {
                    byStepRepository.delete(tcCtvPriceByStepEntity);
                }
            });
        }

    }

    @Override
    @Transactional
    public void saveOrUpdateGroupPrice(PriceGroupForm priceGroupForm) {
        TcPartnerEntity partnerEntity = new TcPartnerEntity();
        BeanUtils.copyProperties(priceGroupForm, partnerEntity);
        partnerRepository.save(partnerEntity);
        TcCtvPriceGroupEntity groupEntity = new TcCtvPriceGroupEntity();
        BeanUtils.copyProperties(priceGroupForm, groupEntity);
        groupEntity.setPartnerId(partnerEntity.getPartnerId());
        priceGroupRepository.save(groupEntity);

    }

    @Override
    public PageCustom<PriceGroupDTO> getAllPriceGroup(PriceGroupSearchForm searchForm) {
        int index = searchForm.getPage() > 0 ? searchForm.getPage() - 1 : 0;
        searchForm.setPage(index);
        if (searchForm.getSize() <= 0) {
            searchForm.setSize(10);
        }
        return tcPriceGroupRepositoryCustom.findByPartnerName(searchForm);
    }

    @Override
    @Transactional
    public void deletePriceStep(Integer priceStepId) {
        Optional<TcCtvPriceByStepEntity> optional = stepRepository.findById(priceStepId);
        if (optional.isPresent()) {
            TcCtvPriceByStepEntity tcCtvPriceByStepEntity = optional.get();
            stepRepository.delete(tcCtvPriceByStepEntity);
        } else {
            throw new ResourceNotfoundException("Không tồn tại PriceStep");
        }

    }
}
