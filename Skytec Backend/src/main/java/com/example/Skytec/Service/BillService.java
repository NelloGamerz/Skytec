package com.example.Skytec.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Skytec.DTO.ExcelCreateRequest;
import com.example.Skytec.Models.BillModel;
import com.example.Skytec.Models.BillModelDto;
import com.example.Skytec.Repository.BillRepository;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private GoogleService googleService;

    public List<BillModelDto> getBillsByActivityName(String activityName) {
        List<BillModel> entities = billRepository.findByActivityName(activityName);

        return entities.stream()
                .map(entity -> new BillModelDto(entity.getFileName(), entity.getSheetUrl()))
                .collect(Collectors.toList());
    }

    public BillModelDto createSheetFromTemplate(ExcelCreateRequest request) throws Exception {
        String newTitle = request.getActivityName() + " " + request.getFileLabel();

        Map<String, String> metadata = new HashMap<>();
        metadata.put("fileLabel", request.getFileLabel());
        metadata.put("activityName", request.getActivityName());

        String newSheetId = googleService.copySheetFromTemplate(
                request.getTemplateId(), newTitle, "153ullnnq9vnuzAIqpSFNEtO2AUA9wcum", metadata);

        String sheetUrl = googleService.getSheetUrl(newSheetId);

        BillModel entity = new BillModel();
        entity.setFileName(request.getFileLabel());
        entity.setActivityName(request.getActivityName());
        entity.setSheetId(newSheetId);
        entity.setSheetUrl(sheetUrl);

        billRepository.save(entity);

        return new BillModelDto(entity.getFileName(), entity.getSheetUrl());
    }
}
