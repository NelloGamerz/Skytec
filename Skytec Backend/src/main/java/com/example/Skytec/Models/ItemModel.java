package com.example.Skytec.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemModel {
    @Id
    private String id;
    private String ItemName;
    private Integer ItemHSN;
    private Double ItemPrice;
    private String ItemUnit;
}
