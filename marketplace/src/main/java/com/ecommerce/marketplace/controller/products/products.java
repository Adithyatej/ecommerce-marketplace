package com.ecommerce.marketplace.controller.products;


import com.ecommerce.marketplace.dto.product.productDTO;
import com.ecommerce.marketplace.service.product.productCategoriesServices;
import com.ecommerce.marketplace.service.product.productServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products-api")
public class products {

    private final productServices productService;

    public products(productServices productServices) {
        this.productService=productServices;
    }

    @PostMapping("/product")
    public ResponseEntity<String> addProduct(@RequestBody productDTO product) {

        String productName = productService.addProducts(product);
        return ResponseEntity.ok(productName +" is added");
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {

        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("/products/{name}")
    public ResponseEntity<?> getProductsByName(@PathVariable String name) {

        return ResponseEntity.ok(productService.getProductsByName(name));
    }

    @PutMapping("/products")
    public ResponseEntity<?> updateProducts(@RequestParam("productName") String name, @RequestBody productDTO product) {

        return ResponseEntity.ok(productService.updateProduct(name, product) +" is updated");
    }

    @DeleteMapping("/products/{name}")
    public ResponseEntity<?> deleteProducts(@PathVariable String name) {

        return ResponseEntity.ok(productService.deleteProduct(name)+ "is deleted");
    }
}
