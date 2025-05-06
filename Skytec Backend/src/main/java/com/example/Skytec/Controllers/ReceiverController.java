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

import com.example.Skytec.Models.ReceiverModel;
import com.example.Skytec.Repository.ReceiverRepository;


@RestController
@RequestMapping("/receivers")
public class ReceiverController {

    @Autowired
    private ReceiverRepository receiverRepository;

    
    @PostMapping("/add")
    public ResponseEntity<ReceiverModel> addReceiver(@RequestBody ReceiverModel receiver) {
        ReceiverModel saved = receiverRepository.save(receiver);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReceiverModel> updateReceiver(@PathVariable String id, @RequestBody ReceiverModel updatedReceiver) {
        return receiverRepository.findById(id)
                .map(receiver -> {
                    receiver.setReceiverName(updatedReceiver.getReceiverName());
                    receiver.setReceiverAddress(updatedReceiver.getReceiverAddress());
                    receiver.setReceiverGSTNO(updatedReceiver.getReceiverGSTNO());
                    receiver.setPlaceToSupply(updatedReceiver.getPlaceToSupply());
                    ReceiverModel saved = receiverRepository.save(receiver);
                    return new ResponseEntity<>(saved, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReceiverModel>> getAllReceivers() {
        return new ResponseEntity<>(receiverRepository.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReceiver(@PathVariable String id) {
        if (receiverRepository.existsById(id)) {
            receiverRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

