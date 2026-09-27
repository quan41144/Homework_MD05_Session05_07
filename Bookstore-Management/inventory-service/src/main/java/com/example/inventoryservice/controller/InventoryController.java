package com.example.inventoryservice.controller;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Inventory> getInventoryByBookId(@PathVariable Long bookId) {
        return inventoryRepository.findByBookId(bookId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Inventory> createOrUpdateInventory(@RequestBody Inventory inventory) {
        return inventoryRepository.findByBookId(inventory.getBookId())
                .map(existing -> {
                    existing.setQuantity(inventory.getQuantity());
                    Inventory updated = inventoryRepository.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> {
                    Inventory saved = inventoryRepository.save(inventory);
                    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
                });
    }

    @DeleteMapping("/book/{bookId}")
    public ResponseEntity<Void> deleteInventoryByBookId(@PathVariable Long bookId) {
        return inventoryRepository.findByBookId(bookId)
                .map(inventory -> {
                    inventoryRepository.delete(inventory);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
