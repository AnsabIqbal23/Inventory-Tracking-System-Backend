package com.bazaar.Inventory_Tracking_System.repository;

import com.bazaar.Inventory_Tracking_System.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
