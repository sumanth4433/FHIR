package com.yourorg.fhir.dto;

public class OpConsultDTO {
    private String patientId;
    private String patientName;
    private String doctorName;
    private String visitDate;
    private java.util.List<DiagnosisEntry> diagnosis_list;
    private java.util.List<AllergyEntry> allergy_list;
    private String symptoms;

    public static class DiagnosisEntry {
        private String code;
        private String display;
        private String text;

        // Getters and Setters
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class AllergyEntry {
        private String code;
        private String display;
        private String clinicalStatus; // active, inactive, resolved

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public String getClinicalStatus() {
            return clinicalStatus;
        }

        public void setClinicalStatus(String clinicalStatus) {
            this.clinicalStatus = clinicalStatus;
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

    public java.util.List<DiagnosisEntry> getDiagnosis_list() {
        return diagnosis_list;
    }

    public void setDiagnosis_list(java.util.List<DiagnosisEntry> diagnosis_list) {
        this.diagnosis_list = diagnosis_list;
    }

    public java.util.List<AllergyEntry> getAllergy_list() {
        return allergy_list;
    }

    public void setAllergy_list(java.util.List<AllergyEntry> allergy_list) {
        this.allergy_list = allergy_list;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
}
