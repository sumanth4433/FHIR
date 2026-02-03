package com.yourorg.fhir.validator;

import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;

import org.hl7.fhir.r4.model.Bundle;
import org.springframework.stereotype.Service;

@Service
public class FhirValidatorService {

    private final FhirValidator validator;

    public FhirValidatorService(FhirValidator validator) {
        this.validator = validator;
    }

    public void validate(Bundle bundle) {
        ValidationResult result = validator.validateWithResult(bundle);
        if (!result.isSuccessful()) {
            throw new RuntimeException(result.getMessages().toString());
        }
    }
}
