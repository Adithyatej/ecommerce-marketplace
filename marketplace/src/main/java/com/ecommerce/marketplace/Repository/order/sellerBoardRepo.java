package com.ecommerce.marketplace.Repository.order;

import com.ecommerce.marketplace.entities.orders.sellerOrderBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface sellerBoardRepo extends JpaRepository<sellerOrderBoard,Long> {
}
