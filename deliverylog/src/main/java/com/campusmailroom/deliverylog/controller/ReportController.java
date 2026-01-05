package com.campusmailroom.deliverylog.controller;

import com.campusmailroom.deliverylog.service.PackageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports") // Base URL: /api/reports
public class ReportController {

    private final PackageService packageService;

    // Inject the PackageService containing the reporting logic
    public ReportController(PackageService packageService) {
        this.packageService = packageService;
    }

    // Maps to: GET http://localhost:8080/api/reports/status-summary
    // This endpoint returns a map of all package statuses and their current count.
    @GetMapping("/status-summary")
    public Map<String, Long> getStatusSummary() {
        // Calls the service layer method to execute the aggregate query
        return packageService.getPackageStatusSummary();
    }
}