package com.yourorg.fhir.builder;

import com.yourorg.fhir.util.FhirConstants;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * Main builder class for generating ABDM-compliant FHIR resources.
 * Handles the construction of various resources like Patient, Practitioner,
 * Observations, and Compositions with appropriate profiles and metadata.
 */
@Component
public class AbdmResourceBuilder {

    private static final java.util.Map<String, String> STANDARD_DISPLAYS = new java.util.HashMap<>();

    static {
        // Medication
        STANDARD_DISPLAYS.put("781405001", "Medicinal product package (product)");

        // Vitals (LOINC)
        STANDARD_DISPLAYS.put("29463-7", "Body weight");
        STANDARD_DISPLAYS.put("8302-2", "Body height");
        STANDARD_DISPLAYS.put("85354-9", "Blood pressure panel with all children optional");
        STANDARD_DISPLAYS.put("8480-6", "Systolic blood pressure");
        STANDARD_DISPLAYS.put("8462-4", "Diastolic blood pressure");
        STANDARD_DISPLAYS.put("8867-4", "Heart rate");
        STANDARD_DISPLAYS.put("9279-1", "Respiratory rate");
        STANDARD_DISPLAYS.put("2708-6", "Oxygen saturation in Arterial blood");
        STANDARD_DISPLAYS.put("8310-5", "Body temperature");
        STANDARD_DISPLAYS.put("8280-0", "Waist Circumference at umbilicus by Tape measure");
    }

    /**
     * Builds a Patient resource with ABDM profile and identifier.
     * 
     * @param name   Patient name
     * @param id     Patient health ID
     * @param gender Patient gender (M/F/O)
     * @param dob    Patient date of birth (YYYY-MM-DD or YYYY)
     * @return FHIR Patient resource
     */
    public Patient buildPatient(String name, String id, String gender, String dob) {
        Patient patient = new Patient();
        patient.setId(UUID.randomUUID().toString()); // Internal FHIR ID
        patient.getMeta().setVersionId("1").addProfile(FhirConstants.PROFILE_PATIENT);

        patient.addIdentifier()
                .setType(new CodeableConcept(
                        new Coding(FhirConstants.SYSTEM_ID_TYPE, "MR", "Medical record number")))
                .setSystem(FhirConstants.SYSTEM_PATIENT_ID).setValue(id != null ? id : "22-7225-4829-5255");

        patient.addName().setText(name != null ? name : "Anonymous Patient");

        if (gender != null) {
            String g = gender.toLowerCase();
            if (g.startsWith("m"))
                patient.setGender(Enumerations.AdministrativeGender.MALE);
            else if (g.startsWith("f"))
                patient.setGender(Enumerations.AdministrativeGender.FEMALE);
            else if (g.startsWith("o"))
                patient.setGender(Enumerations.AdministrativeGender.OTHER);
            else
                patient.setGender(Enumerations.AdministrativeGender.UNKNOWN);
        } else {
            patient.setGender(Enumerations.AdministrativeGender.UNKNOWN);
        }

        if (dob != null) {
            try {
                patient.setBirthDate(com.yourorg.fhir.util.FhirUtil.parseDate(dob));
            } catch (Exception e) {
                // Ignore invalid date
            }
        }

        return patient;
    }

    /**
     * Builds an Organization resource for the clinic/hospital.
     */
    public Organization buildOrganization(String name) {
        Organization org = new Organization();
        org.setId(UUID.randomUUID().toString());
        org.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Organization");
        org.setName(name != null ? name : "Unknown Clinic");

        org.addIdentifier()
                .setSystem("https://facility.ndhm.gov.in")
                .setValue("IN0000")
                .setType(new CodeableConcept(
                        new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "XX", "Organization identifier")));

        return org;
    }

    /**
     * Builds a Practitioner resource with a medical license identifier.
     * 
     * @param name    Doctor name
     * @param license Medical license number
     * @return FHIR Practitioner resource
     */
    public Practitioner buildPractitioner(String name, String license) {
        Practitioner practitioner = new Practitioner();
        practitioner.setId(UUID.randomUUID().toString());
        practitioner.getMeta().setVersionId("1")
                .addProfile(FhirConstants.PROFILE_PRACTITIONER);

        practitioner.addIdentifier()
                .setType(new CodeableConcept(
                        new Coding(FhirConstants.SYSTEM_ID_TYPE, "MD", "Medical License number")))
                .setSystem(FhirConstants.SYSTEM_DOCTOR_LICENSE).setValue(license != null ? license : "LIC-99999");

        practitioner.addName().setText(name != null ? name : "Doctor ABDM");
        return practitioner;
    }

    /**
     * Builds a Condition resource linked to a patient.
     * 
     * @param patient Subject of the condition
     * @param code    SNOMED code
     * @param display Display name
     * @param text    Original text
     * @return FHIR Condition resource
     */
    public Condition buildCondition(Patient patient, String code, String display, String text, Date date) {
        Condition condition = new Condition();
        condition.setId(UUID.randomUUID().toString());
        condition.getMeta().addProfile(FhirConstants.PROFILE_CONDITION);
        condition.setRecordedDate(date != null ? date : new Date());
        condition.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        condition.getCode().addCoding(new Coding(FhirConstants.SYSTEM_SNOMED, code, display)).setText(text);
        return condition;
    }

    /**
     * Builds a Weight Observation (Vital Signs) for Wellness records.
     * 
     * @param patient Subject
     * @param value   Weight value as string
     * @param unit    Unit (e.g., kg)
     * @return FHIR Observation resource
     */
    /**
     * Builds a Generic Vital Sign Observation.
     * 
     * @param patient Subject
     * @param display Display name (e.g., Body Weight)
     * @param code    LOINC Code (e.g., 29463-7)
     * @param value   Value as string
     * @param unit    Unit (e.g., kg)
     * @param date    Effective date
     * @return FHIR Observation resource
     */
    public Observation buildVitalSignObservation(Patient patient, String display, String code, String value,
            String unit, Date date) {
        Observation obs = new Observation();
        obs.setId(UUID.randomUUID().toString());
        obs.getMeta().addProfile(FhirConstants.PROFILE_OBSERVATION);
        // ABDM Wellness requires Vital Signs profile for entries in Vital Signs section
        obs.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns");

        obs.setStatus(Observation.ObservationStatus.FINAL);
        obs.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        obs.setEffective(new DateTimeType(date != null ? date : new Date()));

        obs.addCategory(new CodeableConcept(new Coding(FhirConstants.SYSTEM_OBS_CATEGORY,
                "vital-signs", "Vital Signs")));

        // Use provided code if available, fallback logic for legacy support if needed
        String actualCode = code != null ? code : "29463-7";
        String providedDisplay = display != null ? display : "Body Weight";
        String standardDisplay = STANDARD_DISPLAYS.getOrDefault(actualCode, providedDisplay);

        obs.setCode(new CodeableConcept(new Coding(FhirConstants.SYSTEM_LOINC, actualCode, standardDisplay))
                .setText(providedDisplay));

        // Add specific profiles for common vitals if required by strictly validated
        // validators,
        // but for now, the generic VitalSigns profile is sufficient for most.
        if ("29463-7".equals(actualCode)) {
            obs.getMeta().addProfile("http://hl7.org/fhir/StructureDefinition/bodyweight");
        } else if ("8302-2".equals(actualCode)) {
            obs.getMeta().addProfile("http://hl7.org/fhir/StructureDefinition/bodyheight");
        } else if ("85354-9".equals(actualCode) || "55284-4".equals(actualCode)) {
            obs.getMeta().addProfile("http://hl7.org/fhir/StructureDefinition/bp");
        }
        // ... extend for others if strict validation fails, but usually
        // ObservationVitalSigns is enough.

        double val = 0;
        try {
            val = Double.parseDouble(value);
        } catch (Exception e) {
        }

        obs.setValue(new Quantity().setValue(val).setUnit(unit).setSystem(FhirConstants.SYSTEM_UCUM).setCode(unit));
        obs.addPerformer(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));

        return obs;
    }

    /**
     * Builds a Blood Pressure Observation (Panel).
     */
    public Observation buildBloodPressureObservation(Patient patient, String systolicVal, String diastolicVal,
            String unit, Date date) {
        Observation obs = new Observation();
        obs.setId(UUID.randomUUID().toString());
        obs.getMeta().addProfile(FhirConstants.PROFILE_OBSERVATION);
        obs.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns");
        obs.getMeta().addProfile("http://hl7.org/fhir/StructureDefinition/bp");

        obs.setStatus(Observation.ObservationStatus.FINAL);
        obs.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        obs.setEffective(new DateTimeType(date != null ? date : new Date()));
        obs.addPerformer(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));

        obs.addCategory(
                new CodeableConcept(new Coding(FhirConstants.SYSTEM_OBS_CATEGORY, "vital-signs", "Vital Signs")));
        obs.setCode(new CodeableConcept(new Coding(FhirConstants.SYSTEM_LOINC, FhirConstants.LOINC_BP_PANEL,
                "Blood pressure panel with all children optional")));

        // Systolic Component
        if (systolicVal != null) {
            Observation.ObservationComponentComponent systolic = obs.addComponent();
            systolic.setCode(new CodeableConcept(
                    new Coding(FhirConstants.SYSTEM_LOINC, FhirConstants.LOINC_SYSTOLIC, "Systolic blood pressure")));
            double val = Double.parseDouble(systolicVal);
            systolic.setValue(new Quantity().setValue(val).setUnit(unit != null ? unit : "mm[Hg]")
                    .setSystem(FhirConstants.SYSTEM_UCUM).setCode(unit != null ? unit : "mm[Hg]"));
        }

        // Diastolic Component
        if (diastolicVal != null) {
            Observation.ObservationComponentComponent diastolic = obs.addComponent();
            diastolic.setCode(new CodeableConcept(
                    new Coding(FhirConstants.SYSTEM_LOINC, FhirConstants.LOINC_DIASTOLIC, "Diastolic blood pressure")));
            double val = Double.parseDouble(diastolicVal);
            diastolic.setValue(new Quantity().setValue(val).setUnit(unit != null ? unit : "mm[Hg]")
                    .setSystem(FhirConstants.SYSTEM_UCUM).setCode(unit != null ? unit : "mm[Hg]"));
        }

        return obs;
    }

    /**
     * Builds a MedicationRequest resource.
     * 
     * @param patient        Subject
     * @param author         Requester
     * @param reason         Linked condition
     * @param medicationName Drug name
     * @param medicationCode SNOMED code
     * @param dosage         Dosage instruction text
     * @return FHIR MedicationRequest resource
     */
    public MedicationRequest buildMedicationRequest(Patient patient, Practitioner author, Condition reason,
            String medicationName,
            String medicationCode, String dosage, Date date) {
        MedicationRequest mr = new MedicationRequest();
        mr.setId(UUID.randomUUID().toString());
        mr.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/MedicationRequest");
        mr.setStatus(MedicationRequest.MedicationRequestStatus.ACTIVE);
        mr.setIntent(MedicationRequest.MedicationRequestIntent.ORDER);

        CodeableConcept medCode = new CodeableConcept();
        String code = medicationCode != null ? medicationCode : FhirConstants.CODE_FALLBACK_MEDICATION;
        String name = medicationName != null ? medicationName : "Unspecified Medication";
        String display = STANDARD_DISPLAYS.getOrDefault(code, name);

        // Set the coding with the standard display if available, otherwise fallback to
        // name
        medCode.addCoding(new Coding(FhirConstants.SYSTEM_SNOMED, code, display));

        // Use text for the actual display name / brand name
        medCode.setText(name);

        mr.setMedication(medCode);
        mr.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        mr.setAuthoredOn(date != null ? date : new Date());
        mr.setRequester(new Reference("urn:uuid:" + author.getId()).setType("Practitioner"));

        if (reason != null) {
            mr.addReasonReference(new Reference("urn:uuid:" + reason.getId()).setType("Condition"));
        }

        mr.addDosageInstruction(new Dosage().setText(dosage != null ? dosage : "As directed by physician"));
        return mr;
    }

    /**
     * Builds the Composition for a Prescription Record.
     */
    public Composition buildPrescriptionComposition(Patient patient, Practitioner author,
            java.util.List<MedicationRequest> mrs, Date date) {
        Composition comp = new Composition();
        comp.setId(UUID.randomUUID().toString());
        comp.getMeta().addProfile(FhirConstants.PROFILE_PRESCRIPTION_RECORD);
        comp.setStatus(Composition.CompositionStatus.FINAL);
        comp.setDate(date != null ? date : new Date());
        comp.setTitle("Prescription Record");

        comp.setType(new CodeableConcept(new Coding(FhirConstants.SYSTEM_SNOMED, FhirConstants.CODE_PRESCRIPTION_RECORD,
                "Prescription record")));

        comp.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        comp.addAuthor(new Reference("urn:uuid:" + author.getId()).setType("Practitioner"));

        Composition.SectionComponent section = comp.addSection();
        section.setTitle("Prescription record (record artifact)");
        section.setCode(new CodeableConcept(new Coding(FhirConstants.SYSTEM_SNOMED,
                FhirConstants.CODE_PRESCRIPTION_RECORD, "Prescription record")));
        for (MedicationRequest mr : mrs) {
            section.addEntry(new Reference("urn:uuid:" + mr.getId()).setType("MedicationRequest"));
        }

        return comp;
    }

    /**
     * Builds the Composition for a Discharge Summary.
     */
    public Composition buildDischargeSummaryComposition(Patient patient, Practitioner author, Encounter encounter,
            java.util.List<Condition> conditions, Date date) {
        Composition comp = new Composition();
        comp.setId(UUID.randomUUID().toString());
        comp.getMeta().addProfile(FhirConstants.PROFILE_DISCHARGE_SUMMARY_RECORD);
        comp.setStatus(Composition.CompositionStatus.FINAL);
        comp.setDate(date != null ? date : new Date());
        comp.setTitle("Discharge Summary");

        comp.setType(new CodeableConcept(
                new Coding(FhirConstants.SYSTEM_SNOMED, FhirConstants.CODE_DISCHARGE_SUMMARY, "Discharge summary")));

        comp.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        comp.addAuthor(new Reference("urn:uuid:" + author.getId()).setType("Practitioner"));
        comp.setEncounter(new Reference("urn:uuid:" + encounter.getId()).setType("Encounter"));

        if (conditions != null && !conditions.isEmpty()) {
            Composition.SectionComponent diagSection = comp.addSection();
            diagSection.setTitle("Diagnosis");
            for (Condition cond : conditions) {
                diagSection.addEntry(new Reference("urn:uuid:" + cond.getId()).setType("Condition"));
            }
        }

        return comp;
    }

    /**
     * Builds an Encounter resource.
     */
    public Encounter buildEncounter(Patient patient, Practitioner practitioner, Organization serviceProvider,
            String dateStr, Date date) {
        Encounter enc = new Encounter();
        enc.setId(UUID.randomUUID().toString());
        enc.getMeta().addProfile(FhirConstants.PROFILE_ENCOUNTER);
        enc.setStatus(Encounter.EncounterStatus.FINISHED);
        enc.setClass_(new Coding(FhirConstants.SYSTEM_ENCOUNTER_CLASS, FhirConstants.CODE_ENCOUNTER_INPATIENT,
                "inpatient encounter"));
        enc.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        enc.addParticipant().setIndividual(new Reference("urn:uuid:" + practitioner.getId()).setType("Practitioner"));

        if (serviceProvider != null) {
            enc.setServiceProvider(new Reference("urn:uuid:" + serviceProvider.getId()).setType("Organization"));
        }

        Date actualDate = date != null ? date : new Date();
        enc.getPeriod().setStart(actualDate).setEnd(actualDate);
        return enc;
    }

    /**
     * Builds the Composition for an OP Consult Note.
     */
    public Composition buildOPConsultComposition(Patient patient, Practitioner author, Encounter encounter,
            java.util.List<Condition> conditions, Date date) {
        Composition comp = new Composition();
        comp.setId(UUID.randomUUID().toString());
        comp.getMeta().addProfile(FhirConstants.PROFILE_OP_CONSULT_RECORD);
        comp.setStatus(Composition.CompositionStatus.FINAL);
        comp.setDate(date != null ? date : new Date());
        comp.setTitle("OP Consult Record");

        comp.setType(new CodeableConcept(new Coding(FhirConstants.SYSTEM_SNOMED, FhirConstants.CODE_OP_CONSULT_RECORD,
                "Clinical consultation report")));

        comp.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        comp.addAuthor(new Reference("urn:uuid:" + author.getId()).setType("Practitioner"));
        comp.setEncounter(new Reference("urn:uuid:" + encounter.getId()).setType("Encounter"));

        if (conditions != null && !conditions.isEmpty()) {
            Composition.SectionComponent diagSection = comp.addSection();
            diagSection.setTitle("Diagnosis");
            for (Condition cond : conditions) {
                diagSection.addEntry(new Reference("urn:uuid:" + cond.getId()).setType("Condition"));
            }
        }

        return comp;
    }

    /**
     * Builds a Composite OP Consult Composition.
     */
    public Composition buildCompositeOPConsultComposition(Patient patient, Practitioner author, Organization custodian,
            Encounter encounter,
            java.util.List<Condition> conditions,
            java.util.List<MedicationRequest> medications,
            java.util.List<AllergyIntolerance> allergies,
            java.util.List<Observation> labResults,
            java.util.List<Observation> wellnessObs,
            java.util.List<DiagnosticReport> diagnosticReports,
            Date date) {
        Composition comp = new Composition();
        comp.setId(UUID.randomUUID().toString());
        comp.getMeta().addProfile(FhirConstants.PROFILE_OP_CONSULT_RECORD);
        comp.setStatus(Composition.CompositionStatus.FINAL);
        comp.setDate(date != null ? date : new Date());
        comp.setTitle("OP Consult Record (Composite)");

        comp.setType(new CodeableConcept(new Coding(FhirConstants.SYSTEM_SNOMED, FhirConstants.CODE_OP_CONSULT_RECORD,
                "Clinical consultation report")));

        comp.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        comp.addAuthor(new Reference("urn:uuid:" + author.getId()).setType("Practitioner"));

        if (custodian != null) {
            comp.setCustodian(new Reference("urn:uuid:" + custodian.getId()).setType("Organization"));
        }

        comp.setEncounter(new Reference("urn:uuid:" + encounter.getId()).setType("Encounter"));

        // Diagnosis Section
        if (conditions != null && !conditions.isEmpty()) {
            Composition.SectionComponent section = comp.addSection();
            section.setTitle("Diagnosis");
            for (Condition cond : conditions) {
                section.addEntry(new Reference("urn:uuid:" + cond.getId()).setType("Condition"));
            }
        }

        // Medications Section
        if (medications != null && !medications.isEmpty()) {
            Composition.SectionComponent section = comp.addSection();
            section.setTitle("Medications");
            section.setCode(new CodeableConcept(new Coding(FhirConstants.SYSTEM_SNOMED,
                    FhirConstants.CODE_PRESCRIPTION_RECORD, "Prescription record (record artifact)")));
            for (MedicationRequest mr : medications) {
                section.addEntry(new Reference("urn:uuid:" + mr.getId()).setType("MedicationRequest"));
            }
        }

        // Allergies Section
        if (allergies != null && !allergies.isEmpty()) {
            Composition.SectionComponent section = comp.addSection();
            section.setTitle("Allergies");
            section.setCode(new CodeableConcept(new Coding(FhirConstants.SYSTEM_SNOMED,
                    "722446000", "Allergy record")));
            for (AllergyIntolerance allergy : allergies) {
                section.addEntry(new Reference("urn:uuid:" + allergy.getId()).setType("AllergyIntolerance"));
            }
        }

        // Lab Results Section
        if (diagnosticReports != null && !diagnosticReports.isEmpty()) {
            Composition.SectionComponent section = comp.addSection();
            section.setTitle("Diagnostic Reports");
            section.setCode(new CodeableConcept(new Coding(FhirConstants.SYSTEM_SNOMED,
                    "721981007", "Diagnostic studies report (record artifact)")));
            for (DiagnosticReport dr : diagnosticReports) {
                section.addEntry(new Reference("urn:uuid:" + dr.getId()).setType("DiagnosticReport"));
            }
            // Add observations as entries if needed, or rely on DiagnosticReport links.
        }

        // Wellness/Vital Signs Section
        if (wellnessObs != null && !wellnessObs.isEmpty()) {
            Composition.SectionComponent section = comp.addSection();
            section.setTitle("Vital Signs");
            section.setCode(new CodeableConcept(new Coding(FhirConstants.SYSTEM_SNOMED,
                    "46680005", "Vital signs")));
            for (Observation obs : wellnessObs) {
                section.addEntry(new Reference("urn:uuid:" + obs.getId()).setType("Observation"));
            }
        }

        return comp;
    }

    /**
     * Builds the Composition for a Wellness Record.
     */
    public Composition buildWellnessComposition(Patient patient, Practitioner author,
            java.util.List<Observation> observations, Date date) {
        Composition comp = new Composition();
        comp.setId(UUID.randomUUID().toString());
        comp.getMeta().addProfile(FhirConstants.PROFILE_WELLNESS_RECORD);
        comp.setStatus(Composition.CompositionStatus.FINAL);
        comp.setDate(date != null ? date : new Date());
        comp.setTitle("Wellness Record");

        comp.setType(
                new CodeableConcept(new Coding(FhirConstants.SYSTEM_SNOMED, FhirConstants.CODE_DIAGNOSTIC_REPORT,
                        "Diagnostic studies report")));

        comp.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        comp.addAuthor(new Reference("urn:uuid:" + author.getId()).setType("Practitioner"));

        Composition.SectionComponent section = comp.addSection().setTitle("Vital Signs");
        for (Observation obs : observations) {
            section.addEntry(new Reference("urn:uuid:" + obs.getId()).setType("Observation"));
        }

        return comp;
    }

    /**
     * Builds an Immunization resource.
     */
    public Immunization buildImmunization(Patient patient, String vaccineName, String vaccineCode, Date date) {
        Immunization imm = new Immunization();
        imm.setId(UUID.randomUUID().toString());
        imm.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Immunization");
        imm.setStatus(Immunization.ImmunizationStatus.COMPLETED);
        imm.setVaccineCode(
                new CodeableConcept(
                        new Coding(FhirConstants.SYSTEM_SNOMED, vaccineCode != null ? vaccineCode : "1119305005",
                                vaccineName != null ? vaccineName : "COVID-19 antigen vaccine")));
        imm.setPatient(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        imm.setOccurrence(new DateTimeType(date != null ? date : new Date()));
        return imm;
    }

    /**
     * Builds an AllergyIntolerance resource.
     */
    public AllergyIntolerance buildAllergyIntolerance(Patient patient, Practitioner author, String code, String display,
            String clinicalStatus, Date date) {
        AllergyIntolerance allergy = new AllergyIntolerance();
        allergy.setId(UUID.randomUUID().toString());
        allergy.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/AllergyIntolerance");

        // Use supplied clinical status or default to active
        if ("inactive".equalsIgnoreCase(clinicalStatus)) {
            allergy.setClinicalStatus(new CodeableConcept(new Coding(
                    "http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical", "inactive", "Inactive")));
        } else if ("resolved".equalsIgnoreCase(clinicalStatus)) {
            allergy.setClinicalStatus(new CodeableConcept(new Coding(
                    "http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical", "resolved", "Resolved")));
        } else {
            allergy.setClinicalStatus(new CodeableConcept(new Coding(
                    "http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical", "active", "Active")));
        }

        allergy.setVerificationStatus(new CodeableConcept(new Coding(
                "http://terminology.hl7.org/CodeSystem/allergyintolerance-verification", "confirmed", "Confirmed")));

        allergy.setCode(new CodeableConcept(new Coding(FhirConstants.SYSTEM_SNOMED, code != null ? code : "716186003",
                display != null ? display : "No Known Allergy")));
        allergy.setPatient(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        allergy.setRecorder(new Reference("urn:uuid:" + author.getId()).setType("Practitioner"));
        allergy.setRecordedDate(date != null ? date : new Date());

        return allergy;
    }

    /**
     * Builds the Composition for an Immunization Record.
     */
    public Composition buildImmunizationComposition(Patient patient, Practitioner author,
            java.util.List<Immunization> imms, Date date) {
        Composition comp = new Composition();
        comp.setId(UUID.randomUUID().toString());
        comp.getMeta().addProfile(FhirConstants.PROFILE_IMMUNIZATION_RECORD);
        comp.setStatus(Composition.CompositionStatus.FINAL);
        comp.setDate(date != null ? date : new Date());
        comp.setTitle("Immunization Record");

        comp.setType(new CodeableConcept(new Coding(FhirConstants.SYSTEM_SNOMED, FhirConstants.CODE_IMMUNIZATION_RECORD,
                "Immunization record")));

        comp.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        comp.addAuthor(new Reference("urn:uuid:" + author.getId()).setType("Practitioner"));

        Composition.SectionComponent section = comp.addSection().setTitle("Immunization");
        for (Immunization imm : imms) {
            section.addEntry(new Reference("urn:uuid:" + imm.getId()).setType("Immunization"));
        }
        return comp;
    }

    /**
     * Builds a DocumentReference for attachments.
     */
    public DocumentReference buildDocumentReference(Patient patient, String title, String base64Data, Date date) {
        DocumentReference doc = new DocumentReference();
        doc.setId(UUID.randomUUID().toString());
        doc.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentReference");
        doc.setStatus(Enumerations.DocumentReferenceStatus.CURRENT);
        doc.setDocStatus(DocumentReference.ReferredDocumentStatus.FINAL);
        doc.setDate(date != null ? date : new Date());
        doc.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));

        doc.setType(new CodeableConcept(new Coding(FhirConstants.SYSTEM_SNOMED, "4241000179101", "Laboratory report")));

        doc.getContent().add(new DocumentReference.DocumentReferenceContentComponent(
                new Attachment()
                        .setContentType("application/pdf")
                        .setTitle(title != null ? title : "Medical Document")
                        .setDataElement(new Base64BinaryType(base64Data != null ? base64Data
                                : "JVBERi0xLjcKCjEgMCBvYmogICUgZW50cnkgcG9pbnQKPDwKICAvVHlwZSAvQ2F0YWxvZwogIC9QYWdlcyAyIDAgUgo+PgplbmRvYmoKCjIgMCBvYmoKPDwKICAvVHlwZSAvUGFnZXwKICAvTWVkaWFCb3ggWyAwIDAgMjAwIDIwMCBdCiAgL0NvdW50IDEKICAvS2lkcyBbIDMgMCBSIF0KPj4KZW5kb2JqCgozIDAgb2JqCjw8CiAgL1R5cGUgL1BhZ2UKICAvUGFyZW50IDIgMCBSCj4+CmVuZG9iagoKeHJlZgowIDQKMDAwMDAwMDAwMCA2NTUzNSBmIAowMDAwMDAwMDEwIDAwMDAwIG4gCjAwMDAwMDAwNjAgMDAwMDAgbiAKMDAwMDAwMDExMSAwMDAwMIG4gCnRyYWlsZXIKPDwKICAvU2l6ZSA0CiAgL1Jvb3QgMSAwIFIKPj4Kc3RhcnR4cmVmCjE3MQolJUVPRg="))));

        return doc;
    }

    /**
     * Builds a Procedure resource.
     */
    public Procedure buildProcedure(Patient patient, String code, String display, String text, Date date) {
        Procedure proc = new Procedure();
        proc.setId(UUID.randomUUID().toString());
        proc.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Procedure");
        proc.setStatus(Procedure.ProcedureStatus.COMPLETED);
        proc.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        proc.setPerformed(new DateTimeType(date != null ? date : new Date()));

        proc.setCode(new CodeableConcept(new Coding(FhirConstants.SYSTEM_SNOMED, code != null ? code : "232717009",
                display != null ? display : "Coronary artery bypass grafting")).setText(text));

        return proc;
    }

    /**
     * Builds a CarePlan resource.
     */
    public CarePlan buildCarePlan(Patient patient, String title, String details, Date date) {
        CarePlan cp = new CarePlan();
        cp.setId(UUID.randomUUID().toString());
        cp.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/CarePlan");
        cp.setStatus(CarePlan.CarePlanStatus.ACTIVE);
        cp.setIntent(CarePlan.CarePlanIntent.PLAN);
        cp.setTitle(title != null ? title : "Care Plan");
        cp.setDescription(details);
        cp.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        cp.setCreated(date != null ? date : new Date());
        return cp;
    }

    /**
     * Builds an Appointment resource.
     */
    public Appointment buildAppointment(Patient patient, Practitioner practitioner, String description, Date date) {
        Appointment appt = new Appointment();
        appt.setId(UUID.randomUUID().toString());
        appt.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Appointment");
        appt.setStatus(Appointment.AppointmentStatus.BOOKED);
        appt.setDescription(description);
        appt.setStart(date != null ? date : new Date());
        appt.setEnd(new Date((date != null ? date.getTime() : new Date().getTime()) + 30 * 60 * 1000)); // 30 min
                                                                                                        // duration

        appt.addParticipant().setActor(new Reference("urn:uuid:" + patient.getId()).setType("Patient"))
                .setStatus(Appointment.ParticipationStatus.ACCEPTED);
        if (practitioner != null) {
            appt.addParticipant().setActor(new Reference("urn:uuid:" + practitioner.getId()).setType("Practitioner"))
                    .setStatus(Appointment.ParticipationStatus.ACCEPTED);
        }

        return appt;
    }

    /**
     * Builds a Specimen resource.
     */
    public Specimen buildSpecimen(Patient patient, String code, String display, Date date) {
        Specimen specimen = new Specimen();
        specimen.setId(UUID.randomUUID().toString());
        specimen.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Specimen");
        specimen.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        if (date != null) {
            specimen.setReceivedTime(date);
            specimen.getCollection().setCollected(new DateTimeType(date));
        }
        specimen.setType(new CodeableConcept(new Coding(FhirConstants.SYSTEM_SNOMED, code != null ? code : "119364003",
                display != null ? display : "Serum specimen")));
        return specimen;
    }

    /**
     * Builds a DiagnosticReport for Lab results.
     */
    public DiagnosticReport buildDiagnosticReportLab(Patient patient, Practitioner performer, Organization org,
            java.util.List<Observation> results, Specimen specimen,
            String reportTitle, String reportCode, Date date, String conclusion) {
        DiagnosticReport dr = new DiagnosticReport();
        dr.setId(UUID.randomUUID().toString());
        dr.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DiagnosticReportLab");
        dr.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);
        dr.setCode(new CodeableConcept(new Coding(FhirConstants.SYSTEM_LOINC,
                reportCode != null ? reportCode : "90000-00", reportTitle != null ? reportTitle : "Lab Report")));
        dr.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        dr.setIssued(date != null ? date : new Date());

        if (org != null) {
            dr.addPerformer(new Reference("urn:uuid:" + org.getId()).setType("Organization"));
        }
        if (performer != null) {
            dr.addResultsInterpreter(new Reference("urn:uuid:" + performer.getId()).setType("Practitioner"));
        }
        if (specimen != null) {
            dr.addSpecimen(new Reference("urn:uuid:" + specimen.getId()).setType("Specimen"));
        }
        if (results != null) {
            for (Observation obs : results) {
                dr.addResult(new Reference("urn:uuid:" + obs.getId()).setType("Observation"));
            }
        }
        if (conclusion != null) {
            dr.setConclusion(conclusion);
        }

        return dr;
    }

    /**
     * Builds the Composition for Inpatient Discharge Summary (New Flow).
     */
    public Composition buildIPDischargeSummaryComposition(Patient patient, Practitioner author, Organization custodian,
            Encounter encounter,
            java.util.List<Condition> chiefComplaints,
            java.util.List<Resource> medicalHistory, // Condition or Procedure
            java.util.List<DiagnosticReport> investigations, // DiagnosticReport
            java.util.List<Procedure> procedures,
            java.util.List<MedicationRequest> medications,
            java.util.List<Resource> carePlan, // CarePlan, Appointment
            java.util.List<DocumentReference> documents,
            Date date) {
        Composition comp = new Composition();
        comp.setId(UUID.randomUUID().toString());
        comp.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DischargeSummaryRecord");
        comp.setStatus(Composition.CompositionStatus.FINAL);
        comp.setDate(date != null ? date : new Date());
        comp.setTitle("Discharge Summary");

        comp.setType(new CodeableConcept(new Coding(FhirConstants.SYSTEM_SNOMED, "373942005", "Discharge summary")));

        comp.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        comp.addAuthor(new Reference("urn:uuid:" + author.getId()).setType("Practitioner"));
        if (custodian != null) {
            comp.setCustodian(new Reference("urn:uuid:" + custodian.getId()).setType("Organization"));
        }
        if (encounter != null) {
            comp.setEncounter(new Reference("urn:uuid:" + encounter.getId()).setType("Encounter"));
        }

        // 1. Chief Complaints
        if (chiefComplaints != null && !chiefComplaints.isEmpty()) {
            Composition.SectionComponent section = comp.addSection();
            section.setTitle("Chief complaints");
            section.setCode(new CodeableConcept(
                    new Coding(FhirConstants.SYSTEM_SNOMED, "422843007", "Chief complaint section")));
            for (Condition cond : chiefComplaints) {
                section.addEntry(new Reference("urn:uuid:" + cond.getId()).setType("Condition"));
            }
        }

        // 2. Medical History
        if (medicalHistory != null && !medicalHistory.isEmpty()) {
            Composition.SectionComponent section = comp.addSection();
            section.setTitle("Medical History");
            section.setCode(new CodeableConcept(
                    new Coding(FhirConstants.SYSTEM_SNOMED, "1003642006", "Past medical history section")));
            for (Resource res : medicalHistory) {
                section.addEntry(new Reference("urn:uuid:" + res.getId()).setType(res.getResourceType().name()));
            }
        }

        // 3. Investigations
        if (investigations != null && !investigations.isEmpty()) {
            Composition.SectionComponent section = comp.addSection();
            section.setTitle("Investigations");
            section.setCode(new CodeableConcept(
                    new Coding(FhirConstants.SYSTEM_SNOMED, "721981007", "Diagnostic studies report")));
            for (DiagnosticReport dr : investigations) {
                section.addEntry(new Reference("urn:uuid:" + dr.getId()).setType("DiagnosticReport"));
            }
        }

        // 4. Procedures
        if (procedures != null && !procedures.isEmpty()) {
            Composition.SectionComponent section = comp.addSection();
            section.setTitle("Procedures");
            section.setCode(new CodeableConcept(
                    new Coding(FhirConstants.SYSTEM_SNOMED, "1003640003", "History of past procedure section")));
            for (Procedure proc : procedures) {
                section.addEntry(new Reference("urn:uuid:" + proc.getId()).setType("Procedure"));
            }
        }

        // 5. Medications
        if (medications != null && !medications.isEmpty()) {
            Composition.SectionComponent section = comp.addSection();
            section.setTitle("Medications");
            section.setCode(new CodeableConcept(
                    new Coding(FhirConstants.SYSTEM_SNOMED, "1003606003", "Medication history section")));
            for (MedicationRequest mr : medications) {
                section.addEntry(new Reference("urn:uuid:" + mr.getId()).setType("MedicationRequest"));
            }
        }

        // 6. Care Plan
        if (carePlan != null && !carePlan.isEmpty()) {
            Composition.SectionComponent section = comp.addSection();
            section.setTitle("Care Plan");
            section.setCode(new CodeableConcept(new Coding(FhirConstants.SYSTEM_SNOMED, "734163000", "Care plan")));
            for (Resource res : carePlan) {
                if (res instanceof CarePlan) {
                    section.addEntry(new Reference("urn:uuid:" + res.getId()).setType("CarePlan"));
                }
            }
        }

        // 7. Document Reference
        if (documents != null && !documents.isEmpty()) {
            Composition.SectionComponent section = comp.addSection();
            section.setTitle("Document Reference");
            section.setCode(
                    new CodeableConcept(new Coding(FhirConstants.SYSTEM_SNOMED, "373942005", "Discharge summary")));
            for (DocumentReference doc : documents) {
                section.addEntry(new Reference("urn:uuid:" + doc.getId()).setType("DocumentReference"));
            }
        }

        return comp;
    }
}
