package com.ecommerce.marketplace.Repository.product;

import com.ecommerce.marketplace.entities.product.products;
import com.ecommerce.marketplace.projections.products.productResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface productRepo extends JpaRepository<products,Long> {

    @Query(value = "SELECT p.product_name AS productName,p.brand AS brand,pc.category_name AS productCategory FROM PRODUCTS P JOIN PRODUCT_CATEGORIES PC ON P.CATEGORY_ID = PC.ID",nativeQuery = true)
    List<productResponse> findProductsWithCategory();

    products findByProductName(String name);

    @Query(value = "DELETE FROM PRODUCTS P WHERE P.PRODUCT_NAME =?1",nativeQuery = true)
    Optional<products> deleteByProductName(String name);
}
