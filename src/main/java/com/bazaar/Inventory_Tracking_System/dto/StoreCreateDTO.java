package com.bazaar.Inventory_Tracking_System.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StoreCreateDTO {
    @NotBlank(message = "Store name is required")
    private String name;

    @NotBlank(message = "Store location is required")
    private String location;

    @NotNull(message = "Owner ID is required")
    private Long ownerId;

    // Constructors
    public StoreCreateDTO() {
    }

    public StoreCreateDTO(String name, String location, Long ownerId) {
        this.name = name;
        this.location = location;
        this.ownerId = ownerId;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}