package com.advanceJava.e_com.service;

import com.advanceJava.e_com.dto.CartItemDTO;
import com.advanceJava.e_com.models.CartItem;
import com.advanceJava.e_com.models.Product;
import com.advanceJava.e_com.repository.CartRepository;
import com.advanceJava.e_com.util.DTOMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository repo;
    private final ProductService productService;
    public CartService(CartRepository repo, ProductService productService){
        this.repo = repo;
        this.productService = productService;
    }

    public CartItem addToCart(Long userId, Long productId, int quantity){
        productService.getById(productId);
        Optional<CartItem> existing = repo.findByUserIdAndProductId(userId, productId);
        if(existing.isPresent()) {
            CartItem item = existing.get();
            repo.updateQuantity(item.getId(), item.getQuantity() + quantity);
            item.setQuantity(item.getQuantity() + quantity);
        }
        CartItem item = new CartItem();
        item.setUserId(userId);
        item.setProductId(productId);
        item.setQuantity(quantity);
        return repo.save(item);

    }

    public List<CartItemDTO> getCart(Long userId) {
        List<CartItem> cartItems = repo.findByUserId(userId);
        return cartItems.stream()
                .map(item -> {
                    Product product = productService.getById(item.getProductId());
                    return DTOMapper.toCartItemDTO(item, product);
                })
                .collect(Collectors.toList());
    }

    public void removeItem(Long cartItemId){
        repo.deleteById(cartItemId);
    }

    public void clearCart(Long userId){
        repo.clearByUserId(userId);
    }
}
