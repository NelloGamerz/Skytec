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
import com.example.Skytec.Service.YearService;

@RestController
@RequestMapping("/api")
public class YearController {

    @Autowired
    private YearService yearService;

    @GetMapping("/years")
    public Map<String, List<YearFolderModel>> getAllFolders() {
        return yearService.getAllFolders();
    }

    @PostMapping("/years")
    public YearFolderModel createFolder(@RequestBody YearFolderModel folder) {
        return yearService.createFolder(folder);
    }

    
}
