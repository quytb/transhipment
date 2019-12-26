package com.havaz.transport.api.service;

import com.havaz.transport.api.model.NotificationManagerDTO;

public interface RedisMessagePublisherService {
    void publishMessage(NotificationManagerDTO notificationManagerDTO);
}
