package com.ecommerce.marketplace.controller.products;

import com.ecommerce.marketplace.dto.product.categoryDTO;
import com.ecommerce.marketplace.service.product.productCategoriesServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category-api")
public class categories {

    private final productCategoriesServices productServices;

    public categories(productCategoriesServices productServices) {
        this.productServices=productServices;
    }

    /*add categories*/

    @PostMapping("/category")
    public ResponseEntity<?> addCategories(@RequestBody categoryDTO request) {

        productServices.addCategory(request);


        return ResponseEntity.ok(request.getCategoryName()+ "is added");

    }

    @GetMapping("/category")
    public ResponseEntity<?> getCategoriesAndTheirParentCategories() {

        return ResponseEntity.ok(productServices.getCategoriesAndTheirParentCategories());
    }


    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {

        return ResponseEntity.ok(productServices.getCategories());
    }

    @DeleteMapping("/category/{name}")
    public ResponseEntity<String> deleteCategories(@PathVariable String name) {
        System.out.println(name+ "is added");
        Long response = productServices.deleteCategories(name);
        return ResponseEntity.ok(response + " rows are deleted for Category"+ name);
    }
}
