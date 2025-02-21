package io.nuwe.technical.api.controllers;

import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.services.*;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {

	/* TODO TASK 1: complete the methods */

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id){
        Optional<User> optUser = userService.getUserById(id);
        return optUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user){
        if (user.getName() == null || user.getEmail() == null || user.getAge() < 1 || user.getAge() > 100) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        user.setIsSubscribed(true);
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable("id") int id){
        Optional<User> optUser = userService.getUserById(id);
        if (optUser.isPresent()) {
            User deletedUser = userService.deleteUser(optUser.get());
            return ResponseEntity.ok().body(deletedUser);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/user/all")
    public ResponseEntity<User> deleteAllUsers(){
        userService.deleteAll();
        return ResponseEntity.ok().build();
    }
}
