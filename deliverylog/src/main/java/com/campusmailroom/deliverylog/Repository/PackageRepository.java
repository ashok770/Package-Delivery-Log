package com.campusmailroom.deliverylog.repository;

import com.campusmailroom.deliverylog.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {

    // Custom method to find all packages for a specific receiver (by their userId)
    List<Package> findByReceiverUserId(Long receiverId);

    // NEW METHOD: Finds all packages that match a specific status (e.g., "Received")
    List<Package> findByStatus(String status);
}