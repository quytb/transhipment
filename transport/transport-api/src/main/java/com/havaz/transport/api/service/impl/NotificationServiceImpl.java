package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.common.ManagerNotificationType;
import com.havaz.transport.api.common.NotificationMainURL;
import com.havaz.transport.api.configuration.MessageConfig;
import com.havaz.transport.api.form.NotificationUpdateForm;
import com.havaz.transport.api.model.NotificationManagerDTO;
import com.havaz.transport.api.service.NotificationService;
import com.havaz.transport.api.service.RedisMessagePublisherService;
import com.havaz.transport.dao.entity.NotificationEntity;
import com.havaz.transport.dao.entity.TcNotificationConfigureEntity;
import com.havaz.transport.dao.repository.TcNotificationConfigRepository;
import com.havaz.transport.dao.repository.TcNotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private MessageConfig messageConfig;

    @Autowired
    private TcNotificationRepository notificationRepository;

    @Autowired
    private TcNotificationConfigRepository tcNotificationConfigRepository;

    @Autowired
    private RedisMessagePublisherService redisMessagePublisherService;


    @Override
    public void sendNotificationToWebSocket(String topic, String message) {
        try {
            LOG.info("Send message to topic " + topic);
            template.convertAndSend(topic, message);
            LOG.info("Send message to topic " + topic + "success");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAndSendNotificationToManager(String topic, Integer type, boolean isPickup, List<Integer> bvvIds) {
        NotificationEntity notificationEntity = new NotificationEntity();
        if (type == ManagerNotificationType.NEW_TICKET.getType()) {
            notificationEntity.setContent(messageConfig.getMessage("message.notification.has.new.ticket"));
        } else if (type == ManagerNotificationType.DRIVER_REJECT_PICKUP.getType()) {
            notificationEntity.setContent(messageConfig.getMessage("message.notification.driver.reject.pickup"));
        } else if (type == ManagerNotificationType.CHANGE_TRIP.getType()) {
            notificationEntity.setContent(messageConfig.getMessage("message.notification.change.trip"));
        } else if (type == ManagerNotificationType.DRIVER_CANCEL_CMD.getType()) {
            notificationEntity.setContent(messageConfig.getMessage("message.notification.driver.cancel.cmd"));
        } else if (type == ManagerNotificationType.DRIVER_CONFIRMED_PICKUP.getType()) {
            notificationEntity.setContent(messageConfig.getMessage("message.notification.driver.confirm.cmd"));
        } else if (type == ManagerNotificationType.CLIENT_CANCEL_TRANSPORT.getType()) {
            notificationEntity.setContent(messageConfig.getMessage("message.notification.client.cancel.ticket"));
        } else if (type == ManagerNotificationType.CLIENT_UPDATE_INFO.getType()) {
            notificationEntity.setContent(messageConfig.getMessage("message.notification.client.update.info"));
        }
        notificationEntity.setType(type);
        if (isPickup) {
            notificationEntity.setMainUrl(NotificationMainURL.URL_TCD.getMainUrl());
        } else {
            notificationEntity.setMainUrl(NotificationMainURL.URL_TCT.getMainUrl());
        }
        notificationRepository.save(notificationEntity);

        bvvIds.forEach(bvvId -> {
            TcNotificationConfigureEntity notificationConfigure = new TcNotificationConfigureEntity();
            notificationConfigure.setNotificationId(notificationEntity.getNotificationId());
            notificationConfigure.setBvvId(bvvId);
            tcNotificationConfigRepository.save(notificationConfigure);
        });
        NotificationManagerDTO notificationManagerDTO = new NotificationManagerDTO();
        BeanUtils.copyProperties(notificationEntity, notificationManagerDTO);
        notificationManagerDTO.setBvvIds(bvvIds);
        redisMessagePublisherService.publishMessage(notificationManagerDTO);

    }

    @Override
    public List<NotificationManagerDTO> getNotificationsToManager(int size) {
        final int firstIndex = 0;
        Page<NotificationEntity> entitiesPage = notificationRepository.findAll(PageRequest.of(firstIndex, size,
                Sort.by("createdDate").descending().and(Sort.by("statusRead").descending())));
        List<NotificationEntity> notificationEntities = entitiesPage.getContent();
        List<NotificationManagerDTO> result = new ArrayList<>();
        notificationEntities.forEach(notificationEntity -> {
            NotificationManagerDTO notificationManagerDTO = new NotificationManagerDTO();
            BeanUtils.copyProperties(notificationEntity, notificationManagerDTO);
            List<TcNotificationConfigureEntity> notificationConfigureEntities = tcNotificationConfigRepository.findByNotificationId(notificationEntity.getNotificationId());
            List<Integer> bvvIds = notificationConfigureEntities.stream().map(TcNotificationConfigureEntity::getBvvId).collect(Collectors.toList());
            notificationManagerDTO.setBvvIds(bvvIds);
            result.add(notificationManagerDTO);
        });

        return result;
    }

    @Override
    @Transactional
    public void updateNotification(NotificationUpdateForm notificationUpdateForm) {
        Optional<NotificationEntity> notificationEntityOptional = notificationRepository.findById(notificationUpdateForm.getNotificationId());
        if (notificationEntityOptional.isPresent()) {
            NotificationEntity notificationEntity = notificationEntityOptional.get();
            if (notificationEntity.getStatusRead()) {
                return;
            }
            notificationEntity.setStatusRead(true);
            notificationRepository.save(notificationEntity);
        }
    }

}
