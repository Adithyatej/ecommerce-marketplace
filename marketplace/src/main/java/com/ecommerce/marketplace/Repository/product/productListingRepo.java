package com.ecommerce.marketplace.Repository.product;

import com.ecommerce.marketplace.entities.product.productListings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface productListingRepo extends JpaRepository<productListings,Long> {
}
