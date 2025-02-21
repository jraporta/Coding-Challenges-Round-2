package io.nuwe.technical.api.controllers;

import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.services.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/all")
    public ResponseEntity<List<Notification>> getAllNotifications(){
	List<Notification> notifications = notificationService.getAllNotifications();

	if (notifications.isEmpty()){
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	return new ResponseEntity<>(notifications, HttpStatus.OK);
    }
}
