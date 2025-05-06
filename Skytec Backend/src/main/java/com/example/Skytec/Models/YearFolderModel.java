package com.example.Skytec.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "YearFolder")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YearFolderModel {
    @Id
    private String yearFolderId;
    private String folderName;
}
