package com.yourorg.fhir.controller;

import com.yourorg.fhir.dto.OpConsultDTO;
import com.yourorg.fhir.service.AbdmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fhir/op-consult")
public class OpConsultController {

    private final AbdmService abdmService;

    public OpConsultController(AbdmService abdmService) {
        this.abdmService = abdmService;
    }

    @PostMapping
    public ResponseEntity<String> createOpConsultNote(@RequestBody OpConsultDTO dto) {
        String json = abdmService.createOpConsultNote(dto);
        return ResponseEntity.ok(json);
    }
}
