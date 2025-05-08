package com.example.Skytec.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Skytec.DTO.FolderRequest;
import com.example.Skytec.Models.BillsFolderModel;
import com.example.Skytec.Repository.BillsFolderRepository;

@Service
public class BillFolderService {

    @Autowired
    private BillsFolderRepository billsFolderRepository;

    public List<BillsFolderModel> getFolderByYear(String folderName){
        return billsFolderRepository.findByYearFolderName(folderName);
    }

    public BillsFolderModel createFolder(FolderRequest request){
        BillsFolderModel folder = new BillsFolderModel(null, request.getYearFolderName(), request.getFolderName());
        return billsFolderRepository.save(folder);
    }
    
}
