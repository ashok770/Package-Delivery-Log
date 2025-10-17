package com.campusmailroom.deliverylog.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore; // <-- NEW IMPORT

@Entity
@Table(name = "package_logs")
@Data
public class PackageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    // FIX: Ignore the full Package object during serialization
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    @JsonIgnore // <-- Add this
    private Package pkg;

    // FIX: Ignore the full User object during serialization
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    @JsonIgnore // <-- Add this
    private User updatedBy;

    @Column(length = 20, nullable = false)
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
}