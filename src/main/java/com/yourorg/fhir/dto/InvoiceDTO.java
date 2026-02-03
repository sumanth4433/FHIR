package com.yourorg.fhir.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class InvoiceDTO {

    @JsonProperty("patient_id")
    private String patientId;

    @JsonProperty("patient_name")
    private String patientName;

    @JsonProperty("patient_gender")
    private String patientGender;

    @JsonProperty("patient_dob")
    private String patientDob;

    @JsonProperty("visit_date")
    private String visitDate;

    @JsonProperty("practitioner_name")
    private String practitionerName;

    @JsonProperty("total_amount")
    private double totalAmount;

    @JsonProperty("currency")
    private String currency; // e.g., "INR"

    @JsonProperty("line_items")
    private List<LineItem> lineItems;

    public static class LineItem {
        @JsonProperty("item_name")
        private String itemName;

        @JsonProperty("quantity")
        private int quantity;

        @JsonProperty("unit_price")
        private double unitPrice;

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
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

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getPractitionerName() {
        return practitionerName;
    }

    public void setPractitionerName(String practitionerName) {
        this.practitionerName = practitionerName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }
}
