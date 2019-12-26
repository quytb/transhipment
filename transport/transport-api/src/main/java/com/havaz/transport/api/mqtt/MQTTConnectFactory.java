package com.havaz.transport.api.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;

@Component
public class MQTTConnectFactory {

    private static final Logger log = LoggerFactory.getLogger(MQTTConnectFactory.class);

    private static final int MQTT_MAX_INFLIGHT = 1000;

    private String clientID = MqttClient.generateClientId();

    @Value("${havaz.mqtt.url}")
    private String BROKER_URL;

    @Value("${havaz.mqtt.username}")
    private String M2MIO_USERNAME;

    @Value("${havaz.mqtt.password}")
    private String M2MIO_PASSWORD_MD5;

    @Value("${havaz.mqtt.queue.tracking-driver}")
    private String queueMQTTTrackingDriver;

    private final Semaphore semaphore;
    private volatile MqttClient mqttClient = null;

    public MQTTConnectFactory() {
        this.semaphore = new Semaphore(MQTT_MAX_INFLIGHT);
    }

    MqttClient connect() {
        MqttConnectOptions connOpt = new MqttConnectOptions();
        connOpt.setCleanSession(true);
        connOpt.setKeepAliveInterval(60);
        connOpt.setAutomaticReconnect(true);
        connOpt.setMaxInflight(MQTT_MAX_INFLIGHT);
        connOpt.setUserName(M2MIO_USERNAME);
        connOpt.setPassword(M2MIO_PASSWORD_MD5.toCharArray());

        try {
            if (mqttClient == null) {
                mqttClient = new MqttClient(BROKER_URL, clientID, new MemoryPersistence());
            }
            if (!mqttClient.isConnected()) {
                mqttClient.connect(connOpt);
            }
            mqttClient.setCallback(new CustomMqttCallback(semaphore));
        } catch (MqttException e) {
            log.error("Errors connect mqtt: " + e.getMessage(), e);
        }

        return mqttClient;
    }

    Semaphore getSemaphore() {
        return semaphore;
    }

    private static class CustomMqttCallback implements MqttCallback {

        private static final Logger log = LoggerFactory.getLogger(CustomMqttCallback.class);

        private final Semaphore semaphore;

        private CustomMqttCallback(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void connectionLost(Throwable cause) {
            // log.info("Mqtt's connection is lost", cause);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            /*if (log.isDebugEnabled()) {
                log.debug("Semaphore is released");
            }*/
            semaphore.release();
        }
    }

    //    public void subscribe(){
//        MqttClient mqttClient = connect();
//        try {
//            mqttClient.subscribe(queueMQTTTrackingDriver);
//            mqttClient.setCallback(new MQTTCallback());
//        } catch (MqttException e) {
//            e.printStackTrace();
//            LOG.error("Errors subscribe mqtt "+e.getMessage());
//        }
//    }
}
