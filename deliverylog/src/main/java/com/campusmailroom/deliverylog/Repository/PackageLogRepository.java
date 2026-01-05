package com.campusmailroom.deliverylog.repository;

import com.campusmailroom.deliverylog.model.PackageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Import List

@Repository
public interface PackageLogRepository extends JpaRepository<PackageLog, Long> {

    // Custom method to get all logs for a package, ordered by time
    List<PackageLog> findByPkgPackageIdOrderByTimestampAsc(Long packageId);
}