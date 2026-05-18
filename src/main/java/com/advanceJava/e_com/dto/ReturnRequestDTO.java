package com.advanceJava.e_com.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ReturnRequestDTO {

        private Long id;

        @NotNull(message = "Order ID is required")
        private Long orderId;

        @NotBlank(message = "Reason for return is required")
        @Size(min = 5, max = 500, message = "Reason must be between 5 and 500 characters")
        private String reason;
        private String status;
        private LocalDateTime createdAt;

        public ReturnRequestDTO() {}

        public ReturnRequestDTO(Long id, Long orderId, String reason, String status, LocalDateTime createdAt) {
            this.id = id;
            this.orderId = orderId;
            this.reason = reason;
            this.status = status;
            this.createdAt = createdAt;
        }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
