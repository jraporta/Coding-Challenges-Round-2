package io.nuwe.technical.api.grpc;


import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

import io.grpc.StatusRuntimeException;

import io.nuwe.technical.api.entities.*;

import io.nuwe.technical.api.lib.NotificationProto.*;
import io.nuwe.technical.api.lib.NotificationServiceGrpc.NotificationServiceBlockingStub;

import net.devh.boot.grpc.client.inject.GrpcClient;


@Service
public class GrpcClientService {

	/* TODO: TASK 3 - Complete the file to fix communication between notification and frontend*/

    @GrpcClient("frontend")
    private NotificationServiceBlockingStub notificationStub;

    public boolean pushNotification(final Notification notification){
		try {
			DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

			NotificationRequest req = NotificationRequest.newBuilder()
					.setId(notification.getId())
					.setUserSenderId(notification.getUserSenderId())
					.setUserReceiverId(notification.getUserReceiverId())
					.setMessageId(notification.getMessageId())
					.setBody(notification.getBody())
					.setSentAt(notification.getSentAt().format(formatter))
					.build();

			final NotificationResponse res = this.notificationStub.pushNotification(req);

            return res != null && res.getNotificationArrived();

        } catch (final StatusRuntimeException e){
			return false;
		}
    }
}
