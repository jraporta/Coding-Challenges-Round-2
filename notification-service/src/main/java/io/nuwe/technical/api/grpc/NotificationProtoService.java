package io.nuwe.technical.api.grpc;

import io.grpc.stub.StreamObserver;
import io.nuwe.technical.api.lib.NotificationProto.*;
import io.nuwe.technical.api.lib.NotificationServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;

import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.services.NotificationService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class NotificationProtoService extends NotificationServiceGrpc.NotificationServiceImplBase {

    private NotificationService notificationService;

    private GrpcClientService grpcClientService;

	private static final Logger log = LoggerFactory.getLogger(NotificationProtoService.class);

    public NotificationProtoService(NotificationService notificationService, GrpcClientService grpcClientService) {
        this.notificationService = notificationService;
        this.grpcClientService = grpcClientService;
    }

    /* TODO: TASK 3 - Complete the method to push notifications*/

    @Override
    public void pushNotification(NotificationRequest req, StreamObserver<NotificationResponse> responseObserver) {

        log.info("Received NotificationRequest: id={}, userSenderId={}, userReceiverId={}, messageId={}, body={}, sentAt={}",
                req.getId(), req.getUserSenderId(), req.getUserReceiverId(), req.getMessageId(), req.getBody(), req.getSentAt());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime sentAt = LocalDateTime.parse(req.getSentAt(), formatter);

        Notification notification = new Notification(
                req.getMessageId(),
                req.getUserSenderId(),
                req.getUserReceiverId(),
                req.getBody(),
                sentAt
        );

        log.info("Sending Notification to FrontEnd: {}", notification);
        boolean notificationArrived = grpcClientService.pushNotification(notification);

        log.info("Sending NotificationResponse: notificationArrived={}", notificationArrived);
        NotificationResponse reply = NotificationResponse.newBuilder()
                .setNotificationArrived(notificationArrived)
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();

        log.info("Saving Notification: {}", notification);
        notification.setNotificationArrived(notificationArrived);
        notificationService.saveNotification(notification);
    }


}
