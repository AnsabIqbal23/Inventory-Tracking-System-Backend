package com.bazaar.Inventory_Tracking_System.controller;

import com.bazaar.Inventory_Tracking_System.dto.ProductDTO;
import com.bazaar.Inventory_Tracking_System.entity.Product;
import com.bazaar.Inventory_Tracking_System.entity.Store;
import com.bazaar.Inventory_Tracking_System.exception.ResourceNotFoundException;
import com.bazaar.Inventory_Tracking_System.repository.ProductRepository;
import com.bazaar.Inventory_Tracking_System.repository.StoreRepository;
import com.bazaar.Inventory_Tracking_System.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/store/{storeId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Product>> getProductsByStoreId(@PathVariable Long storeId) {
        // Verify store exists
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id " + storeId));

        List<Product> products = productRepository.findByStoreId(storeId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        Store store = storeRepository.findById(productDTO.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id " + productDTO.getStoreId()));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setStore(store);
        product.setUpdatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        // Validate that restricted fields aren't being changed
        if (!product.getName().equals(productDTO.getName())) {
            return ResponseEntity.badRequest().body("Product name cannot be updated");
        }

        if (productDTO.getStoreId() != null && !product.getStore().getId().equals(productDTO.getStoreId())) {
            return ResponseEntity.badRequest().body("Store ID cannot be updated");
        }

        if (productDTO.getQuantity() != null && !product.getQuantity().equals(productDTO.getQuantity())) {
            return ResponseEntity.badRequest().body("Quantity cannot be updated directly. Please use stock movement API");
        }

        // Update permitted fields
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());

        // Update the timestamp
        product.setUpdatedAt(LocalDateTime.now());

        Product updatedProduct = productRepository.save(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}