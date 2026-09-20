package com.ecommerce.marketplace.entities.orders;

import com.ecommerce.marketplace.entities.seller.Seller;
import com.ecommerce.marketplace.enums.SellerOrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
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

        @Setter
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "order_id", nullable = false)
        private Orders orders;

        @Setter
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "seller_id", nullable = false)
        private Seller seller;

        @Setter
        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false)
        private SellerOrderStatus status;


        @OneToMany(mappedBy = "sellerOrder",cascade = CascadeType.ALL)
        private List<orderItems> orderItem =new ArrayList<>();

    }

