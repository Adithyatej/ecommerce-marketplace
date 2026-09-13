package com.ecommerce.marketplace.controller.OrderSystem;


import com.ecommerce.marketplace.dto.cartSystem.cartRequestDTO;
import com.ecommerce.marketplace.entities.orders.Order;
import com.ecommerce.marketplace.service.order.OrderServices;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")
public class orders {

    private final OrderServices orderServices;

    public orders(OrderServices orderServices) {
        this.orderServices=orderServices;
    }
    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@Valid @RequestBody cartRequestDTO cart) {

        Object added = orderServices.makeOrder(cart);
        if (added==null) { return ResponseEntity.status(500).body("unable to handle the request");}
        else {return ResponseEntity.ok("order created");}
    }

}
