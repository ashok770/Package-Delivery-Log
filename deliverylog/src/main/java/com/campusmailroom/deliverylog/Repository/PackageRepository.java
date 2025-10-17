package com.campusmailroom.deliverylog.repository;

import com.campusmailroom.deliverylog.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    //
}