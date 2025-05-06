package com.example.Skytec.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Skytec.DTO.TemplateRequest;
import com.example.Skytec.Models.TemplateModel;
import com.example.Skytec.Repository.TemplateRepository;
import com.example.Skytec.Service.TemplateService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private TemplateRepository templateRepository;


    @PostMapping("/create-template-sheet")
    public ResponseEntity<TemplateModel> createTemplate(@RequestBody TemplateRequest request) {
        log.info("Received request to create template: {}", request);
        System.out.println(request);

        try {
            TemplateModel model = templateService.createTemplate(request);
            return ResponseEntity.ok(model);
        } catch (Exception e) {
            log.error("Error occurred while creating template", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/templates")
    public List<TemplateModel> getAllTemplates() {
        return templateRepository.findAll();
    }
}
