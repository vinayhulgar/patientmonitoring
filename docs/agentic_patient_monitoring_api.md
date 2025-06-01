# Agentic Patient Monitoring System - API Specification

## Table of Contents
- [Overview](#overview)
- [Authentication](#authentication)
- [Data Models](#data-models)
- [API Endpoints](#api-endpoints)
- [Error Handling](#error-handling)
- [Rate Limiting](#rate-limiting)
- [Webhooks](#webhooks)
- [Pagination](#pagination)

## Overview

**Version:** 1.0  
**Base URL:** `https://api.patientmonitoring.healthcare/v1`  
**Protocol:** HTTPS  
**Data Format:** JSON  
**Authentication:** Bearer Token (JWT)

This API provides endpoints for managing patient monitoring, alerts, vital signs data, and analytics in an agentic healthcare monitoring system.

## Authentication

All API requests require authentication using JWT Bearer tokens.

```http
Authorization: Bearer <jwt_token>
```

### Token Endpoints

#### Obtain Token
```http
POST /auth/token
Content-Type: application/json

{
  "username": "string",
  "password": "string",
  "facility_id": "string"
}
```

**Response:**
```json
{
  "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
  "refresh_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
  "token_type": "bearer",
  "expires_in": 3600,
  "scope": "read write admin"
}
```

#### Refresh Token
```http
POST /auth/refresh
Content-Type: application/json

{
  "refresh_token": "string"
}
```

## Data Models

### Patient
```json
{
  "patient_id": "uuid",
  "medical_record_number": "string",
  "first_name": "string",
  "last_name": "string",
  "date_of_birth": "date",
  "gender": "string",
  "admission_date": "datetime",
  "discharge_date": "datetime|null",
  "room_number": "string",
  "bed_number": "string",
  "attending_physician_id": "uuid",
  "primary_nurse_id": "uuid",
  "monitoring_status": "active|inactive|paused",
  "baseline_vitals": {
    "heart_rate": {"min": "number", "max": "number", "avg": "number"},
    "blood_pressure": {"systolic": {"min": "number", "max": "number"}, "diastolic": {"min": "number", "max": "number"}},
    "temperature": {"min": "number", "max": "number", "avg": "number"},
    "respiratory_rate": {"min": "number", "max": "number", "avg": "number"},
    "oxygen_saturation": {"min": "number", "max": "number", "avg": "number"}
  },
  "medical_conditions": ["string"],
  "medications": ["string"],
  "allergies": ["string"],
  "created_at": "datetime",
  "updated_at": "datetime"
}
```

### VitalSigns
```json
{
  "vital_signs_id": "uuid",
  "patient_id": "uuid",
  "timestamp": "datetime",
  "heart_rate": "number|null",
  "blood_pressure_systolic": "number|null",
  "blood_pressure_diastolic": "number|null",
  "temperature": "number|null",
  "respiratory_rate": "number|null",
  "oxygen_saturation": "number|null",
  "pain_level": "number|null",
  "consciousness_level": "string|null",
  "device_id": "string",
  "source": "manual|automatic|imported",
  "quality_score": "number",
  "created_at": "datetime"
}
```

### Alert
```json
{
  "alert_id": "uuid",
  "patient_id": "uuid",
  "alert_type": "vital_signs_anomaly|deterioration_pattern|medication_due|custom",
  "severity": "low|medium|high|critical",
  "title": "string",
  "description": "string",
  "triggered_by": "string",
  "confidence_score": "number",
  "status": "new|acknowledged|in_progress|resolved|dismissed",
  "assigned_to": "uuid|null",
  "acknowledged_at": "datetime|null",
  "acknowledged_by": "uuid|null",
  "resolved_at": "datetime|null",
  "resolved_by": "uuid|null",
  "resolution_notes": "string|null",
  "escalation_level": "number",
  "auto_escalate_at": "datetime|null",
  "related_vitals": ["uuid"],
  "recommended_actions": ["string"],
  "created_at": "datetime",
  "updated_at": "datetime"
}
```

### User
```json
{
  "user_id": "uuid",
  "username": "string",
  "email": "string",
  "first_name": "string",
  "last_name": "string",
  "role": "doctor|nurse|admin|technician",
  "department": "string",
  "facility_id": "uuid",
  "phone_number": "string",
  "notification_preferences": {
    "email": "boolean",
    "sms": "boolean",
    "push": "boolean",
    "severity_threshold": "low|medium|high|critical"
  },
  "is_active": "boolean",
  "last_login": "datetime",
  "created_at": "datetime",
  "updated_at": "datetime"
}
```

### MonitoringRule
```json
{
  "rule_id": "uuid",
  "name": "string",
  "description": "string",
  "patient_id": "uuid|null",
  "condition_type": "threshold|pattern|ml_model",
  "parameters": "object",
  "alert_severity": "low|medium|high|critical",
  "is_active": "boolean",
  "created_by": "uuid",
  "created_at": "datetime",
  "updated_at": "datetime"
}
```

## API Endpoints

### Patient Management

#### Get All Patients
```http
GET /patients?page=1&limit=50&status=active&unit=ICU
```

**Parameters:**
- `page` (optional): Page number (default: 1)
- `limit` (optional): Items per page (default: 50, max: 100)
- `status` (optional): Filter by monitoring status
- `unit` (optional): Filter by hospital unit
- `assigned_to` (optional): Filter by assigned staff member

**Response:**
```json
{
  "patients": [Patient],
  "pagination": {
    "current_page": 1,
    "total_pages": 10,
    "total_items": 500,
    "items_per_page": 50
  }
}
```

#### Get Patient by ID
```http
GET /patients/{patient_id}
```

**Response:** `Patient` object

#### Create Patient
```http
POST /patients
Content-Type: application/json

{
  "medical_record_number": "string",
  "first_name": "string",
  "last_name": "string",
  "date_of_birth": "date",
  "gender": "string",
  "room_number": "string",
  "bed_number": "string",
  "attending_physician_id": "uuid",
  "primary_nurse_id": "uuid"
}
```

**Response:** `Patient` object

#### Update Patient
```http
PUT /patients/{patient_id}
Content-Type: application/json

{
  "room_number": "string",
  "monitoring_status": "active|inactive|paused",
  "primary_nurse_id": "uuid"
}
```

**Response:** `Patient` object

#### Delete Patient
```http
DELETE /patients/{patient_id}
```

**Response:** `204 No Content`

### Vital Signs Management

#### Get Patient Vital Signs
```http
GET /patients/{patient_id}/vitals?start_time=2023-01-01T00:00:00Z&end_time=2023-01-02T00:00:00Z&limit=100
```

**Parameters:**
- `start_time` (optional): Start of time range
- `end_time` (optional): End of time range
- `limit` (optional): Maximum number of records (default: 100, max: 1000)
- `vital_type` (optional): Filter by specific vital sign type

**Response:**
```json
{
  "vital_signs": [VitalSigns],
  "patient_id": "uuid",
  "time_range": {
    "start": "datetime",
    "end": "datetime"
  }
}
```

#### Create Vital Signs Entry
```http
POST /patients/{patient_id}/vitals
Content-Type: application/json

{
  "timestamp": "datetime",
  "heart_rate": 72,
  "blood_pressure_systolic": 120,
  "blood_pressure_diastolic": 80,
  "temperature": 98.6,
  "respiratory_rate": 16,
  "oxygen_saturation": 98,
  "device_id": "monitor_001",
  "source": "automatic"
}
```

**Response:** `VitalSigns` object

#### Bulk Create Vital Signs
```http
POST /patients/{patient_id}/vitals/bulk
Content-Type: application/json

{
  "vital_signs": [VitalSigns]
}
```

**Response:**
```json
{
  "created": "number",
  "failed": "number",
  "errors": ["string"]
}
```

### Alert Management

#### Get All Alerts
```http
GET /alerts?status=new&severity=high&assigned_to=me&page=1&limit=50
```

**Parameters:**
- `status` (optional): Filter by alert status
- `severity` (optional): Filter by severity level
- `assigned_to` (optional): Filter by assigned user ('me' for current user)
- `patient_id` (optional): Filter by specific patient
- `start_date` (optional): Filter alerts from date
- `end_date` (optional): Filter alerts to date

**Response:**
```json
{
  "alerts": [Alert],
  "pagination": {
    "current_page": 1,
    "total_pages": 5,
    "total_items": 250,
    "items_per_page": 50
  }
}
```

#### Get Alert by ID
```http
GET /alerts/{alert_id}
```

**Response:** `Alert` object

#### Create Alert
```http
POST /alerts
Content-Type: application/json

{
  "patient_id": "uuid",
  "alert_type": "vital_signs_anomaly",
  "severity": "high",
  "title": "Elevated Heart Rate",
  "description": "Patient heart rate consistently above baseline",
  "triggered_by": "monitoring_agent_hr_001",
  "confidence_score": 0.85,
  "recommended_actions": ["Check patient", "Review medications"]
}
```

**Response:** `Alert` object

#### Update Alert Status
```http
PATCH /alerts/{alert_id}/status
Content-Type: application/json

{
  "status": "acknowledged|in_progress|resolved|dismissed",
  "notes": "string",
  "assigned_to": "uuid"
}
```

**Response:** `Alert` object

#### Acknowledge Alert
```http
POST /alerts/{alert_id}/acknowledge
Content-Type: application/json

{
  "notes": "string"
}
```

**Response:** `Alert` object

#### Resolve Alert
```http
POST /alerts/{alert_id}/resolve
Content-Type: application/json

{
  "resolution_notes": "string",
  "actions_taken": ["string"]
}
```

**Response:** `Alert` object

### Monitoring Rules

#### Get Monitoring Rules
```http
GET /monitoring-rules?patient_id={uuid}&is_active=true
```

**Response:**
```json
{
  "rules": [MonitoringRule]
}
```

#### Create Monitoring Rule
```http
POST /monitoring-rules
Content-Type: application/json

{
  "name": "High Heart Rate Alert",
  "description": "Alert when heart rate exceeds patient baseline by 20%",
  "patient_id": "uuid",
  "condition_type": "threshold",
  "parameters": {
    "vital_sign": "heart_rate",
    "operator": "greater_than",
    "threshold_type": "percentage_above_baseline",
    "value": 20,
    "duration_minutes": 5
  },
  "alert_severity": "medium"
}
```

**Response:** `MonitoringRule` object

#### Update Monitoring Rule
```http
PUT /monitoring-rules/{rule_id}
Content-Type: application/json

{
  "name": "Updated Rule Name",
  "is_active": false
}
```

**Response:** `MonitoringRule` object

### Analytics and Reporting

#### Get System Analytics
```http
GET /analytics/dashboard?start_date=2023-01-01&end_date=2023-01-31
```

**Response:**
```json
{
  "summary": {
    "total_patients": 150,
    "active_alerts": 23,
    "alerts_resolved_today": 45,
    "average_response_time_minutes": 12.5
  },
  "alert_metrics": {
    "by_severity": {
      "critical": 5,
      "high": 18,
      "medium": 42,
      "low": 15
    },
    "by_type": {
      "vital_signs_anomaly": 35,
      "deterioration_pattern": 20,
      "medication_due": 25
    }
  },
  "patient_outcomes": {
    "early_interventions": 28,
    "prevented_deteriorations": 12,
    "average_length_of_stay_days": 4.2
  }
}
```

#### Get Patient-Specific Analytics
```http
GET /analytics/patients/{patient_id}?days=7
```

**Response:**
```json
{
  "patient_id": "uuid",
  "monitoring_duration_hours": 168,
  "total_alerts": 12,
  "vital_signs_stability": {
    "heart_rate": "stable",
    "blood_pressure": "improving",
    "temperature": "stable"
  },
  "trend_analysis": {
    "overall_trend": "improving",
    "risk_score": 0.25,
    "predicted_discharge_readiness": "2023-01-15"
  }
}
```

#### Export Report
```http
GET /analytics/reports/export?type=alert_summary&format=pdf&start_date=2023-01-01&end_date=2023-01-31
```

**Response:** File download (PDF/CSV/Excel)

### Real-time Data Streaming

#### WebSocket Connection for Real-time Updates
```
WSS /ws/realtime?token={jwt_token}&patient_id={uuid}
```

**Message Types:**
```json
// Vital signs update
{
  "type": "vital_signs",
  "data": VitalSigns
}

// New alert
{
  "type": "alert_created",
  "data": Alert
}

// Alert status change
{
  "type": "alert_updated",
  "data": Alert
}

// Patient status change
{
  "type": "patient_updated",
  "data": Patient
}
```

## Error Handling

### Standard Error Response Format
```json
{
  "error": {
    "code": "string",
    "message": "string",
    "details": "object|null",
    "timestamp": "datetime",
    "request_id": "uuid"
  }
}
```

### HTTP Status Codes
- `200` - Success
- `201` - Created
- `204` - No Content
- `400` - Bad Request
- `401` - Unauthorized
- `403` - Forbidden
- `404` - Not Found
- `409` - Conflict
- `422` - Unprocessable Entity
- `429` - Too Many Requests
- `500` - Internal Server Error
- `503` - Service Unavailable

### Common Error Codes
- `INVALID_TOKEN` - JWT token is invalid or expired
- `INSUFFICIENT_PERMISSIONS` - User lacks required permissions
- `PATIENT_NOT_FOUND` - Specified patient does not exist
- `ALERT_NOT_FOUND` - Specified alert does not exist
- `VALIDATION_ERROR` - Request data validation failed
- `DUPLICATE_ENTRY` - Attempting to create duplicate resource
- `RATE_LIMIT_EXCEEDED` - Too many requests in time window

## Rate Limiting

API requests are rate limited based on user role and endpoint:

- **Standard Users**: 1000 requests per hour
- **Admin Users**: 5000 requests per hour
- **System Integration**: 10000 requests per hour

Rate limit headers are included in all responses:
```http
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1609459200
```

## Webhooks

Configure webhooks to receive real-time notifications for important events.

### Webhook Configuration
```http
POST /webhooks
Content-Type: application/json

{
  "url": "https://your-system.com/webhook",
  "events": ["alert.created", "alert.critical", "patient.deteriorating"],
  "secret": "your_webhook_secret",
  "is_active": true
}
```

### Webhook Event Types
- `alert.created` - New alert generated
- `alert.critical` - Critical severity alert
- `alert.acknowledged` - Alert acknowledged by staff
- `alert.resolved` - Alert resolved
- `patient.admitted` - New patient admission
- `patient.discharged` - Patient discharge
- `patient.deteriorating` - Patient condition deteriorating
- `vital_signs.anomaly` - Abnormal vital signs detected

### Webhook Payload Example
```json
{
  "event": "alert.created",
  "timestamp": "2023-01-15T10:30:00Z",
  "data": Alert,
  "webhook_id": "uuid"
}
```

## Pagination

All list endpoints support pagination using the following parameters:

- `page` - Page number (1-based, default: 1)
- `limit` - Items per page (default: 50, max: 100)

Pagination information is included in the response:
```json
{
  "pagination": {
    "current_page": 1,
    "total_pages": 10,
    "total_items": 500,
    "items_per_page": 50,
    "has_next": true,
    "has_previous": false
  }
}
```

## SDK and Integration Examples

### Python SDK Example
```python
from agentic_monitoring import PatientMonitoringClient

client = PatientMonitoringClient(
    base_url="https://api.patientmonitoring.healthcare/v1",
    token="your_jwt_token"
)

# Get all active alerts
alerts = client.alerts.list(status="new", severity="high")

# Create vital signs entry
vital_signs = client.vital_signs.create(
    patient_id="uuid",
    heart_rate=85,
    blood_pressure_systolic=130,
    blood_pressure_diastolic=85
)

# Acknowledge alert
client.alerts.acknowledge(alert_id="uuid", notes="Checked patient")
```

### JavaScript SDK Example
```javascript
import { PatientMonitoringAPI } from '@healthcare/agentic-monitoring';

const api = new PatientMonitoringAPI({
  baseURL: 'https://api.patientmonitoring.healthcare/v1',
  token: 'your_jwt_token'
});

// Real-time alert subscription
api.realtime.subscribe('alerts', (alert) => {
  console.log('New alert:', alert);
  // Handle alert in your application
});

// Get patient analytics
const analytics = await api.analytics.getPatient('patient-uuid', { days: 7 });
```

## Versioning

This API uses URL-based versioning. The current version is `v1`. When breaking changes are introduced, a new version will be released (e.g., `v2`).

Version-specific changes will be documented in the changelog, and deprecated versions will be supported for at least 12 months after the release of a new version.

## Support and Documentation

- **API Documentation**: https://docs.patientmonitoring.healthcare
- **SDK Downloads**: https://github.com/healthcare/agentic-monitoring-sdks
- **Support**: api-support@patientmonitoring.healthcare
- **Status Page**: https://status.patientmonitoring.healthcare