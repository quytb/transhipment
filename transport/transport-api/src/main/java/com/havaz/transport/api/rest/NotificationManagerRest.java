package com.havaz.transport.api.rest;

import com.havaz.transport.api.common.ResultMessage;
import com.havaz.transport.api.form.NotificationUpdateForm;
import com.havaz.transport.api.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/notification")
public class NotificationManagerRest {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/list")
    public ResponseEntity getNotification(@RequestParam(name = "size", required = false, defaultValue = "20") int size) {

        return new ResponseEntity<>(notificationService.getNotificationsToManager(size), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateStatusReadNotificaiton(@RequestBody NotificationUpdateForm notificationUpdateForm) {
        notificationService.updateNotification(notificationUpdateForm);
        return new ResponseEntity<>(new ResultMessage("success", "Lưu trạng thái thành công"), HttpStatus.OK);
    }
}
