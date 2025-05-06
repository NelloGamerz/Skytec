package com.example.Skytec.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.Skytec.Models.ItemModel;

public interface ItemRepository extends MongoRepository<ItemModel, String> {
    
}
