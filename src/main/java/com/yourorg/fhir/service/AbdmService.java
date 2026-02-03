package com.yourorg.fhir.service;

import ca.uhn.fhir.context.FhirContext;
import com.yourorg.fhir.builder.AbdmResourceBuilder;
import com.yourorg.fhir.util.FhirConstants;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Service responsible for orchestrating the creation of ABDM-compliant FHIR
 * Document Bundles.
 * It uses AbdmResourceBuilder to construct individual resources and wraps them
 * in a
 * Bundle for various document types (Prescription, Discharge Summary, etc.).
 */
@Service
public class AbdmService {

    private final FhirContext fhirContext;
    private final AbdmResourceBuilder builder;
    private final com.yourorg.fhir.builder.DiagnosticReportBuilder diagnosticReportBuilder;
    private final com.yourorg.fhir.builder.ObservationBuilder observationBuilder;

    public AbdmService(FhirContext fhirContext, AbdmResourceBuilder builder,
            com.yourorg.fhir.builder.DiagnosticReportBuilder diagnosticReportBuilder,
            com.yourorg.fhir.builder.ObservationBuilder observationBuilder) {
        this.fhirContext = fhirContext;
        this.builder = builder;
        this.diagnosticReportBuilder = diagnosticReportBuilder;
        this.observationBuilder = observationBuilder;
    }

    /**
     * Wraps a list of resources into a FHIR DOCUMENT Bundle.
     * 
     * @param composition The document header
     * @param resources   List of supporting resources (Patient, Practitioner,
     *                    Observations, etc.)
     * @return FHIR Bundle
     */
    private Bundle wrapInBundle(Composition composition, java.util.List<? extends Resource> resources, Date date) {
        Bundle bundle = new Bundle();
        bundle.setId(UUID.randomUUID().toString());
        bundle.setType(Bundle.BundleType.DOCUMENT);
        bundle.setTimestamp(date != null ? date : new Date());
        bundle.getMeta().setVersionId("1")
                .addProfile(FhirConstants.PROFILE_DOC_BUNDLE);

        bundle.setIdentifier(new Identifier().setSystem(FhirConstants.SYSTEM_BUNDLE_ID)
                .setValue(UUID.randomUUID().toString()));

        // Add Composition first
        bundle.addEntry().setFullUrl("urn:uuid:" + composition.getId()).setResource(composition);

        // Add other resources
        for (Resource res : resources) {
            bundle.addEntry().setFullUrl("urn:uuid:" + res.getId()).setResource(res);
        }

        return bundle;
    }

    private String serialize(Bundle bundle) {
        return fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
    }

    /**
     * Creates an ABDM Prescription Record bundle.
     */
    public String createPrescription(com.yourorg.fhir.dto.PrescriptionDTO dto) {
        Date visitDate = com.yourorg.fhir.util.FhirUtil.parseDate(dto.getVisitDate());
        Patient patient = builder.buildPatient(dto.getPatientName(), dto.getPatientId(), null, null);
        Practitioner doc = builder.buildPractitioner(dto.getDoctorName(), null);

        // Use provided diagnosis or fallback to generic
        String diagCode = dto.getDiagnosisCode() != null ? dto.getDiagnosisCode() : "297142003";
        String diagText = dto.getDiagnosis() != null ? dto.getDiagnosis() : "Patient presents with symptoms";

        Condition condition = builder.buildCondition(patient, diagCode, diagText, diagText, visitDate);

        java.util.List<MedicationRequest> medsList = new java.util.ArrayList<>();
        if (dto.getMedication_list() != null) {
            for (com.yourorg.fhir.dto.PrescriptionDTO.MedicationEntry entry : dto.getMedication_list()) {
                medsList.add(builder.buildMedicationRequest(patient, doc, condition, entry.getMedicationName(),
                        entry.getMedicationCode(), entry.getDosageInstruction(), visitDate));
            }
        }

        Composition comp = builder.buildPrescriptionComposition(patient, doc, medsList, visitDate);

        java.util.List<Resource> resources = new java.util.ArrayList<>();
        resources.add(patient);
        resources.add(doc);
        resources.add(condition);
        resources.addAll(medsList);

        return serialize(wrapInBundle(comp, resources, visitDate));
    }

    /**
     * Creates an ABDM Discharge Summary bundle.
     */
    public String createDischargeSummary(com.yourorg.fhir.dto.DischargeSummaryDTO dto) {
        Date visitDate = com.yourorg.fhir.util.FhirUtil.parseDate(dto.getVisitDate());
        Patient patient = builder.buildPatient(dto.getPatientName(), dto.getPatientId(), null, null);
        Practitioner doc = builder.buildPractitioner(dto.getDoctorName(), null);
        Encounter enc = builder.buildEncounter(patient, doc, null, dto.getAdmissionDate(), visitDate);

        java.util.List<Condition> condList = new java.util.ArrayList<>();
        if (dto.getDiagnosis_list() != null) {
            for (com.yourorg.fhir.dto.DischargeSummaryDTO.DiagnosisEntry entry : dto.getDiagnosis_list()) {
                condList.add(builder.buildCondition(patient, entry.getCode(), entry.getDisplay(), entry.getText(),
                        visitDate));
            }
        }

        Composition comp = builder.buildDischargeSummaryComposition(patient, doc, enc, condList, visitDate);

        java.util.List<Resource> resources = new java.util.ArrayList<>();
        resources.add(patient);
        resources.add(doc);
        resources.add(enc);
        resources.addAll(condList);

        return serialize(wrapInBundle(comp, resources, visitDate));
    }

    /**
     * Creates an ABDM OP Consult Record bundle.
     */
    public String createOpConsultNote(com.yourorg.fhir.dto.OpConsultDTO dto) {
        Date visitDate = com.yourorg.fhir.util.FhirUtil.parseDate(dto.getVisitDate());
        Patient patient = builder.buildPatient(dto.getPatientName(), dto.getPatientId(), null, null);
        Practitioner doc = builder.buildPractitioner(dto.getDoctorName(), null);
        Encounter enc = builder.buildEncounter(patient, doc, null, dto.getVisitDate(), visitDate);

        java.util.List<Condition> condList = new java.util.ArrayList<>();
        if (dto.getDiagnosis_list() != null) {
            for (com.yourorg.fhir.dto.OpConsultDTO.DiagnosisEntry entry : dto.getDiagnosis_list()) {
                condList.add(builder.buildCondition(patient, entry.getCode(), entry.getDisplay(), entry.getText(),
                        visitDate));
            }
        }

        Composition comp = builder.buildOPConsultComposition(patient, doc, enc, condList, visitDate);

        java.util.List<Resource> resources = new java.util.ArrayList<>();
        resources.add(patient);
        resources.add(doc);
        resources.add(enc);
        resources.addAll(condList);

        return serialize(wrapInBundle(comp, resources, visitDate));
    }

    /**
     * Creates an ABDM Immunization Record bundle.
     */
    public String createImmunizationRecord(com.yourorg.fhir.dto.ImmunizationRecordDTO dto) {
        Date visitDate = com.yourorg.fhir.util.FhirUtil.parseDate(dto.getVisitDate());
        Patient patient = builder.buildPatient(dto.getPatientName(), dto.getPatientId(), null, null);
        Practitioner doc = builder.buildPractitioner(dto.getDoctorName(), null);

        java.util.List<Immunization> immList = new java.util.ArrayList<>();
        if (dto.getImmunization_list() != null) {
            for (com.yourorg.fhir.dto.ImmunizationRecordDTO.ImmunizationEntry entry : dto.getImmunization_list()) {
                immList.add(
                        builder.buildImmunization(patient, entry.getVaccineName(), entry.getVaccineCode(), visitDate));
            }
        }

        Composition comp = builder.buildImmunizationComposition(patient, doc, immList, visitDate);

        java.util.List<Resource> resources = new java.util.ArrayList<>();
        resources.add(patient);
        resources.add(doc);
        resources.addAll(immList);

        return serialize(wrapInBundle(comp, resources, visitDate));
    }

    /**
     * Creates an ABDM Wellness Record (Vital Signs) bundle.
     */
    public String createWellnessRecord(com.yourorg.fhir.dto.WellnessRecordDTO dto) {
        Date visitDate = com.yourorg.fhir.util.FhirUtil.parseDate(dto.getVisitDate());
        Patient patient = builder.buildPatient(dto.getPatientName(), dto.getPatientId(), null, null);
        Practitioner doc = builder.buildPractitioner(dto.getDoctorName(), null);

        java.util.List<Observation> obsList = new java.util.ArrayList<>();
        if (dto.getObservation_list() != null) {
            String sysVal = null;
            String diaVal = null;
            String bpUnit = "mm[Hg]";

            // First pass: find BP
            for (com.yourorg.fhir.dto.WellnessRecordDTO.ObservationEntry entry : dto.getObservation_list()) {
                if (FhirConstants.LOINC_SYSTOLIC.equals(entry.getObservationCode())) {
                    sysVal = entry.getValue();
                } else if (FhirConstants.LOINC_DIASTOLIC.equals(entry.getObservationCode())) {
                    diaVal = entry.getValue();
                }
            }

            // Create BP Obs if found
            if (sysVal != null || diaVal != null) {
                Observation bpObs = builder.buildBloodPressureObservation(patient, sysVal, diaVal, bpUnit, visitDate);
                obsList.add(bpObs);
            }

            // Second pass: add others
            for (com.yourorg.fhir.dto.WellnessRecordDTO.ObservationEntry entry : dto.getObservation_list()) {
                if (!FhirConstants.LOINC_SYSTOLIC.equals(entry.getObservationCode()) &&
                        !FhirConstants.LOINC_DIASTOLIC.equals(entry.getObservationCode())) {

                    obsList.add(builder.buildVitalSignObservation(patient, entry.getObservationType(),
                            entry.getObservationCode(), entry.getValue(), entry.getUnit(), visitDate));
                }
            }
        }

        Composition comp = builder.buildWellnessComposition(patient, doc, obsList, visitDate);

        java.util.List<Resource> resources = new java.util.ArrayList<>();
        resources.add(patient);
        resources.add(doc);
        resources.addAll(obsList);

        return serialize(wrapInBundle(comp, resources, visitDate));
    }

    /**
     * Creates an ABDM Health Document Record (Attachments) bundle.
     */
    public String createHealthDocumentRecord(com.yourorg.fhir.dto.HealthDocumentRecordDTO dto) {
        Date visitDate = com.yourorg.fhir.util.FhirUtil.parseDate(dto.getVisitDate());
        Patient patient = builder.buildPatient(dto.getPatientName(), dto.getPatientId(), null, null);
        Practitioner doc = builder.buildPractitioner(dto.getDoctorName(), null);

        java.util.List<DocumentReference> docRefs = new java.util.ArrayList<>();
        if (dto.getAttachment_list() != null) {
            for (com.yourorg.fhir.dto.HealthDocumentRecordDTO.AttachmentEntry entry : dto.getAttachment_list()) {
                docRefs.add(builder.buildDocumentReference(patient, entry.getDocumentTitle(),
                        entry.getFileContentBase64(), visitDate));
            }
        }

        Composition comp = new Composition();
        comp.setId(UUID.randomUUID().toString());
        comp.getMeta().addProfile(FhirConstants.PROFILE_HEALTH_DOCUMENT_RECORD);
        comp.setStatus(Composition.CompositionStatus.FINAL);
        comp.setDate(visitDate != null ? visitDate : new Date());
        comp.setTitle("Health Document Record");
        comp.setSubject(new Reference("urn:uuid:" + patient.getId()).setType("Patient"));
        comp.addAuthor(new Reference("urn:uuid:" + doc.getId()).setType("Practitioner"));
        comp.setType(new CodeableConcept(
                new Coding(FhirConstants.SYSTEM_SNOMED, FhirConstants.CODE_HEALTH_DOCUMENT, "Record artifact")));

        Composition.SectionComponent section = comp.addSection();
        for (DocumentReference docRef : docRefs) {
            section.addEntry(new Reference("urn:uuid:" + docRef.getId()));
        }

        java.util.List<Resource> resources = new java.util.ArrayList<>();
        resources.add(patient);
        resources.add(doc);
        resources.addAll(docRefs);

        return serialize(wrapInBundle(comp, resources, visitDate));
    }

    /**
     * Creates an ABDM Composite OP Consult Bundle (Wellness + Prescription + Lab +
     * Diagnosis).
     */
    public String createCompositeBundle(com.yourorg.fhir.dto.CompositeRequestDTO dto) {
        Date visitDate = com.yourorg.fhir.util.FhirUtil.parseDate(dto.getVisitDate());
        Patient patient = builder.buildPatient(dto.getPatientName(), dto.getPatientId(), dto.getPatientGender(),
                dto.getPatientDob());
        Practitioner doc = builder.buildPractitioner(dto.getDoctorName(), null);

        Organization clinic = null;
        if (dto.getClinicName() != null && !dto.getClinicName().isEmpty()) {
            clinic = builder.buildOrganization(dto.getClinicName());
        }

        Encounter enc = builder.buildEncounter(patient, doc, clinic, dto.getVisitDate(), visitDate);

        java.util.List<Resource> resources = new java.util.ArrayList<>();
        resources.add(patient);
        resources.add(doc);
        resources.add(enc);
        if (clinic != null)
            resources.add(clinic);

        // 1. Process OpConsult (Diagnosis)
        java.util.List<Condition> condList = new java.util.ArrayList<>();
        if (dto.getOpConsult() != null && dto.getOpConsult().getDiagnosis_list() != null) {
            for (com.yourorg.fhir.dto.OpConsultDTO.DiagnosisEntry entry : dto.getOpConsult().getDiagnosis_list()) {
                Condition cond = builder.buildCondition(patient, entry.getCode(), entry.getDisplay(), entry.getText(),
                        visitDate);
                condList.add(cond);
                resources.add(cond);
            }
        }

        // 2. Process Prescription (Medications)
        java.util.List<MedicationRequest> medList = new java.util.ArrayList<>();
        if (dto.getPrescription() != null && dto.getPrescription().getMedication_list() != null) {
            Condition contextCond = !condList.isEmpty() ? condList.get(0) : null;
            for (com.yourorg.fhir.dto.PrescriptionDTO.MedicationEntry entry : dto.getPrescription()
                    .getMedication_list()) {
                MedicationRequest mr = builder.buildMedicationRequest(patient, doc, contextCond,
                        entry.getMedicationName(), entry.getMedicationCode(), entry.getDosageInstruction(), visitDate);
                medList.add(mr);
                resources.add(mr);
            }
        }

        // 3. Process Wellness (Vitals)
        java.util.List<Observation> wellnessList = new java.util.ArrayList<>();
        if (dto.getWellnessRecord() != null && dto.getWellnessRecord().getObservation_list() != null) {
            String sysVal = null;
            String diaVal = null;
            String bpUnit = "mm[Hg]";

            // First pass: find BP
            for (com.yourorg.fhir.dto.WellnessRecordDTO.ObservationEntry entry : dto.getWellnessRecord()
                    .getObservation_list()) {
                if (FhirConstants.LOINC_SYSTOLIC.equals(entry.getObservationCode())) {
                    sysVal = entry.getValue();
                } else if (FhirConstants.LOINC_DIASTOLIC.equals(entry.getObservationCode())) {
                    diaVal = entry.getValue();
                }
            }

            // Create BP Obs if found
            if (sysVal != null || diaVal != null) {
                Observation bpObs = builder.buildBloodPressureObservation(patient, sysVal, diaVal, bpUnit, visitDate);
                wellnessList.add(bpObs);
                resources.add(bpObs);
            }

            // Second pass: add others
            for (com.yourorg.fhir.dto.WellnessRecordDTO.ObservationEntry entry : dto.getWellnessRecord()
                    .getObservation_list()) {
                if (!FhirConstants.LOINC_SYSTOLIC.equals(entry.getObservationCode()) &&
                        !FhirConstants.LOINC_DIASTOLIC.equals(entry.getObservationCode())) {

                    Observation obs = builder.buildVitalSignObservation(patient, entry.getObservationType(),
                            entry.getObservationCode(), entry.getValue(), entry.getUnit(), visitDate);
                    wellnessList.add(obs);
                    resources.add(obs);
                }
            }
        }

        // 4. Process Lab Reports
        java.util.List<DiagnosticReport> reports = new java.util.ArrayList<>();
        if (dto.getLabReport() != null && dto.getLabReport().getTest_list() != null
                && !dto.getLabReport().getTest_list().isEmpty()) {
            // Create one master DiagnosticReport for this session
            String mainCode = "11502-2";
            String mainDisplay = "Laboratory report";

            DiagnosticReport report = diagnosticReportBuilder.buildFromDto(
                    patient.getId(), dto.getPatientName(),
                    mainCode, mainDisplay,
                    doc, visitDate);

            // Create Observations for each test
            for (com.yourorg.fhir.dto.LabReportDTO.TestEntry entry : dto.getLabReport().getTest_list()) {
                com.yourorg.fhir.dto.LabReportDTO.TestEntry mappedEntry = new com.yourorg.fhir.dto.LabReportDTO.TestEntry();
                mappedEntry.setTestName(entry.getTestName());
                mappedEntry.setLoincCode(entry.getLoincCode());
                mappedEntry.setResultValue(entry.getResultValue());
                mappedEntry.setUnit(entry.getUnit());

                Observation obs = observationBuilder.buildLabObservation(mappedEntry, patient.getId(), doc, visitDate);
                diagnosticReportBuilder.linkResult(report, obs);
                resources.add(obs);
            }

            reports.add(report);
            resources.add(report);
        }

        // 5. Process Allergies (from OpConsultDTO)
        java.util.List<AllergyIntolerance> allergyList = new java.util.ArrayList<>();
        if (dto.getOpConsult() != null && dto.getOpConsult().getAllergy_list() != null) {
            for (com.yourorg.fhir.dto.OpConsultDTO.AllergyEntry entry : dto.getOpConsult().getAllergy_list()) {
                AllergyIntolerance allergy = builder.buildAllergyIntolerance(patient, doc,
                        entry.getCode(), entry.getDisplay(), entry.getClinicalStatus(), visitDate);
                allergyList.add(allergy);
                resources.add(allergy);
            }
        }

        Composition comp = builder.buildCompositeOPConsultComposition(patient, doc, clinic, enc, condList, medList,
                allergyList, null,
                wellnessList, reports, visitDate);

        return serialize(wrapInBundle(comp, resources, visitDate));
    }
}
