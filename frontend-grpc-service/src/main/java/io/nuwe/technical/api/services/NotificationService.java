package io.nuwe.technical.api.services;

import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.repositories.*;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository){
	    this.notificationRepository= notificationRepository;
    }

    public List<Notification> getAllNotifications(){
        return notificationRepository.findAll();
    }

    public void saveNotification(Notification n){
	notificationRepository.save(n);
    }
}
