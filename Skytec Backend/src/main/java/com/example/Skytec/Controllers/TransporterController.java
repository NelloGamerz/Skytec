package com.example.Skytec.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.example.Skytec.Repository.TransporterRepository;

@RestController
@RequestMapping("/transporters")
public class TransporterController {

    @Autowired
    private TransporterRepository transporterRepository;

    // Add a transporter
    @PostMapping("/add")
    public ResponseEntity<TransporterModel> addTransporter(@RequestBody TransporterModel transporter) {
        TransporterModel saved = transporterRepository.save(transporter);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Update a transporter by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<TransporterModel> updateTransporter(@PathVariable String id, @RequestBody TransporterModel updatedTransporter) {
        return transporterRepository.findById(id)
                .map(transporter -> {
                    transporter.setTransporterName(updatedTransporter.getTransporterName());
                    TransporterModel saved = transporterRepository.save(transporter);
                    return new ResponseEntity<>(saved, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all transporters
    @GetMapping("/all")
    public ResponseEntity<List<TransporterModel>> getAllTransporters() {
        return new ResponseEntity<>(transporterRepository.findAll(), HttpStatus.OK);
    }

    // Delete a transporter by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTransporter(@PathVariable String id) {
        if (transporterRepository.existsById(id)) {
            transporterRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
