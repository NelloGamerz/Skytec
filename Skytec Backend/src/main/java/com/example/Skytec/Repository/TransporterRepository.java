package com.example.Skytec.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.Skytec.Models.TransporterModel;

public interface TransporterRepository extends MongoRepository<TransporterModel, String>{
    
}
