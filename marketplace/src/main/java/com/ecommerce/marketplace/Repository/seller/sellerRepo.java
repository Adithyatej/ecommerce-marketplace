package com.ecommerce.marketplace.Repository.seller;

import com.ecommerce.marketplace.entities.seller.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface sellerRepo extends JpaRepository<Seller,Long> {

    @Query(value = "SELECT * FROM SELLERS WHERE email = ?1 or phone = ?2", nativeQuery = true)
    public Seller findByEmailOrPhone(String email, String phone);


    Seller findByEmail(String seller);


    @Query(value = "SELECT DISTINCT S.* FROM SELLERS S JOIN PRODUCT_LISTINGS PL ON PL.SELLER_ID=S.ID JOIN CART_ITEMS CI ON CI.LISTING_ID=PL.ID WHERE PL.ID IN (:items)",nativeQuery = true)
    List<Seller> findSellersByListing(@Param("items") List<Long> listings);
}
