package com.bazaar.Inventory_Tracking_System.dto;

import java.util.Set;

public class LoginResponseDto {
    private String message;
    private Long userId;
    private String username;
    private String email;
    private String phone;
    private Set<String> roles;
    private boolean success;
    private String token;

    // Constructors
    public LoginResponseDto() {
    }

    public LoginResponseDto(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public LoginResponseDto(String message, Long userId, String username, String email, String phone, Set<String> roles, boolean success, String token) {
        this.message = message;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.roles = roles;
        this.success = success;
        this.token = token;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}