package com.ecommerce.marketplace.entities.customer;


import com.ecommerce.marketplace.entities.user;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Table(name = "customer")
public class Customer extends user {


    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
    @Column(name="balance_points",nullable = false)
    @ColumnDefault("0.0")
    private double balancePoints;


    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;


}
