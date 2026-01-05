package com.campusmailroom.deliverylog.controller;

import com.campusmailroom.deliverylog.model.Package;
import com.campusmailroom.deliverylog.model.PackageLog;
import com.campusmailroom.deliverylog.model.User; // ADDED: Required for setting Sender/Receiver
import com.campusmailroom.deliverylog.service.PackageService;
import com.campusmailroom.deliverylog.service.UserService; // ADDED: Required for looking up User objects
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal; // ADDED: Required for handling weight
import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final PackageService packageService;
    private final UserService userService; // ADDED: Inject UserService to fetch User objects

    @Autowired
    public PackageController(PackageService packageService, UserService userService) {
        this.packageService = packageService;
        this.userService = userService;
    }

    // Endpoint 1: Log a New Package (CREATE)
    // FIX: Changed from @RequestBody to @RequestParam to accept HTML form data
    @PostMapping
    public ResponseEntity<Package> logPackage(
            @RequestParam String description,
            @RequestParam(required = false) Double weight,
            @RequestParam(required = false) String expectedDelivery, // Accepted as String from form
            @RequestParam(name = "receiver.userId") Long recipientId, // Gets the selected user ID
            @RequestParam Long staffId) {

        // 1. Manually build the Package object from request parameters
        Package newPkg = new Package();
        newPkg.setDescription(description);

        // Handle optional weight field conversion
        if (weight != null) {
            newPkg.setWeight(BigDecimal.valueOf(weight));
        }

        // 2. Set the User FKs using IDs fetched by the service layer
        User recipient = userService.getUserById(recipientId);
        User sender = userService.getUserById(staffId); // Using staff as placeholder sender ID

        newPkg.setReceiver(recipient);
        newPkg.setSender(sender);

        // Note: Skipping complex Date parsing from String for simplicity (optional field)

        // 3. Log the package via the service (this also sets status to 'Received')
        Package savedPackage = packageService.logNewPackage(newPkg, staffId);

        return new ResponseEntity<>(savedPackage, HttpStatus.CREATED);
    }

    // Endpoint 2: Get Package Details by ID (READ)
    // ... (This endpoint remains the same)
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
    // ... (This endpoint remains the same)
    @GetMapping("/user/{recipientId}")
    public ResponseEntity<List<Package>> getPackagesForRecipient(@PathVariable Long recipientId) {
        List<Package> packages = packageService.getPackagesByRecipientId(recipientId);
        return new ResponseEntity<>(packages, HttpStatus.OK);
    }

    // Endpoint 4: Update Package Status (UPDATE - Mark as Picked Up)
    // ... (This endpoint remains the same)
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
    // ... (This endpoint remains the same)
    @GetMapping("/{packageId}/logs")
    public ResponseEntity<List<PackageLog>> getPackageHistory(@PathVariable Long packageId) {
        List<PackageLog> logs = packageService.getPackageHistory(packageId);
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }
}