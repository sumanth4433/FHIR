package com.yourorg.fhir.dto;

import java.util.List;

public class IPDischargeSummaryDTO {
    // Patient Details
    private String patientId;
    private String patientName;
    private String patientGender;
    private String patientDob;
    private String patientMobile;

    // Encounter Details
    private String encounterId;
    private String admissionDate;
    private String dischargeDate;
    private String dischargeDisposition; // e.g. "Home", "Transfer"

    // Practitioner/Organization Details
    private String doctorName;
    private String doctorLicense;
    private String hospitalName; // Organization

    // Sections
    private List<ConditionEntry> chiefComplaints;
    private List<HistoryEntry> medicalHistory; // Conditions or Procedures
    private List<InvestigationEntry> investigations;
    private List<ProcedureEntry> procedures;
    private List<MedicationEntry> medications;
    private CarePlanEntry carePlan;
    private DocumentReferenceEntry documentReference;

    public static class ConditionEntry {
        private String code;
        private String display;
        private String text;
        private String clinicalStatus; // active, recurrence, etc.

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

        public String getClinicalStatus() {
            return clinicalStatus;
        }

        public void setClinicalStatus(String clinicalStatus) {
            this.clinicalStatus = clinicalStatus;
        }
    }

    public static class HistoryEntry {
        private String type; // "Condition" or "Procedure"
        private String code;
        private String display;
        private String text;

        // Getters and Setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

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

    public static class InvestigationEntry {
        private String title; // "Lipid Panel"
        private String code; // LOINC code for report
        private String date;
        private List<ObservationEntry> results;
        private String conclusion;

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<ObservationEntry> getResults() {
            return results;
        }

        public void setResults(List<ObservationEntry> results) {
            this.results = results;
        }

        public String getConclusion() {
            return conclusion;
        }

        public void setConclusion(String conclusion) {
            this.conclusion = conclusion;
        }
    }

    public static class ObservationEntry {
        private String testName;
        private String code;
        private String value;
        private String unit;

        // Getters and Setters
        public String getTestName() {
            return testName;
        }

        public void setTestName(String testName) {
            this.testName = testName;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }

    public static class ProcedureEntry {
        private String procedureName;
        private String code;
        private String date;
        private String note;

        // Getters and Setters
        public String getProcedureName() {
            return procedureName;
        }

        public void setProcedureName(String procedureName) {
            this.procedureName = procedureName;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }

    public static class MedicationEntry {
        private String medicationName;
        private String code; // SNOMED
        private String dosage;

        // Getters and Setters
        public String getMedicationName() {
            return medicationName;
        }

        public void setMedicationName(String medicationName) {
            this.medicationName = medicationName;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDosage() {
            return dosage;
        }

        public void setDosage(String dosage) {
            this.dosage = dosage;
        }
    }

    public static class CarePlanEntry {
        private String title;
        private String date;
        private String details; // Narrative
        private AppointmentEntry followUp;

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public AppointmentEntry getFollowUp() {
            return followUp;
        }

        public void setFollowUp(AppointmentEntry followUp) {
            this.followUp = followUp;
        }
    }

    public static class AppointmentEntry {
        private String date;
        private String reason;

        // Getters and Setters
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    public static class DocumentReferenceEntry {
        private String title;
        private String base64Data;

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBase64Data() {
            return base64Data;
        }

        public void setBase64Data(String base64Data) {
            this.base64Data = base64Data;
        }
    }

    // Main Getters and Setters
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

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientDob() {
        return patientDob;
    }

    public void setPatientDob(String patientDob) {
        this.patientDob = patientDob;
    }

    public String getPatientMobile() {
        return patientMobile;
    }

    public void setPatientMobile(String patientMobile) {
        this.patientMobile = patientMobile;
    }

    public String getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(String encounterId) {
        this.encounterId = encounterId;
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

    public String getDischargeDisposition() {
        return dischargeDisposition;
    }

    public void setDischargeDisposition(String dischargeDisposition) {
        this.dischargeDisposition = dischargeDisposition;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorLicense() {
        return doctorLicense;
    }

    public void setDoctorLicense(String doctorLicense) {
        this.doctorLicense = doctorLicense;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public List<ConditionEntry> getChiefComplaints() {
        return chiefComplaints;
    }

    public void setChiefComplaints(List<ConditionEntry> chiefComplaints) {
        this.chiefComplaints = chiefComplaints;
    }

    public List<HistoryEntry> getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(List<HistoryEntry> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public List<InvestigationEntry> getInvestigations() {
        return investigations;
    }

    public void setInvestigations(List<InvestigationEntry> investigations) {
        this.investigations = investigations;
    }

    public List<ProcedureEntry> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<ProcedureEntry> procedures) {
        this.procedures = procedures;
    }

    public List<MedicationEntry> getMedications() {
        return medications;
    }

    public void setMedications(List<MedicationEntry> medications) {
        this.medications = medications;
    }

    public CarePlanEntry getCarePlan() {
        return carePlan;
    }

    public void setCarePlan(CarePlanEntry carePlan) {
        this.carePlan = carePlan;
    }

    public DocumentReferenceEntry getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(DocumentReferenceEntry documentReference) {
        this.documentReference = documentReference;
    }
}
