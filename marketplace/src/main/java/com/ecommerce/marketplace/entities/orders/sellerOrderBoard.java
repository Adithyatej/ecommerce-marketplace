package com.ecommerce.marketplace.entities.orders;

import com.ecommerce.marketplace.entities.seller.Seller;
import com.ecommerce.marketplace.enums.SellerOrderStatus;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seller_orders",
uniqueConstraints = @UniqueConstraint(
        name = "unique_seller_order",
        columnNames = {"order_id","seller_id"}
))
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


        @OneToMany(mappedBy = "sellerOrder")
        private List<orderItems> orderItem =new ArrayList<>();
    }

