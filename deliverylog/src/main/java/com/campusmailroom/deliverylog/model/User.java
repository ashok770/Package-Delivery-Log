package com.campusmailroom.deliverylog.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @Column(length = 20)
    private String contact;

    @Column(length = 20, nullable = false)
    private String role;

    @Column(length = 255)
    private String remarks;

    @Temporal(TemporalType.TIMESTAMP)
    private Date joinDate;
}