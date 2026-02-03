package com.yourorg.fhir.dto;

public class DischargeSummaryDTO {
    private String patientId;
    private String patientName;
    private String doctorName;
    private String visitDate;
    private String admissionDate;
    private String dischargeDate;
    private java.util.List<DiagnosisEntry> diagnosis_list;
    private String dischargeAdvice;

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

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public java.util.List<DiagnosisEntry> getDiagnosis_list() {
        return diagnosis_list;
    }

    public void setDiagnosis_list(java.util.List<DiagnosisEntry> diagnosis_list) {
        this.diagnosis_list = diagnosis_list;
    }

    public String getDischargeAdvice() {
        return dischargeAdvice;
    }

    public void setDischargeAdvice(String dischargeAdvice) {
        this.dischargeAdvice = dischargeAdvice;
    }
}
