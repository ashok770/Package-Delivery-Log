package com.campusmailroom.deliverylog.repository;

import com.campusmailroom.deliverylog.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // NEW IMPORT for custom query
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {

    // Custom method to find all packages for a specific receiver (by their userId)
    List<Package> findByReceiverUserId(Long receiverId);

    // Finds all packages that match a specific status (used for the dashboard filter)
    List<Package> findByStatus(String status);

    // NEW METHOD: Custom JPQL Query to group packages by status and count them.
    // This is required for the Reporting feature.
    @Query("SELECT p.status, COUNT(p) FROM Package p GROUP BY p.status")
    List<Object[]> countPackagesByStatus();
}