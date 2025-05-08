package com.example.Skytec.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.Skytec.Models.ItemModel;
import com.example.Skytec.Repository.ItemRepository;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public ResponseEntity<ItemModel> addItem(ItemModel items) {
        ItemModel item = itemRepository.save(items);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    public ResponseEntity<List<ItemModel>> getAllItems(){
        List<ItemModel> items = itemRepository.findAll();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    public ResponseEntity<Void> deleteItem(String id){
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<ItemModel> updateItem(String id, ItemModel updatedItem){
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

}
