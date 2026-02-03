package com.yourorg.fhir.controller;

import com.yourorg.fhir.dto.LabReportDTO;
import com.yourorg.fhir.service.LabReportService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fhir")
public class DiagnosticReportController {

    private final LabReportService labService;

    public DiagnosticReportController(LabReportService labService) {
        this.labService = labService;
    }

    @PostMapping("/diagnostic-report/lab")
    public ResponseEntity<String> createLabReport(@RequestBody LabReportDTO dto) {
        return ResponseEntity.ok(labService.createLabReport(dto));
    }
}
