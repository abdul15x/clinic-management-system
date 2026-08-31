# 🏥 Clinic Management System

A full-stack **Clinic Management System** built with **Spring Boot**, **MongoDB**, **Thymeleaf**, and **Bootstrap**. It provides role-based access control for managing patients, doctors, appointments, prescriptions, and medical records with both web UI and REST API support.

---

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
  - [Local Setup](#local-setup)
  - [Docker Setup](#docker-setup)
- [API Documentation](#api-documentation)
- [Authentication](#authentication)
- [Project Structure](#project-structure)
- [Screenshots](#screenshots)
- [Testing](#testing)
- [License](#license)

---

## ✨ Features

### Core Modules
- **Patient Management** — Register, update, search, and manage patient records
- **Doctor Management** — Manage doctor profiles with specializations
- **Appointment Scheduling** — Book, reschedule, and cancel appointments
- **Prescription Management** — Create and track patient prescriptions
- **Medical Records** — Maintain diagnosis and treatment history
- **Dashboard** — Real-time statistics overview of all clinic data

### Security & Access Control
- **JWT + Session Dual Authentication** — API uses JWT tokens, web UI uses form-based sessions
- **Role-Based Access Control (RBAC)** — ADMIN, DOCTOR, RECEPTIONIST roles with different permissions
- **Password Encryption** — BCrypt hashing for secure credential storage

### Technical Features
- **Bean Validation** — Jakarta Validation on all forms and DTOs
- **Global Exception Handling** — Custom error pages and standardized JSON error responses
- **Swagger/OpenAPI** — Interactive API documentation at `/swagger-ui.html`
- **Docker Support** — One-command containerized deployment with `docker-compose`
- **Unit Tests** — 27 JUnit + Mockito tests covering service layer logic

---

## 🛠 Tech Stack

| Layer | Technology |
|-------|-----------|
| **Backend** | Java 26, Spring Boot 3.5.1, Spring Security, Spring Data MongoDB |
| **Database** | MongoDB 7.0 |
| **Security** | JWT (jjwt 0.12.6), BCrypt, Spring Security |
| **Frontend** | Thymeleaf 3.1, Bootstrap 5.3, Bootstrap Icons |
| **API Docs** | SpringDoc OpenAPI (Swagger UI) |
| **Testing** | JUnit 5, Mockito, AssertJ |
| **Build Tool** | Maven |
| **Containerization** | Docker, Docker Compose |

---

## 📦 Prerequisites

### Local Development
- **Java 26** (JDK)
- **Maven 3.9+**
- **MongoDB 7.0** (local instance or MongoDB Atlas)

### Docker Deployment
- **Docker Desktop** (includes Docker Compose v2)

---

## 🚀 Installation

### Local Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/clinic-management-system.git
   cd clinic-management-system
   Start MongoDB
Make sure MongoDB is running on localhost:27017
Or update application.yml with your MongoDB connection string
Build and run
bash
mvn clean install
mvn spring-boot:run
Access the application
Web UI: http://localhost:8080
Swagger API Docs: http://localhost:8080/swagger-ui.html
Default login credentials (create via register page)
Register an ADMIN user first at /register
Docker Setup
Build the JAR
bash
mvn clean package -DskipTests
Start with Docker Compose
bash
docker compose up --build
Access the application
Web UI: http://localhost:8080
MongoDB: localhost:27017 (for external tools like MongoDB Compass)
Stop containers
bash
docker compose down
🔐 Authentication
Web UI (Session-Based)
Login at /login with username and password
Session is maintained via cookies
Logout via /logout
REST API (JWT-Based)
Register — POST /api/auth/register
Login — POST /api/auth/login → returns JWT token
Use token — Add header: Authorization: Bearer YOUR_TOKEN
Role Permissions
Table
Feature	ADMIN	DOCTOR	RECEPTIONIST
View all data	✅	✅	✅
Manage Patients	✅	❌	✅
Manage Doctors	✅	❌	❌
Book Appointments	✅	❌	✅
Write Prescriptions	✅	✅	❌
Add Medical Records	✅	✅	❌
Manage Users	✅	❌	❌
📡 API Endpoints
Table
Module	Base Path	Auth
Auth	/api/auth/**	Public (register/login)
Dashboard	/api/dashboard/stats	Bearer Token
Patients	/api/patients/**	Bearer Token
Doctors	/api/doctors/**	Bearer Token
Appointments	/api/appointments/**	Bearer Token
Prescriptions	/api/prescriptions/**	Bearer Token
Medical Records	/api/medical-records/**	Bearer Token
Full interactive documentation: http://localhost:8080/swagger-ui.html
🧪 Testing
Run unit tests with Maven:
bash
mvn test
Test Coverage:
PatientServiceTest — 10 tests
DoctorServiceTest — 10 tests
UserServiceTest — 7 tests
Total: 27 tests, all passing
📁 Project Structure 
src/
├── main/java/com/clinic/
│   ├── config/           # MongoDB, Security, OpenAPI config
│   ├── controller/       # REST + View controllers
│   ├── dto/              # Request/Response DTOs
│   ├── entity/           # MongoDB document entities
│   ├── enums/            # Gender, Role, Status enums
│   ├── exception/        # Custom exceptions + global handler
│   ├── repository/       # Spring Data MongoDB repositories
│   ├── service/          # Business logic layer
│   ├── security/         # JWT utilities and filters
│   └── util/             # ID generators
├── main/resources/
│   ├── templates/        # Thymeleaf HTML pages
│   └── application.yml   # App configuration
└── test/java/            # JUnit + Mockito unit tests
