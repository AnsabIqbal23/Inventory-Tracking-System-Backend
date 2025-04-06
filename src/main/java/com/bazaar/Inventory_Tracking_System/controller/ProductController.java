//package com.bazaar.Inventory_Tracking_System.controller;
//
//import com.bazaar.Inventory_Tracking_System.entity.Product;
//import com.bazaar.Inventory_Tracking_System.service.ProductService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/products")
//public class ProductController {
//
//    @Autowired
//    private ProductService productService;
//
//    @GetMapping
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    public List<Product> getAllProducts() {
//        return productService.getAllProducts();
//    }
//
//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public Product createProduct(@Valid @RequestBody Product product) {
//        return productService.createProduct(product);
//    }
//
//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public Product updateProduct(@PathVariable Long id, @Valid @RequestBody Product productDetails) {
//        return productService.updateProduct(id, productDetails);
//    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
//        productService.deleteProduct(id);
//        return ResponseEntity.noContent().build();
//    }
//}


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

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDTO productDTO) {
        Store store = storeRepository.findById(productDTO.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setStore(store);

        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Product updateProduct(@PathVariable Long id, @Valid @RequestBody Product productDetails) {
        return productService.updateProduct(id, productDetails);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
