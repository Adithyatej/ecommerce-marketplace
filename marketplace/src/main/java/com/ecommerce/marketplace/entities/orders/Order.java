package com.ecommerce.marketplace.entities.orders;

import com.ecommerce.marketplace.entities.customer.Address;
import com.ecommerce.marketplace.entities.customer.Customer;
import com.ecommerce.marketplace.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "orders_table")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    private Customer customerId;

    @Setter
    @Column(name = "total_amount")
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


}
