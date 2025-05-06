package com.example.Skytec.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.Skytec.Models.ReceiverModel;


public interface ReceiverRepository extends MongoRepository<ReceiverModel, String>{
    
}
