package com.havaz.transport.api.rabbit.publisher.impl;

import com.havaz.transport.api.form.RabbitDataForm;
import com.havaz.transport.api.form.VeActionForm;
import com.havaz.transport.api.model.KhachHavazDTO;
import com.havaz.transport.api.model.NotifyLenhDTO;
import com.havaz.transport.api.model.TripDTO;
import com.havaz.transport.api.rabbit.publisher.RabbitMQPublisher;
import com.havaz.transport.core.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RabbitMQPublisherImpl implements RabbitMQPublisher {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQPublisherImpl.class);

    @Autowired
    @Qualifier("rabbitTemplateSan")
    private RabbitTemplate rabbitTemplateSan;

    @Autowired
    @Qualifier("rabbitTemplateERP")
    private RabbitTemplate rabbitTemplateErp;

    @Value("${havaz.rabbitmq.san.exchange}")
    private String exchange;

    @Value("${havaz.rabbitmq.san.routing-key}")
    private String routingKey;

    @Value("${havaz.rabbitmq.san.on-send-message}")
    private boolean onSendMsg;

    @Value("${havaz.rabbitmq.san.driver.arrived.routing-key}")
    private String driverRoutingkey;

    @Value("${havaz.rabbitmq.san.driver.confirmed.routing-key}")
    private String driverConfirmRoutingKey;

    @Value("${havaz.rabbitmq.transport.transferticket.exchange}")
    private String transferTicketExchange;

    @Value("${havaz.rabbitmq.transport.transferticket.routing-key}")
    private String transferTicketRoutingKey;;

    @Override
    public void sendTicketUpdate(VeActionForm veActionForm) {
        log.info("Start send ticket update to rabbitmq:: {}", veActionForm);
//        TransferTicketMessage message = new TransferTicketMessage();
//        message.setMerchantID("4");
//        message.setContent(veActionForm);
        rabbitTemplateErp
                .convertAndSend(transferTicketExchange, transferTicketRoutingKey, veActionForm);
        log.info("Send ticket update to rabbitmq done:: {}", veActionForm);
    }

    @Override
    public void sendMsgTrackingVehicle(TripDTO tripDTO) {
        log.debug("Data send:: {}", tripDTO);
        RabbitDataForm rabbitDataForm = new RabbitDataForm();
        rabbitDataForm.setOccuredOn(
                DateTimeUtils.convertLocalDateTimeToString(LocalDateTime.now(),
                                                           DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS));
        rabbitDataForm.setEventType(routingKey);
        rabbitDataForm.setPayload(tripDTO);
        if (onSendMsg) {
            rabbitTemplateSan.convertAndSend(exchange, routingKey, rabbitDataForm);
            log.debug("Send msg to RabbitMQ done: {}", rabbitDataForm);
        }
    }

    @Override
    public void sendMsgTrackingDriver(KhachHavazDTO khachDTO) {
        RabbitDataForm rabbitDataForm = new RabbitDataForm();
        rabbitDataForm.setOccuredOn(
                DateTimeUtils.convertLocalDateTimeToString(LocalDateTime.now(),
                                                           DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS));
        rabbitDataForm.setEventType(driverRoutingkey);
        rabbitDataForm.setPayload(khachDTO);
        if (onSendMsg) {
            rabbitTemplateSan.convertAndSend(exchange, driverRoutingkey, rabbitDataForm);
        }
        log.info("send Data Driver has Arrived Ok");
    }

    @Override
    public void sendMsgDriverConfirmLenh(NotifyLenhDTO notifyLenhDTO) {
        RabbitDataForm rabbitDataForm = new RabbitDataForm();
        rabbitDataForm.setOccuredOn(DateTimeUtils
                                             .convertLocalDateTimeToString(LocalDateTime.now(),
                                                                           DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS));
        rabbitDataForm.setEventType(driverConfirmRoutingKey);
        rabbitDataForm.setPayload(notifyLenhDTO);
        if (onSendMsg) {
            rabbitTemplateSan.convertAndSend(exchange, driverConfirmRoutingKey, rabbitDataForm);
        }
    }
}
