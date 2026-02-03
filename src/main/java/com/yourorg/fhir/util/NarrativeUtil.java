package com.yourorg.fhir.util;

import org.hl7.fhir.r4.model.Narrative;

public class NarrativeUtil {

    private NarrativeUtil() {
    }

    public static Narrative minimalDiagnosticReportNarrative() {
        Narrative narrative = new Narrative();
        narrative.setStatus(Narrative.NarrativeStatus.GENERATED);
        narrative.setDivAsString(
            "<div xmlns=\"http://www.w3.org/1999/xhtml\">Diagnostic Report</div>"
        );
        return narrative;
    }
}
