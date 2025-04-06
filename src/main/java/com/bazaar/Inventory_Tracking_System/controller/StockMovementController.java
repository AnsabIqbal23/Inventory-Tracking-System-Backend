//package com.bazaar.Inventory_Tracking_System.controller;
//
//import com.bazaar.Inventory_Tracking_System.entity.StockMovement;
//import com.bazaar.Inventory_Tracking_System.service.StockMovementService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/stock-movements")
//public class StockMovementController {
//
//    @Autowired
//    private StockMovementService stockMovementService;
//
//    @GetMapping
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    public List<StockMovement> getAllStockMovements() {
//        return stockMovementService.getAllStockMovements();
//    }
//
//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public StockMovement createStockMovement(@Valid @RequestBody StockMovement stockMovement) {
//        return stockMovementService.createStockMovement(stockMovement);
//    }
//
//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public StockMovement updateStockMovement(@PathVariable Long id, @Valid @RequestBody StockMovement stockMovementDetails) {
//        return stockMovementService.updateStockMovement(id, stockMovementDetails);
//    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Void> deleteStockMovement(@PathVariable Long id) {
//        stockMovementService.deleteStockMovement(id);
//        return ResponseEntity.noContent().build();
//    }
//}


package com.bazaar.Inventory_Tracking_System.controller;

import com.bazaar.Inventory_Tracking_System.entity.StockMovement;
import com.bazaar.Inventory_Tracking_System.event.StockUpdateEvent;
import com.bazaar.Inventory_Tracking_System.service.StockMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

    @Autowired
    private StockMovementService stockMovementService;

    @Autowired
    private ApplicationEventPublisher eventPublisher; // Event publisher

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<StockMovement> getAllStockMovements() {
        return stockMovementService.getAllStockMovements();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public StockMovement createStockMovement(@Valid @RequestBody StockMovement stockMovement) {
        StockMovement savedStockMovement = stockMovementService.createStockMovement(stockMovement);

        // 🔥 Publish stock update event asynchronously
        eventPublisher.publishEvent(new StockUpdateEvent(this, savedStockMovement));

        return savedStockMovement;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public StockMovement updateStockMovement(@PathVariable Long id, @Valid @RequestBody StockMovement stockMovementDetails) {
        return stockMovementService.updateStockMovement(id, stockMovementDetails);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStockMovement(@PathVariable Long id) {
        stockMovementService.deleteStockMovement(id);
        return ResponseEntity.noContent().build();
    }
}
