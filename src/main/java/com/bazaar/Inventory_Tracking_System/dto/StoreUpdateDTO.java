package com.bazaar.Inventory_Tracking_System.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StoreUpdateDTO {
    private String name;
    private String location;

    @JsonProperty("owner_id")
    private Long ownerId;

    // Constructors
    public StoreUpdateDTO() {
    }

    public StoreUpdateDTO(String name, String location, Long ownerId) {
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