package com.yourorg.fhir.controller;

import com.yourorg.fhir.dto.InvoiceDTO;
import com.yourorg.fhir.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fhir/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<String> createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        String bundleJson = invoiceService.generateInvoiceBundle(invoiceDTO);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(bundleJson);
    }
}
