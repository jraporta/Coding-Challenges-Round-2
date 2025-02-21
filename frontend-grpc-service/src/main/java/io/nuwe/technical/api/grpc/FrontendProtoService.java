package io.nuwe.technical.api.grpc;

import io.grpc.stub.StreamObserver;
import io.nuwe.technical.api.lib.NotificationProto.*;
import io.nuwe.technical.api.lib.NotificationServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.nuwe.technical.api.entities.Notification;
import io.nuwe.technical.api.repositories.NotificationRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class FrontendProtoService extends NotificationServiceGrpc.NotificationServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(FrontendProtoService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void pushNotification(NotificationRequest req, StreamObserver<NotificationResponse> responseObserver) {
        try {
            logger.info("Received NotificationRequest: id={}, userSenderId={}, userReceiverId={}, messageId={}, body={}, sentAt={}",
                    req.getId(), req.getUserSenderId(), req.getUserReceiverId(), req.getMessageId(), req.getBody(), req.getSentAt());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
            LocalDateTime sentAt = LocalDateTime.parse(req.getSentAt(), formatter);

            Notification notification = new Notification();
            notification.setId(req.getId());
            notification.setUserSenderId(req.getUserSenderId());
            notification.setUserReceiverId(req.getUserReceiverId());
            notification.setMessageId(req.getMessageId());
            notification.setBody(req.getBody());
            notification.setSentAt(sentAt);
            notification.setNotificationArrived(true);

            logger.info("Saving Notification: {}", notification);

            notificationRepository.save(notification);

            NotificationResponse reply = NotificationResponse.newBuilder()
                    .setNotificationArrived(true)
                    .build();

            logger.info("Sending NotificationResponse: notificationArrived={}", reply.getNotificationArrived());

            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        } catch (Exception e) {
            logger.error("Error processing NotificationRequest: {}", req, e);
            responseObserver.onError(e);
        }
    }
}
