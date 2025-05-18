INSERT INTO patients (id, first_name, last_name, dob, gender, email, phone_number, created_at)
VALUES
  ('11111111-1111-1111-1111-111111111111', 'John', 'Doe', '1990-01-01', 'MALE', 'john.doe@example.com', '1234567890', NOW()),
  ('22222222-2222-2222-2222-222222222222', 'Jane', 'Smith', '1985-06-15', 'FEMALE', 'jane.smith@example.com', '0987654321', NOW());

INSERT INTO vital_signs (id, patient_id, timestamp, type, value, unit)
VALUES
  ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', '11111111-1111-1111-1111-111111111111', NOW(), 'HEART_RATE', 85, 'bpm'),
  ('aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2', '11111111-1111-1111-1111-111111111111', NOW(), 'TEMPERATURE', 37.2, '°C'),
  ('aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaa3', '22222222-2222-2222-2222-222222222222', NOW(), 'BLOOD_PRESSURE', 120, 'mmHg');


INSERT INTO alerts (id, patient_id, vital_sign_id, timestamp, message, priority, route_to, acknowledged)
VALUES
  ('bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbb1', '11111111-1111-1111-1111-111111111111', 'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', NOW(),
   'Heart rate above normal threshold', 'HIGH', 'Doctor', FALSE),

  ('bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbb2', '22222222-2222-2222-2222-222222222222', 'aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaa3', NOW(),
   'Blood pressure within normal range', 'LOW', 'Nurse Station', TRUE);
