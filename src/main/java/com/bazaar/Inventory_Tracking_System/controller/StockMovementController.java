package com.bazaar.Inventory_Tracking_System.controller;

import com.bazaar.Inventory_Tracking_System.dto.StockMovementDTO;
import com.bazaar.Inventory_Tracking_System.entity.Product;
import com.bazaar.Inventory_Tracking_System.entity.StockMovement;
import com.bazaar.Inventory_Tracking_System.event.StockUpdateEvent;
import com.bazaar.Inventory_Tracking_System.exception.ResourceNotFoundException;
import com.bazaar.Inventory_Tracking_System.repository.ProductRepository;
import com.bazaar.Inventory_Tracking_System.service.StockMovementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

    @Autowired
    private StockMovementService stockMovementService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<StockMovement> getAllStockMovements() {
        return stockMovementService.getAllStockMovements();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<StockMovement> getStockMovementById(@PathVariable Long id) {
        StockMovement stockMovement = stockMovementService.getStockMovementById(id);
        return ResponseEntity.ok(stockMovement);
    }

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<StockMovement>> getStockMovementsByProductId(@PathVariable Long productId) {
        // Verify product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

        List<StockMovement> stockMovements = stockMovementService.getStockMovementsByProductId(productId);
        return ResponseEntity.ok(stockMovements);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> createStockMovement(@Valid @RequestBody StockMovementDTO stockMovementDTO) {
        try {
            // Find the product
            Product product = productRepository.findById(stockMovementDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + stockMovementDTO.getProductId()));

            // Create the stock movement
            StockMovement stockMovement = new StockMovement();
            stockMovement.setProduct(product);
            stockMovement.setQuantityChange(stockMovementDTO.getQuantityChange());
            stockMovement.setTimestamp(LocalDateTime.now());

            // Update the product quantity
            int newQuantity = product.getQuantity() + stockMovementDTO.getQuantityChange();
            if (newQuantity < 0) {
                return ResponseEntity.badRequest().body("Operation would result in negative inventory. Current quantity: " +
                        product.getQuantity() + ", Requested change: " + stockMovementDTO.getQuantityChange());
            }

            product.setQuantity(newQuantity);
            product.setUpdatedAt(LocalDateTime.now()); // Update the general timestamp
            productRepository.save(product);

            // Save the stock movement
            StockMovement savedStockMovement = stockMovementService.createStockMovement(stockMovement);

            // Publish event
            eventPublisher.publishEvent(new StockUpdateEvent(this, savedStockMovement));

            return ResponseEntity.ok(savedStockMovement);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing stock movement: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> updateStockMovement(@PathVariable Long id, @Valid @RequestBody StockMovementDTO stockMovementDTO) {
        try {
            // Find the existing stock movement
            StockMovement stockMovement = stockMovementService.getStockMovementById(id);

            // Calculate the difference between old and new quantity change
            int quantityDifference = stockMovementDTO.getQuantityChange() - stockMovement.getQuantityChange();

            // Find the product
            Product product = stockMovement.getProduct();

            // If the product ID is being changed, return an error (not allowed)
            if (!product.getId().equals(stockMovementDTO.getProductId())) {
                return ResponseEntity.badRequest().body("Changing the product associated with a stock movement is not allowed");
            }

            // Update the existing product quantity by the difference
            int newQuantity = product.getQuantity() + quantityDifference;
            if (newQuantity < 0) {
                return ResponseEntity.badRequest().body("Operation would result in negative inventory. Current quantity: " +
                        product.getQuantity() + ", Original change: " + stockMovement.getQuantityChange() +
                        ", New requested change: " + stockMovementDTO.getQuantityChange());
            }

            product.setQuantity(newQuantity);
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);

            // Update the stock movement
            stockMovement.setQuantityChange(stockMovementDTO.getQuantityChange());

            // Save the updated stock movement
            StockMovement updatedStockMovement = stockMovementService.updateStockMovement(id, stockMovement);

            return ResponseEntity.ok(updatedStockMovement);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating stock movement: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> deleteStockMovement(@PathVariable Long id) {
        try {
            // Find the stock movement
            StockMovement stockMovement = stockMovementService.getStockMovementById(id);

            // Revert the effect on product quantity
            Product product = stockMovement.getProduct();
            int newQuantity = product.getQuantity() - stockMovement.getQuantityChange();
            if (newQuantity < 0) {
                return ResponseEntity.badRequest().body("Deleting this stock movement would result in negative inventory. " +
                        "Current quantity: " + product.getQuantity() + ", Movement quantity: " + stockMovement.getQuantityChange());
            }

            product.setQuantity(newQuantity);
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);

            // Delete the stock movement
            stockMovementService.deleteStockMovement(id);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting stock movement: " + e.getMessage());
        }
    }
}