package com.bazaar.Inventory_Tracking_System.event;

import com.bazaar.Inventory_Tracking_System.entity.StockMovement;
import org.springframework.context.ApplicationEvent;

public class StockUpdateEvent extends ApplicationEvent {
    private final StockMovement stockMovement;

    public StockUpdateEvent(Object source, StockMovement stockMovement) {
        super(source);
        this.stockMovement = stockMovement;
    }

    public StockMovement getStockMovement() {
        return stockMovement;
    }
}
