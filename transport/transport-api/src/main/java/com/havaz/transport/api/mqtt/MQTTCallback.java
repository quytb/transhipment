package com.havaz.transport.api.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.havaz.transport.api.form.location.Point;
import com.havaz.transport.api.model.LocationDTO;
import com.havaz.transport.api.service.impl.RedisService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MQTTCallback {
    private static final Logger log = LoggerFactory.getLogger(MQTTCallback.class);

    @Value("${havaz.mqtt.queue.tracking-driver}")
    private String queueMQTTTrackingDriver;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQTTConnectFactory mqttConnectFactory;

    public void messageArrived() {
        try {
            MqttClient mqttClient = mqttConnectFactory.connect();
            mqttClient.subscribe(queueMQTTTrackingDriver, (topic, msg) -> {
                LocationDTO locationDTO = objectMapper.readValue(msg.getPayload(),
                                                                 LocationDTO.class);
                //tao ra key gom taixeId
                String keyDriver = locationDTO.getTaiXeId() + "";

                //tao ra value la toa do (Point) cua tai xe de luu va redis
                Point valueDriver = new Point(locationDTO.getLatitude(),
                                              locationDTO.getLongitude());

                //luu vao redis
                redisService.saveLocationDriver(keyDriver, valueDriver);

            });
        } catch (Exception ex) {
            log.error("Can not read message mqtt: " + ex.getMessage(), ex);
        }
    }
}
