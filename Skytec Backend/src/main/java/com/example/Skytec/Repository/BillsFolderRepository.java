package com.example.Skytec.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.Skytec.Models.BillsFolderModel;

public interface BillsFolderRepository extends MongoRepository<BillsFolderModel, String>{
    List<BillsFolderModel> findByYearFolderName(String yearFolderName);   
}
