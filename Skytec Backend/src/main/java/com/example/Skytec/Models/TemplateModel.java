package com.example.Skytec.Models;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "BillTemplates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateModel {
    
    private String id;
    private String name;
    private String sheetId;
    private String sheetUrl;
}
