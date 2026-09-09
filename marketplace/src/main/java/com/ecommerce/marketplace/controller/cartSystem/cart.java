package com.ecommerce.marketplace.controller.cartSystem;


import com.ecommerce.marketplace.dto.cartSystem.cartRequestDTO;
import com.ecommerce.marketplace.entities.cart.cartItems;
import com.ecommerce.marketplace.service.cart.cartServices;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cart")
public class cart {

    private final cartServices cartServices;

    public cart(cartServices cartServices) {
        this.cartServices=cartServices;
    }

    @PostMapping("/item")
    public ResponseEntity<?> addToCart(@RequestBody cartRequestDTO cart) {

        Object Added = cartServices.addProductToCart(cart);

        if (Added==null) { return ResponseEntity.status(500).body("unable to handle the request");}
        else {return ResponseEntity.ok("added to cart");}
    }

    @GetMapping("/{customer}")
    public ResponseEntity<?> viewCart(@PathVariable String customer) {

        return ResponseEntity.ok(cartServices.viewCart(customer));
    }

}
