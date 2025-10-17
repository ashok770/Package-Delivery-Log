package com.campusmailroom.deliverylog.repository;

import com.campusmailroom.deliverylog.model.PackageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageLogRepository extends JpaRepository<PackageLog, Long> {
    //
}