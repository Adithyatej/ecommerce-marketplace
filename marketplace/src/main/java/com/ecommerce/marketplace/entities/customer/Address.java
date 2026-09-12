package com.ecommerce.marketplace.entities.customer;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Setter
    @Column(name = "flatNo/doorNo", nullable = false)
    private String flatNoOrDoorNo;

    @Setter
    @Column(name = "street", nullable = false)
    private String street;

    @Setter
    @Column(name = "area", nullable = false)
    private String area;

    @Setter
    @Column(name = "city", nullable = false)
    private String city;

    @Setter
    @Column(name = "state", nullable = false)
    private String state;

    @Setter
    @Column(name = "pincode", nullable = false)
    private Long pincode;

    @Setter
    @ManyToOne
    @JoinColumn(name="customer_id",referencedColumnName = "id")
    private Customer customer;


}

