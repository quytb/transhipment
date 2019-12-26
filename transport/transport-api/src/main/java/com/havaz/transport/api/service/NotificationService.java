package com.havaz.transport.api.service;

import com.havaz.transport.api.form.NotificationUpdateForm;
import com.havaz.transport.api.model.NotificationManagerDTO;

import java.util.List;

public interface NotificationService {
    void sendNotificationToWebSocket(String topic, String message);

    void saveAndSendNotificationToManager(String topic, Integer type, boolean isPickup, List<Integer> bvvIds);

    List<NotificationManagerDTO> getNotificationsToManager(int size);

    void updateNotification(NotificationUpdateForm notificationUpdateForm);
}
