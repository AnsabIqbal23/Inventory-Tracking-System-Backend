package com.bazaar.Inventory_Tracking_System.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class StockMovementDTO {
    private Long id;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity change is required")
    private Integer quantityChange;

    private LocalDateTime timestamp;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(Integer quantityChange) {
        this.quantityChange = quantityChange;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}