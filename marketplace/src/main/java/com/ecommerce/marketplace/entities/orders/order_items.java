package com.ecommerce.marketplace.entities.orders;


import com.ecommerce.marketplace.entities.product.productListings;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "order_items")
public class order_items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private orders orderId;

    @ManyToOne
    @JoinColumn(name = "productListing_id",referencedColumnName = "id")
    private productListings productListingsId;

    @Setter
    @Column(name = "order_quantity")
    private Integer quantity;

    @Setter
    @Column(name = "unit price")
    private Double unitPrice;



}
