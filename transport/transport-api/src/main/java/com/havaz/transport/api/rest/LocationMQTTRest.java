package com.havaz.transport.api.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.havaz.transport.api.form.PushTopicForm;
import com.havaz.transport.api.mqtt.MqttPublisher;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/location")
public class LocationMQTTRest {

    private static final Logger log = LoggerFactory.getLogger(LocationMQTTRest.class);

    @Autowired
    private MqttPublisher mqttPublisher;

    @Autowired
    private ObjectMapper objectMapper;

    @ApiOperation(value = "Gửi thông tin cho MQTT")
    @PostMapping("/push")
    public ResponseEntity<String> pushLocation(@RequestBody PushTopicForm pushTopicForm) {
        log.debug("Gửi thông tin cho MQTT");
        try {
            mqttPublisher.publish(objectMapper.writeValueAsString(pushTopicForm.getContent()),
                                  pushTopicForm.getTopics());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Sending successfully", HttpStatus.OK);
    }
}
