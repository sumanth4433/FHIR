package com.yourorg.fhir.dto;

import java.util.List;

public class PrescriptionDTO {
    private String patientId;
    private String patientName;
    private String doctorName;
    private String visitDate;
    private String diagnosis; // Optional
    private String diagnosisCode; // SNOMED code
    private List<MedicationEntry> medication_list;

    public static class MedicationEntry {
        private String medicationName;
        private String medicationCode; // SNOMED code
        private String dosageInstruction;

        public String getMedicationName() {
            return medicationName;
        }

        public void setMedicationName(String medicationName) {
            this.medicationName = medicationName;
        }

        public String getMedicationCode() {
            return medicationCode;
        }

        public void setMedicationCode(String medicationCode) {
            this.medicationCode = medicationCode;
        }

        public String getDosageInstruction() {
            return dosageInstruction;
        }

        public void setDosageInstruction(String dosageInstruction) {
            this.dosageInstruction = dosageInstruction;
        }
    }

    // Getters and Setters
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDiagnosisCode() {
        return diagnosisCode;
    }

    public void setDiagnosisCode(String diagnosisCode) {
        this.diagnosisCode = diagnosisCode;
    }

    public List<MedicationEntry> getMedication_list() {
        return medication_list;
    }

    public void setMedication_list(List<MedicationEntry> medication_list) {
        this.medication_list = medication_list;
    }
}
