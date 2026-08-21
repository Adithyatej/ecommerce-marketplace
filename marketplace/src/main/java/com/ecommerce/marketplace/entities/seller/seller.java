package com.ecommerce.marketplace.entities.seller;

import com.ecommerce.marketplace.entities.product.productListings;
import com.ecommerce.marketplace.entities.user;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Table(name = "sellers")
public class seller extends user {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Setter
    @Column(name = "store_name",nullable = false)
    private String storeName;

    @Setter
    @Column(name="store_description")
    private String storeDescription;

    @Setter
    @Column(name="rating")
    private double rating;

    @Setter
    @Embedded
    private sellerAddress address;

    @Setter
    @OneToMany(mappedBy = "seller")
    private List<productListings> sellerproductListingsList;



}
