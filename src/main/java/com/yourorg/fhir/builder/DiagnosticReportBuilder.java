package com.yourorg.fhir.builder;

import com.yourorg.fhir.util.FhirConstants;
import com.yourorg.fhir.util.NarrativeUtil;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class DiagnosticReportBuilder {

        /**
         * Builds a DiagnosticReport resource following ABDM formatting.
         * Includes results interpreter, conclusion, and LOINC coding.
         * 
         * @param patientId    The ID of the patient
         * @param patientName  The name of the patient
         * @param mainCode     The LOINC code for the entire report
         * @param mainDisplay  The display name for the report code
         * @param practitioner The interpreter of the results
         * @return FHIR DiagnosticReport resource
         */
        public DiagnosticReport buildFromDto(String patientId, String patientName, String mainCode, String mainDisplay,
                        Practitioner practitioner, Date date) {

                DiagnosticReport report = new DiagnosticReport();

                report.setId(UUID.randomUUID().toString()); // Use UUID for Bundle fullUrl matching

                // ABDM Diagnostic Report Lab Profile
                report.getMeta().addProfile(FhirConstants.PROFILE_DIAGNOSTIC_REPORT_LAB);

                // Narrative (Mandatory Best Practice)
                report.setText(NarrativeUtil.minimalDiagnosticReportNarrative());

                report.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);

                Date actualDate = date != null ? date : new Date();
                report.setIssued(actualDate);
                report.setEffective(new DateTimeType(actualDate));

                // Subject (Reference to Patient)
                report.setSubject(
                                new Reference("urn:uuid:" + patientId)
                                                .setDisplay(patientName)
                                                .setType("Patient"));

                // Results Interpreter (Reference to Practitioner) - Required by ABDM
                report.addResultsInterpreter(
                                new Reference("urn:uuid:" + practitioner.getId())
                                                .setType("Practitioner"));

                // Conclusion - Required by ABDM
                report.setConclusion("Laboratory Results");

                CodeableConcept code = new CodeableConcept();
                code.addCoding()
                                .setSystem(FhirConstants.SYSTEM_LOINC)
                                .setCode(mainCode != null ? mainCode : "11502-2")
                                .setDisplay(mainDisplay != null ? mainDisplay : "Laboratory report");
                report.setCode(code);

                return report;
        }

        /**
         * Links an Observation to the DiagnosticReport.
         * Uses urn:uuid scheme for internal references.
         */
        public void linkResult(DiagnosticReport report, Observation obs) {
                report.addResult(
                                new Reference("urn:uuid:" + obs.getId()));
        }
}
