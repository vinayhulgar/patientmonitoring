CREATE TABLE patients (
    id UUID PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    gender VARCHAR(10) CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')) NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone_number VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE vital_signs (
    id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    type VARCHAR(50) CHECK (
        type IN ('HEART_RATE', 'BLOOD_PRESSURE', 'TEMPERATURE', 'RESPIRATION_RATE', 'OXYGEN_SATURATION')
    ) NOT NULL,
    value DOUBLE PRECISION NOT NULL,
    unit VARCHAR(10) NOT NULL,
    CONSTRAINT fk_vital_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

CREATE TABLE alerts (
    id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    vital_sign_id UUID NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    message TEXT NOT NULL,
    priority VARCHAR(10) CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')) NOT NULL,
    route_to VARCHAR(100) NOT NULL,
    acknowledged BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_alert_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    CONSTRAINT fk_alert_vital FOREIGN KEY (vital_sign_id) REFERENCES vital_signs(id) ON DELETE CASCADE
);


CREATE INDEX idx_vitals_patient_id ON vital_signs(patient_id);
CREATE INDEX idx_alerts_patient_id ON alerts(patient_id);
CREATE INDEX idx_alerts_vital_sign_id ON alerts(vital_sign_id);
