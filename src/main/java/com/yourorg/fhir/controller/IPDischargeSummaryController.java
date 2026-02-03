package com.yourorg.fhir.controller;

import com.yourorg.fhir.dto.IPDischargeSummaryDTO;
import com.yourorg.fhir.service.IPDischargeSummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fhir/ip-discharge-summary")
public class IPDischargeSummaryController {

    private final IPDischargeSummaryService service;

    public IPDischargeSummaryController(IPDischargeSummaryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> createIPDischargeSummary(@RequestBody IPDischargeSummaryDTO dto) {
        String json = service.generateIPDischargeSummary(dto);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(json);
    }
}
