package com.bazaar.Inventory_Tracking_System.controller;

import com.bazaar.Inventory_Tracking_System.dto.UserDto;
import com.bazaar.Inventory_Tracking_System.dto.UserRegistrationDto;
import com.bazaar.Inventory_Tracking_System.dto.PasswordUpdateDto;
import com.bazaar.Inventory_Tracking_System.entity.Role;
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

    // User registration (creates a user with only ROLE_USER role)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        // Check if user already exists
        if (userService.findByUsername(userRegistrationDto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        // Set ROLE_USER role by default
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);

        // Create new user
        userService.createUser(userRegistrationDto.getUsername(), userRegistrationDto.getPassword(), roles);

        return ResponseEntity.status(201).body("User registered successfully");
    }

    // Admin registration (requires ROLE_ADMIN role to create another admin)
    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        // Check if user already exists
        if (userService.findByUsername(userRegistrationDto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        // Set ROLE_ADMIN role
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_ADMIN);

        // Create new admin
        userService.createUser(userRegistrationDto.getUsername(), userRegistrationDto.getPassword(), roles);

        return ResponseEntity.status(201).body("Admin registered successfully");
    }

    // Get All Users (without password)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Get User By ID (without password)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUserById(id);
        return userOptional.map(user -> ResponseEntity.ok(convertToDto(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete User
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // Current user update their own password - updated URL without an ID parameter
    @PutMapping("/password")
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

            return ResponseEntity.ok("Password updated successfully");
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Current password is incorrect");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
            return ResponseEntity.badRequest().body("New password is required");
        }

        try {
            // Find the user first
            Optional<User> userOptional = userService.findByUsername(username);
            if (userOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            userService.adminUpdateUserPassword(userOptional.get().getId(), newPassword);
            return ResponseEntity.ok("User password updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Helper method to convert User entity to UserDto
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());

        // Convert Role enum set to String set
        Set<String> roleNames = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
        dto.setRoles(roleNames);

        return dto;
    }
}
