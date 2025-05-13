package com.example.Skytec.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillModelDto {
    private String fileName;
    private String sheetUrl;
}
