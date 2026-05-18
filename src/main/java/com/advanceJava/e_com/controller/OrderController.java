package com.advanceJava.e_com.controller;

import com.advanceJava.e_com.dto.OrderDTO;
import com.advanceJava.e_com.repository.UserRepository;
import com.advanceJava.e_com.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;
    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService; this.userRepository = userRepository;
    }

    private Long userId(Authentication auth) {
        return userRepository.findByUserName(auth.getName()).get().getId();
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@Valid @RequestBody Map<String, String> body, Authentication auth) {
        return ResponseEntity.ok(orderService.checkout(
                userId(auth), body.get("street"), body.get("city"), body.get("postalCode")));
    }

    @GetMapping("/my")
    public ResponseEntity<?> myOrders(Authentication auth) {
        return ResponseEntity.ok(orderService.getMyOrders(userId(auth))); // List<OrderDTO>
    }

    @GetMapping("/my/{id}")
    public ResponseEntity<?> myOrder(@PathVariable Long id, Authentication auth) {
        // Optional: add ownership check
        OrderDTO order = orderService.getById(id);
        if (!order.getUserId().equals(userId(auth))) {
            throw new RuntimeException("Access denied");
        }
        return ResponseEntity.ok(orderService.getOrderWithItems(id));
    }


}
