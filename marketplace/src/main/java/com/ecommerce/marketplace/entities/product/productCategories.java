package com.ecommerce.marketplace.entities.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Table(name="product_categories")
public class productCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Setter
    @ManyToOne
    @JoinColumn(name = "parent_category_id",referencedColumnName = "id")
    private productCategories parentCategoryId;


    @OneToMany(mappedBy = "parentCategoryId",cascade = CascadeType.ALL)
    private List<productCategories> categoriesList;

    @Setter
    @OneToMany(mappedBy = "productCategories", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<products> products;




}
