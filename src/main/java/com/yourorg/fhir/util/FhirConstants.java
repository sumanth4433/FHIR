package com.yourorg.fhir.util;

/**
 * Central repository for all FHIR profiles, terminologies, and system URLs
 * used across the ABDM integration.
 */
public class FhirConstants {

    // --- Profiles ---
    public static final String PROFILE_PATIENT = "https://nrces.in/ndhm/fhir/r4/StructureDefinition/Patient";
    public static final String PROFILE_PRACTITIONER = "https://nrces.in/ndhm/fhir/r4/StructureDefinition/Practitioner";
    public static final String PROFILE_CONDITION = "https://nrces.in/ndhm/fhir/r4/StructureDefinition/Condition";
    public static final String PROFILE_OBSERVATION = "https://nrces.in/ndhm/fhir/r4/StructureDefinition/Observation";
    public static final String PROFILE_DOC_BUNDLE = "https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle";
    public static final String PROFILE_ENCOUNTER = "https://nrces.in/ndhm/fhir/r4/StructureDefinition/Encounter";

    public static final String PROFILE_PRESCRIPTION_RECORD = "https://nrces.in/ndhm/fhir/r4/StructureDefinition/PrescriptionRecord";
    public static final String PROFILE_DIAGNOSTIC_REPORT_LAB = "https://nrces.in/ndhm/fhir/r4/StructureDefinition/DiagnosticReportLab";
    public static final String PROFILE_DISCHARGE_SUMMARY_RECORD = "https://nrces.in/ndhm/fhir/r4/StructureDefinition/DischargeSummaryRecord";
    public static final String PROFILE_OP_CONSULT_RECORD = "https://nrces.in/ndhm/fhir/r4/StructureDefinition/OPConsultRecord";
    public static final String PROFILE_IMMUNIZATION_RECORD = "https://nrces.in/ndhm/fhir/r4/StructureDefinition/ImmunizationRecord";
    public static final String PROFILE_WELLNESS_RECORD = "https://nrces.in/ndhm/fhir/r4/StructureDefinition/WellnessRecord";
    public static final String PROFILE_HEALTH_DOCUMENT_RECORD = "https://nrces.in/ndhm/fhir/r4/StructureDefinition/HealthDocumentRecord";

    // --- Systems ---
    public static final String SYSTEM_SNOMED = "http://snomed.info/sct";
    public static final String SYSTEM_LOINC = "http://loinc.org";
    public static final String SYSTEM_UCUM = "http://unitsofmeasure.org";
    public static final String SYSTEM_OBS_CATEGORY = "http://terminology.hl7.org/CodeSystem/observation-category";
    public static final String SYSTEM_ID_TYPE = "http://terminology.hl7.org/CodeSystem/v2-0203";
    public static final String SYSTEM_ENCOUNTER_CLASS = "http://terminology.hl7.org/CodeSystem/v3-ActCode";

    public static final String SYSTEM_PATIENT_ID = "https://healthid.ndhm.gov.in";
    public static final String SYSTEM_DOCTOR_LICENSE = "https://doctor.ndhm.gov.in";
    public static final String SYSTEM_BUNDLE_ID = "https://www.xyz-hospital.com/bundles";

    // --- Codes ---
    public static final String CODE_PRESCRIPTION_RECORD = "440545006";
    public static final String CODE_DISCHARGE_SUMMARY = "373942005";
    public static final String CODE_OP_CONSULT = "371530004";
    public static final String CODE_OP_CONSULT_RECORD = "371530004";
    public static final String CODE_DIAGNOSTIC_REPORT = "721981007";
    public static final String CODE_IMMUNIZATION_RECORD = "41000179103";
    public static final String CODE_HEALTH_DOCUMENT = "419891008";

    public static final String LOINC_BP_PANEL = "85354-9";
    public static final String LOINC_SYSTOLIC = "8480-6";
    public static final String LOINC_DIASTOLIC = "8462-4";

    public static final String CODE_FALLBACK_MEDICATION = "1145423002";
    public static final String CODE_ENCOUNTER_INPATIENT = "IMP";
}
