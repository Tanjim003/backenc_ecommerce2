//package com.advanceJava.e_com.controller;
//
//import com.advanceJava.e_com.repository.UserRepository;
//import com.advanceJava.e_com.service.CartService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/cart")
//public class CartController {
//    private final CartService cartService;
//    private final UserRepository userRepository;
//    public CartController(CartService cartService, UserRepository userRepository) {
//        this.cartService = cartService; this.userRepository = userRepository;
//    }
//
//    @GetMapping
//    public ResponseEntity<?> view(Authentication auth) {
//        return ResponseEntity.ok(cartService.getCart(userId(auth)));
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<?> add(@RequestBody Map<String, Object> body, Authentication auth) {
//        Long productId = Long.valueOf(body.get("productId").toString());
//        int quantity = Integer.parseInt(body.get("quantity").toString());
//        return ResponseEntity.ok(cartService.addToCart(userId(auth), productId, quantity));
//    }
//
//    @DeleteMapping("/remove/{cartItemId}")
//    public ResponseEntity<?> remove(@PathVariable Long cartItemId) {
//        cartService.removeItem(cartItemId);
//        return ResponseEntity.ok(Map.of("message", "Item removed"));
//    }
//}
