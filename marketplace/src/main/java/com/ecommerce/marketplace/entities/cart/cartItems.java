package com.ecommerce.marketplace.entities.cart;


import com.ecommerce.marketplace.entities.product.productListings;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "cart_items")
public class cartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id",referencedColumnName = "id")
    private customerCart cart;


    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id",referencedColumnName = "id")
    private productListings productListings;

    @Setter
    @Column(name = "quantity")
    private Integer quantity;

}
