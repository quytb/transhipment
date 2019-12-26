package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.common.LenhConstants;
import com.havaz.transport.api.common.PriceType;
import com.havaz.transport.api.form.VeForm;
import com.havaz.transport.api.model.PartnerPriceGroupDTO;
import com.havaz.transport.api.repository.TcPriceGroupRepositoryCustom;
import com.havaz.transport.api.service.TcTripDetailService;
import com.havaz.transport.dao.entity.TcCtvPriceByDistanceEntity;
import com.havaz.transport.dao.entity.TcCtvPriceByStepEntity;
import com.havaz.transport.dao.entity.TcLenhEntity;
import com.havaz.transport.dao.entity.TcTripDetailEntity;
import com.havaz.transport.dao.entity.TcVeEntity;
import com.havaz.transport.dao.entity.TcVtcCtvEntity;
import com.havaz.transport.dao.repository.TcCtvPriceByDistanceRepository;
import com.havaz.transport.dao.repository.TcCtvPriceByStepRepository;
import com.havaz.transport.dao.repository.TcTripDetailRepository;
import com.havaz.transport.dao.repository.TcVeRepository;
import com.havaz.transport.dao.repository.TcVtcCtvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Service
@Transactional(readOnly = true)
public class TcTripDetailServiceImpl implements TcTripDetailService {

    @Autowired
    private TcVeRepository tcVeRepository;

    @Autowired
    private TcTripDetailRepository tcTripDetailRepository;

    @Autowired
    private TcVtcCtvRepository vtcCtvRepository;

    @Autowired
    private TcPriceGroupRepositoryCustom tcPriceGroupRepositoryCustom;

    @Autowired
    private TcCtvPriceByDistanceRepository byDistanceRepository;

    @Autowired
    private TcCtvPriceByStepRepository byStepRepository;

    @Override
    @Transactional
    public void saveTcTripDetail(TcLenhEntity tcLenhEntity, VeForm veForm, int type) {
        TcTripDetailEntity tripDetailEntity = new TcTripDetailEntity();
        tripDetailEntity.setLenhId(tcLenhEntity.getTcLenhId());
        List<Integer> bvvIds = veForm.getBvvIds();
        List<TcVeEntity> veEntities = tcVeRepository.findByBvvIds(bvvIds);
        if (!veEntities.isEmpty()) {
            BigDecimal distanceToHub;
            if (type == LenhConstants.LENH_DON) {
                tripDetailEntity.setTcVttCtvId(veEntities.get(0).getVungTCDon());
                distanceToHub = BigDecimal.valueOf(veEntities.get(0).getTcDistanceToHubDon());
                tripDetailEntity.setDistance(distanceToHub);
            } else {
                tripDetailEntity.setTcVttCtvId(veEntities.get(0).getVungTCTra());
                distanceToHub = BigDecimal.valueOf(veEntities.get(0).getTcDistanceToHubTra());
                tripDetailEntity.setDistance(distanceToHub);
            }
            Optional<TcVtcCtvEntity> vtcCtvEntity = vtcCtvRepository
                    .findByTcVttIdAndTcCtvIdAndStatus(tripDetailEntity.getTcVttCtvId(),
                                                      tcLenhEntity.getLaiXeId(), Constant.ACTIVE);
            if (vtcCtvEntity.isPresent()) {
                PartnerPriceGroupDTO partnerPriceGroupDTO = tcPriceGroupRepositoryCustom.findPriceGroupPartner(vtcCtvEntity.get().getPriceGroupId());
                if (partnerPriceGroupDTO.getTypePrice() == PriceType.BY_DISTANCE.getType()) {
                    Optional<TcCtvPriceByDistanceEntity> optionalByDistance = byDistanceRepository.findByPriceId(partnerPriceGroupDTO.getPriceId());
                    if (optionalByDistance.isPresent()) {
                        partnerPriceGroupDTO.setPriceByDistanceEntity(optionalByDistance.get());
                        BigDecimal distanceStandard = BigDecimal.valueOf(optionalByDistance.get().getDistance());

                        // todo Init Value distance
                        BigDecimal distanceFlowStander;
                        BigDecimal distanceFlowOver = BigDecimal.ZERO;

                        if (distanceStandard.compareTo(distanceToHub) <= 0) {
                            distanceFlowStander = distanceStandard;
                            distanceFlowOver = distanceToHub.subtract(distanceStandard);
                        } else {
                            distanceFlowStander = distanceToHub;
                        }
                        BigDecimal amountStander = distanceFlowStander.multiply(BigDecimal.valueOf(optionalByDistance.get().getPrice()));
                        BigDecimal amountOver = distanceFlowOver.multiply(BigDecimal.valueOf(optionalByDistance.get().getPriceOver()));
                        BigDecimal amount = amountStander.add(amountOver);
                        if (partnerPriceGroupDTO.getDiscountRange() != 0) {
                            amount = amount.multiply(BigDecimal.valueOf((Constant.PERCENT_UNIT - partnerPriceGroupDTO.getDiscountRange()) / Constant.PERCENT_UNIT));
                            tripDetailEntity.setDiscountRange(BigDecimal.valueOf(partnerPriceGroupDTO.getDiscountRange()));
                        }
                        tripDetailEntity.setAmount(amount);
                    }
                } else {
                    List<TcCtvPriceByStepEntity> priceByStepEntities = byStepRepository.findByPriceId(partnerPriceGroupDTO.getPriceId());
                    Stack<TcCtvPriceByStepEntity> stepEntityStack = new Stack<>();
                    Collections.reverse(priceByStepEntities);
                    stepEntityStack.addAll(priceByStepEntities);
                    BigDecimal amount = calculateAmount(stepEntityStack, distanceToHub, stepEntityStack.peek());
                    if (partnerPriceGroupDTO.getDiscountRange() != 0) {
                        amount = amount.multiply(BigDecimal.valueOf((Constant.PERCENT_UNIT - partnerPriceGroupDTO.getDiscountRange()) / Constant.PERCENT_UNIT));
                        tripDetailEntity.setDiscountRange(BigDecimal.valueOf(partnerPriceGroupDTO.getDiscountRange()));
                    }
                    tripDetailEntity.setAmount(amount);

                }
                tripDetailEntity.setStatus(Constant.ZERO);
                tcTripDetailRepository.save(tripDetailEntity);
            }
        }

    }

    private BigDecimal calculateAmount(Stack<TcCtvPriceByStepEntity> stack, BigDecimal distanceToHub,
                                       TcCtvPriceByStepEntity outSideStep) {
        if (stack.size() == 0) {
            return distanceToHub.multiply(BigDecimal.valueOf(outSideStep.getPrice()));
        }

        if (distanceToHub.compareTo(BigDecimal.valueOf(stack.peek().getDistanceTo() - stack.peek().getDistanceFrom())) <= 0) {
            return distanceToHub.multiply(BigDecimal.valueOf(stack.peek().getPrice()));
        }

        TcCtvPriceByStepEntity stackEntity = stack.pop();
        BigDecimal distancePrice = BigDecimal.valueOf(stackEntity.getDistanceTo()).subtract(BigDecimal.valueOf(stackEntity.getDistanceFrom()));
        BigDecimal bigDecimal = distancePrice.multiply(BigDecimal.valueOf(stackEntity.getPrice()));
        return bigDecimal.add(calculateAmount(stack, distanceToHub.subtract(BigDecimal.valueOf(stackEntity.getDistanceTo() - stackEntity.getDistanceFrom())), stackEntity));
    }

}
