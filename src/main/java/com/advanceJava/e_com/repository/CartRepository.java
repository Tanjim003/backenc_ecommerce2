package com.advanceJava.e_com.repository;

import com.advanceJava.e_com.models.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartRepository {
    CartItem save(CartItem item);
    List<CartItem> findByUserId(Long userId);
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);
    void updateQuantity(Long id, int quantity);
    void deleteById(Long id);
    void clearByUserId(Long userId);
}
