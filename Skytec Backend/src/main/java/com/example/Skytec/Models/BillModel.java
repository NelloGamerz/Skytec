package com.example.Skytec.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillModel {
    @Id
    private String id;

    private String fileName;
    private String sheetUrl;
    private String sheetId;
    private String activityName;
}
