package com.havaz.transport.api.rest;

import com.havaz.transport.api.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
public class NotificationRest {

    @Autowired
    private NotificationService notificationService;

//    @MessageMapping("/hello")
//    public String greeting(MessageHeaders messageHeaders, @Payload String message, @Header(name = "simpSessionId") String sessionId) throws Exception {
//
//        System.out.println("send message to "+sessionId+": "+message);
//        notificationService.sendNotificationToWebSocket("ahihi",message);
//
//        return message;
//    }


    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
    public String greeting(String message) throws Exception {
        notificationService.sendNotificationToWebSocket("/topic/noti", message);
        return message;
    }
}
