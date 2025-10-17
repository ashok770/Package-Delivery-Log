package com.campusmailroom.deliverylog.service;

import com.campusmailroom.deliverylog.model.Package;
import com.campusmailroom.deliverylog.model.PackageLog;
import com.campusmailroom.deliverylog.model.User;
import com.campusmailroom.deliverylog.repository.PackageRepository;
import com.campusmailroom.deliverylog.repository.PackageLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;
    private final PackageLogRepository packageLogRepository;
    private final UserService userService;

    @Autowired
    public PackageServiceImpl(PackageRepository packageRepository,
                              PackageLogRepository packageLogRepository,
                              UserService userService) {
        this.packageRepository = packageRepository;
        this.packageLogRepository = packageLogRepository;
        this.userService = userService;
    }

    // --- 1. Log New Package ---
    @Override
    public Package logNewPackage(Package pkg, Long mailroomStaffId) {
        User staff = userService.getUserById(mailroomStaffId);

        pkg.setReceivedAt(new Date());
        pkg.setStatus("Received");

        Package savedPackage = packageRepository.save(pkg);

        PackageLog log = new PackageLog();
        log.setPkg(savedPackage);
        log.setUpdatedBy(staff);
        log.setStatus("Received");
        log.setTimestamp(new Date());
        packageLogRepository.save(log);

        return savedPackage;
    }

    // --- 2. Get Package Details ---
    @Override
    public Package getPackageById(Long packageId) {
        return packageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Package not found with ID: " + packageId));
    }

    // --- 3. Get Packages by Recipient ---
    @Override
    public List<Package> getPackagesByRecipientId(Long recipientId) {
        return packageRepository.findByReceiverUserId(recipientId);
    }

    // --- 4. Update Package Status (FIXED IMPLEMENTATION) ---
    @Override
    public Package updatePackageStatus(Long packageId, String newStatus, Long staffId) {
        Package pkg = getPackageById(packageId);
        User staff = userService.getUserById(staffId);

        // 1. Update the Package status
        pkg.setStatus(newStatus);

        // If status is 'Picked Up', record the actual pickup time
        if (newStatus.equalsIgnoreCase("Picked Up")) {
            pkg.setPickupDate(new Date()); // <-- UPDATED LINE: Using the dedicated pickupDate field
        }

        Package updatedPackage = packageRepository.save(pkg);

        // 2. Create the audit log entry
        PackageLog log = new PackageLog();
        log.setPkg(updatedPackage);
        log.setUpdatedBy(staff);
        log.setStatus(newStatus);
        log.setTimestamp(new Date());
        packageLogRepository.save(log);

        return updatedPackage;
    }

    // --- 5. Get Package History ---
    @Override
    public List<PackageLog> getPackageHistory(Long packageId) {
        return packageLogRepository.findByPkgPackageIdOrderByTimestampAsc(packageId);
    }
}