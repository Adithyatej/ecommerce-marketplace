package com.ecommerce.marketplace.service.order;

import com.ecommerce.marketplace.Mapping.AddressMapper;
import com.ecommerce.marketplace.Mapping.OrderMapper;
import com.ecommerce.marketplace.Repository.customer.customerRepo;
import com.ecommerce.marketplace.Repository.order.OrderItemsRepo;
import com.ecommerce.marketplace.Repository.order.OrderRepo;
import com.ecommerce.marketplace.Repository.order.sellerBoardRepo;
import com.ecommerce.marketplace.Repository.product.productListingRepo;
import com.ecommerce.marketplace.Repository.seller.sellerRepo;
import com.ecommerce.marketplace.dto.OrderSystem.OrderRequestDTO;
import com.ecommerce.marketplace.dto.cartSystem.cartRequestDTO;
import com.ecommerce.marketplace.entities.cart.cartItems;
import com.ecommerce.marketplace.entities.customer.Address;
import com.ecommerce.marketplace.entities.customer.Customer;
import com.ecommerce.marketplace.entities.orders.Orders;
import com.ecommerce.marketplace.entities.orders.ShippingAddress;
import com.ecommerce.marketplace.entities.orders.orderItems;
import com.ecommerce.marketplace.entities.orders.sellerOrderBoard;
import com.ecommerce.marketplace.entities.product.productListings;
import com.ecommerce.marketplace.entities.seller.Seller;
import com.ecommerce.marketplace.enums.OrderStatus;
import com.ecommerce.marketplace.enums.ProductListingStatus;
import com.ecommerce.marketplace.enums.SellerOrderStatus;
import com.ecommerce.marketplace.exceptions.AddressNotFoundException;
import com.ecommerce.marketplace.exceptions.NocancellationPolicyException;
import com.ecommerce.marketplace.exceptions.OrderNotFoundException;
import com.ecommerce.marketplace.exceptions.ProductOutOfStockException;
import com.ecommerce.marketplace.projections.products.OrdersResponse;
import com.ecommerce.marketplace.projections.products.sellerOrders;
import com.ecommerce.marketplace.service.cart.cartServices;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderServices {

    private final OrderRepo orderRepo;
    private final OrderItemsRepo orderItemsRepo;
    private final cartServices cartServices;
    private final customerRepo customerRepo;
    private final sellerRepo sellerRepo;
    private final sellerBoardRepo sellerBoardRepo;
    private final productListingRepo productListingRepo;

    private final AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);
    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    public OrderServices(OrderRepo orderRepo, OrderItemsRepo orderItemsRepo,cartServices cartServices,customerRepo customerRepo,sellerRepo sellerRepo,sellerBoardRepo sellerBoardRepo,productListingRepo productListingRepo) {
        this.orderRepo=orderRepo;
        this.orderItemsRepo=orderItemsRepo;
        this.cartServices=cartServices;
        this.customerRepo=customerRepo;
        this.sellerRepo=sellerRepo;
        this.sellerBoardRepo=sellerBoardRepo;
        this.productListingRepo=productListingRepo;

    }

    @Transactional
    public Boolean makeOrder(OrderRequestDTO cart) {

        List<cartItems> items=new ArrayList<>();
        List<Long> listings= new ArrayList<>();
        listings.add(cart.getListingId());
    // checking if buying exists
        System.out.println("before executing the order-items");
        items = orderItemsRepo.findOrderItem(cart.getEmail(),cart.getListingId());
        log.info("order items found: {}", items == null ? 0 : items.size());
        System.out.println("address query before");
        Address address = customerRepo.findAddressByEmail(cart.getEmail(), cart.getShippingAddress()).orElseThrow(()-> new AddressNotFoundException("Address does not exist. create a new one"));
        System.out.println("address query after execution"+ address.getArea()+" "+address.getStreet());
        if (items == null || items.isEmpty()) {
            cartRequestDTO cartItem = orderMapper.itemToCart(cart);
            items.add(cartServices.addProductToCart(cartItem));
        } else {
            for (cartItems cartItem : items) {
                if (cartItem.getProductListings().getStatus()== ProductListingStatus.CURRENTLY_OUT_OF_STOCK && cartItem.getProductListings().getStockQuantity()<cart.getQuantity()) {
                    System.out.println("if executed");
                    throw new ProductOutOfStockException("product seems to be out of stock");
                }
                else {
                    System.out.println("else executed");
                    cartItem.setQuantity(cart.getQuantity());
                }
            }
        }

       Boolean added =  createOrder(items, cart.getEmail(), address, listings);
        return added;
    }


    public Boolean createOrder(List<cartItems> cart,String email,Address address,List<Long> listings){

        System.out.println("create an order before execution");
        Orders order=null;
        sellerOrderBoard sellerOrder=null;
        orderItems orderItem=null;
        Customer customer = customerRepo.findByEmail(email);
        log.info("customer info is {},{}",customer.getEmail(),customer.getPhoneNumber());
        BigDecimal totalAmount = cart.stream().map(item -> item.getProductListings().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))).toList().stream().reduce(BigDecimal.ZERO,BigDecimal::add);
        log.info("price is {}",totalAmount);
        LocalDateTime orderDateTime = LocalDateTime.now();
        log.info("local date and time is {}",orderDateTime);
        OrderStatus status= OrderStatus.CREATED;
        log.info("status is,{}",status);
        ShippingAddress shippingAddress = addressMapper.toShippingAddress(address);
        log.info("adress is {},{},{}",shippingAddress.getStreet(),shippingAddress.getArea(),shippingAddress.getCity());

        Orders orders = orderMapper.toEntity(customer,totalAmount,status,shippingAddress,orderDateTime);
        log.info("data is {},{},{}",orders.getCreatedAt(),orders.getId(),orders.getStatus());
        order = orderRepo.save(orders);
        System.out.println("order info is :"+ order.getId()+" "+order.getCustomer().getEmail()+" "+ order.getCreatedAt()+" "+order.getStatus());
        List<Seller> sellers = findAllSellerOfProduct(listings);
        System.out.println("sellers size is "+sellers.size());
        Map<Long,sellerOrderBoard> sellerOrderMap = new HashMap<>();
        for(Seller seller: sellers) {
           sellerOrder = sellerBoardRepo.save(orderMapper.toSellerBoardEntity(order,seller, SellerOrderStatus.PENDING));
           order.getSellerOrders().add(sellerOrder); //birectional mapping
            sellerOrderMap.put(seller.getId(),sellerOrder);
           log.info("items are {},{},{}",sellerOrder.getSeller().getUsername(),sellerOrder.getId(),sellerOrder.getOrders().getId());
        }

        for (int i=0;i<cart.size();i++) {
            productListings pl = cart.get(i).getProductListings();
            Seller sellerOfListing = pl.getSeller();
            sellerOrderBoard sellerBoard = sellerOrderMap.get(sellerOfListing.getId());
            log.info("the unit price is {}",pl.getPrice());
            orderItem = orderMapper.toOrderItemsEntity(pl, sellerBoard, cart.get(i).getQuantity(), pl.getPrice());
            log.info("the mapper unit price is {}",orderItem.getUnitPrice());
            orderItemsRepo.save(orderItem);
            updateProductListing(pl,cart.get(i).getQuantity());

        }

        return orderItem!=null&&sellerOrder!=null&&order!=null;
    }

    public List<Seller> findAllSellerOfProduct(List<Long> listings) {

        List<Seller> sellers = sellerRepo.findSellersByListing(listings);
        return sellers;
    }

    public void updateProductListing(productListings listing,Integer quantity) {

            Integer stock = listing.getStockQuantity();
            listing.setStockQuantity(stock-quantity);
            if (listing.getStockQuantity()<10) {
                listing.setStatus(ProductListingStatus.SELLING_FAST);
            }
            if (listing.getStockQuantity()==0)  {
                listing.setStatus(ProductListingStatus.CURRENTLY_OUT_OF_STOCK);
            }
                productListingRepo.save(listing);
    }

    public List<OrdersResponse> viewOrders(String email) {

        List<OrdersResponse> orders = orderRepo.getAllOrders(email);

        if (orders==null) {
            throw new OrderNotFoundException("orders are not avaiable since you haven't placed any order");
        }
        else {
            return orders;
        }
    }

    public @Nullable List<OrdersResponse> viewOrderById(String email, Long id) {

        List<OrdersResponse> orderItems = orderRepo.getOrderById(email,id);

        if (orderItems==null) {
            throw new OrderNotFoundException("order not found");
        }
        else {
            return orderItems;
        }

    }

    @Modifying
    @Transactional
    public Integer cancelOrder(String email, Long order) {
        Integer cancelled =0;
        Orders orders = orderRepo.cancelOrder(email,order).orElseThrow(()-> new OrderNotFoundException("order could not be found"));

        log.info("order info is {},{}",orders.getId(),orders.getStatus());
            if (orders.getStatus()==OrderStatus.COMPLETED || orders.getStatus()==OrderStatus.CANCELLED || orders.getStatus()==OrderStatus.DELIVERED)
            {
                throw new NocancellationPolicyException("order cannot be cancelled at this stage");
            }
            else {
                orders.setStatus(OrderStatus.CANCELLED);
                orderRepo.save(orders);
                List<sellerOrderBoard> sellers = sellerBoardRepo.findSellerOrderBoardByOrderId(order);

                if (!sellers.isEmpty()) {

                    // UPDATING THE QUANTITY OF STOCK IN PRODUCT LISTINGS
                    for(sellerOrderBoard seller:sellers) {
                        seller.setStatus(SellerOrderStatus.CANCELLED);
                    }
                    log.info("before updated");
                    sellerBoardRepo.saveAll(sellers);
                    log.info("updated");
                    List<Long> sellersIds = sellers.stream().map(sellerOrderBoard::getId).toList();
                    Integer rows = orderItemsRepo.findOrderItemBySeller(sellersIds);

                    cancelled=rows;


//                    //UPDATE THE STATUS
//                    for (productListings products: rows) {
//
//                        if (products.getStockQuantity() > 10) {
//                            products.setStatus(ProductListingStatus.AVAILABLE);
//                            productListingRepo.save(products);
//                        }
//                    }

                }
                return cancelled;

            }

    }


    public List<sellerOrders> getSellerOrders(String email) {

        return sellerBoardRepo.findSellerOrdersByMail(email).orElseThrow(()-> new OrderNotFoundException("seller orders are not found "));
    }

    public List<sellerOrders> getSellerOrdersByStatus(String email, String status) {
        return sellerBoardRepo.findSellerOrdersByMailAndStatus(email).orElseThrow(()-> new OrderNotFoundException("seller orders are not found with status "+ status));
    }
}

