package com.ecommerce.marketplace.entities.customer;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Table(name = "security_tokens")
@Entity
public class Securitytoken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Setter
    @Column(name = "token",nullable = false)
    private String hashToken;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id",referencedColumnName = "id")
    private Customer customer;

    @Setter
    @Column(name = "expiry_date_time",nullable = false)
    private LocalDateTime expiresAt;

    @Setter
    @Column(name = "revoked", nullable = false)
    private boolean revoked;
}
