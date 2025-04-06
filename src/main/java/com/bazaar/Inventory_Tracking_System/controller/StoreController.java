package com.bazaar.Inventory_Tracking_System.controller;

import com.bazaar.Inventory_Tracking_System.dto.StoreCreateDTO;
import com.bazaar.Inventory_Tracking_System.dto.StoreDTO;
import com.bazaar.Inventory_Tracking_System.dto.StoreUpdateDTO;
import com.bazaar.Inventory_Tracking_System.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<StoreDTO> getAllStores() {
        return storeService.getAllStores();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<StoreDTO> getStoreById(@PathVariable Long id) {
        StoreDTO store = storeService.getStoreById(id);
        return ResponseEntity.ok(store);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public StoreDTO createStore(@Valid @RequestBody StoreCreateDTO storeCreateDTO) {
        return storeService.createStore(storeCreateDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public StoreDTO updateStore(@PathVariable Long id, @RequestBody StoreUpdateDTO storeUpdateDTO) {
        return storeService.updateStore(id, storeUpdateDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.ok("Store deleted successfully");
    }
}