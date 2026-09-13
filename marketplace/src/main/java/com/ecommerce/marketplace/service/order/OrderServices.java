package com.ecommerce.marketplace.service.order;

import com.ecommerce.marketplace.Repository.customerRepo;
import com.ecommerce.marketplace.Repository.order.OrderItemsRepo;
import com.ecommerce.marketplace.Repository.order.OrderRepo;
import com.ecommerce.marketplace.dto.cartSystem.cartRequestDTO;
import com.ecommerce.marketplace.entities.cart.cartItems;
import com.ecommerce.marketplace.entities.customer.Customer;
import com.ecommerce.marketplace.entities.orders.Order;
import com.ecommerce.marketplace.entities.orders.ShippingAddress;
import com.ecommerce.marketplace.enums.OrderStatus;
import com.ecommerce.marketplace.service.cart.cartServices;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
public class OrderServices {

    private final OrderRepo orderRepo;
    private final OrderItemsRepo orderItemsRepo;
    private final cartServices cartServices;
    private final customerRepo customerRepo;

    public OrderServices(OrderRepo orderRepo, OrderItemsRepo orderItemsRepo,cartServices cartServices,customerRepo customerRepo) {
        this.orderRepo=orderRepo;
        this.orderItemsRepo=orderItemsRepo;
        this.cartServices=cartServices;
        this.customerRepo=customerRepo;

    }

    @Transactional
    public Object makeOrder(cartRequestDTO cart) {

        cartItems item=null;
    // checking if buying exists
        item = orderItemsRepo.findOrderItem(cart.getEmail(),cart.getListingId());

        if (item==null) {
            item = cartServices.addProductToCart(cart);
        }
        else {
            item.setQuantity(cart.getQuantity());
        }

    }


    public Object createOrder(cartItems cart,String email) {

        Customer customer = customerRepo.findByEmail(email);
        log.info("customer info is {},{}",customer.getEmail(),customer.getPhoneNumber());
        BigDecimal totalAmount = cart.getProductListings().getPrice();
        log.info("price is {}",totalAmount);
        LocalDateTime orderDateTime = LocalDateTime.now();
        log.info("local date and time is {}",orderDateTime);
        OrderStatus status= OrderStatus.CREATED;
        // ShippingAddress address=




    }

}
