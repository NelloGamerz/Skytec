package com.example.Skytec.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "BillFolder")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillsFolderModel {
    @Id
    private String id;

    private String yearFolderName; // e.g., "2025-2026"
    private String folderName;  
}
