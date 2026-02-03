package com.yourorg.fhir.dto;

import java.util.List;

public class LabReportDTO {

    private String patientId;
    private String patientName;
    private String visitDate;
    private List<TestEntry> test_list;

    public static class TestEntry {
        private String testName;
        private String loincCode;
        private String resultValue;
        private String unit;

        public String getTestName() {
            return testName;
        }

        public void setTestName(String testName) {
            this.testName = testName;
        }

        public String getLoincCode() {
            return loincCode;
        }

        public void setLoincCode(String loincCode) {
            this.loincCode = loincCode;
        }

        public String getResultValue() {
            return resultValue;
        }

        public void setResultValue(String resultValue) {
            this.resultValue = resultValue;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }

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

    public List<TestEntry> getTest_list() {
        return test_list;
    }

    public void setTest_list(List<TestEntry> test_list) {
        this.test_list = test_list;
    }
}
