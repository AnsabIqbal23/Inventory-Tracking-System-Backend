package com.bazaar.Inventory_Tracking_System.dto;

public class StoreDTO {
    private Long id;
    private String name;
    private String location;
    private Long ownerId;
    private String ownerUsername;

    // Constructors
    public StoreDTO() {
    }

    public StoreDTO(Long id, String name, String location, Long ownerId, String ownerUsername) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.ownerId = ownerId;
        this.ownerUsername = ownerUsername;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }
}