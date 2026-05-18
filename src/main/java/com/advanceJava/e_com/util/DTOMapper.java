package com.advanceJava.e_com.util;

import com.advanceJava.e_com.dto.*;
import com.advanceJava.e_com.models.*;

import java.math.BigDecimal;

public class DTOMapper {
    public static ProductDTO toProductDTO(Product p) {
        if (p == null) return null;
        return new ProductDTO(p.getId(), p.getName(), p.getDescription(),
                p.getPrice(), p.getStockQuantity(), p.getImagePath());
    }

    public static CartItemDTO toCartItemDTO(CartItem item, Product product) {
        CartItemDTO dto = new CartItemDTO();
        dto.setCartItemId(item.getId());
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setProductPrice(product.getPrice());
        dto.setProductImagePath(product.getImagePath());
        dto.setQuantity(item.getQuantity());
        dto.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        return dto;
    }

    public static OrderDTO toOrderDTO(Order o) {
        return new OrderDTO(o.getId(), o.getUserId(), o.getStatus(),
                o.getTotalAmount(), o.getStreet(), o.getCity(),
                o.getPostalCode(), o.getCreatedAt());
    }

    public static OrderItemDTO toOrderItemDTO(OrderItem oi, String productName) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(oi.getId());
        dto.setOrderId(oi.getOrderId());
        dto.setProductId(oi.getProductId());
        dto.setProductName(productName);
        dto.setQuantity(oi.getQuantity());
        dto.setPriceAtPurchase(oi.getPriceAtPurchase());
        dto.setSubtotal(oi.getPriceAtPurchase().multiply(BigDecimal.valueOf(oi.getQuantity())));
        return dto;
    }

    public static ReturnRequestDTO toReturnRequestDTO(ReturnRequest r) {
        return new ReturnRequestDTO(r.getId(), r.getOrderId(), r.getReason(),
                r.getStatus(), r.getCreatedAt());
    }

    public static UserDTO toUserDTO(User u) {
        return new UserDTO(u.getId(), u.getUsername(), u.getRole());
    }

}
