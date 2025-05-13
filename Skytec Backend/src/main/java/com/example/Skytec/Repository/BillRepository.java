package com.example.Skytec.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.Skytec.Models.BillModel;

public interface BillRepository extends MongoRepository<BillModel, String>{
    List<BillModel> findByActivityName(String activityName);
}
