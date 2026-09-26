package com.ecommerce.marketplace.controller.products;


import com.ecommerce.marketplace.dto.product.productListingDTO;
import com.ecommerce.marketplace.projections.products.productListingResponse;
import com.ecommerce.marketplace.service.product.productServices;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productsList")
public class productListing {


    private final productServices productService;

    public productListing(productServices productService) {
        this.productService=productService;
    }

    @PostMapping("/product")
    public ResponseEntity<?> addToProductList(@Valid @RequestBody productListingDTO product) {

        Long id = productService.addProductToList(product);

        return id>0?ResponseEntity.ok("product added to the list"): ResponseEntity.status(500).body("product not added to list");
    }

    @GetMapping("/listing/{seller}")
    public ResponseEntity<?> viewListing(@PathVariable String seller) {

        List<productListingResponse> products = productService.viewListing(seller);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{seller}/listings")
    public ResponseEntity<?> viewListingByProduct(@PathVariable String seller, @RequestParam("name") String name, @RequestParam("brand") String brand) {

        List<productListingResponse> response = productService.viewListingByProduct(seller, name,brand);
        return ResponseEntity.ok(response);

    }

    @PatchMapping("/listing/update-stock")
    public ResponseEntity<?> updateStockQuantity(@Valid @RequestBody productListingDTO product) {

       Integer rows =  productService.updateStock(product);

       return ResponseEntity.ok( "Stock Quantity is updated!!! ");
    }



    @PatchMapping("/listing/update-price")
    public ResponseEntity<?> updatePrice(@Valid @RequestBody productListingDTO product) {

        Integer rows =  productService.updatePrice(product);

        return ResponseEntity.ok( "Price is updated!!! ");
    }

    @PatchMapping("/listing/update-status")
    public ResponseEntity<?> updateStatus(@Valid @RequestBody productListingDTO product) {

        Integer rows =  productService.updateStatus(product);

        return ResponseEntity.ok( "Status is updated!!! ");
    }

    @DeleteMapping("/{seller}/listing/deactivate")
    public ResponseEntity<?> deactivateProduct(@PathVariable String seller, @RequestParam("name") String name, @RequestParam("brand") String brand) {

        Integer rows = productService.deactivateProduct(seller, name,brand);

        return ResponseEntity.ok( "Listing associated with seller is deactivated!!! ");
    }

    @GetMapping("/listings")
    public ResponseEntity<?> viewListings() {

        return ResponseEntity.ok(productService.viewAllProducts());
    }

}
