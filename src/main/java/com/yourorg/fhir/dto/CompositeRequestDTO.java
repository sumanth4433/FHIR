package com.yourorg.fhir.dto;

public class CompositeRequestDTO {
    private String patientId;
    private String patientName;
    private String visitDate;
    private String doctorName;

    private String patientGender;
    private String patientDob;
    private String clinicName;

    private PrescriptionDTO prescription;
    private LabReportDTO labReport;
    private OpConsultDTO opConsult;
    private WellnessRecordDTO wellnessRecord;
    private DischargeSummaryDTO dischargeSummary;
    private InvoiceDTO invoice;
    private ImmunizationRecordDTO immunizationRecord;
    private HealthDocumentRecordDTO healthDocumentRecord;

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

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public PrescriptionDTO getPrescription() {
        return prescription;
    }

    public void setPrescription(PrescriptionDTO prescription) {
        this.prescription = prescription;
    }

    public LabReportDTO getLabReport() {
        return labReport;
    }

    public void setLabReport(LabReportDTO labReport) {
        this.labReport = labReport;
    }

    public OpConsultDTO getOpConsult() {
        return opConsult;
    }

    public void setOpConsult(OpConsultDTO opConsult) {
        this.opConsult = opConsult;
    }

    public WellnessRecordDTO getWellnessRecord() {
        return wellnessRecord;
    }

    public void setWellnessRecord(WellnessRecordDTO wellnessRecord) {
        this.wellnessRecord = wellnessRecord;
    }

    public DischargeSummaryDTO getDischargeSummary() {
        return dischargeSummary;
    }

    public void setDischargeSummary(DischargeSummaryDTO dischargeSummary) {
        this.dischargeSummary = dischargeSummary;
    }

    public InvoiceDTO getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
    }

    public ImmunizationRecordDTO getImmunizationRecord() {
        return immunizationRecord;
    }

    public void setImmunizationRecord(ImmunizationRecordDTO immunizationRecord) {
        this.immunizationRecord = immunizationRecord;
    }

    public HealthDocumentRecordDTO getHealthDocumentRecord() {
        return healthDocumentRecord;
    }

    public void setHealthDocumentRecord(HealthDocumentRecordDTO healthDocumentRecord) {
        this.healthDocumentRecord = healthDocumentRecord;
    }
}
