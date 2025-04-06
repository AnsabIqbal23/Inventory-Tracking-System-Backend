package com.bazaar.Inventory_Tracking_System.listener;

import com.bazaar.Inventory_Tracking_System.event.StockUpdateEvent;
import com.bazaar.Inventory_Tracking_System.entity.StockMovement;
import com.bazaar.Inventory_Tracking_System.repository.StockMovementRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class StockUpdateListener {

    private final StockMovementRepository stockMovementRepository;

    public StockUpdateListener(StockMovementRepository stockMovementRepository) {
        this.stockMovementRepository = stockMovementRepository;
    }

    @Async  // Enables async execution
    @EventListener
    public void handleStockUpdate(StockUpdateEvent event) {
        StockMovement stockMovement = event.getStockMovement();
        stockMovementRepository.save(stockMovement); // Process stock update
        System.out.println("Stock updated asynchronously: " + stockMovement);
    }
}
