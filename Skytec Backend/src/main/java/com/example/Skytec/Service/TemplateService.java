package com.example.Skytec.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Skytec.DTO.TemplateRequest;
import com.example.Skytec.Models.TemplateModel;
import com.example.Skytec.Repository.TemplateRepository;

import lombok.extern.slf4j.Slf4j;

// @Service
// public class TemplateService {

//     @Autowired
//     private TemplateRepository repository;

//     @Autowired
//     private GoogleService googleService;

//     public TemplateModel createTemplate(TemplateRequest request) throws Exception {
//         String sheetId = googleService.createGoogleSheet(request.getName());
//         String sheetUrl = googleService.getSheetUrl(sheetId);

//         TemplateModel model = new TemplateModel();
//         model.setName(request.getName());
//         model.setSheetId(sheetId);
//         model.setSheetUrl(sheetUrl);

//         return repository.save(model);
//     }
// }

@Slf4j
@Service
public class TemplateService {


    @Autowired
    private TemplateRepository repository;

    @Autowired
    private GoogleService googleService;

    public TemplateModel createTemplate(TemplateRequest request) throws Exception {
        log.info("Creating template with name: {}", request.getName());

        try {
            String sheetId = googleService.createGoogleSheet(request.getName());
            log.info("Google Sheet created with ID: {}", sheetId);

            String sheetUrl = googleService.getSheetUrl(sheetId);
            log.info("Google Sheet URL: {}", sheetUrl);

            TemplateModel model = new TemplateModel();
            model.setName(request.getName());
            model.setSheetId(sheetId);
            model.setSheetUrl(sheetUrl);

            TemplateModel savedModel = repository.save(model);
            log.info("Template saved to repository with ID: {}", savedModel.getId());

            return savedModel;
        } catch (Exception e) {
            log.error("Failed to create template '{}': {}", request.getName(), e.getMessage(), e);
            throw e; // Re-throw so calling controller can handle it
        }
    }
}
