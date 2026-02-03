package com.yourorg.fhir.service;

import com.yourorg.fhir.builder.AbdmResourceBuilder;
import com.yourorg.fhir.dto.InvoiceDTO;
import com.yourorg.fhir.util.FhirConstants;
import com.yourorg.fhir.util.FhirUtil;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InvoiceService {

    private final AbdmResourceBuilder resourceBuilder;

    @Autowired
    public InvoiceService(AbdmResourceBuilder resourceBuilder) {
        this.resourceBuilder = resourceBuilder;
    }

    public String generateInvoiceBundle(InvoiceDTO dto) {
        try {
            // 1. Create Patient
            Patient patient = resourceBuilder.buildPatient(
                    dto.getPatientName(),
                    dto.getPatientId(),
                    dto.getPatientGender(),
                    dto.getPatientDob());

            // 2. Create Practitioner
            Practitioner practitioner = resourceBuilder.buildPractitioner(
                    dto.getPractitionerName(),
                    "Medical Practitioner");

            // 3. Create Charge Items and collect prices
            List<ChargeItem> chargeItems = new ArrayList<>();
            List<Double> unitPrices = new ArrayList<>();

            if (dto.getLineItems() != null) {
                for (InvoiceDTO.LineItem item : dto.getLineItems()) {
                    chargeItems.add(resourceBuilder.buildChargeItem(
                            patient,
                            item.getItemName(),
                            item.getQuantity())); // Pass quantity only
                    unitPrices.add(item.getUnitPrice()); // Collect separate prices
                }
            }

            // 4. Create Invoice
            Invoice invoice = resourceBuilder.buildInvoice(
                    patient,
                    chargeItems,
                    unitPrices, // Pass collected prices
                    dto.getTotalAmount(),
                    dto.getCurrency());

            // 5. Create Composition
            Date visitDate = FhirUtil.parseDate(dto.getVisitDate());
            Composition composition = resourceBuilder.buildInvoiceComposition(
                    patient,
                    practitioner,
                    visitDate,
                    invoice);

            // 6. Create Bundle
            Bundle bundle = new Bundle();
            bundle.setId(java.util.UUID.randomUUID().toString());
            bundle.setType(Bundle.BundleType.DOCUMENT);
            bundle.setTimestamp(new Date());

            // Add Identifier (Required)
            bundle.setIdentifier(new Identifier()
                    .setSystem("https://www.xyz-hospital.com/bundles")
                    .setValue(java.util.UUID.randomUUID().toString()));

            // Set Meta with Profile and Version
            bundle.setMeta(new Meta()
                    .setVersionId("1")
                    .addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle"));

            // Add resources to Bundle
            bundle.addEntry().setFullUrl("urn:uuid:" + composition.getId()).setResource(composition);
            bundle.addEntry().setFullUrl("urn:uuid:" + patient.getId()).setResource(patient);
            bundle.addEntry().setFullUrl("urn:uuid:" + practitioner.getId()).setResource(practitioner);
            bundle.addEntry().setFullUrl("urn:uuid:" + invoice.getId()).setResource(invoice);

            for (ChargeItem item : chargeItems) {
                bundle.addEntry().setFullUrl("urn:uuid:" + item.getId()).setResource(item);
            }

            // Serialize
            ca.uhn.fhir.context.FhirContext ctx = ca.uhn.fhir.context.FhirContext.forR4();
            return ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);

        } catch (Exception e) {
            throw new RuntimeException("Error generating Invoice Bundle", e);
        }
    }
}
