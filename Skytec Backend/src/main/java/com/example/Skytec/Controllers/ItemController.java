package com.example.Skytec.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.example.Skytec.Repository.ItemRepository;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/add")
    public ResponseEntity<ItemModel> addItem(@RequestBody ItemModel items) {
        ItemModel item = itemRepository.save(items);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ItemModel> updateItem(@PathVariable String id, @RequestBody ItemModel updatedItem) {
        return itemRepository.findById(id)
                .map(item -> {
                    item.setItemName(updatedItem.getItemName());
                    item.setItemHSN(updatedItem.getItemHSN());
                    item.setItemPrice(updatedItem.getItemPrice());
                    item.setItemUnit(updatedItem.getItemUnit());
                    ItemModel saved = itemRepository.save(item);
                    return new ResponseEntity<>(saved, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemModel>> getAllItems() {
        List<ItemModel> items = itemRepository.findAll();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
