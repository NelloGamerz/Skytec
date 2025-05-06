package com.example.Skytec.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Transporters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransporterModel {
    
    @Id
    private String id;
    private String TransporterName;
}
