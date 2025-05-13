package com.example.Skytec.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Skytec.Models.ItemModel;
import com.example.Skytec.Service.ItemService;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/add")
    public ResponseEntity<ItemModel> addItem(@RequestBody ItemModel items) {
        return itemService.addItem(items);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ItemModel> updateItem(@PathVariable String id, @RequestBody ItemModel updatedItem) {
        return itemService.updateItem(id, updatedItem);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemModel>> getAllItems() {
        return itemService.getAllItems();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        return itemService.deleteItem(id);
    }

    @GetMapping("/{itemName}")
    public Optional<ItemModel> getItemByName(@PathVariable String itemName) {
        return itemService.getItemByName(itemName);
    }
}
