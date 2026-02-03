# ABDM FHIR Service

A production-ready Spring Boot service for generating ABDM-compliant FHIR R4 Document Bundles. This service supports multiple entries for all major document types including Prescriptions, Lab Reports, Discharge Summaries, and more.

## Features

- **ABDM Compliant**: Generates FHIR Bundles following the NRCES (NDHM) profiles.
- **Multi-Entry Support**: Handle lists of medications, diagnoses, test results, or attachments in a single API call.
- **Production Standard**: Centralized constants, zero hardcoding, and comprehensive Javadoc.
- **Dockerized**: Ready to deploy with Docker and Docker Compose.

## Supported Document Types

| Document Type | Endpoint | Multi-Entry Field |
| :--- | :--- | :--- |
| **Lab Report** | `/fhir/diagnostic-report/lab` | `test_list` |
| **Prescription** | `/fhir/prescription` | `medication_list` |
| **Wellness Record** | `/fhir/wellness` | `observation_list` |
| **Discharge Summary** | `/fhir/discharge-summary` | `diagnosis_list` |
| **Op Consult** | `/fhir/op-consult` | `diagnosis_list` |
| **Immunization Record** | `/fhir/immunization` | `immunization_list` |
| **Health Document** | `/fhir/health-document` | `attachment_list` |

## Getting Started

### Prerequisites

- Docker and Docker Compose
- Java 17 (for local development)
- Maven (for local development)

### Running with Docker

```bash
docker compose up --build
```

The service will be available at `http://localhost:8080`.

## API Usage Examples

Complete sample JSON payloads for every API are located in:
`src/main/resources/samples/`

### Example: Multi-Entry Prescription
```bash
curl -X POST http://localhost:8080/fhir/prescription \
-H "Content-Type: application/json" \
-d @src/main/resources/samples/prescription.json
```

## Project Structure

- `src/main/java/com/yourorg/fhir/builder/`: FHIR resource and bundle construction logic.
- `src/main/java/com/yourorg/fhir/service/`: Orchestration and serialization services.
- `src/main/java/com/yourorg/fhir/dto/`: Input Data Transfer Objects.
- `src/main/java/com/yourorg/fhir/util/`: Centralized constants and narratives.
- `src/main/resources/samples/`: Example payloads for all APIs.

## Maintenance

To update profiles or system URLs, modify `com.yourorg.fhir.util.FhirConstants.java`.

---
*Created for ABDM FHIR Integration Workflows.*
