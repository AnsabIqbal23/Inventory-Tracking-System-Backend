package com.bazaar.Inventory_Tracking_System.config;

import com.bazaar.Inventory_Tracking_System.entity.Role;
import com.bazaar.Inventory_Tracking_System.entity.User;
import com.bazaar.Inventory_Tracking_System.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initAdminUser(UserRepository userRepository) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@gmail.com");

                // ✅ Hash the password using BCrypt
                String hashedPassword = new BCryptPasswordEncoder().encode("12345678");
                admin.setPassword(hashedPassword);

                // Optional fields
                admin.setPhone("+1234567890");
                admin.setLocation("123 Business Road");
                admin.setCity("Karachi");
                admin.setState("Sindh");
                admin.setCountry("Pakistan");

                // ✅ Set status as string
                admin.setStatus("ACTIVE");

                // ✅ Set role(s)
                admin.setRoles(Set.of(Role.ROLE_ADMIN));

                userRepository.save(admin);
                System.out.println("✔ Default admin user created.");
            } else {
                System.out.println("ℹ Admin user already exists.");
            }
        };
    }
}
