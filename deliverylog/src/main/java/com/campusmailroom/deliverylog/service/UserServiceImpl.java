package com.campusmailroom.deliverylog.service;

import com.campusmailroom.deliverylog.model.User;
import com.campusmailroom.deliverylog.model.UserDropdownDto; // Must be imported
import com.campusmailroom.deliverylog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerNewUser(User user) {
        user.setJoinDate(new Date());

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("Student");
        }

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // CRITICAL FIX: Implementation for the abstract method is ADDED here.
    @Override
    public List<UserDropdownDto> getAllRecipientsForDropdown() {
        // Delegates to the custom repository method to fetch the safe DTOs.
        return userRepository.findAllForDropdown();
    }
}