package com.ecommerce.marketplace.Repository.order;

import com.ecommerce.marketplace.entities.cart.cartItems;
import com.ecommerce.marketplace.entities.orders.orderItems;
import com.ecommerce.marketplace.entities.orders.sellerOrderBoard;
import com.ecommerce.marketplace.entities.product.productListings;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemsRepo extends JpaRepository<orderItems,Long> {


    @Query(value = "SELECT CI.* FROM CART_ITEMS CI JOIN CUSTOMER_CART CC ON CI.CART_ID=CC.ID JOIN CUSTOMER C ON CC.CUSTOMER_ID=C.ID WHERE C.EMAIL=?1 AND CI.LISTING_ID=?2 ",nativeQuery = true)
    List<cartItems> findOrderItem(String email, Long listingId);



    @Modifying
    @Query(value = "UPDATE ORDER_ITEMS AS OI JOIN SELLER_ORDERS AS SO ON SO.ID=OI.SELLER_ORDER JOIN PRODUCT_LISTINGS AS PL ON OI.PRODUCT_LISTING_ID= PL.ID SET PL.STOCK_QUANTITY= PL.STOCK_QUANTITY+OI.ORDER_QUANTITY WHERE SO.ID IN (:sellers)",nativeQuery = true)
    Integer findOrderItemBySeller(@Param("sellers") List<Long> sellers);
}
