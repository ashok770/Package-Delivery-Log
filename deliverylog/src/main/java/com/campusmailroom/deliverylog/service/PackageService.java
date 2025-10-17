package com.campusmailroom.deliverylog.service;

import com.campusmailroom.deliverylog.model.Package;
import com.campusmailroom.deliverylog.model.PackageLog;
import java.util.List;

public interface PackageService {

    // 1. Log a new package when it arrives (CREATE)
    Package logNewPackage(Package pkg, Long mailroomStaffId);

    // 2. Get details for a single package
    Package getPackageById(Long packageId);

    // 3. Get all packages for a specific recipient (User's view)
    List<Package> getPackagesByRecipientId(Long recipientId);

    // 4. Update the package status (e.g., mark as 'Picked Up')
    Package updatePackageStatus(Long packageId, String newStatus, Long staffId);

    // 5. Get the history/log for a specific package
    List<PackageLog> getPackageHistory(Long packageId);
}