package com.yourorg.fhir.builder;

import com.yourorg.fhir.util.FhirConstants;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * Specialized builder for Laboratory Diagnostic Report Bundles.
 * Follows the specific structure required for Lab Reports in ABDM.
 */
@Component
public class DiagnosticReportBundleBuilder {

        /**
         * Builds a complete Document Bundle for a Diagnostic Report.
         */
        public Bundle build(
                        Patient patient,
                        Practitioner practitioner,
                        DiagnosticReport report,
                        java.util.List<Observation> observations,
                        Date date) {

                Bundle bundle = new Bundle();
                bundle.setType(Bundle.BundleType.DOCUMENT);
                bundle.setId(UUID.randomUUID().toString());

                Date actualDate = date != null ? date : new Date();
                bundle.setTimestamp(actualDate);

                bundle.getMeta().setVersionId("1").addProfile(FhirConstants.PROFILE_DOC_BUNDLE);

                Composition composition = buildComposition(patient, practitioner, report, actualDate);

                String compositionUrl = "urn:uuid:" + composition.getId();

                bundle.setIdentifier(
                                new Identifier()
                                                .setSystem(FhirConstants.SYSTEM_BUNDLE_ID)
                                                .setValue(UUID.randomUUID().toString()));

                // Add entries. Order matters: Composition first.
                bundle.addEntry()
                                .setFullUrl(compositionUrl)
                                .setResource(composition);

                bundle.addEntry()
                                .setFullUrl("urn:uuid:" + patient.getId())
                                .setResource(patient);

                for (Observation observation : observations) {
                        bundle.addEntry()
                                        .setFullUrl("urn:uuid:" + observation.getId())
                                        .setResource(observation);
                }

                bundle.addEntry()
                                .setFullUrl("urn:uuid:" + practitioner.getId())
                                .setResource(practitioner);

                bundle.addEntry()
                                .setFullUrl("urn:uuid:" + report.getId())
                                .setResource(report);

                return bundle;
        }

        private Composition buildComposition(
                        Patient patient,
                        Practitioner practitioner,
                        DiagnosticReport report,
                        Date date) {

                Composition composition = new Composition();
                composition.setId(UUID.randomUUID().toString());

                composition.getMeta()
                                .addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DiagnosticReportRecord");

                composition.setStatus(Composition.CompositionStatus.FINAL);
                composition.setDate(date != null ? date : new Date());

                composition.setType(
                                new CodeableConcept().addCoding(
                                                new Coding(FhirConstants.SYSTEM_SNOMED,
                                                                FhirConstants.CODE_DIAGNOSTIC_REPORT,
                                                                "Diagnostic studies report")));

                composition.setTitle("Diagnostic studies report");

                composition.setSubject(
                                new Reference("urn:uuid:" + patient.getId())
                                                .setType("Patient"));

                composition.addAuthor(
                                new Reference("urn:uuid:" + practitioner.getId())
                                                .setType("Practitioner"));

                composition.addSection(
                                new Composition.SectionComponent()
                                                .setTitle("Diagnostic studies report")
                                                .addEntry(
                                                                new Reference("urn:uuid:" + report.getId())
                                                                                .setType("DiagnosticReport")));

                return composition;
        }
}
