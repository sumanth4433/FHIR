package com.yourorg.fhir.controller;

import com.yourorg.fhir.dto.DischargeSummaryDTO;
import com.yourorg.fhir.service.AbdmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fhir/discharge-summary")
public class DischargeSummaryController {

    private final AbdmService abdmService;

    public DischargeSummaryController(AbdmService abdmService) {
        this.abdmService = abdmService;
    }

    @PostMapping
    public ResponseEntity<String> createDischargeSummary(@RequestBody DischargeSummaryDTO dto) {
        String json = abdmService.createDischargeSummary(dto);
        return ResponseEntity.ok(json);
    }
}
