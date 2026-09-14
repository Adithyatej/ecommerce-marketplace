package com.ecommerce.marketplace.entities.orders;

import com.ecommerce.marketplace.entities.customer.Customer;
import com.ecommerce.marketplace.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "orders_table")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    private Customer customer;

    @Setter
    @Column(name = "total_amount",scale = 2, precision = 12,nullable = false)
    private BigDecimal totalAmount;

    @Setter
    @Column(name = "order_date")
    private LocalDateTime createdAt;

    @Setter
    @Column(name = "order_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Setter
    @Embedded
    private ShippingAddress shippingAddress;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<sellerOrderBoard> sellerOrders = new ArrayList<>();


}
