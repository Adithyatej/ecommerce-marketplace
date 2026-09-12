package com.ecommerce.marketplace.entities.cart;

import com.ecommerce.marketplace.entities.customer.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Table(name = "customer_cart")
public class customerCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;



    @Setter
    @OneToMany(mappedBy = "cart",orphanRemoval = true)
    private List<cartItems> items;

}
