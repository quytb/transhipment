package com.havaz.transport.api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.havaz.transport.api.model.NotificationManagerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RedisMessageSubscriber implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(RedisMessageSubscriber.class);

    @Value("${havaz.stomp.topic}")
    private String topicNotification;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Start Listen Message");
        try {
            NotificationManagerDTO notificationManagerDTO = objectMapper.readValue(message.toString(),
                    NotificationManagerDTO.class);
            simpMessagingTemplate.convertAndSend(topicNotification, notificationManagerDTO);
        } catch (IOException e) {
            log.error("Error Convert DTO", e);
        }
        log.info("End Listen Message");
    }
}
