package com.ecommerce.marketplace.entities.orders;

import com.ecommerce.marketplace.entities.customer.Address;
import com.ecommerce.marketplace.entities.customer.customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "orders_table")
public class orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    private customer customerId;

    @Setter
    @Column(name = "order_date")
    private LocalDate orderDate;

    @Setter
    @Column(name = "order_status")
    private String order_status;

    @Setter
    @ManyToOne
    @JoinColumn(name = "shipping_address_id",referencedColumnName = "id")
    private Address shippingAddress;


}
