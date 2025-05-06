package com.example.Skytec.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FolderRequest {
    private String yearFolderName;
    private String folderName;
}
