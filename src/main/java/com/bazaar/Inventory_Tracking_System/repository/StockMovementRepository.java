package com.bazaar.Inventory_Tracking_System.repository;

import com.bazaar.Inventory_Tracking_System.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findByProductId(Long productId);
}
