package com.example.Skytec.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
    @Indexed(unique = true)
    private String itemName;
    private Integer itemHSN;
    private Double itemPrice;
    private String itemUnit;
}
