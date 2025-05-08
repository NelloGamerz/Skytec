package com.example.Skytec.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.Skytec.Models.TransporterModel;
import com.example.Skytec.Repository.TransporterRepository;

@Service
public class TransporterService {
    
    @Autowired
    private TransporterRepository transporterRepository;

    public ResponseEntity<List<TransporterModel>> getAllTransporters(){
        return new ResponseEntity<>(transporterRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<TransporterModel> addTransporter(TransporterModel transporter) {
        TransporterModel saved = transporterRepository.save(transporter);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    public ResponseEntity<TransporterModel> updateTransporter(String id, TransporterModel updatedTransporter) {
        return transporterRepository.findById(id)
                .map(transporter -> {
                    transporter.setTransporterName(updatedTransporter.getTransporterName());
                    TransporterModel saved = transporterRepository.save(transporter);
                    return new ResponseEntity<>(saved, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Void> deleteTransporter(String id) {
        if (transporterRepository.existsById(id)) {
            transporterRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
