package com.campusmailroom.deliverylog.controller;

import com.campusmailroom.deliverylog.model.Package;
// Remove User import if no longer needed, use UserDropdownDto instead
import com.campusmailroom.deliverylog.model.UserDropdownDto; // <-- NEW IMPORT
import com.campusmailroom.deliverylog.repository.PackageRepository;
import com.campusmailroom.deliverylog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebController {

    private final PackageRepository packageRepository;
    private final UserService userService;

    public WebController(PackageRepository packageRepository, UserService userService) {
        this.packageRepository = packageRepository;
        this.userService = userService;
    }

    // Maps to: GET http://localhost:8080/dashboard (Pulls pending packages)
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<Package> pendingPackages = packageRepository.findByStatus("Received");
        model.addAttribute("packages", pendingPackages);
        return "dashboard";
    }

    // --- NEW ENDPOINT: Show New Package Form (FINAL FIX HERE) ---
    @GetMapping("/package/new")
    public String showNewPackageForm(Model model) {
        model.addAttribute("package", new Package());

        // CRITICAL FIX: Fetch safe DTOs for recipients
        List<UserDropdownDto> recipients = userService.getAllRecipientsForDropdown();
        model.addAttribute("recipients", recipients);

        // Fetch staff ID (Assuming User ID 1 is the Admin)
        model.addAttribute("staffId", 1L); // Hardcode for now, will be fixed by security later

        return "new_package";
    }
}