# Hospital Appointment Management System

A modern desktop application built using **Java Swing** and **SQLite (JDBC)** that helps hospitals manage patients and their appointments efficiently. The system provides an intuitive graphical interface for registering patients, scheduling appointments, viewing appointment history, and maintaining hospital records.

---

## Overview

The Hospital Appointment Management System is a Java desktop application designed to simplify hospital appointment scheduling. Instead of maintaining manual records, hospital staff can use this system to digitally manage patient information and appointments.

The application follows a layered architecture using the **DAO (Data Access Object)** design pattern, making the code modular, maintainable, and scalable.

---

## Features

### Patient Management

- Add new patients
- View all registered patients
- Store patient details in SQLite database
- Input validation for patient information

---

### Appointment Management

- Book appointments
- View scheduled appointments
- Associate appointments with registered patients
- Delete or cancel appointments

---

### Modern User Interface

- Clean Java Swing interface
- FlatLaf modern look and feel
- Responsive forms
- Table-based data display
- Easy navigation between modules

---

### Database

- SQLite database
- Automatic database initialization
- Persistent storage
- JDBC connectivity

---

## Tech Stack

| Technology | Purpose |
|------------|----------|
| Java | Programming Language |
| Java Swing | Desktop GUI |
| JDBC | Database Connectivity |
| SQLite | Database |
| FlatLaf | Modern Swing Theme |
| SLF4J | Logging |
| DAO Pattern | Data Access Layer |

---

## Project Structure

```
HospitalAppointmentSystem
│
├── src/
│   └── com/
│       └── hospital/
│           ├── database/
│           │     ├── DatabaseManager.java
│           │     ├── dao/
│           │     └── dao/sqlite/
│           │
│           ├── model/
│           │     ├── Patient.java
│           │     └── Appointment.java
│           │
│           ├── ui/
│           │     ├── MainFrame.java
│           │     ├── DashboardPanel.java
│           │     ├── PatientsPanel.java
│           │     └── AppointmentsPanel.java
│           │
│           ├── util/
│           │     └── ValidationUtils.java
│           │
│           └── Main.java
│
├── lib/
├── bin/
├── hospital.db
├── build.bat
└── run.bat
```

---

## Architecture

```
                User
                  │
                  ▼
          Java Swing GUI
                  │
                  ▼
        Business Logic Layer
                  │
                  ▼
          DAO (Data Access)
                  │
                  ▼
            JDBC Driver
                  │
                  ▼
           SQLite Database
```

---

## Modules

### 1. Patient Module

Responsible for managing patient records.

Functions:

- Register Patient
- View Patients
- Validate Patient Information

---

### 2. Appointment Module

Responsible for appointment scheduling.

Functions:

- Book Appointment
- View Appointments
- Cancel Appointment

---

### 3. Database Module

Handles database operations.

Responsibilities:

- Database Connection
- CRUD Operations
- SQL Execution

---

### 4. Validation Module

Ensures correct user input before storing data.

Examples:

- Empty field validation
- Phone number validation
- Required fields checking

---

## Database Schema

### Patients

| Column | Type |
|---------|------|
| id | INTEGER |
| name | TEXT |
| age | INTEGER |
| gender | TEXT |
| phone | TEXT |

---

### Appointments

| Column | Type |
|---------|------|
| id | INTEGER |
| patient_id | INTEGER |
| doctor | TEXT |
| appointment_date | TEXT |
| appointment_time | TEXT |

---

## Application Workflow

```
Launch Application
        │
        ▼
    Dashboard
        │
 ┌──────┴──────────┐
 │                 │
 ▼                 ▼
Patients      Appointments
 │                 │
 ▼                 ▼
Add Patient   Book Appointment
 │                 │
 ▼                 ▼
SQLite Database
        │
        ▼
Display Updated Records
```

---

## Libraries Used

- sqlite-jdbc
- FlatLaf
- SLF4J API
- SLF4J Simple

---

## Getting Started

### Prerequisites

- Java JDK 17 or above
- Git
- SQLite JDBC Driver (already included)

### Clone Repository

```bash
git clone https://github.com/satyam22744/HospitalAppointmentSys.git
```

### Navigate

```bash
cd HospitalAppointmentSys
```

### Build

```bash
build.bat
```

### Run

```bash
run.bat
```

Or execute directly:

```bash
java -cp "bin;lib/*" com.hospital.Main
```

---

## Application Screens

- Dashboard
- Patients Panel
- Appointment Panel
- SQLite Database

*(Screenshots can be added here later.)*

---

## Validation

The application validates:

- Empty fields
- Invalid patient details
- Invalid appointment entries
- Database consistency

---

## Future Enhancements

- Doctor Management
- Login Authentication
- Admin Dashboard
- Search & Filter
- Edit Patient Details
- Edit Appointment
- Appointment Reminder
- Email Notifications
- PDF Reports
- Print Receipts
- Backup & Restore Database
- Multi-user Support

---

## Learning Outcomes

This project demonstrates:

- Java Swing GUI Development
- Object-Oriented Programming
- JDBC Connectivity
- SQLite Database Management
- DAO Design Pattern
- MVC-inspired Project Structure
- Desktop Application Development

---

## Author

**Satyam Kumar**

GitHub: https://github.com/satyam22744

---

## Support

If you find this project useful, consider giving it a star on GitHub.
