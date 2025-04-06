package com.bazaar.Inventory_Tracking_System.repository;

import com.bazaar.Inventory_Tracking_System.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStoreId(Long storeId);
}
