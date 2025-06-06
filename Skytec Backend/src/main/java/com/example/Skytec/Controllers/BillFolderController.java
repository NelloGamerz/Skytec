package com.example.Skytec.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Skytec.DTO.FolderRequest;
import com.example.Skytec.Models.BillsFolderModel;
import com.example.Skytec.Service.BillFolderService;

@RestController
@RequestMapping("/api")
public class BillFolderController {

    @Autowired
    private BillFolderService billFolderService;

    @GetMapping("getFoldersByYear")
    public List<BillsFolderModel> getFoldersByYear(@RequestParam("folderName") String folderName) {
        return billFolderService.getFolderByYear(folderName);
    }

    @PostMapping("/addFolder")
    public BillsFolderModel createFolder(@RequestBody FolderRequest request) {
        return billFolderService.createFolder(request);
    }
}
