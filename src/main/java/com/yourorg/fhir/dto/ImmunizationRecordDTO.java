package com.yourorg.fhir.dto;

public class ImmunizationRecordDTO {
    private String patientId;
    private String patientName;
    private String doctorName;
    private String visitDate;
    private java.util.List<ImmunizationEntry> immunization_list;

    public static class ImmunizationEntry {
        private String vaccineName;
        private String vaccineCode; // SNOMED
        private String doseDate;

        // Getters and Setters
        public String getVaccineName() {
            return vaccineName;
        }

        public void setVaccineName(String vaccineName) {
            this.vaccineName = vaccineName;
        }

        public String getVaccineCode() {
            return vaccineCode;
        }

        public void setVaccineCode(String vaccineCode) {
            this.vaccineCode = vaccineCode;
        }

        public String getDoseDate() {
            return doseDate;
        }

        public void setDoseDate(String doseDate) {
            this.doseDate = doseDate;
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

    public java.util.List<ImmunizationEntry> getImmunization_list() {
        return immunization_list;
    }

    public void setImmunization_list(java.util.List<ImmunizationEntry> immunization_list) {
        this.immunization_list = immunization_list;
    }
}
