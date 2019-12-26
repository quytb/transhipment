package com.havaz.transport.api.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class MqttPublisher {

    private static final Logger log = LoggerFactory.getLogger(MqttPublisher.class);

    @Value("${havaz.mqtt.queue.tracking-driver}")
    private String queueMQTTTrackingDriver;

    @Autowired
    private MQTTConnectFactory mqttConnectFactory;

    public void publish(String content, List<String> topics) {
        boolean acquired = false;
        final int pubQoS = 1;
        if (topics == null) {
            topics = new ArrayList<>(1);
        }
        topics.add(queueMQTTTrackingDriver);
        MqttClient mqttClient = mqttConnectFactory.connect();
        for (String topic : topics) {
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(pubQoS);
            message.setRetained(true);
            try {
                acquired = mqttConnectFactory.getSemaphore().tryAcquire(2, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }

            try {
                if (acquired) {
                    mqttClient.publish(topic, message);
                }
            } catch (MqttException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
