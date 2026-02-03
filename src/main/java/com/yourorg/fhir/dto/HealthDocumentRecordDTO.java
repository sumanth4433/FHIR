package com.yourorg.fhir.dto;

public class HealthDocumentRecordDTO {
    private String patientId;
    private String patientName;
    private String doctorName;
    private String visitDate;
    private java.util.List<AttachmentEntry> attachment_list;

    public static class AttachmentEntry {
        private String documentTitle;
        private String documentType; // SNOMED code name
        private String fileContentBase64; // Base64 encoded file

        // Getters and Setters
        public String getDocumentTitle() {
            return documentTitle;
        }

        public void setDocumentTitle(String documentTitle) {
            this.documentTitle = documentTitle;
        }

        public String getDocumentType() {
            return documentType;
        }

        public void setDocumentType(String documentType) {
            this.documentType = documentType;
        }

        public String getFileContentBase64() {
            return fileContentBase64;
        }

        public void setFileContentBase64(String fileContentBase64) {
            this.fileContentBase64 = fileContentBase64;
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

    public java.util.List<AttachmentEntry> getAttachment_list() {
        return attachment_list;
    }

    public void setAttachment_list(java.util.List<AttachmentEntry> attachment_list) {
        this.attachment_list = attachment_list;
    }
}
