package com.campusmailroom.deliverylog.controller;

import com.campusmailroom.deliverylog.model.Package;
import com.campusmailroom.deliverylog.model.User;
import com.campusmailroom.deliverylog.model.UserDropdownDto;
import com.campusmailroom.deliverylog.repository.PackageRepository;
import com.campusmailroom.deliverylog.service.PackageService;
import com.campusmailroom.deliverylog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebController {

    private final PackageRepository packageRepository;
    private final UserService userService;
    private final PackageService packageService;

    // UPDATED CONSTRUCTOR
    public WebController(PackageRepository packageRepository, UserService userService, PackageService packageService) {
        this.packageRepository = packageRepository;
        this.userService = userService;
        this.packageService = packageService;
    }

    // --- NEW ENDPOINT: Home Page (Navigation Hub) ---
    // Maps to: GET http://localhost:8080/ or http://localhost:8080/index
    @GetMapping({"/", "/index"})
    public String showHomePage() {
        return "index"; // Renders index.html
    }

    // --- NEW ENDPOINT: Reports Page (Frontend View for Report Data) ---
    // Maps to: GET http://localhost:8080/reports
    @GetMapping("/reports")
    public String showReportsPage() {
        return "reports"; // Renders reports.html
    }

    // --- Existing: Dashboard (Pulls pending packages) ---
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<Package> pendingPackages = packageRepository.findByStatus("Received");
        model.addAttribute("packages", pendingPackages);
        return "dashboard";
    }

    // --- Existing: Show New Package Form ---
    @GetMapping("/package/new")
    public String showNewPackageForm(Model model) {
        model.addAttribute("package", new Package());

        List<UserDropdownDto> recipients = userService.getAllRecipientsForDropdown();
        model.addAttribute("recipients", recipients);

        model.addAttribute("staffId", 1L);

        return "new_package";
    }

    // --- Existing: Show Search Page and Handle Search Query ---
    @GetMapping("/search")
    public String showSearchPage(@RequestParam(required = false) Long packageId, Model model) {
        if (packageId != null) {
            try {
                Package pkg = packageService.getPackageById(packageId);
                model.addAttribute("package", pkg);

                User recipient = userService.getUserById(pkg.getReceiver().getUserId());
                model.addAttribute("recipientUser", recipient);

            } catch (RuntimeException e) {
                model.addAttribute("error", "Package ID " + packageId + " not found.");
            }
        }
        model.addAttribute("staffId", 1L);
        return "pickup_search";
    }

    // --- Existing: Handle Pickup Action (POST) ---
    @PostMapping("/package/{packageId}/pickup")
    public String handlePickup(@PathVariable Long packageId, @RequestParam Long staffId) {
        try {
            packageService.updatePackageStatus(packageId, "Picked Up", staffId);

            return "redirect:/dashboard?status=pickedup";

        } catch (RuntimeException e) {
            return "redirect:/search?error=true&packageId=" + packageId;
        }
    }

    // Inside WebController.java

// ... existing code ...

    // --- NEW ENDPOINT: Handle Form Submission (POST) ---
// This handles the data submitted from new_package.html
    @PostMapping("/package/log")
    public String handleNewPackageSubmission(
            @RequestParam String description,
            @RequestParam(required = false) Double weight,
            @RequestParam(required = false) String expectedDelivery,
            @RequestParam(name = "receiver.userId") Long recipientId,
            @RequestParam Long staffId) {

        try {
            // 1. Manually build the Package object from request parameters
            Package newPkg = new Package();
            newPkg.setDescription(description);

            if (weight != null) {
                newPkg.setWeight(java.math.BigDecimal.valueOf(weight));
            }
            // NOTE: Date/Time parsing is complex, skipping for simplicity as before.

            // 2. Set the User FKs using IDs fetched by the service layer
            User recipient = userService.getUserById(recipientId);
            User sender = userService.getUserById(staffId);

            newPkg.setReceiver(recipient);
            newPkg.setSender(sender);

            // 3. Log the package via the service
            packageService.logNewPackage(newPkg, staffId);

            // 4. Redirect to Dashboard with a success flag
            return "redirect:/dashboard?success=log";

        } catch (RuntimeException e) {
            // Redirect back to form with an error flag
            return "redirect:/package/new?error=true";
        }
    }

    // Inside WebController.java

// ... existing code ...

    // --- NEW ENDPOINT: API Documentation View ---
// Maps to: GET http://localhost:8080/api-docs
    @GetMapping("/api-docs")
    public String showApiDocs() {
        return "api_docs"; // Renders api_docs.html
    }
}