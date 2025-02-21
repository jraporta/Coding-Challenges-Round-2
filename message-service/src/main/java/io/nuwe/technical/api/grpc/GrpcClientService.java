package io.nuwe.technical.api.grpc;


import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import io.grpc.StatusRuntimeException;

import io.nuwe.technical.api.entities.*;

import io.nuwe.technical.api.lib.UserProto.*;
import io.nuwe.technical.api.lib.UserServiceGrpc.UserServiceBlockingStub;

import io.nuwe.technical.api.lib.NotificationProto.*;
import io.nuwe.technical.api.lib.NotificationServiceGrpc.NotificationServiceBlockingStub;

import net.devh.boot.grpc.client.inject.GrpcClient;


@Service
public class GrpcClientService {

    @GrpcClient("user")
    private UserServiceBlockingStub userStub;

    @GrpcClient("notification")
    private NotificationServiceBlockingStub notificationStub;

    public Optional<UserResponse> getUser(final long id){
	try {
	    final UserResponse res = this.userStub.getUser(UserRequest.newBuilder().setId(id).build());
	    if (res == null || res.getId() < 0){
		return Optional.empty();
	    }
	    return Optional.of(res);
	} catch (final StatusRuntimeException e){
	    return Optional.empty();
	}
    }

    public boolean pushNotification(Message message){
	try {
	    DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

	    NotificationRequest.Builder notificationBuilder  = NotificationRequest.newBuilder()
		.setId(0)
		.setUserSenderId(message.getUserSenderId())
		.setUserReceiverId(message.getUserReceiverId())
		.setUserSenderId(message.getUserSenderId())
		.setMessageId(message.getId())
		.setBody(message.getBody())
		.setSentAt(message.getSentAt().format(formatter));

	    final NotificationResponse res = this.notificationStub.pushNotification(notificationBuilder.build());
        return res != null && res.getNotificationArrived();
    } catch (final StatusRuntimeException e){
	    return false;
	}
    }
}
