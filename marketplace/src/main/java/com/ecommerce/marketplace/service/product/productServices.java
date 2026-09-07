package com.ecommerce.marketplace.service.product;

import com.ecommerce.marketplace.Mapping.productMapper;
import com.ecommerce.marketplace.Repository.product.productCategoriesRepo;
import com.ecommerce.marketplace.Repository.product.productListingRepo;
import com.ecommerce.marketplace.Repository.product.productRepo;
import com.ecommerce.marketplace.Repository.sellerRepo;
import com.ecommerce.marketplace.dto.product.categoryDTO;
import com.ecommerce.marketplace.dto.product.productDTO;
import com.ecommerce.marketplace.dto.product.productListingDTO;
import com.ecommerce.marketplace.entities.product.productCategories;
import com.ecommerce.marketplace.entities.product.productListings;
import com.ecommerce.marketplace.entities.product.products;
import com.ecommerce.marketplace.entities.seller.seller;
import com.ecommerce.marketplace.exceptions.IdNotFoundException;
import com.ecommerce.marketplace.exceptions.categoryNotFoundException;
import com.ecommerce.marketplace.exceptions.productAlreadyExistsException;
import com.ecommerce.marketplace.exceptions.productNotFoundException;
import com.ecommerce.marketplace.projections.products.productListingResponse;
import com.ecommerce.marketplace.projections.products.productResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class productServices {


        private final productCategoriesRepo productCategoriesRepo;
        private final productRepo productRepo;
        private final sellerRepo sellerRepo;
        private final productListingRepo productListingRepo;

        private productMapper productMapper = Mappers.getMapper(productMapper.class);

        public productServices(productCategoriesRepo productCategoriesRepo, productRepo productRepo,sellerRepo sellerRepo,productListingRepo productListingRepo){
            this.productRepo=productRepo;
            this.productCategoriesRepo=productCategoriesRepo;
            this.sellerRepo= sellerRepo;
            this.productListingRepo=productListingRepo;
        }



        public String addProducts(productDTO product) {

            productCategories info = productCategoriesRepo.findByCategoryName(product.getProductCategory()).orElseThrow(
                    ()-> new IdNotFoundException("Category Not Found")
            );

            products productContent = productRepo.save(productMapper.toEntity(product,info));

            return productContent.getProductName();

        }

        public List<productResponse> getProducts() {

            List<productResponse> products = productRepo.findProductsWithCategory();
            if (products==null || products.isEmpty()) {
                throw new categoryNotFoundException("Categories not found");
            }

            else {
                return products;
            }
        }

        public productDTO getProductsByName(String name) {

            name = name.replace("-"," ").toLowerCase();

            products product = productRepo.findByProductName(name);
            if (product==null) {
                throw new IdNotFoundException("Product Not Found");
            }

            else {
                return productMapper.toDTO(product);
            }

        }


        @Transactional
    public @Nullable String updateProduct(String name, productDTO product) {

            name = name.replace("-"," ").toLowerCase();

            products existingProduct = productRepo.findByProductName(name);

            if (existingProduct==null) {
                throw new IdNotFoundException("Product Not Found");
            }
            else {
                if (product.getProductName() != null) {
                    existingProduct.setProductName(product.getProductName());
                }
                if (product.getProductDescription() != null) {
                    existingProduct.setProductDescription(product.getProductDescription());
                }
                if (product.getBrand() != null) {
                    existingProduct.setBrand(product.getBrand());
                }
                if (product.getProductCategory() != null) {
                    productCategories productCategory = productCategoriesRepo.findByCategoryName(product.getProductName()).orElseThrow(() -> new IdNotFoundException("Category Not Found"));
                    existingProduct.setProductCategories(productCategory);
                }

                products updatedProduct = productRepo.save(existingProduct);
                return updatedProduct.getProductName();
            }
        }


    @Transactional
    public Object deleteProduct(String name) {
            name = name.replace("-"," ").toLowerCase();

            products product = productRepo.deleteByProductName(name).orElseThrow( ()-> new IdNotFoundException("Product Not Found"));

            return product.getProductName();
    }

    @Transactional
    public Long addProductToList(@Valid productListingDTO product) {

            products productData = productRepo.findProductWithProductNameAndBrand(product.getProductName(),product.getBrand());
            System.out.println(productData.getProductName()+"  "+ productData.getBrand());
            seller seller = sellerRepo.findByEmail(product.getSeller());
            System.out.println(seller.getUsername()+"  "+seller.getEmail());


        List<productListings> productsList = productListingRepo.findProductList(productData.getId(),seller.getId());

        System.out.println("size of productList is"+productsList.size());

        if (!productsList.isEmpty()) {
            throw new productAlreadyExistsException("product is already listed. Look for increasing stock");
        }
        else {

            productListings listed = productListingRepo.save(productMapper.toProductListEntity(product, productData, seller));
            System.out.println(listed.getProduct().getProductName() + " " + listed.getSeller().getUsername() + " " + listed.getStockQuantity());
            return listed.getId();
        }
    }

    public List<productListingResponse> viewListing(String seller) {

        List<productListingResponse> products = productListingRepo.findProductListingBySeller(seller);

        System.out.println(products.size());

        if (products==null || products.isEmpty()) {
            throw new productNotFoundException("seller is not selling");
        }
        else {
            return products;
        }
    }

    public List<productListingResponse> viewListingByProduct(String seller,String name, String brand) {
        System.out.println(seller+"   "+name+"   "+brand);
        List<productListingResponse> products = productListingRepo.findProductListingByPRODUCT(seller, name, brand);

        System.out.println(products.size());

        if (products==null || products.isEmpty()) {
            throw new productNotFoundException("product is not present in the list");
        }
        else {
            return products;
        }
    }

    @Transactional
    public Integer updateStock(productListingDTO product) {

            Integer rows = productListingRepo.updateStockQuantityByProduct(product.getSeller(),product.getProductName(),product.getBrand(),product.getStockQuantity());


            if (rows>0) {
                return rows;
            }
            else {
                throw new productNotFoundException("product is not updated in the list");
            }


    }

    @Transactional
    public Integer updatePrice(@Valid productListingDTO product) {
            Integer rows = productListingRepo.updatePriceByProduct(product.getSeller(),product.getProductName(),product.getBrand(),product.getPrice());

        if (rows>0) {
            return rows;
        }
        else {
            throw new productNotFoundException("product is not updated in the list");
        }
    }


    @Transactional
    public Integer updateStatus(@Valid productListingDTO product) {
        Integer rows = productListingRepo.updateStatusByProduct(product.getSeller(),product.getProductName(),product.getBrand(),product.getStatus());

        if (rows>0) {
            return rows;
        }
        else {
            throw new productNotFoundException("product is not updated in the list");
        }
    }


    @Transactional
    public Integer deactivateProduct(String seller, String name, String brand) {
       Integer rows = productListingRepo.deleteProductListing(seller,name,brand);
        if (rows>0) {
            return rows;
        }
        else {
            throw new productNotFoundException("product is not deleted in the list");
        }


    }

    public List<productListingResponse> viewAllProducts() {

            List<productListingResponse> products = productListingRepo.findAllProductListing();

            if (products==null || products.isEmpty()) {
                throw new productNotFoundException("productListings is Empty");
            }
            else {
                return products;
            }
    }
}
