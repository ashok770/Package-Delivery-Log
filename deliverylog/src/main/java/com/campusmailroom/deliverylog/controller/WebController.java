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
}