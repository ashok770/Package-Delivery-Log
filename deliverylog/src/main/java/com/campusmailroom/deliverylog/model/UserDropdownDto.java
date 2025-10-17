package com.campusmailroom.deliverylog.model;

import lombok.AllArgsConstructor;
import lombok.Data;

// This DTO only holds the ID and Name—simple, safe data for the frontend.
@Data
@AllArgsConstructor
public class UserDropdownDto {
    private Long id;
    private String name;
    private String email;
}