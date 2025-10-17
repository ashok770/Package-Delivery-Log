package com.campusmailroom.deliverylog.service;

import com.campusmailroom.deliverylog.model.User;
import java.util.List;

public interface UserService {

    // 1. Create a new user (Staff/Student/Admin)
    User registerNewUser(User user);

    // 2. Find a user by their ID (for lookups)
    User getUserById(Long userId);

    // 3. Find a user by their unique email (useful for login/check)
    User getUserByEmail(String email);

    // 4. Get all users
    List<User> getAllUsers();
}