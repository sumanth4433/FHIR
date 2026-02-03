package com.yourorg.fhir.controller;

import com.yourorg.fhir.dto.PrescriptionDTO;
import com.yourorg.fhir.service.AbdmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fhir/prescription")
public class PrescriptionController {

    private final AbdmService abdmService;

    public PrescriptionController(AbdmService abdmService) {
        this.abdmService = abdmService;
    }

    @PostMapping
    public ResponseEntity<String> createPrescription(@RequestBody PrescriptionDTO dto) {
        String json = abdmService.createPrescription(dto);
        return ResponseEntity.ok(json);
    }
}
