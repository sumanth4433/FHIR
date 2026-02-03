package com.yourorg.fhir.builder;

import com.yourorg.fhir.dto.LabReportDTO;
import com.yourorg.fhir.util.FhirConstants;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Component
public class ObservationBuilder {

        /**
         * Builds an Observation resource representing a Laboratory test result.
         * Uses standard LOINC codes and UCUM units.
         * 
         * @param entry        Input data for a single test (LOINC, value, unit)
         * @param patientId    The ID of the patient
         * @param practitioner The performer of the observation
         * @return FHIR Observation resource
         */
        public Observation buildLabObservation(LabReportDTO.TestEntry entry, String patientId,
                        Practitioner practitioner, Date date) {

                Observation obs = new Observation();
                obs.setId(UUID.randomUUID().toString()); // Use UUID for Bundle fullUrl matching

                obs.setStatus(Observation.ObservationStatus.FINAL);

                // Category = Laboratory (Mandatory for Lab Observations)
                obs.addCategory(
                                new CodeableConcept().addCoding(
                                                new Coding(
                                                                FhirConstants.SYSTEM_OBS_CATEGORY,
                                                                "laboratory",
                                                                "Laboratory")));

                // Test code (Using LOINC standard)
                obs.setCode(
                                new CodeableConcept().addCoding(
                                                new Coding(
                                                                FhirConstants.SYSTEM_LOINC,
                                                                entry.getLoincCode(),
                                                                entry.getTestName())));

                // Subject (Reference to the Patient in the Bundle)
                // Uses urn:uuid scheme for internal bundle references
                obs.setSubject(
                                new Reference("urn:uuid:" + patientId));

                // Performer (Reference to the Practitioner in the Bundle)
                obs.addPerformer(
                                new Reference("urn:uuid:" + practitioner.getId()));

                // Result value (Quantity with UCUM units)
                String value = entry.getResultValue();
                if (value == null || value.isEmpty()) {
                        value = "0"; // Default
                }

                try {
                        obs.setValue(
                                        new Quantity()
                                                        .setValue(new BigDecimal(value))
                                                        .setUnit(entry.getUnit() != null ? entry.getUnit() : "unit")
                                                        .setSystem(FhirConstants.SYSTEM_UCUM));
                } catch (Exception e) {
                        // Fallback if parsing fails
                        obs.setValue(new Quantity().setValue(0).setSystem(FhirConstants.SYSTEM_UCUM));
                }

                Date actualDate = date != null ? date : new Date();
                obs.setEffective(new DateTimeType(actualDate));
                obs.setIssued(actualDate);

                return obs;
        }
}
