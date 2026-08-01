# User Stories - Patient Appointment Portal

## Admin User Stories

### User Story 1

**Title:**  
_As an Admin, I want to log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**
1. The admin can log in using valid credentials.
2. Invalid login attempts display an appropriate error message.
3. The admin is redirected to the dashboard after a successful login.

**Priority:** High  
**Story Points:** 3

**Notes:**
- Authentication should be secure.
- Only authorized admins can access the dashboard.

---

### User Story 2

**Title:**  
_As an Admin, I want to log out of the portal, so that I can protect system access._

**Acceptance Criteria:**
1. The admin can log out from any page.
2. The current session is terminated.
3. The admin is redirected to the login page.

**Priority:** High  
**Story Points:** 2

**Notes:**
- Prevent unauthorized access after logout.

---

### User Story 3

**Title:**  
_As an Admin, I want to add doctors to the portal, so that patients can book appointments with them._

**Acceptance Criteria:**
1. The admin can enter doctor information.
2. Required fields are validated.
3. The doctor is successfully saved in the system.

**Priority:** High  
**Story Points:** 5

**Notes:**
- Doctor email should be unique.

---

### User Story 4

**Title:**  
_As an Admin, I want to delete a doctor's profile, so that outdated records are removed._

**Acceptance Criteria:**
1. The admin can select a doctor to delete.
2. A confirmation is displayed before deletion.
3. The doctor record is removed successfully.

**Priority:** Medium  
**Story Points:** 3

**Notes:**
- Prevent deletion if business rules require existing appointments to be handled first.

---

### User Story 5

**Title:**  
_As an Admin, I want to run a stored procedure in MySQL to view the number of appointments per month, so that I can track usage statistics._

**Acceptance Criteria:**
1. The stored procedure executes successfully.
2. Monthly appointment totals are returned.
3. Results can be viewed by the admin.

**Priority:** Medium  
**Story Points:** 5

**Notes:**
- Uses a MySQL stored procedure.

---

## Patient User Stories

### User Story 6

**Title:**  
_As a Patient, I want to view a list of doctors without logging in, so that I can explore available doctors before registering._

**Acceptance Criteria:**
1. Visitors can access the doctor list.
2. Doctor names and specializations are displayed.
3. No login is required.

**Priority:** Medium  
**Story Points:** 3

**Notes:**
- Personal information should not be displayed.

---

### User Story 7

**Title:**  
_As a Patient, I want to sign up using my email and password, so that I can book appointments._

**Acceptance Criteria:**
1. A new account can be created.
2. Email addresses must be unique.
3. The patient can log in after registration.

**Priority:** High  
**Story Points:** 5

**Notes:**
- Passwords should be securely stored.

---

### User Story 8

**Title:**  
_As a Patient, I want to log into the portal, so that I can manage my appointments._

**Acceptance Criteria:**
1. Patients can log in with valid credentials.
2. Invalid credentials show an error message.
3. Successful login redirects to the patient dashboard.

**Priority:** High  
**Story Points:** 3

**Notes:**
- Authentication is required before booking appointments.

---

### User Story 9

**Title:**  
_As a Patient, I want to log out of the portal, so that I can secure my account._

**Acceptance Criteria:**
1. Patients can log out at any time.
2. The session is terminated.
3. The login page is displayed after logout.

**Priority:** High  
**Story Points:** 2

**Notes:**
- Protect patient privacy.

---

### User Story 10

**Title:**  
_As a Patient, I want to book a one-hour appointment with a doctor, so that I can receive medical consultation._

**Acceptance Criteria:**
1. Available time slots are displayed.
2. Patients can select an available one-hour slot.
3. The appointment is successfully saved.

**Priority:** High  
**Story Points:** 8

**Notes:**
- Double booking is not allowed.

---

### User Story 11

**Title:**  
_As a Patient, I want to view my upcoming appointments, so that I can prepare accordingly._

**Acceptance Criteria:**
1. Upcoming appointments are displayed.
2. Appointment date, time, and doctor information are shown.
3. Past appointments are not included.

**Priority:** Medium  
**Story Points:** 3

**Notes:**
- Only future appointments should be displayed.

---

## Doctor User Stories

### User Story 12

**Title:**  
_As a Doctor, I want to log into the portal, so that I can manage my appointments._

**Acceptance Criteria:**
1. Doctors can log in with valid credentials.
2. Invalid login attempts display an error.
3. Successful login redirects to the doctor dashboard.

**Priority:** High  
**Story Points:** 3

**Notes:**
- Only registered doctors can log in.

---

### User Story 13

**Title:**  
_As a Doctor, I want to log out of the portal, so that I can protect my data._

**Acceptance Criteria:**
1. Doctors can log out from any page.
2. The session is terminated.
3. The login page is displayed.

**Priority:** High  
**Story Points:** 2

**Notes:**
- Prevent unauthorized access.

---

### User Story 14

**Title:**  
_As a Doctor, I want to view my appointment calendar, so that I can stay organized._

**Acceptance Criteria:**
1. Upcoming appointments are displayed.
2. Appointment details are visible.
3. The calendar updates automatically after changes.

**Priority:** High  
**Story Points:** 5

**Notes:**
- Display appointments in chronological order.

---

### User Story 15

**Title:**  
_As a Doctor, I want to mark my unavailable dates and times, so that patients can only book available slots._

**Acceptance Criteria:**
1. Doctors can mark unavailable periods.
2. Unavailable slots cannot be booked.
3. Availability updates immediately.

**Priority:** High  
**Story Points:** 5

**Notes:**
- Existing appointments should remain unaffected.

---

### User Story 16

**Title:**  
_As a Doctor, I want to update my profile with my specialization and contact information, so that patients have up-to-date information._

**Acceptance Criteria:**
1. Doctors can edit their profile.
2. Changes are saved successfully.
3. Updated information is visible to patients.

**Priority:** Medium  
**Story Points:** 3

**Notes:**
- Required fields must be validated.

---

### User Story 17

**Title:**  
_As a Doctor, I want to view patient details for upcoming appointments, so that I can be prepared before consultations._

**Acceptance Criteria:**
1. Doctors can access patient information for scheduled appointments.
2. Only authorized doctors can view patient data.
3. Patient details are displayed accurately.

**Priority:** High  
**Story Points:** 5

**Notes:**
- Patient privacy and security must be maintained.