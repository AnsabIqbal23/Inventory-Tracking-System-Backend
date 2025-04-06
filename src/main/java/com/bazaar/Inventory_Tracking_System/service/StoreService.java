package com.bazaar.Inventory_Tracking_System.service;

import com.bazaar.Inventory_Tracking_System.dto.StoreCreateDTO;
import com.bazaar.Inventory_Tracking_System.dto.StoreDTO;
import com.bazaar.Inventory_Tracking_System.dto.StoreUpdateDTO;
import com.bazaar.Inventory_Tracking_System.entity.Store;
import com.bazaar.Inventory_Tracking_System.entity.User;
import com.bazaar.Inventory_Tracking_System.repository.StoreRepository;
import com.bazaar.Inventory_Tracking_System.repository.UserRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    public List<StoreDTO> getAllStores() {
        return storeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StoreDTO getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id " + id));
        return convertToDTO(store);
    }

    public StoreDTO createStore(StoreCreateDTO storeCreateDTO) {
        User owner = userRepository.findById(storeCreateDTO.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + storeCreateDTO.getOwnerId()));

        Store store = new Store();
        store.setName(storeCreateDTO.getName());
        store.setLocation(storeCreateDTO.getLocation());
        store.setOwner(owner);

        Store savedStore = storeRepository.save(store);
        return convertToDTO(savedStore);
    }

    public StoreDTO updateStore(Long id, StoreUpdateDTO storeUpdateDTO) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id " + id));

        // Update only the provided fields
        if (storeUpdateDTO.getName() != null) {
            store.setName(storeUpdateDTO.getName());
        }

        if (storeUpdateDTO.getLocation() != null) {
            store.setLocation(storeUpdateDTO.getLocation());
        }

        // Owner is only updated if explicitly provided
        if (storeUpdateDTO.getOwnerId() != null) {
            User owner = userRepository.findById(storeUpdateDTO.getOwnerId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + storeUpdateDTO.getOwnerId()));
            store.setOwner(owner);
        }

        Store updatedStore = storeRepository.save(store);
        return convertToDTO(updatedStore);
    }

    public void deleteStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id " + id));
        storeRepository.delete(store);
    }

    private StoreDTO convertToDTO(Store store) {
        StoreDTO dto = new StoreDTO();
        dto.setId(store.getId());
        dto.setName(store.getName());
        dto.setLocation(store.getLocation());
        dto.setOwnerId(store.getOwner().getId());
        dto.setOwnerUsername(store.getOwner().getUsername());  // Assuming User has getUsername() method
        return dto;
    }
}