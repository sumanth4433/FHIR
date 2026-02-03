package com.yourorg.fhir.service;

import com.yourorg.fhir.builder.AbdmResourceBuilder;
import com.yourorg.fhir.dto.IPDischargeSummaryDTO;
import com.yourorg.fhir.util.FhirUtil;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class IPDischargeSummaryService {

    private final AbdmResourceBuilder builder;

    public IPDischargeSummaryService(AbdmResourceBuilder builder) {
        this.builder = builder;
    }

    public String generateIPDischargeSummary(IPDischargeSummaryDTO dto) {
        try {
            Date visitDate = FhirUtil.parseDate(dto.getAdmissionDate()); // Use admission date as base or discharge date
            Date dischargeDate = FhirUtil.parseDate(dto.getDischargeDate());
            Date now = new Date();

            // 1. Patient & Practitioner & Organization
            Patient patient = builder.buildPatient(dto.getPatientName(), dto.getPatientId(), dto.getPatientGender(),
                    dto.getPatientDob());
            Practitioner doc = builder.buildPractitioner(dto.getDoctorName(), dto.getDoctorLicense());
            Organization org = builder.buildOrganization(dto.getHospitalName());

            // 2. Encounter
            Encounter encounter = builder.buildEncounter(patient, doc, org, dto.getAdmissionDate(), visitDate);
            if (dto.getDischargeDisposition() != null) {
                Encounter.EncounterHospitalizationComponent hosp = new Encounter.EncounterHospitalizationComponent();
                hosp.setDischargeDisposition(
                        new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/discharge-disposition",
                                "home", dto.getDischargeDisposition())));
                encounter.setHospitalization(hosp);
            }
            // Update period to include discharge date
            if (dischargeDate != null) {
                encounter.getPeriod().setEnd(dischargeDate);
            }

            // 3. Sections
            List<Condition> chiefComplaints = new ArrayList<>();
            if (dto.getChiefComplaints() != null) {
                for (IPDischargeSummaryDTO.ConditionEntry entry : dto.getChiefComplaints()) {
                    chiefComplaints.add(builder.buildCondition(patient, entry.getCode(), entry.getDisplay(),
                            entry.getText(), visitDate));
                }
            }

            List<Resource> medicalHistory = new ArrayList<>();
            if (dto.getMedicalHistory() != null) {
                for (IPDischargeSummaryDTO.HistoryEntry entry : dto.getMedicalHistory()) {
                    if ("Procedure".equalsIgnoreCase(entry.getType())) {
                        medicalHistory.add(builder.buildProcedure(patient, entry.getCode(), entry.getDisplay(),
                                entry.getText(), visitDate));
                    } else {
                        medicalHistory.add(builder.buildCondition(patient, entry.getCode(), entry.getDisplay(),
                                entry.getText(), visitDate));
                    }
                }
            }

            List<Resource> investigationDependentResources = new ArrayList<>();
            List<DiagnosticReport> investigations = new ArrayList<>();
            if (dto.getInvestigations() != null) {
                for (IPDischargeSummaryDTO.InvestigationEntry entry : dto.getInvestigations()) {
                    List<Observation> observations = new ArrayList<>();
                    if (entry.getResults() != null) {
                        for (IPDischargeSummaryDTO.ObservationEntry obsEntry : entry.getResults()) {
                            Observation obs = builder.buildVitalSignObservation(patient, obsEntry.getTestName(),
                                    obsEntry.getCode(), obsEntry.getValue(), obsEntry.getUnit(), visitDate);
                            observations.add(obs);
                            investigationDependentResources.add(obs);
                        }
                    }
                    // Create a specimen if needed
                    Specimen specimen = builder.buildSpecimen(patient, "119364003", "Serum specimen", visitDate);
                    investigationDependentResources.add(specimen);

                    DiagnosticReport dr = builder.buildDiagnosticReportLab(patient, doc, org, observations, specimen,
                            entry.getTitle(), entry.getCode(), visitDate, entry.getConclusion());
                    investigations.add(dr);
                }
            }

            List<Procedure> procedures = new ArrayList<>();
            if (dto.getProcedures() != null) {
                for (IPDischargeSummaryDTO.ProcedureEntry entry : dto.getProcedures()) {
                    procedures.add(builder.buildProcedure(patient, entry.getCode(), entry.getProcedureName(),
                            entry.getNote(), FhirUtil.parseDate(entry.getDate())));
                }
            }

            List<MedicationRequest> medications = new ArrayList<>();
            if (dto.getMedications() != null) {
                for (IPDischargeSummaryDTO.MedicationEntry entry : dto.getMedications()) {
                    // Assuming reason is primary diagnosis for now, or null
                    medications.add(builder.buildMedicationRequest(patient, doc, null, entry.getMedicationName(),
                            entry.getCode(), entry.getDosage(), dischargeDate));
                }
            }

            List<Resource> carePlanList = new ArrayList<>();
            if (dto.getCarePlan() != null) {
                CarePlan cp = builder.buildCarePlan(patient, dto.getCarePlan().getTitle(),
                        dto.getCarePlan().getDetails(), dischargeDate);
                carePlanList.add(cp);

                if (dto.getCarePlan().getFollowUp() != null) {
                    Appointment appt = builder.buildAppointment(patient, doc,
                            dto.getCarePlan().getFollowUp().getReason(),
                            FhirUtil.parseDate(dto.getCarePlan().getFollowUp().getDate()));
                    carePlanList.add(appt);
                    // Link appointment to careplan activity reference conceptually, or just list it
                }
            }

            List<DocumentReference> documents = new ArrayList<>();
            if (dto.getDocumentReference() != null) {
                documents.add(builder.buildDocumentReference(patient, dto.getDocumentReference().getTitle(),
                        dto.getDocumentReference().getBase64Data(), dischargeDate));
            }

            // 4. Composition
            Composition composition = builder.buildIPDischargeSummaryComposition(patient, doc, org, encounter,
                    chiefComplaints, medicalHistory, investigations, procedures, medications, carePlanList, documents,
                    dischargeDate);

            // 5. Bundle
            Bundle bundle = new Bundle();
            bundle.setId(UUID.randomUUID().toString());
            bundle.setType(Bundle.BundleType.DOCUMENT);
            bundle.setTimestamp(new Date());
            bundle.getMeta().setVersionId("1").setLastUpdated(new Date())
                    .addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");

            bundle.setIdentifier(new Identifier().setSystem("http://hip.in").setValue(UUID.randomUUID().toString()));

            // Add entries
            addEntry(bundle, composition);
            addEntry(bundle, patient);
            addEntry(bundle, doc);
            addEntry(bundle, org);
            addEntry(bundle, encounter);

            for (Condition c : chiefComplaints)
                addEntry(bundle, c);
            for (Resource r : medicalHistory)
                addEntry(bundle, r);
            for (Resource r : investigationDependentResources)
                addEntry(bundle, r);
            for (DiagnosticReport dr : investigations)
                addEntry(bundle, dr);

            for (Procedure p : procedures)
                addEntry(bundle, p);
            for (MedicationRequest m : medications)
                addEntry(bundle, m);
            for (Resource r : carePlanList)
                addEntry(bundle, r);
            for (DocumentReference d : documents)
                addEntry(bundle, d);

            // Use HAPI FHIR Parser
            ca.uhn.fhir.context.FhirContext ctx = ca.uhn.fhir.context.FhirContext.forR4();
            return ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    private void addEntry(Bundle bundle, Resource resource) {
        bundle.addEntry().setFullUrl("urn:uuid:" + resource.getId()).setResource(resource);
    }
}
