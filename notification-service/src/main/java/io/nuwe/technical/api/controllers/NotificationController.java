package io.nuwe.technical.api.controllers;

import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.services.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notification/all")
    public ResponseEntity<List<Notification>> getAllNotifications(){
	List<Notification> notifications = notificationService.getAllNotifications();

	if (notifications.isEmpty()){
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	return new ResponseEntity<>(notifications, HttpStatus.OK);
    }
}
