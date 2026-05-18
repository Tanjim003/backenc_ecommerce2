package com.advanceJava.e_com.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CartItemDTO {
    private Long cartItemId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    private String productName;
    private BigDecimal productPrice;
    private String productImagePath;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 999, message = "Quantity cannot exceed 999")
    private Integer quantity;

    private BigDecimal subtotal;

    public CartItemDTO() {}

    public CartItemDTO(Long cartItemId, Long productId, String productName,
                       BigDecimal productPrice, String productImagePath,
                       Integer quantity, BigDecimal subtotal) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImagePath = productImagePath;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImagePath() {
        return productImagePath;
    }

    public void setProductImagePath(String productImagePath) {
        this.productImagePath = productImagePath;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
