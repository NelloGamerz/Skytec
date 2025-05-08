package com.example.Skytec.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.Skytec.Models.ReceiverModel;
import com.example.Skytec.Repository.ReceiverRepository;

@Service
public class ReceiverService {
    
    @Autowired
    private ReceiverRepository receiverRepository;

    public ResponseEntity<List<ReceiverModel>> getAllReceivers(){
        return new ResponseEntity<>(receiverRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<ReceiverModel> addReceiver(ReceiverModel receiver){
        ReceiverModel saved = receiverRepository.save(receiver);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    public ResponseEntity<ReceiverModel> updateReceiver(String id, ReceiverModel updatedReceiver){
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

    public ResponseEntity<Void> deleteReceiver(String id){
        if (receiverRepository.existsById(id)) {
            receiverRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
