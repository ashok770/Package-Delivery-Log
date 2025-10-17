package com.campusmailroom.deliverylog.controller;

import com.campusmailroom.deliverylog.model.User;
import com.campusmailroom.deliverylog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users") // Base URL for all endpoints in this controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint 1: Register a New User (Mailroom Staff or Student)
    // Maps to: POST /api/users
    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerNewUser(user);
        // Returns the created user with a 201 Created status
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    // Endpoint 2: Get All Users (Admin/Staff View)
    // Maps to: GET /api/users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        // Returns the list of users with a 200 OK status
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Endpoint 3: Get User Details by ID
    // Maps to: GET /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            // Returns the user with a 200 OK status
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Returns a 404 Not Found status if the user doesn't exist
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}