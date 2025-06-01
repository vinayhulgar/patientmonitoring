openapi: 3.0.3
info:
title: Patient Monitoring System API
description: |
RESTful API for a comprehensive patient monitoring system that enables healthcare providers
to track patient vital signs, manage medical records, handle alerts, and monitor patient status in real-time.

    **Key Features:**
    - Real-time vital signs monitoring
    - Patient registration and management
    - Alert system for critical conditions
    - Medical records management
    - Device integration for automated data collection
    - Role-based access control
version: 1.0.0
contact:
name: API Support
email: support@patientmonitor.com
license:
name: MIT
url: https://opensource.org/licenses/MIT

servers:
- url: https://api.patientmonitor.com/v1
  description: Production server
- url: https://staging-api.patientmonitor.com/v1
  description: Staging server
- url: http://localhost:8080/v1
  description: Development server

security:
- BearerAuth: []

tags:
- name: Authentication
  description: User authentication and authorization
- name: Patients
  description: Patient registration and management
- name: Vital Signs
  description: Real-time vital signs monitoring
- name: Alerts
  description: Alert management and notifications
- name: Medical Records
  description: Patient medical history and records
- name: Devices
  description: Medical device management and integration
- name: Users
  description: Healthcare staff management

paths:
# Authentication Endpoints
/auth/login:
post:
tags:
- Authentication
summary: User login
description: Authenticate user and return access token
requestBody:
required: true
content:
application/json:
schema:
type: object
required:
- email
- password
properties:
email:
type: string
format: email
example: doctor@hospital.com
password:
type: string
format: password
example: securePassword123
responses:
'200':
description: Successful authentication
content:
application/json:
schema:
type: object
properties:
access_token:
type: string
refresh_token:
type: string
expires_in:
type: integer
user:
$ref: '#/components/schemas/User'
'401':
description: Invalid credentials
content:
application/json:
schema:
$ref: '#/components/schemas/Error'

/auth/refresh:
post:
tags:
- Authentication
summary: Refresh access token
requestBody:
required: true
content:
application/json:
schema:
type: object
required:
- refresh_token
properties:
refresh_token:
type: string
responses:
'200':
description: New access token generated
content:
application/json:
schema:
type: object
properties:
access_token:
type: string
expires_in:
type: integer

# Patient Endpoints
/patients:
get:
tags:
- Patients
summary: Get all patients
description: Retrieve a paginated list of all patients
parameters:
- name: page
in: query
schema:
type: integer
default: 1
- name: limit
in: query
schema:
type: integer
default: 20
maximum: 100
- name: status
in: query
schema:
type: string
enum: [active, inactive, discharged, critical]
- name: search
in: query
schema:
type: string
description: Search by patient name or ID
responses:
'200':
description: List of patients
content:
application/json:
schema:
type: object
properties:
data:
type: array
items:
$ref: '#/components/schemas/Patient'
pagination:
$ref: '#/components/schemas/Pagination'

    post:
      tags:
        - Patients
      summary: Register new patient
      description: Create a new patient record
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/PatientCreateRequest'
      responses:
        '201':
          description: Patient created successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Patient'
        '400':
          description: Invalid patient data
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Error'

/patients/{patientId}:
get:
tags:
- Patients
summary: Get patient by ID
parameters:
- name: patientId
in: path
required: true
schema:
type: string
example: "12345"
responses:
'200':
description: Patient details
content:
application/json:
schema:
$ref: '#/components/schemas/Patient'
'404':
description: Patient not found

    put:
      tags:
        - Patients
      summary: Update patient information
      parameters:
        - name: patientId
          in: path
          required: true
          schema:
            type: string
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/PatientUpdateRequest'
      responses:
        '200':
          description: Patient updated successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Patient'

    delete:
      tags:
        - Patients
      summary: Deactivate patient
      parameters:
        - name: patientId
          in: path
          required: true
          schema:
            type: string
      responses:
        '204':
          description: Patient deactivated successfully

# Vital Signs Endpoints
/patients/{patientId}/vitals:
get:
tags:
- Vital Signs
summary: Get patient vital signs
description: Retrieve vital signs history for a specific patient
parameters:
- name: patientId
in: path
required: true
schema:
type: string
- name: start_date
in: query
schema:
type: string
format: date-time
- name: end_date
in: query
schema:
type: string
format: date-time
- name: type
in: query
schema:
type: string
enum: [heart_rate, blood_pressure, temperature, oxygen_saturation, respiratory_rate]
responses:
'200':
description: Vital signs data
content:
application/json:
schema:
type: object
properties:
data:
type: array
items:
$ref: '#/components/schemas/VitalSigns'
patient_id:
type: string
period:
type: object
properties:
start:
type: string
format: date-time
end:
type: string
format: date-time

    post:
      tags:
        - Vital Signs
      summary: Record vital signs
      description: Add new vital signs measurement for a patient
      parameters:
        - name: patientId
          in: path
          required: true
          schema:
            type: string
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/VitalSignsRequest'
      responses:
        '201':
          description: Vital signs recorded successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/VitalSigns'

/patients/{patientId}/vitals/latest:
get:
tags:
- Vital Signs
summary: Get latest vital signs
description: Get the most recent vital signs for a patient
parameters:
- name: patientId
in: path
required: true
schema:
type: string
responses:
'200':
description: Latest vital signs
content:
application/json:
schema:
$ref: '#/components/schemas/VitalSigns'

# Alerts Endpoints
/alerts:
get:
tags:
- Alerts
summary: Get all alerts
description: Retrieve system alerts with filtering options
parameters:
- name: status
in: query
schema:
type: string
enum: [active, acknowledged, resolved]
- name: priority
in: query
schema:
type: string
enum: [low, medium, high, critical]
- name: patient_id
in: query
schema:
type: string
- name: page
in: query
schema:
type: integer
default: 1
- name: limit
in: query
schema:
type: integer
default: 20
responses:
'200':
description: List of alerts
content:
application/json:
schema:
type: object
properties:
data:
type: array
items:
$ref: '#/components/schemas/Alert'
pagination:
$ref: '#/components/schemas/Pagination'

    post:
      tags:
        - Alerts
      summary: Create new alert
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/AlertCreateRequest'
      responses:
        '201':
          description: Alert created successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Alert'

/alerts/{alertId}:
get:
tags:
- Alerts
summary: Get alert by ID
parameters:
- name: alertId
in: path
required: true
schema:
type: string
responses:
'200':
description: Alert details
content:
application/json:
schema:
$ref: '#/components/schemas/Alert'

    patch:
      tags:
        - Alerts
      summary: Update alert status
      parameters:
        - name: alertId
          in: path
          required: true
          schema:
            type: string
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              properties:
                status:
                  type: string
                  enum: [acknowledged, resolved]
                notes:
                  type: string
      responses:
        '200':
          description: Alert updated successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Alert'

# Medical Records Endpoints
/patients/{patientId}/records:
get:
tags:
- Medical Records
summary: Get patient medical records
parameters:
- name: patientId
in: path
required: true
schema:
type: string
- name: type
in: query
schema:
type: string
enum: [diagnosis, medication, procedure, lab_result, note]
- name: start_date
in: query
schema:
type: string
format: date
- name: end_date
in: query
schema:
type: string
format: date
responses:
'200':
description: Medical records
content:
application/json:
schema:
type: array
items:
$ref: '#/components/schemas/MedicalRecord'

    post:
      tags:
        - Medical Records
      summary: Add medical record
      parameters:
        - name: patientId
          in: path
          required: true
          schema:
            type: string
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/MedicalRecordRequest'
      responses:
        '201':
          description: Medical record added successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/MedicalRecord'

# Device Management Endpoints
/devices:
get:
tags:
- Devices
summary: Get all monitoring devices
parameters:
- name: status
in: query
schema:
type: string
enum: [online, offline, maintenance]
- name: type
in: query
schema:
type: string
enum: [heart_monitor, blood_pressure, thermometer, pulse_oximeter]
responses:
'200':
description: List of devices
content:
application/json:
schema:
type: array
items:
$ref: '#/components/schemas/Device'

    post:
      tags:
        - Devices
      summary: Register new device
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/DeviceRequest'
      responses:
        '201':
          description: Device registered successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Device'

/devices/{deviceId}/assign:
post:
tags:
- Devices
summary: Assign device to patient
parameters:
- name: deviceId
in: path
required: true
schema:
type: string
requestBody:
required: true
content:
application/json:
schema:
type: object
required:
- patient_id
properties:
patient_id:
type: string
responses:
'200':
description: Device assigned successfully

components:
securitySchemes:
BearerAuth:
type: http
scheme: bearer
bearerFormat: JWT

schemas:
# User Schema
User:
type: object
properties:
id:
type: string
example: "user123"
email:
type: string
format: email
example: "doctor@hospital.com"
first_name:
type: string
example: "John"
last_name:
type: string
example: "Doe"
role:
type: string
enum: [doctor, nurse, admin, technician]
example: "doctor"
department:
type: string
example: "Cardiology"
created_at:
type: string
format: date-time
last_login:
type: string
format: date-time

    # Patient Schemas
    Patient:
      type: object
      properties:
        id:
          type: string
          example: "12345"
        first_name:
          type: string
          example: "Jane"
        last_name:
          type: string
          example: "Smith"
        date_of_birth:
          type: string
          format: date
          example: "1990-01-15"
        gender:
          type: string
          enum: [male, female, other]
          example: "female"
        phone:
          type: string
          example: "+1-555-0123"
        email:
          type: string
          format: email
          example: "jane.smith@email.com"
        emergency_contact:
          $ref: '#/components/schemas/EmergencyContact'
        medical_record_number:
          type: string
          example: "MRN001234"
        admission_date:
          type: string
          format: date-time
        room_number:
          type: string
          example: "A-101"
        status:
          type: string
          enum: [active, inactive, discharged, critical]
          example: "active"
        assigned_doctor:
          type: string
          example: "Dr. Johnson"
        created_at:
          type: string
          format: date-time
        updated_at:
          type: string
          format: date-time

    PatientCreateRequest:
      type: object
      required:
        - first_name
        - last_name
        - date_of_birth
        - gender
      properties:
        first_name:
          type: string
        last_name:
          type: string
        date_of_birth:
          type: string
          format: date
        gender:
          type: string
          enum: [male, female, other]
        phone:
          type: string
        email:
          type: string
          format: email
        emergency_contact:
          $ref: '#/components/schemas/EmergencyContact'
        room_number:
          type: string
        assigned_doctor:
          type: string

    PatientUpdateRequest:
      type: object
      properties:
        first_name:
          type: string
        last_name:
          type: string
        phone:
          type: string
        email:
          type: string
          format: email
        emergency_contact:
          $ref: '#/components/schemas/EmergencyContact'
        room_number:
          type: string
        assigned_doctor:
          type: string
        status:
          type: string
          enum: [active, inactive, discharged, critical]

    EmergencyContact:
      type: object
      properties:
        name:
          type: string
          example: "John Smith"
        relationship:
          type: string
          example: "Spouse"
        phone:
          type: string
          example: "+1-555-0124"

    # Vital Signs Schemas
    VitalSigns:
      type: object
      properties:
        id:
          type: string
        patient_id:
          type: string
        timestamp:
          type: string
          format: date-time
        heart_rate:
          type: integer
          example: 72
          description: "Beats per minute"
        blood_pressure:
          $ref: '#/components/schemas/BloodPressure'
        temperature:
          type: number
          format: float
          example: 98.6
          description: "Temperature in Fahrenheit"
        oxygen_saturation:
          type: integer
          example: 98
          description: "Percentage"
        respiratory_rate:
          type: integer
          example: 16
          description: "Breaths per minute"
        device_id:
          type: string
        recorded_by:
          type: string
        notes:
          type: string

    BloodPressure:
      type: object
      properties:
        systolic:
          type: integer
          example: 120
        diastolic:
          type: integer
          example: 80

    VitalSignsRequest:
      type: object
      required:
        - timestamp
      properties:
        timestamp:
          type: string
          format: date-time
        heart_rate:
          type: integer
        blood_pressure:
          $ref: '#/components/schemas/BloodPressure'
        temperature:
          type: number
          format: float
        oxygen_saturation:
          type: integer
        respiratory_rate:
          type: integer
        device_id:
          type: string
        notes:
          type: string

    # Alert Schemas
    Alert:
      type: object
      properties:
        id:
          type: string
        patient_id:
          type: string
        type:
          type: string
          enum: [vital_sign_abnormal, device_malfunction, medication_due, emergency]
        priority:
          type: string
          enum: [low, medium, high, critical]
        status:
          type: string
          enum: [active, acknowledged, resolved]
        title:
          type: string
          example: "High Heart Rate Alert"
        message:
          type: string
          example: "Patient heart rate exceeds normal range (>100 bpm)"
        value:
          type: string
          example: "120 bpm"
        threshold:
          type: string
          example: "100 bpm"
        created_at:
          type: string
          format: date-time
        acknowledged_at:
          type: string
          format: date-time
        acknowledged_by:
          type: string
        resolved_at:
          type: string
          format: date-time
        resolved_by:
          type: string
        notes:
          type: string

    AlertCreateRequest:
      type: object
      required:
        - patient_id
        - type
        - priority
        - title
        - message
      properties:
        patient_id:
          type: string
        type:
          type: string
          enum: [vital_sign_abnormal, device_malfunction, medication_due, emergency]
        priority:
          type: string
          enum: [low, medium, high, critical]
        title:
          type: string
        message:
          type: string
        value:
          type: string
        threshold:
          type: string

    # Medical Record Schemas
    MedicalRecord:
      type: object
      properties:
        id:
          type: string
        patient_id:
          type: string
        type:
          type: string
          enum: [diagnosis, medication, procedure, lab_result, note]
        title:
          type: string
          example: "Hypertension Diagnosis"
        description:
          type: string
        date:
          type: string
          format: date-time
        provider:
          type: string
          example: "Dr. Johnson"
        department:
          type: string
          example: "Cardiology"
        attachments:
          type: array
          items:
            type: string
        created_at:
          type: string
          format: date-time

    MedicalRecordRequest:
      type: object
      required:
        - type
        - title
        - description
        - date
      properties:
        type:
          type: string
          enum: [diagnosis, medication, procedure, lab_result, note]
        title:
          type: string
        description:
          type: string
        date:
          type: string
          format: date-time
        department:
          type: string

    # Device Schemas
    Device:
      type: object
      properties:
        id:
          type: string
        name:
          type: string
          example: "Heart Monitor #001"
        type:
          type: string
          enum: [heart_monitor, blood_pressure, thermometer, pulse_oximeter, multi_parameter]
        manufacturer:
          type: string
          example: "MedTech Corp"
        model:
          type: string
          example: "MT-2000"
        serial_number:
          type: string
          example: "SN123456789"
        status:
          type: string
          enum: [online, offline, maintenance, error]
        location:
          type: string
          example: "Room A-101"
        assigned_patient_id:
          type: string
        last_maintenance:
          type: string
          format: date
        next_maintenance:
          type: string
          format: date
        firmware_version:
          type: string
          example: "1.2.3"
        created_at:
          type: string
          format: date-time

    DeviceRequest:
      type: object
      required:
        - name
        - type
        - manufacturer
        - model
        - serial_number
      properties:
        name:
          type: string
        type:
          type: string
          enum: [heart_monitor, blood_pressure, thermometer, pulse_oximeter, multi_parameter]
        manufacturer:
          type: string
        model:
          type: string
        serial_number:
          type: string
        location:
          type: string

    # Utility Schemas
    Pagination:
      type: object
      properties:
        page:
          type: integer
          example: 1
        limit:
          type: integer
          example: 20
        total:
          type: integer
          example: 150
        total_pages:
          type: integer
          example: 8

    Error:
      type: object
      properties:
        error:
          type: string
          example: "Invalid request"
        message:
          type: string
          example: "The request body contains invalid data"
        code:
          type: integer
          example: 400