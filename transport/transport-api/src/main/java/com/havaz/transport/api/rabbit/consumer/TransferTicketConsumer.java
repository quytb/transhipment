package com.havaz.transport.api.rabbit.consumer;

import com.havaz.transport.api.configuration.RabbitMQConfig;
import com.havaz.transport.api.form.VeActionForm;
import com.havaz.transport.api.rabbit.message.TransferTicketMessage;
import com.havaz.transport.api.rabbit.publisher.RabbitMQPublisher;
import com.havaz.transport.api.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn("transferTicketBinding")
public class TransferTicketConsumer {

    private static final Logger log = LoggerFactory.getLogger(TransferTicketConsumer.class);

    @Autowired
    private CommonService commonService;

    @Autowired
    private RabbitMQPublisher rabbitMQPublisher;

    @RabbitListener(containerFactory = RabbitMQConfig.SPRING_BEAN_RABBIT_LISTENER_CONTAINER_FACTORY_ERP,
                    queues = "${havaz.rabbitmq.erp.queue.transfer-ticket}")
    public void listen(TransferTicketMessage message) {
        log.debug("Message is read: {}", message);
        try {
            log.debug("Start transfer vé: {}", message);
            commonService.transferVeERPToVeTC(message.getContent());
            rabbitMQPublisher.sendTicketUpdate(message.getContent());
            log.debug("Transfer ve done: {}", message);
        } catch (Exception e) {
            log.error("Lỗi transfer vé", e);
        }
    }

    @RabbitListener(containerFactory = RabbitMQConfig.SPRING_BEAN_RABBIT_LISTENER_CONTAINER_FACTORY_ERP,
                    queues = "${havaz.rabbitmq.erp.queue.transfer-ticket-sync}")
    public void listenTransferVeSync(TransferTicketMessage message) {
        log.debug("Message is read: {}", message);
        try {
            log.debug("Start transfer vé sync: {}", message);
            commonService.transferVeERPToVeTC(message.getContent());
            rabbitMQPublisher.sendTicketUpdate(message.getContent());
            log.debug("TransferVe sync done: {}", message);
        } catch (Exception e) {
            log.error("Lỗi transfer vé sync: ", e);
        }
    }

    @RabbitListener(containerFactory = RabbitMQConfig.SPRING_BEAN_RABBIT_LISTENER_CONTAINER_FACTORY_ERP,
                    queues = "${havaz.rabbitmq.transport.transferticket.queue}")
    public void listenUpdateTransferTicket(VeActionForm message) {
        log.debug("Message update ticket is read: {}", message);
        try {
            log.debug("Start transfer update ticket: {}", message);
            commonService.updateTransferVeERPToVeTC(message);
            log.debug("Transfer update ticket done: {}", message);
        } catch (Exception e) {
            log.error("Lỗi transfer vé", e);
        }
    }
}
