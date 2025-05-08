package com.example.Skytec.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Skytec.Models.TransporterModel;
import com.example.Skytec.Service.TransporterService;

@RestController
@RequestMapping("/transporters")
public class TransporterController {

    @Autowired
    private TransporterService transporterService;

    @PostMapping("/add")
    public ResponseEntity<TransporterModel> addTransporter(@RequestBody TransporterModel transporter) {
        return transporterService.addTransporter(transporter);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TransporterModel> updateTransporter(@PathVariable String id, @RequestBody TransporterModel updatedTransporter) {
        return transporterService.updateTransporter(id, updatedTransporter);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TransporterModel>> getAllTransporters() {
        return transporterService.getAllTransporters();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTransporter(@PathVariable String id) {
        return transporterService.deleteTransporter(id);
    }
}
