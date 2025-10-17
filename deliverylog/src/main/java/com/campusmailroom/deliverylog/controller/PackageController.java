package com.campusmailroom.deliverylog.controller;

import com.campusmailroom.deliverylog.model.Package;
import com.campusmailroom.deliverylog.model.PackageLog;
import com.campusmailroom.deliverylog.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages") // Base URL: /api/packages
public class PackageController {

    private final PackageService packageService;

    @Autowired
    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    // Endpoint 1: Log a New Package (CREATE)
    // Maps to: POST /api/packages?staffId={id}
    @PostMapping
    public ResponseEntity<Package> logPackage(
            @RequestBody Package pkg,
            @RequestParam Long staffId) { // Mailroom staff ID is passed as a query parameter

        Package savedPackage = packageService.logNewPackage(pkg, staffId);
        return new ResponseEntity<>(savedPackage, HttpStatus.CREATED);
    }

    // Endpoint 2: Get Package Details by ID (READ)
    // Maps to: GET /api/packages/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Package> getPackageById(@PathVariable Long id) {
        try {
            Package pkg = packageService.getPackageById(id);
            return new ResponseEntity<>(pkg, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint 3: Get Packages by Recipient ID (READ for user/staff view)
    // Maps to: GET /api/packages/user/{id}
    @GetMapping("/user/{recipientId}")
    public ResponseEntity<List<Package>> getPackagesForRecipient(@PathVariable Long recipientId) {
        List<Package> packages = packageService.getPackagesByRecipientId(recipientId);
        return new ResponseEntity<>(packages, HttpStatus.OK);
    }

    // Endpoint 4: Update Package Status (UPDATE - Mark as Picked Up)
    // Maps to: POST /api/packages/{id}/status?staffId={id}&status={newStatus}
    @PostMapping("/{packageId}/status")
    public ResponseEntity<Package> updateStatus(
            @PathVariable Long packageId,
            @RequestParam String status,
            @RequestParam Long staffId) {

        try {
            Package updatedPkg = packageService.updatePackageStatus(packageId, status, staffId);
            return new ResponseEntity<>(updatedPkg, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Catches "Package Not Found" or future business rule exceptions
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint 5: Get Package History/Log (READ)
    // Maps to: GET /api/packages/{id}/logs
    @GetMapping("/{packageId}/logs")
    public ResponseEntity<List<PackageLog>> getPackageHistory(@PathVariable Long packageId) {
        List<PackageLog> logs = packageService.getPackageHistory(packageId);
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }
}