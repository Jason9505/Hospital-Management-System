# Hospital Management System

A Java Swing desktop application for managing hospital operations — patients, doctors, appointments, and user access with role-based permissions.

## Architecture

### User Hierarchy (Login System)

```
User (abstract)
├── Admin         → Full Access
├── DoctorUser    → View own appointments, view patient records
└── Receptionist  → Manage patients & appointments
```

- Login credentials are stored in `users.txt` (pipe-delimited: `username|password|role|doctorID`)
- If `users.txt` doesn't exist, it's auto-generated with default accounts on first launch

### Domain Models

| Model | Fields | Persisted In |
|---|---|---|
| `Patient` | ID, name, age, gender, medical history | `patients.txt` |
| `Doctor` | ID, name, specialization | `doctors.txt` |
| `Appointment` | ID, Patient, Doctor, date, time | `appointments.txt` |

### Manager Layer (Data Access)

Each domain model has a corresponding manager class that handles CRUD operations and file persistence:
- `PatientManager` — manages `patients.txt`
- `DoctorManager` — manages `doctors.txt`
- `AppointmentManager` — manages `appointments.txt`, depends on PatientManager + DoctorManager

All managers load from file on construction and save on every mutation. A single shared instance of each manager is created in `MainMenu` and passed to all sub-windows, ensuring data consistency.

### GUI Layer

| Window | Purpose | File |
|---|---|---|
| `LoginGUI` | Username/password authentication | `LoginGUI.java` |
| `MainMenu` | Role-based navigation dashboard | `MainMenu.java` |
| `PatientGUI` | CRUD for patient records | `PatientGUI.java` |
| `DoctorGUI` | CRUD for doctors + view schedule | `DoctorGUI.java` |
| `AppointmentGUI` | Book/view appointments | `AppointmentGUI.java` |

### Role-Based Access

| Feature | Admin | Doctor | Receptionist |
|---|---|---|---|
| Appointment Booking | Full access | View only (own appts) | Full access |
| Patient Records | Full access | View only | Full access |
| Doctor Management | Full access | — | — |
| Reports | Full access | Full access | — |

When a Doctor logs in:
- The `AppointmentGUI` table is filtered to show only their appointments
- The doctor combo box is pre-selected and disabled
- The Book button is disabled
- In `PatientGUI`, Add/Update/Remove buttons are disabled

## How to Run

### Prerequisites
- Java JDK 8 or later

### Build & Run

```bash
# Compile all source files
javac -d build src/*.java

# Run the application
java -cp build MainMenu
```

The `users.txt` file will be auto-generated on first launch with default accounts.

## Test Credentials

| Username | Password | Role | Linked Doctor |
|---|---|---|---|
| `admin` | `admin123` | Admin | — |
| `drlim` | `pass123` | Doctor | D001 — Dr Lim (Cardiology) |
| `drwong` | `pass123` | Doctor | D002 — Dr Wong (Pediatrics) |
| `drlee` | `pass123` | Doctor | D003 — Dr Lee (Neurology) |
| `reception` | `pass123` | Receptionist | — |

## Data Files

All data is stored in pipe-delimited (`|`) text files in the project root:
- `users.txt` — user login credentials
- `patients.txt` — patient records
- `doctors.txt` — doctor profiles
- `appointments.txt` — appointment bookings

These files are created automatically with sample data on first run and persist across sessions.
