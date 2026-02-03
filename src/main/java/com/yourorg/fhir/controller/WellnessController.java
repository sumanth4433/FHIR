package com.yourorg.fhir.controller;

import com.yourorg.fhir.dto.WellnessRecordDTO;
import com.yourorg.fhir.service.AbdmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fhir/wellness")
public class WellnessController {

    private final AbdmService abdmService;

    public WellnessController(AbdmService abdmService) {
        this.abdmService = abdmService;
    }

    @PostMapping
    public ResponseEntity<String> createWellnessRecord(@RequestBody WellnessRecordDTO dto) {
        String json = abdmService.createWellnessRecord(dto);
        return ResponseEntity.ok(json);
    }
}
