package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.form.location.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service("RedisService")
public class RedisService {
    private static final Logger LOG = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate template;

    @Value("${havaz.redis.key}")
    private String key;

    public void saveLocationDriver(String id, Point point) {
        template.opsForHash().put(key, id, point);
    }

    public Point getLocationDriver(String id) {
        try {
            return (Point) template.opsForHash().get(key, id);
        } catch (Exception e) {
            LOG.error("Error get point from redis " + e.getMessage(), e);
            return null;
        }
    }
}
