package com.example.Skytec.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.Skytec.Models.TemplateModel;

public interface TemplateRepository extends MongoRepository<TemplateModel, String>{
    
}
