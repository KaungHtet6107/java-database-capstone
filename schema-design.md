# Smart Clinic Management System - Schema Design

## MySQL Database Design

MySQL is used for structured data that requires relationships, consistency, and validation. The main operational data such as patients, doctors, appointments, and administrators are stored in MySQL.

---

### Table: patients

Stores information about registered patients.

- id: INT, Primary Key, Auto Increment
- first_name: VARCHAR(50), Not Null
- last_name: VARCHAR(50), Not Null
- email: VARCHAR(100), Unique, Not Null
- password: VARCHAR(255), Not Null
- phone_number: VARCHAR(20)
- date_of_birth: DATE
- created_at: DATETIME, Not Null

Notes:
- Patient information is retained for medical history.
- Deleting a patient should not automatically delete appointment history.

---

### Table: doctors

Stores information about doctors.

- id: INT, Primary Key, Auto Increment
- first_name: VARCHAR(50), Not Null
- last_name: VARCHAR(50), Not Null
- email: VARCHAR(100), Unique, Not Null
- password: VARCHAR(255), Not Null
- specialization: VARCHAR(100), Not Null
- phone_number: VARCHAR(20)
- availability_status: BOOLEAN
- created_at: DATETIME, Not Null

Notes:
- Each doctor can manage their own availability.
- Doctor email must be unique.

---

### Table: appointments

Stores patient appointments with doctors.

- id: INT, Primary Key, Auto Increment
- patient_id: INT, Foreign Key → patients(id)
- doctor_id: INT, Foreign Key → doctors(id)
- appointment_time: DATETIME, Not Null
- duration: INT, Default 60 minutes
- status: INT
    - 0 = Scheduled
    - 1 = Completed
    - 2 = Cancelled
- created_at: DATETIME

Relationships:
- One patient can have many appointments.
- One doctor can have many appointments.

Notes:
- The system should prevent doctors from having overlapping appointments.
- Appointment history should be stored permanently.

---

### Table: admin

Stores administrator accounts.

- id: INT, Primary Key, Auto Increment
- username: VARCHAR(50), Unique, Not Null
- email: VARCHAR(100), Unique, Not Null
- password: VARCHAR(255), Not Null
- created_at: DATETIME

Notes:
- Admin users manage doctors and system operations.

---

### Table: doctor_availability

Stores doctor working schedules.

- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- available_date: DATE, Not Null
- start_time: TIME, Not Null
- end_time: TIME, Not Null

Notes:
- Patients can only book available time slots.
- Prevents appointment conflicts.

---

### Table: payments

Stores payment information for appointments.

- id: INT, Primary Key, Auto Increment
- appointment_id: INT, Foreign Key → appointments(id)
- amount: DECIMAL(10,2)
- payment_status: VARCHAR(20)
- payment_date: DATETIME

Notes:
- Payment records are linked to appointments.

---

## MongoDB Collection Design

MongoDB is used for flexible data that may change frequently, such as prescriptions, doctor notes, feedback, and additional medical information.

---

### Collection: prescriptions

Example document:

```json
{
  "_id": "ObjectId('64abc123456')",
  "appointmentId": 101,
  "patientId": 25,
  "doctorId": 10,
  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500mg",
      "frequency": "Twice daily"
    },
    {
      "name": "Vitamin C",
      "dosage": "1000mg",
      "frequency": "Once daily"
    }
  ],
  "doctorNotes": "Patient should take medication after meals.",
  "diagnosis": "Common cold",
  "attachments": [
    {
      "fileName": "blood_test.pdf",
      "fileType": "PDF"
    }
  ],
  "metadata": {
    "createdBy": "Dr. Smith",
    "createdDate": "2026-08-01",
    "tags": [
      "follow-up",
      "medicine"
    ]
  }
}
```

Notes:
- MongoDB stores prescription details because medical information can have different structures.
- Patient and doctor IDs are stored instead of full objects to avoid duplicate data.
- New fields can be added easily when requirements change.