package com.bazaar.Inventory_Tracking_System.controller;

import com.bazaar.Inventory_Tracking_System.dto.*;
import com.bazaar.Inventory_Tracking_System.entity.User;
import com.bazaar.Inventory_Tracking_System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody LoginDTO loginDto) {
        try {
            LoginResponseDto response = userService.login(loginDto);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new LoginResponseDto("Login failed: " + e.getMessage(), false)
            );
        }
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@Valid @RequestBody LoginDTO loginDto) {
        try {
            LoginResponseDto response = userService.login(loginDto);

            if (response.isSuccess()) {
                // Check if user has admin role
                if (response.getRoles().contains("ROLE_ADMIN")) {
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.badRequest().body(
                            new LoginResponseDto("Access denied. Admin role required.", false)
                    );
                }
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new LoginResponseDto("Admin login failed: " + e.getMessage(), false)
            );
        }
    }

    // =================== SIGNUP ENDPOINTS (NO AUTH REQUIRED) ===================

    // User registration (no auth required)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        try {
            String message = userService.createUser(userRegistrationDto);
            return ResponseEntity.status(201).body(Map.of("message", message, "success", true));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage(), "success", false));
        }
    }

    // Admin registration with extended fields (no auth required)
    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminSignupDto adminSignupDto) {
        try {
            String message = userService.createAdmin(adminSignupDto);
            return ResponseEntity.status(201).body(Map.of("message", message, "success", true));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage(), "success", false));
        }
    }

    // =================== AUTHENTICATED ENDPOINTS ===================

    // Get All Users (admin only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Get User By ID (admin only)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUserById(id);
        return userOptional.map(user -> ResponseEntity.ok(convertToDto(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete User (admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    // Current user update their own password
    @PutMapping("/password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody PasswordUpdateDto passwordUpdateDto) {
        try {
            // Get the current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            userService.updatePassword(
                    currentUsername,
                    passwordUpdateDto.getCurrentPassword(),
                    passwordUpdateDto.getNewPassword()
            );

            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Current password is incorrect"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // Admin update any user's password by username
    @PutMapping("/admin/password/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminUpdateUserPassword(
            @PathVariable String username,
            @RequestBody Map<String, String> payload) {

        String newPassword = payload.get("password");
        if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "New password is required"));
        }

        try {
            // Find the user first
            Optional<User> userOptional = userService.findByUsername(username);
            if (userOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            userService.adminUpdateUserPassword(userOptional.get().getId(), newPassword);
            return ResponseEntity.ok(Map.of("message", "User password updated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // Helper method to convert User entity to UserDto
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setLocation(user.getLocation());
        dto.setCity(user.getCity());
        dto.setState(user.getState());
        dto.setCountry(user.getCountry());

        // Convert Role enum set to String set
        Set<String> roleNames = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
        dto.setRoles(roleNames);

        return dto;
    }
}