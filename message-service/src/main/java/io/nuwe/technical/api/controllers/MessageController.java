package io.nuwe.technical.api.controllers;

import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.services.*;

import io.nuwe.technical.api.lib.UserProto.UserResponse;

import io.nuwe.technical.api.grpc.GrpcClientService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
public class MessageController {

    private MessageService messageService;

    private GrpcClientService grpcClientService;

    public MessageController(MessageService messageService, GrpcClientService grpcClientService) {
        this.messageService = messageService;
        this.grpcClientService = grpcClientService;
    }

    @GetMapping("/message/all")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();

        if (messages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/messages/to/{id}")
    public ResponseEntity<List<Message>> getMessagesByUserReceiverId(@PathVariable("id") long id) {

        Optional<UserResponse> user = grpcClientService.getUser(id);

        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        assert user.get().getId() >= 0;

        List<Message> messages = messageService.getAllMessagesByUserReceiverId(id);

        if (messages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/messages/from/{id}")
    public ResponseEntity<?> getMessagesByUserSenderId(@PathVariable("id") long id) {

        Optional<UserResponse> user = grpcClientService.getUser(id);

        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        assert user.get().getId() >= 0 : "User Id should be greater than 0 and is == " + user.get().getId();

        List<Message> messages = messageService.getAllMessagesByUserSenderId(id);


        UserResponse u = user.get();
        String res = "User: " + u.getName() + " and Id " + u.getId();

        if (messages.isEmpty()) {
            return new ResponseEntity<>(res, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/messages/from/{from}/to/{to}")
    public ResponseEntity<List<Message>> getMessagesByUserSenderId(@PathVariable("from") long userSenderId, @PathVariable("to") long userReceiverId) {

        Optional<UserResponse> userSender = grpcClientService.getUser(userSenderId);
        Optional<UserResponse> userReceiver = grpcClientService.getUser(userReceiverId);

        if (userSender.isEmpty() || userReceiver.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Message> messages = messageService.getAllMessagesByUserSenderIdAndUserReceiverId(userSenderId, userReceiverId);

        if (messages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/message/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable("id") int id) {
        Optional<Message> optMessage = messageService.getMessageById(id);

        if (optMessage.isPresent()) {
            return new ResponseEntity<>(optMessage.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /* TODO: TASK 2 */

    @PostMapping("/message")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {

        Optional<UserResponse> userSender = grpcClientService.getUser(message.getUserSenderId());
        Optional<UserResponse> userReceiver = grpcClientService.getUser(message.getUserReceiverId());

        if (userSender.isEmpty() || userReceiver.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        message.setSentAt(LocalDateTime.now());
        messageService.createMessage(message);
        if (userReceiver.get().getIsSubscribed()) {
            boolean pushSuccess = grpcClientService.pushNotification(message);
            if (pushSuccess) {
                log.info("Notification sent");
            } else {
                log.error("Failed to send notification");
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @DeleteMapping("/message/{id}")
    public ResponseEntity<Message> deleteMessageById(@PathVariable("id") int id) {

        Optional<Message> optMessage = messageService.getMessageById(id);

        if (optMessage.isPresent()) {
            Message deletedMessage = messageService.deleteMessage(optMessage.get());
            return new ResponseEntity<>(deletedMessage, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/message/all")
    public ResponseEntity<Message> deleteAllMessages() {
        messageService.deleteAll();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
