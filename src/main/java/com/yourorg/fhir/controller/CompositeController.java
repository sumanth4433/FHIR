package com.yourorg.fhir.controller;

import com.yourorg.fhir.dto.CompositeRequestDTO;
import com.yourorg.fhir.service.AbdmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fhir/composite")
public class CompositeController {

    private final AbdmService abdmService;

    public CompositeController(AbdmService abdmService) {
        this.abdmService = abdmService;
    }

    @PostMapping("/op-consult")
    public ResponseEntity<String> createCompositeOpConsult(@RequestBody CompositeRequestDTO requestDTO) {
        String bundleJson = abdmService.createCompositeBundle(requestDTO);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(bundleJson);
    }
}
