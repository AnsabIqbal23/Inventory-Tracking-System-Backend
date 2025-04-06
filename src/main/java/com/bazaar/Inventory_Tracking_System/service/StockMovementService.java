package com.bazaar.Inventory_Tracking_System.service;

import com.bazaar.Inventory_Tracking_System.entity.StockMovement;
import com.bazaar.Inventory_Tracking_System.repository.StockMovementRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockMovementService {

    @Autowired
    private StockMovementRepository stockMovementRepository;

    public List<StockMovement> getAllStockMovements() {
        return stockMovementRepository.findAll();
    }

    public StockMovement createStockMovement(StockMovement stockMovement) {
        return stockMovementRepository.save(stockMovement);
    }

    public StockMovement updateStockMovement(Long id, StockMovement stockMovementDetails) {
        StockMovement stockMovement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock Movement not found with id " + id));
        stockMovement.setProduct(stockMovementDetails.getProduct());
        stockMovement.setQuantityChange(stockMovementDetails.getQuantityChange());
        stockMovement.setTimestamp(stockMovementDetails.getTimestamp());
        return stockMovementRepository.save(stockMovement);
    }

    public void deleteStockMovement(Long id) {
        StockMovement stockMovement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock Movement not found with id " + id));
        stockMovementRepository.delete(stockMovement);
    }
}
