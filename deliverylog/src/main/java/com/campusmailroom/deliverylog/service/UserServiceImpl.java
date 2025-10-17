package com.campusmailroom.deliverylog.service;

import com.campusmailroom.deliverylog.model.User;
import com.campusmailroom.deliverylog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // Dependency Injection: Spring provides the UserRepository instance
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerNewUser(User user) {
        // Set the join date before saving
        user.setJoinDate(new Date());

        // Ensure role is not null
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("Student"); // Default role if not specified
        }

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        // Find by ID, or throw an exception (or return null) if not found
        // Optional is used for safety
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    @Override
    public User getUserByEmail(String email) {
        // Note: We need to add a custom method to UserRepository for this!
        // We'll update UserRepository in the next step. For now, let's use a dummy.
        // For now, let's keep it simple by getting all and filtering (bad practice, but temporary)
        // Correct implementation requires a custom repository method.
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}