package io.nuwe.technical.api.grpc;


import org.springframework.stereotype.Service;

import java.util.Optional;

import io.grpc.StatusRuntimeException;

import io.nuwe.technical.api.entities.*;

import io.nuwe.technical.api.lib.UserProto.*;

import io.nuwe.technical.api.lib.UserServiceGrpc.UserServiceBlockingStub;

import net.devh.boot.grpc.client.inject.GrpcClient;


@Service
public class GrpcClientService {

    @GrpcClient("myself")
    private UserServiceBlockingStub userStub;

    public Optional<User> getUser(final long id) {

        Optional<User> optUser;

        try {
            final UserResponse res = this.userStub.getUser(UserRequest.newBuilder().setId(id).build());
            long resId = res.getId();
            if (resId < 0) {
                optUser = Optional.empty();
            } else {
                User user = new User();
                user.setId(res.getId());
                user.setName(res.getName());
                user.setEmail(res.getEmail());
                user.setAge(res.getAge());
                user.setIsSubscribed(res.getIsSubscribed());
                optUser = Optional.of(user);
            }
            return optUser;
        } catch (final StatusRuntimeException e) {
            optUser = Optional.empty();
            return optUser;
        }
    }
}
