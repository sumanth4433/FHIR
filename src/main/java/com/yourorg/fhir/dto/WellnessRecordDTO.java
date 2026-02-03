package com.yourorg.fhir.dto;

import java.util.List;

public class WellnessRecordDTO {
    private String patientId;
    private String patientName;
    private String doctorName;
    private String visitDate;
    private List<ObservationEntry> observation_list;

    public static class ObservationEntry {
        private String observationType; // e.g., Body Weight
        private String observationCode; // e.g., 29463-7
        private String value;
        private String unit;

        public String getObservationType() {
            return observationType;
        }

        public void setObservationType(String observationType) {
            this.observationType = observationType;
        }

        public String getObservationCode() {
            return observationCode;
        }

        public void setObservationCode(String observationCode) {
            this.observationCode = observationCode;
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

    public List<ObservationEntry> getObservation_list() {
        return observation_list;
    }

    public void setObservation_list(List<ObservationEntry> observation_list) {
        this.observation_list = observation_list;
    }
}
