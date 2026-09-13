package com.ecommerce.marketplace.Repository.order;

import com.ecommerce.marketplace.entities.cart.cartItems;
import com.ecommerce.marketplace.entities.orders.orderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderItemsRepo extends JpaRepository<orderItems,Long> {


    @Query(value = "SELECT CI.* FROM CART_ITEMS CI JOIN CUSTOMER_CART CC ON CI.CART_ID=CC.ID JOIN CUSTOMER C ON CC.CUSTOMER_ID=C.ID WHERE C.EMAIL=?1 AND CI.LISTING_ID=?2",nativeQuery = true)
    cartItems findOrderItem(String email, Long listingId);
}
