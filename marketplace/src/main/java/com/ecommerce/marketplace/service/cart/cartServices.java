package com.ecommerce.marketplace.service.cart;

import com.ecommerce.marketplace.Mapping.cartItemMapper;
import com.ecommerce.marketplace.Repository.cart.cartItemsRepo;
import com.ecommerce.marketplace.Repository.cart.customerCartRepo;
import com.ecommerce.marketplace.Repository.customerRepo;
import com.ecommerce.marketplace.Repository.product.productListingRepo;
import com.ecommerce.marketplace.dto.cartSystem.cartRequestDTO;
import com.ecommerce.marketplace.entities.cart.cartItems;
import com.ecommerce.marketplace.entities.cart.customerCart;
import com.ecommerce.marketplace.entities.customer.customer;
import com.ecommerce.marketplace.entities.product.productListings;
import com.ecommerce.marketplace.exceptions.IdNotFoundException;
import com.ecommerce.marketplace.exceptions.cartAlreadyExistsException;
import com.ecommerce.marketplace.exceptions.cartNotFoundException;
import com.ecommerce.marketplace.exceptions.productNotFoundException;
import com.ecommerce.marketplace.projections.products.customerCartResponse;
import jakarta.transaction.Transactional;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class cartServices {


    private final customerRepo customerRepo;
    private final customerCartRepo customerCartRepo;
    private final productListingRepo productListingRepo;
    private final cartItemsRepo cartItemsRepo;

    private final cartItemMapper cartMapper = Mappers.getMapper(cartItemMapper.class);

    public cartServices(customerRepo customerRepo, customerCartRepo customerCartRepo, productListingRepo productListingRepo,cartItemsRepo cartItemsRepo) {
        this.customerRepo=customerRepo;
        this.customerCartRepo=customerCartRepo;
        this.productListingRepo=productListingRepo;
        this.cartItemsRepo=cartItemsRepo;
    }

    @Transactional
    public Object addProductToCart(cartRequestDTO cart) {

        cartItems added = null;
        customer customerInformation = customerRepo.findByEmail(cart.getEmail());


        if (customerInformation == null) {
            throw new IdNotFoundException("user not found");
        } else {
            System.out.println(customerInformation.getEmail() + " " + customerInformation.getUsername() + " " + customerInformation.getId());

            customerCart cartInformation = customerCartRepo.findByCustomerId(customerInformation.getId());


            if (cartInformation == null) {
                customerCart newCart = new customerCart();
                newCart.setCustomer(customerInformation);
                customerCart savedCart = customerCartRepo.save(newCart);

                productListings productListing = productListingRepo.findByIdAndStatus(cart.getListingId()).orElseThrow(() -> new productNotFoundException("product not listed"));

                cartItems cartItem = cartMapper.toEntity(savedCart, productListing, cart.getQuantity());

                System.out.println(cartItem.getCart().getId() + " " + cartItem.getQuantity());
                added = cartItemsRepo.save(cartItem);
                return added;

            } else {
                System.out.println(cartInformation.getCustomer().getEmail() + " " + cartInformation.getCustomer().getUsername() + " " + cartInformation.getId());
                productListings productListing = productListingRepo.findByIdAndStatus(cart.getListingId()).orElseThrow(() -> new productNotFoundException("product not listed"));

                cartItems item = cartItemsRepo.findCartItemByCartIdAndListingId(cartInformation.getId(), productListing.getId());

                if (item==null) {
                    cartItems cartItem = cartMapper.toEntity(cartInformation, productListing, cart.getQuantity());

                    System.out.println(cartItem.getCart().getId() + " " + cartItem.getQuantity());
                    added = cartItemsRepo.save(cartItem);
                    return added;
                }

                else {
                     added = increaseQuantity(item.getId(),cart.getQuantity());
                     return added;
                }

            }


        }
    }

    public cartItems increaseQuantity(Long id,Integer quantity) {
        cartItems updated  = cartItemsRepo.findById(id).orElseThrow(()-> new IdNotFoundException("cart item not found"));
        updated.setQuantity(updated.getQuantity()+quantity);
        return cartItemsRepo.save(updated);

    }

    @Transactional
    public String decreaseQuantity(Long id,Integer quantity) {
        cartItems updated  = cartItemsRepo.findById(id).orElseThrow(()-> new IdNotFoundException("cart item not found"));

        updated.setQuantity(updated.getQuantity()-quantity);
        if (updated.getQuantity()==0) {

             return deleteCartItem(id);

        }
        else {

            cartItems decreased = cartItemsRepo.save(updated);

            if (decreased!=null) {
                return "cart item decreased";
            }
            else {
                return "cart not updated";
            }

        }


    }

    public List<customerCartResponse> viewCart(String email) {

        System.out.println(email);
        List<customerCartResponse> cart = customerCartRepo.viewCartByCustomer(email);


        if (cart==null || cart.isEmpty()) {
            throw new productNotFoundException("cart is Empty");
        }
        else {
            return cart;
        }
    }



    public String deleteCartItem(Long id) {

        cartItemsRepo.deleteById(id);
        return "cart item is deleted";

    }

    @Transactional
    public String deleteCartItem(String email,Long id) {

        cartItems cart = cartItemsRepo.deleteByMailAndCartId(email,id);

        if (cartItemsRepo.findById(cart.getId()).isPresent()) {
            throw new cartAlreadyExistsException("cart still exists");
        }
        else {
            return "cart item is deleted";
        }
    }

    @Transactional
    public String deleteCart(String email) {

        cartItems cart = cartItemsRepo.deleteCartByMail(email);

        if (customerCartRepo.findByEmail(email).isPresent()) {
            throw new cartAlreadyExistsException("cart still exists");
        }
        else {
            return "cart is deleted";
        }
    }
}

