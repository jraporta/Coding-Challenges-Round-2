package io.nuwe.technical.api.grpc;

import io.grpc.stub.StreamObserver;
import io.nuwe.technical.api.lib.UserProto.*;
import io.nuwe.technical.api.lib.UserServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;

import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.services.UserService;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Example grpc server service implementation class.
 */
@GrpcService
public class UserProtoService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserService userService;

    @Override
    public void getUser(UserRequest req, StreamObserver<UserResponse> responseObserver) {
	long id = (long)req.getId();
	Optional<User> optUser = this.userService.getUserById(id);

	final UserResponse reply;
	if (optUser.isPresent()){
	    User user = optUser.get();
	    reply = UserResponse.newBuilder()
		.setId(user.getId())
		.setName(user.getName())
		.setEmail(user.getEmail())
		.setAge(user.getAge())
		.setIsSubscribed(user.getIsSubscribed())
		.build();
	}else {
	    reply = UserResponse.newBuilder().setId(-1).build();
	}

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
