//package com.bazaar.Inventory_Tracking_System.service;
//
//import com.bazaar.Inventory_Tracking_System.entity.Product;
//import com.bazaar.Inventory_Tracking_System.repository.ProductRepository;
//import org.apache.kafka.common.errors.ResourceNotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ProductService {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    public List<Product> getAllProducts() {
//        return productRepository.findAll();
//    }
//
//    public Product createProduct(Product product) {
//        return productRepository.save(product);
//    }
//
//    public Product updateProduct(Long id, Product productDetails) {
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
//        product.setName(productDetails.getName());
//        product.setDescription(productDetails.getDescription());
//        product.setPrice(productDetails.getPrice());
//        product.setQuantity(productDetails.getQuantity());
//        product.setStore(productDetails.getStore());
//        return productRepository.save(product);
//    }
//
//    public void deleteProduct(Long id) {
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
//        productRepository.delete(product);
//    }
//}


package com.bazaar.Inventory_Tracking_System.service;

import com.bazaar.Inventory_Tracking_System.entity.Product;
import com.bazaar.Inventory_Tracking_System.repository.ProductRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        product.setStore(productDetails.getStore());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        productRepository.delete(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }
}
