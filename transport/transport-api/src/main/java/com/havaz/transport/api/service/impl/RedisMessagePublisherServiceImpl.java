package com.havaz.transport.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.havaz.transport.api.model.NotificationManagerDTO;
import com.havaz.transport.api.service.RedisMessagePublisherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisMessagePublisherServiceImpl implements RedisMessagePublisherService {
    private static final Logger log = LoggerFactory.getLogger(RedisMessagePublisherServiceImpl.class);

    @Value("${havaz.redis.pubsub.channel}")
    private String channel;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void publishMessage(NotificationManagerDTO notificationManagerDTO) {
        try {
            log.info("Send  Message To Redis");
            String data = objectMapper.writeValueAsString(notificationManagerDTO);
            stringRedisTemplate.convertAndSend(channel, data);
            log.info("Sent Message To Redis");
        } catch (JsonProcessingException e) {
            log.error("error for send Notification To Manager", e);
        }

    }
}
