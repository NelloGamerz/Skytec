package com.example.Skytec.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.Skytec.Models.ItemModel;

public interface ItemRepository extends MongoRepository<ItemModel, String> {
    Optional<ItemModel> findByItemName(String itemName);
}
