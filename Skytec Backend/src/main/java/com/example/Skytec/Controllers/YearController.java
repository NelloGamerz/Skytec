package com.example.Skytec.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Skytec.Models.YearFolderModel;
import com.example.Skytec.Repository.YearfolderRepository;

@RestController
@RequestMapping("/api")
public class YearController {

    @Autowired
    private YearfolderRepository yearfolderRepository;

    // @GetMapping
    // public List<YearFolderModel> getAllFolders(){
    // return yearfolderRepository.findAll();
    // }

    @GetMapping("/years")
    public Map<String, List<YearFolderModel>> getAllFolders() {
        List<YearFolderModel> folders = yearfolderRepository.findAll();
        return Map.of("data", folders);
    }

    @PostMapping("/years")
    public YearFolderModel createFolder(@RequestBody YearFolderModel folder) {
        return yearfolderRepository.save(folder);
    }

    
}
