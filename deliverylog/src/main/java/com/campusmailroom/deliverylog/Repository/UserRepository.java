package com.campusmailroom.deliverylog.repository;

import com.campusmailroom.deliverylog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // <-- Import the Optional class

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a User by their email address.
     * Spring Data JPA automatically generates the query for this method.
     */
    Optional<User> findByEmail(String email);
}