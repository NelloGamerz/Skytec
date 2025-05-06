package com.example.Skytec.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Receivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiverModel {
    @Id
    private String id;
    private String ReceiverName;
    private String ReceiverAddress;
    private String ReceiverGSTNO;
    private String PlaceToSupply;
}
