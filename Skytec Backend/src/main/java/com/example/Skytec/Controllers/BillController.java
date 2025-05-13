package com.example.Skytec.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Skytec.DTO.ExcelCreateRequest;
import com.example.Skytec.Models.BillModelDto;
import com.example.Skytec.Service.BillService;

@RestController
@RequestMapping("/api")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping("/bills")
    public List<BillModelDto> getBills(@RequestParam String activityName) {
        return billService.getBillsByActivityName(activityName);
    }

    @PostMapping("/create-excel")
    public ResponseEntity<BillModelDto> createExcel(@RequestBody ExcelCreateRequest request) {
        try {
            BillModelDto created = billService.createSheetFromTemplate(request);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
