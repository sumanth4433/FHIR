package com.yourorg.fhir.service;

import ca.uhn.fhir.context.FhirContext;
import com.yourorg.fhir.builder.DiagnosticReportBuilder;
import com.yourorg.fhir.builder.DiagnosticReportBundleBuilder;
import com.yourorg.fhir.builder.ObservationBuilder;

import com.yourorg.fhir.dto.LabReportDTO;
import com.yourorg.fhir.util.FhirUtil;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Practitioner;

import org.springframework.stereotype.Service;

@Service
public class LabReportService {

    private final FhirContext fhirContext;
    private final DiagnosticReportBuilder diagnosticReportBuilder;
    private final DiagnosticReportBundleBuilder diagnosticReportBundleBuilder;
    private final ObservationBuilder observationBuilder;

    public LabReportService(
            FhirContext fhirContext,
            DiagnosticReportBuilder diagnosticReportBuilder,
            DiagnosticReportBundleBuilder diagnosticReportBundleBuilder,
            ObservationBuilder observationBuilder) {
        this.fhirContext = fhirContext;
        this.diagnosticReportBuilder = diagnosticReportBuilder;
        this.diagnosticReportBundleBuilder = diagnosticReportBundleBuilder;
        this.observationBuilder = observationBuilder;
    }

    /**
     * Main orchestration method to generate a FHIR DiagnosticReport Bundle.
     * 
     * Workflow:
     * 1. Create Patient and Practitioner resources (using helpers/DTO).
     * 2. Create Observation (Lab Result).
     * 3. Create DiagnosticReport and link to Observation and Interpreter.
     * 4. Bundle everything into a FHIR Document Bundle (Composition + Resources).
     * 
     * @param dto Data configuration object
     * @return Serialized JSON String of the FHIR Bundle
     */
    public String createLabReport(LabReportDTO dto) {

        // 1. Build Date
        java.util.Date visitDate = FhirUtil.parseDate(dto.getVisitDate());

        // 1a. Build Patient using ID from input
        Patient patient = FhirUtil.buildPatient(dto.getPatientId(), dto.getPatientName());

        // 1b. Build Practitioner (mocked)
        Practitioner practitioner = FhirUtil.buildPractitioner();

        // Use first test details for the main DiagnosticReport code if entries exist
        String mainCode = "11502-2";
        String mainDisplay = "Laboratory report";
        if (dto.getTest_list() != null && !dto.getTest_list().isEmpty()) {
            mainCode = dto.getTest_list().get(0).getLoincCode();
            mainDisplay = dto.getTest_list().get(0).getTestName();
        }

        // 2. Build DiagnosticReport (The report container)
        DiagnosticReport report = diagnosticReportBuilder.buildFromDto(patient.getId(), dto.getPatientName(),
                mainCode, mainDisplay, practitioner, visitDate);

        // 3. Build Observations (The actual lab result values)
        java.util.List<Observation> observations = new java.util.ArrayList<>();
        if (dto.getTest_list() != null) {
            for (LabReportDTO.TestEntry entry : dto.getTest_list()) {
                Observation obs = observationBuilder.buildLabObservation(entry, patient.getId(), practitioner,
                        visitDate);
                observations.add(obs);

                // Link Observation → DiagnosticReport.result[]
                diagnosticReportBuilder.linkResult(report, obs);
            }
        }

        // 4. Wrap all resources into an ABDM-compliant Document Bundle
        Bundle bundle = diagnosticReportBundleBuilder.build(patient, practitioner, report, observations, visitDate);

        // Debug output
        System.out.println(
                fhirContext.newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(bundle));

        // 5. Serialize to JSON
        return fhirContext.newJsonParser()
                .setPrettyPrint(true)
                .encodeResourceToString(bundle);
    }
}
