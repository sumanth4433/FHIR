package com.yourorg.fhir.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FhirConfig {

    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forR4();
    }

    @Bean
    public FhirValidator fhirValidator(FhirContext ctx) {
        // Base FHIR validation only (syntax + core rules)
        return ctx.newValidator();
    }
}
