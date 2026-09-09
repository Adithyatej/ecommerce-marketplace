package com.ecommerce.marketplace.Repository.cart;

import com.ecommerce.marketplace.entities.cart.cartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface cartItemsRepo extends JpaRepository<cartItems,Long> {

    @Query(value = "SELECT * FROM CART_ITEMS WHERE CART_ID=?1 AND LISTING_ID=?2",nativeQuery = true)
    cartItems findCartItemByCartIdAndListingId(Long cart_id,Long listing_id);

    @Modifying
    @Query(value = "UPDATE CART_ITEMS SET QUANTITY=?1 WHERE id=?2",nativeQuery = true)
    Integer updateQuantity(Integer quantity,Long id);
}
