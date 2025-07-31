package com.bazaar.Inventory_Tracking_System.service;

import com.bazaar.Inventory_Tracking_System.dto.AdminSignupDto;
import com.bazaar.Inventory_Tracking_System.dto.LoginDTO;
import com.bazaar.Inventory_Tracking_System.dto.LoginResponseDto;
import com.bazaar.Inventory_Tracking_System.dto.UserRegistrationDto;
import com.bazaar.Inventory_Tracking_System.entity.User;
import com.bazaar.Inventory_Tracking_System.entity.Role;
import com.bazaar.Inventory_Tracking_System.repository.UserRepository;
import com.bazaar.Inventory_Tracking_System.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Login method with token generation
    public LoginResponseDto login(LoginDTO loginDto) {
        try {
            // Find user by username
            Optional<User> userOptional = userRepository.findByUsername(loginDto.getUsername());

            if (userOptional.isEmpty()) {
                return new LoginResponseDto("Invalid username or password", false);
            }

            User user = userOptional.get();

            // Verify password
            if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                return new LoginResponseDto("Invalid username or password", false);
            }

            // Convert roles to string set
            Set<String> roleNames = user.getRoles().stream()
                    .map(Enum::name)
                    .collect(Collectors.toSet());

            // Generate token using your existing method
            String token = jwtUtils.generateJwtToken(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    roleNames
            );

            // Return successful login response with token
            return new LoginResponseDto(
                    "Login successful",
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPhone(),
                    roleNames,
                    true,
                    token
            );

        } catch (Exception e) {
            return new LoginResponseDto("Login failed: " + e.getMessage(), false);
        }
    }

    // Regular user registration with password confirmation
    public String createUser(UserRegistrationDto userRegistrationDto) {
        // Check if passwords match
        if (!userRegistrationDto.isPasswordMatching()) {
            throw new RuntimeException("Passwords do not match");
        }

        // Check if username already exists
        if (userRepository.existsByUsername(userRegistrationDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));

        // Set default role as USER
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        user.setRoles(roles);

        userRepository.save(user);
        return "User registered successfully";
    }

    // Admin registration with extended fields and password confirmation
    public String createAdmin(AdminSignupDto adminSignupDto) {
        // Check if passwords match
        if (!adminSignupDto.isPasswordMatching()) {
            throw new RuntimeException("Passwords do not match");
        }

        // Check if username already exists
        if (userRepository.existsByUsername(adminSignupDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(adminSignupDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Check if phone already exists
        if (userRepository.existsByPhone(adminSignupDto.getPhone())) {
            throw new RuntimeException("Phone No. already exists");
        }

        User user = new User();
        user.setUsername(adminSignupDto.getUsername());
        user.setEmail(adminSignupDto.getEmail());
        user.setPassword(passwordEncoder.encode(adminSignupDto.getPassword()));
        user.setPhone(adminSignupDto.getPhone());
        user.setLocation(adminSignupDto.getLocation());
        user.setCity(adminSignupDto.getCity());
        user.setState(adminSignupDto.getState());
        user.setCountry(adminSignupDto.getCountry());

        // Set role as ADMIN
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_ADMIN);
        user.setRoles(roles);

        userRepository.save(user);
        return "Admin registered successfully";
    }

    // Legacy method for backward compatibility
    public void createUser(String username, String password, Set<Role> roles) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }

    // Update user's own password
    public void updatePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        // Update and save new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // Admin force password update
    public void adminUpdateUserPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Update and save new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}