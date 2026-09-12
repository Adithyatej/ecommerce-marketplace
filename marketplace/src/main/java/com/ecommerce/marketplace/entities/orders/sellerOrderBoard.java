package com.ecommerce.marketplace.entities.orders;

import com.ecommerce.marketplace.entities.seller.Seller;
import com.ecommerce.marketplace.enums.SellerOrderStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "seller_orders")
public class sellerOrderBoard {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "order_id", nullable = false)
        private Order order;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "seller_id", nullable = false)
        private Seller seller;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private SellerOrderStatus status;
    }

