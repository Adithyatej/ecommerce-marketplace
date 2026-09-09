package com.ecommerce.marketplace.Repository.cart;

import com.ecommerce.marketplace.entities.cart.customerCart;
import com.ecommerce.marketplace.projections.products.customerCartResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface customerCartRepo extends JpaRepository<customerCart, Long> {

    @Query(value = "SELECT CC.* FROM CUSTOMER_CART CC JOIN CUSTOMER C ON CC.CUSTOMER_ID=C.ID WHERE C.ID=?1",nativeQuery = true)
    customerCart findByCustomerId(Long id);


    @Query(value = "SELECT P.PRODUCT_NAME AS productName, P.BRAND AS brand, S.STORE_NAME AS storeName, PL.PRICE AS price, CI.QUANTITY AS quantity FROM CART_ITEMS CI JOIN CUSTOMER_CART CC ON CI.CART_ID=CC.ID JOIN CUSTOMER C ON CC.CUSTOMER_ID=C.ID JOIN PRODUCT_LISTINGS PL ON CI.LISTING_ID=PL.ID JOIN PRODUCTS P ON PL.PRODUCT_ID=P.ID JOIN SELLERS S ON PL.SELLER_ID = S.ID WHERE C.EMAIL=?1",nativeQuery = true)
    List<customerCartResponse> viewCartByCustomer(String email);

}
