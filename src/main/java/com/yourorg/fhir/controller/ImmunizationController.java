package com.yourorg.fhir.controller;

import com.yourorg.fhir.dto.ImmunizationRecordDTO;
import com.yourorg.fhir.service.AbdmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fhir/immunization")
public class ImmunizationController {

    private final AbdmService abdmService;

    public ImmunizationController(AbdmService abdmService) {
        this.abdmService = abdmService;
    }

    @PostMapping
    public ResponseEntity<String> createImmunization(@RequestBody ImmunizationRecordDTO dto) {
        String json = abdmService.createImmunizationRecord(dto);
        return ResponseEntity.ok(json);
    }
}
