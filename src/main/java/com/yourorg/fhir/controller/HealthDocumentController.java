package com.yourorg.fhir.controller;

import com.yourorg.fhir.dto.HealthDocumentRecordDTO;
import com.yourorg.fhir.service.AbdmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fhir/health-document")
public class HealthDocumentController {

    private final AbdmService abdmService;

    public HealthDocumentController(AbdmService abdmService) {
        this.abdmService = abdmService;
    }

    @PostMapping
    public ResponseEntity<String> createHealthDocument(@RequestBody HealthDocumentRecordDTO dto) {
        String json = abdmService.createHealthDocumentRecord(dto);
        return ResponseEntity.ok(json);
    }
}
