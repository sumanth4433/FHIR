package com.yourorg.fhir.util;

import java.util.UUID;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;

public class FhirUtil {

        public static Reference patientRef(String id) {
                return new Reference("urn:uuid:" + id);
        }

        public static Identifier uuidIdentifier() {
                return new Identifier()
                                .setSystem("urn:ietf:rfc:3986")
                                .setValue("urn:uuid:" + UUID.randomUUID());
        }

        /**
         * Helper to create a Patient resource compliant with ABDM profiles.
         * Including MR (Medical Record) identifier.
         */
        public static Patient buildPatient(String id, String name) {
                Patient patient = new Patient();
                patient.setId(UUID.randomUUID().toString());

                // ABDM Patient Profile
                patient.getMeta().addProfile(
                                "https://nrces.in/ndhm/fhir/r4/StructureDefinition/Patient");

                // Add MR (Medical Record) Identifier with proper system/code
                patient.addIdentifier()
                                .setSystem("https://www.xyz-hospital.com/patient-id")
                                .setValue(id != null ? id : "12345")
                                .getType().addCoding()
                                .setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
                                .setCode("MR")
                                .setDisplay("Medical record number");

                patient.addName().setText(name);
                return patient;
        }

        /**
         * Helper to create a Practitioner resource compliant with ABDM profiles.
         * Including (mock) Medical License identifier.
         */
        public static Practitioner buildPractitioner() {

                Practitioner practitioner = new Practitioner();
                // Static UUID for demo purposes; in real app this would come from DB
                practitioner.setId("1f332c6b-568b-45e9-bf9e-70bf32302cb6");

                // ABDM Practitioner Profile
                practitioner.getMeta().addProfile(
                                "https://nrces.in/ndhm/fhir/r4/StructureDefinition/Practitioner");

                // Add Medical License Identifier
                practitioner.addIdentifier()
                                .setSystem("https://doctor-license.com")
                                .setValue("1231231")
                                .getType().addCoding()
                                .setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
                                .setCode("MD")
                                .setDisplay("Medical License number");

                practitioner.addName()
                                .setText("Doctor ABDM")
                                .setFamily("Doctor")
                                .addGiven("ABDM");

                return practitioner;
        }

        public static java.util.Date parseDate(String dateStr) {
                if (dateStr == null || dateStr.isEmpty()) {
                        return new java.util.Date();
                }
                try {
                        // Attempt standard ISO date format first
                        return java.sql.Date.valueOf(dateStr);
                } catch (Exception e) {
                        try {
                                return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                        } catch (Exception ex) {
                                return new java.util.Date();
                        }
                }
        }
}
