package com.bazaar.Inventory_Tracking_System.dto;

import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PasswordUpdateDto {
    @NotBlank(message = "Current password is required")
    @JsonProperty("currentpassword")  // Match the case in your JSON
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @JsonProperty("newpassword")  // Match the case in your JSON
    private String newPassword;

    @NotBlank(message = "Confirm password is required")
    @JsonProperty("confirmpassword")  // Match the case in your JSON
    private String confirmPassword;

    public PasswordUpdateDto() {
    }

    public PasswordUpdateDto(String currentPassword, String newPassword, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}