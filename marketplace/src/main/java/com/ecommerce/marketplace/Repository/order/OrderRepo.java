package com.ecommerce.marketplace.Repository.order;
import java.util.List;
import java.util.Optional;

import com.ecommerce.marketplace.entities.orders.Orders;
import com.ecommerce.marketplace.projections.products.OrdersResponse;
import jakarta.persistence.criteria.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;

@Repository
public interface OrderRepo extends JpaRepository<Orders, Long> {


    @Query(value = "SELECT OT.ID AS orderId, OI.ID AS orderItemId, P.PRODUCT_NAME AS productName, P.BRAND AS brand, SO.STATUS as status FROM ORDERS_TABLE OT JOIN SELLER_ORDERS SO ON SO.ORDER_ID=OT.ID JOIN ORDER_ITEMS OI ON OI.SELLER_ORDER=SO.ID JOIN PRODUCT_LISTINGS PL ON OI.PRODUCT_LISTING_ID=PL.ID JOIN PRODUCTS P ON PL.PRODUCT_ID=P.ID JOIN CUSTOMER C ON C.ID= OT.CUSTOMER_ID WHERE C.EMAIL = :email ORDER BY OT.ORDER_DATE DESC",nativeQuery = true)
    List<OrdersResponse> getAllOrders(@Param("email") String username);


    @Query(value = "SELECT OT.ID AS orderId, OI.ID AS orderItemId, P.PRODUCT_NAME AS productName, P.BRAND AS brand, SO.STATUS as status FROM ORDERS_TABLE OT JOIN SELLER_ORDERS SO ON SO.ORDER_ID=OT.ID JOIN ORDER_ITEMS OI ON OI.SELLER_ORDER=SO.ID JOIN PRODUCT_LISTINGS PL ON OI.PRODUCT_LISTING_ID=PL.ID JOIN PRODUCTS P ON PL.PRODUCT_ID=P.ID JOIN CUSTOMER C ON C.ID= OT.CUSTOMER_ID WHERE C.EMAIL = :email AND OT.ID=:id",nativeQuery = true)
    List<OrdersResponse> getOrderById(@Param("email")String email, @Param("id")Long id);


    @Query(value = "SELECT OT.* FROM ORDERS_TABLE OT JOIN CUSTOMER C ON C.ID = OT.CUSTOMER_ID WHERE C.EMAIL = :email AND OT.ID = :order", nativeQuery = true)
    Optional<Orders> cancelOrder(@Param("email") String email, @Param("order") Long order);

}
