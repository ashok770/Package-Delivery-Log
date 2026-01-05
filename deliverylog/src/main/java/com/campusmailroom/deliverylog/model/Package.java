package com.campusmailroom.deliverylog.model;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List; // For the logs list
import com.fasterxml.jackson.annotation.JsonIgnore; // <-- NEW IMPORT for FIX

@Entity
@Table(name = "packages")
@Data
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;

    // FIX: Using @JsonIgnore to prevent Jackson crash on lazy-loaded sender data
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = true)
    @JsonIgnore // <-- Add this
    private User sender;

    // FIX: Using @JsonIgnore to prevent Jackson crash on lazy-loaded receiver data
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    @JsonIgnore // <-- Add this
    private User receiver;

    @Column(length = 255)
    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(length = 20, nullable = false)
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expectedDelivery;

    @Temporal(TemporalType.TIMESTAMP)
    private Date pickupDate;

    // Inverse relationship to PackageLog table (Audit Trail)
    // FIX: Must ignore this during serialization to avoid infinite loop
    @OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // <-- Add this
    private List<PackageLog> logs;
}