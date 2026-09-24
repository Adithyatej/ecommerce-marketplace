package com.ecommerce.marketplace.Repository.order;

import com.ecommerce.marketplace.entities.orders.sellerOrderBoard;
import com.ecommerce.marketplace.enums.SellerOrderStatus;
import com.ecommerce.marketplace.projections.products.sellerOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface sellerBoardRepo extends JpaRepository<sellerOrderBoard,Long> {


    @Query(value = "SELECT * FROM SELLER_ORDERS AS SO WHERE SO.ORDER_ID=:order",nativeQuery = true)
    List<sellerOrderBoard> findSellerOrderBoardByOrderId(@Param("order") Long order);

    @Query(value ="SELECT * FROM SELLER_ORDERS AS SO JOIN SELLERS S ON SO.SELLER_ID=S.ID WHERE S.EMAIL=?1",nativeQuery = true)
    Optional<List<sellerOrders>> findSellerOrdersByMail(String email);


    @Query(value = "SELECT * FROM SELLER_ORDERS AS SO JOIN SELLERS S ON SO.SELLER_ID=S.ID WHERE S.EMAIL=?1 AND SO.status=?2",nativeQuery = true)
    List<sellerOrders> findSellerOrdersByMailAndStatus(String email, String status);
}
