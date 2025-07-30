package com.bazaar.Inventory_Tracking_System.config;

import com.bazaar.Inventory_Tracking_System.entity.User;
import com.bazaar.Inventory_Tracking_System.entity.Role;
import com.bazaar.Inventory_Tracking_System.entity.Store;
import com.bazaar.Inventory_Tracking_System.entity.Product;
import com.bazaar.Inventory_Tracking_System.repository.UserRepository;
import com.bazaar.Inventory_Tracking_System.repository.StoreRepository;
import com.bazaar.Inventory_Tracking_System.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        loadInitialData();
    }

    private void loadInitialData() {
        // Only load data if database is empty
        if (userRepository.count() == 0) {

            // Create Admin User
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@bazaar.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setPhone("+1234567890");
            admin.setLocation("Headquarters");
            admin.setCity("New York");
            admin.setState("NY");
            admin.setCountry("USA");

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(Role.ROLE_ADMIN);
            admin.setRoles(adminRoles);

            admin = userRepository.save(admin);
            System.out.println("Created admin user: " + admin.getUsername());

            // Create Regular User
            User user = new User();
            user.setUsername("user");
            user.setEmail("user@bazaar.com");
            user.setPassword(passwordEncoder.encode("user123"));

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(Role.ROLE_USER);
            user.setRoles(userRoles);

            user = userRepository.save(user);
            System.out.println("Created regular user: " + user.getUsername());

            // Create Store Manager
            User storeManager = new User();
            storeManager.setUsername("manager");
            storeManager.setEmail("manager@bazaar.com");
            storeManager.setPassword(passwordEncoder.encode("manager123"));
            storeManager.setPhone("+1234567891");
            storeManager.setLocation("Store Front");
            storeManager.setCity("Los Angeles");
            storeManager.setState("CA");
            storeManager.setCountry("USA");

            Set<Role> managerRoles = new HashSet<>();
            managerRoles.add(Role.ROLE_USER);
            storeManager.setRoles(managerRoles);

            storeManager = userRepository.save(storeManager);
            System.out.println("Created store manager: " + storeManager.getUsername());

            // Create Stores
            Store mainStore = new Store();
            mainStore.setName("Main Store");
            mainStore.setLocation("Downtown Plaza");
            mainStore.setOwner(admin);
            mainStore = storeRepository.save(mainStore);
            System.out.println("Created store: " + mainStore.getName());

            Store branchStore = new Store();
            branchStore.setName("Branch Store");
            branchStore.setLocation("Shopping Mall");
            branchStore.setOwner(storeManager);
            branchStore = storeRepository.save(branchStore);
            System.out.println("Created store: " + branchStore.getName());

            // Create Products for Main Store
            Product laptop = new Product();
            laptop.setName("Gaming Laptop");
            laptop.setDescription("High-performance gaming laptop with RTX graphics");
            laptop.setPrice(1299.99);
            laptop.setQuantity(25);
            laptop.setStore(mainStore);
            laptop.setUpdatedAt(LocalDateTime.now());
            productRepository.save(laptop);

            Product mouse = new Product();
            mouse.setName("Wireless Mouse");
            mouse.setDescription("Ergonomic wireless mouse with precision tracking");
            mouse.setPrice(49.99);
            mouse.setQuantity(100);
            mouse.setStore(mainStore);
            mouse.setUpdatedAt(LocalDateTime.now());
            productRepository.save(mouse);

            Product keyboard = new Product();
            keyboard.setName("Mechanical Keyboard");
            keyboard.setDescription("RGB mechanical keyboard with blue switches");
            keyboard.setPrice(129.99);
            keyboard.setQuantity(75);
            keyboard.setStore(mainStore);
            keyboard.setUpdatedAt(LocalDateTime.now());
            productRepository.save(keyboard);

            // Create Products for Branch Store
            Product headphones = new Product();
            headphones.setName("Bluetooth Headphones");
            headphones.setDescription("Noise-cancelling wireless headphones");
            headphones.setPrice(199.99);
            headphones.setQuantity(50);
            headphones.setStore(branchStore);
            headphones.setUpdatedAt(LocalDateTime.now());
            productRepository.save(headphones);

            Product smartphone = new Product();
            smartphone.setName("Smartphone");
            smartphone.setDescription("Latest Android smartphone with 128GB storage");
            smartphone.setPrice(699.99);
            smartphone.setQuantity(30);
            smartphone.setStore(branchStore);
            smartphone.setUpdatedAt(LocalDateTime.now());
            productRepository.save(smartphone);

            Product tablet = new Product();
            tablet.setName("Tablet");
            tablet.setDescription("10-inch tablet perfect for work and entertainment");
            tablet.setPrice(399.99);
            tablet.setQuantity(40);
            tablet.setStore(branchStore);
            tablet.setUpdatedAt(LocalDateTime.now());
            productRepository.save(tablet);

            System.out.println("Sample data loaded successfully!");
            System.out.println("\n=== LOGIN CREDENTIALS ===");
            System.out.println("Admin - Username: admin, Password: admin123");
            System.out.println("User - Username: user, Password: user123");
            System.out.println("Manager - Username: manager, Password: manager123");
            System.out.println("========================");
        }
    }
}