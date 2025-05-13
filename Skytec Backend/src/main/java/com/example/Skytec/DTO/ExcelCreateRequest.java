package com.example.Skytec.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelCreateRequest {
    private String activityName;
    private String templateId;
    private String templateName;
    private String templateSheetUrl;
    private String fileLabel;
}
