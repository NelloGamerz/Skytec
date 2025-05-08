package com.example.Skytec.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Skytec.Models.YearFolderModel;
import com.example.Skytec.Repository.YearfolderRepository;

@Service
public class YearService {
    
    @Autowired
    private YearfolderRepository yearfolderRepository;

    public Map<String, List<YearFolderModel>> getAllFolders() {
        List<YearFolderModel> folders = yearfolderRepository.findAll();
        return Map.of("data", folders);
    }

    public YearFolderModel createFolder(YearFolderModel folder) {
        return yearfolderRepository.save(folder);
    }
}
