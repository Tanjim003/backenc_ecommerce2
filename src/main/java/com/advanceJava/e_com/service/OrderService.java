package com.advanceJava.e_com.service;

import com.advanceJava.e_com.models.CartItem;
import com.advanceJava.e_com.models.Order;
import com.advanceJava.e_com.models.OrderItem;
import com.advanceJava.e_com.models.Product;
import com.advanceJava.e_com.repository.CartRepository;
import com.advanceJava.e_com.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepo, CartRepository cartRepo, ProductService productService){
        this.orderRepo = orderRepo;
        this.cartRepo = cartRepo;
        this.productService = productService;

    }

    public Order checkout(Long userId, String street, String city, String postalCode) {
        List<CartItem> cartItems = cartRepo.findByUserId(userId);
        if (cartItems.isEmpty()) throw new IllegalStateException("Cart is empty");

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cartItems) {
            Product p = productService.getById(ci.getProductId());
            if (p.getStockQuantity() < ci.getQuantity())
                throw new IllegalStateException("Insufficient stock: " + p.getName());
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("CONFIRMED");
        order.setTotalAmount(total);
        order.setStreet(street);
        order.setCity(city);
        order.setPostalCode(postalCode);
        Order saved = orderRepo.save(order);

        for (CartItem ci : cartItems) {
            Product p = productService.getById(ci.getProductId());
            OrderItem oi = new OrderItem();
            oi.setOrderId(saved.getId());
            oi.setProductId(ci.getProductId());
            oi.setQuantity(ci.getQuantity());
            oi.setPriceAtPurchase(p.getPrice());
            orderRepo.saveOrderItem(oi);
            productService.reduceStock(ci.getProductId(), ci.getQuantity());


        }

        cartRepo.clearByUserId(userId);
        return saved;
    }

    public List<Order> getMyOrders(Long userId) {
        return orderRepo.findByUserId(userId);
    }

    public Order getById(Long id) {
        return orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public List<OrderItem> getItems(Long orderId) {
        return orderRepo.findItemsByOrderId(orderId);
    }

    public void updateStatus(Long orderId, String status) {
        orderRepo.updateStatus(orderId, status);
    }





    }
