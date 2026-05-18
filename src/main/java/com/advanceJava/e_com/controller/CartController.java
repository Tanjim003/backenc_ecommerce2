package com.advanceJava.e_com.controller;
import com.advanceJava.e_com.dto.CartItemDTO;
import com.advanceJava.e_com.models.User;
import com.advanceJava.e_com.repository.UserRepository;
import com.advanceJava.e_com.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final UserRepository userRepository;
    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService; this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<?> view(Authentication auth) {
        List<CartItemDTO> cart = cartService.getCart(userId(auth));
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody Map<String, Object> body, Authentication auth) {
        Long productId = Long.valueOf(body.get("productId").toString());
        int quantity = Integer.parseInt(body.get("quantity").toString());
        return ResponseEntity.ok(cartService.addToCart(userId(auth), productId, quantity));
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<?> remove(@PathVariable Long cartItemId) {
        cartService.removeItem(cartItemId);
        return ResponseEntity.ok(Map.of("message", "Item removed"));
    }

    private Long userId(Authentication auth) {
        String username = auth.getName();   // or username, depending on your UserDetails setup
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return user.getId();
    }


}
