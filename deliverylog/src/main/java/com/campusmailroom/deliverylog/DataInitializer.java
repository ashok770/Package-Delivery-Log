package com.campusmailroom.deliverylog;

import com.campusmailroom.deliverylog.model.User;
import com.campusmailroom.deliverylog.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;

    // Inject the UserRepository
    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Only run if the Admin user doesn't already exist
        if (userRepository.findByEmail("admin@campus.edu").isEmpty()) {

            System.out.println("--- Seeding Initial Admin User ---");

            User admin = new User();
            admin.setName("Default Mailroom Admin");
            admin.setEmail("admin@campus.edu");
            admin.setRole("Admin"); // CRITICAL: This role is needed for security/logging
            admin.setContact("1234567890");
            admin.setJoinDate(new Date());

            userRepository.save(admin);

            System.out.println("--- Seeding Initial Admin User Complete (ID: 1) ---");

            // OPTIONAL: Seed a sample student user for immediate testing
            User student = new User();
            student.setName("Sample Student User");
            student.setEmail("student@campus.edu");
            student.setRole("Student");
            student.setJoinDate(new Date());
            userRepository.save(student);

            System.out.println("--- Sample Student User Seeded (ID: 2) ---");
        }
    }
}