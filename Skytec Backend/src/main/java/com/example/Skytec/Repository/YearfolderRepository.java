package com.example.Skytec.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.Skytec.Models.YearFolderModel;

public interface YearfolderRepository extends MongoRepository<YearFolderModel, String> {
    
}
