package com.ecommerce.marketplace.controller.OrderSystem;


import com.ecommerce.marketplace.dto.OrderSystem.OrderRequestDTO;
import com.ecommerce.marketplace.projections.products.OrdersResponse;
import com.ecommerce.marketplace.projections.products.sellerOrders;
import com.ecommerce.marketplace.service.order.OrderServices;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orders")
public class orders {

    private final OrderServices orderServices;

    public orders(OrderServices orderServices) {
        this.orderServices=orderServices;
    }
    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequestDTO cart) {

        Boolean added = orderServices.makeOrder(cart);
        if (added==false) { return ResponseEntity.status(500).body("unable to handle the request");}
        else {return ResponseEntity.ok("order created");}
    }


    @GetMapping("/{email}")
    public ResponseEntity<List<OrdersResponse>> viewOrders(@PathVariable String email) {

        return ResponseEntity.ok(orderServices.viewOrders(email));
    }

    @GetMapping("/{email}/{id}")
    public ResponseEntity<List<OrdersResponse>> viewOrderById(@PathVariable String email, @PathVariable Long id) {

        return ResponseEntity.ok(orderServices.viewOrderById(email,id));
    }



    @PostMapping("/{email}/{order}/cancelOrder")
    public ResponseEntity<?> cancelOrder(@PathVariable Long order,@PathVariable String email) {

        Integer cancelled = orderServices.cancelOrder(email,order);

        if (cancelled>= 1){
            return ResponseEntity.ok("order cancelled");
        }
        else {
            return ResponseEntity.status(500).body("unable to cancel. check the logs");
        }
    }

    @GetMapping("/seller/{email}/orders")
    public ResponseEntity<?> viewSellerOrders(@PathVariable String email) {
        List<sellerOrders> sellers = orderServices.getSellerOrders(email);

        if(!sellers.isEmpty()){
            return ResponseEntity.ok(sellers);
        }
        else {
            return ResponseEntity.status(400).body("seller orders exist");
        }

    }

    @GetMapping("/seller/{email}/{status}")
    public ResponseEntity<?> viewSellerOrdersStatus(@PathVariable String email, @PathVariable String status) {
        List<sellerOrders> sellers = orderServices.getSellerOrdersByStatus(email,status);
    }
}
