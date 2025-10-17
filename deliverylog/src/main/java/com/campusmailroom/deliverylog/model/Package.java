package com.campusmailroom.deliverylog.model;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "packages")
@Data
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = true)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
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
}