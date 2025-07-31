package com.bazaar.Inventory_Tracking_System.dto;

import jakarta.validation.constraints.NotBlank;

public class UserStatusUpdateDto {
    @NotBlank(message = "Status is required")
    private String status;

    // Constructors
    public UserStatusUpdateDto() {
    }

    public UserStatusUpdateDto(String status) {
        this.status = status;
    }

    // Getters and setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}