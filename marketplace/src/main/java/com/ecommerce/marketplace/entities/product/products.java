package com.ecommerce.marketplace.entities.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Table(name = "products")
public class products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Setter
    @Column(name = "product_description", nullable = false, columnDefinition = "TEXT")
    private String productDescription;

    @Setter
    @Column(name = "brand", nullable=false)
    private String brand;

    @Setter
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private productCategories productCategories;

    @Setter
    @OneToMany(mappedBy = "product",orphanRemoval = true)
    private List<productListings> productListingsList;


}
