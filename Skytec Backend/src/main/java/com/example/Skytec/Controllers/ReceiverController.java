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

import com.example.Skytec.Models.ReceiverModel;
import com.example.Skytec.Service.ReceiverService;


@RestController
@RequestMapping("/receivers")
public class ReceiverController {

    @Autowired
    private ReceiverService receiverService;

    
    @PostMapping("/add")
    public ResponseEntity<ReceiverModel> addReceiver(@RequestBody ReceiverModel receiver) {
        return receiverService.addReceiver(receiver);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReceiverModel> updateReceiver(@PathVariable String id, @RequestBody ReceiverModel updatedReceiver) {
        return receiverService.updateReceiver(id, updatedReceiver);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReceiverModel>> getAllReceivers() {
        return receiverService.getAllReceivers();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReceiver(@PathVariable String id) {
        return receiverService.deleteReceiver(id);
    }
}

