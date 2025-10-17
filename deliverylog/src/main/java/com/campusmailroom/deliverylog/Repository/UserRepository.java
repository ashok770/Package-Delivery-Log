package com.campusmailroom.deliverylog.repository;

import com.campusmailroom.deliverylog.model.User;
import com.campusmailroom.deliverylog.model.UserDropdownDto; // Import the DTO
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA query method
    Optional<User> findByEmail(String email);

    // Custom JPQL Query to fetch safe DTOs for the dropdown
    @Query("SELECT new com.campusmailroom.deliverylog.model.UserDropdownDto(u.userId, u.name, u.email) FROM User u")
    List<UserDropdownDto> findAllForDropdown();
}