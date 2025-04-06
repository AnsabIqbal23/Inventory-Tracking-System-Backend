package com.bazaar.Inventory_Tracking_System.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRegistrationDto {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    public UserRegistrationDto() {
    }

    public UserRegistrationDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
