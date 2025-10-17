package com.campusmailroom.deliverylog.repository;

import com.campusmailroom.deliverylog.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Import List

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {

    // Custom method to find all packages for a specific receiver (by their userId)
    List<Package> findByReceiverUserId(Long receiverId);
}