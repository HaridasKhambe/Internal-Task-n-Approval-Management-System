# 🚀 TaskFlowX – Internal Task & Approval Management System

## 📘 Overview
**TaskFlowX** is a backend-first enterprise workflow system designed to **manage internal tasks, approvals, and accountability** within an organization.  
It focuses on **workflow enforcement, auditability, performance, and security**, instead of basic CRUD operations.

---

## 🧠 Problem Statement
In many organizations, internal tasks are handled through **emails, spreadsheets, or informal tools**, leading to poor traceability and inefficiency.

> 🎯 **TaskFlowX** solves this by providing a structured backend system with controlled workflows, audit trails, and performance-aware design.

---

## 🎯 Key Objectives
- **Workflow Enforcement:** Ensure tasks follow a strict, backend-controlled lifecycle.
- **Accountability:** Track who performed what action and when.
- **Performance Awareness:** Optimize reads using caching and async processing.
- **Security:** Enforce role-based access with JWT authentication.
- **Scalability:** Design services that can evolve without refactoring.

---

## ⚙️ Technology Stack

### 🖥️ Backend
| Technology | Purpose |
|------------|--------|
| **Spring Boot 3.x** | Core backend framework |
| **Spring MVC** | REST API architecture |
| **Spring Security + JWT** | Authentication & RBAC |
| **Spring Data JPA (Hibernate)** | ORM & persistence |
| **MySQL 8** | Relational database |
| **Caffeine Cache** | In-memory caching |
| **@Async + Thread Pool** | Non-blocking background tasks |
| **Spring Validation** | Input validation |
| **SpringDoc OpenAPI** | Swagger documentation |
| **Maven** | Build & dependency management |

---

## 👥 User Roles
TaskFlowX intentionally supports **only three roles** to keep the scope clean and realistic.

### 🔹 ADMIN
- Manage users
- View system-wide audit logs

### 🔹 MANAGER
- Create and assign tasks
- Approve or reject submitted tasks
- Track task progress

### 🔹 EMPLOYEE
- View assigned tasks
- Update task status
- Rework rejected tasks

---

## 🔄 Core Workflow
> CREATED → ASSIGNED → IN_PROGRESS → SUBMITTED → APPROVED / REJECTED → CLOSED


### Backend-Enforced Rules
- Task cannot start unless assigned
- Task cannot be approved unless submitted
- Only Managers can approve or reject
- Rejected tasks return to ASSIGNED for rework
- Invalid transitions are blocked in code

> ⚠️ This system is **workflow-driven**, not CRUD-driven.

---

## 🧩 Core Features

### ✅ Workflow-Driven Task Management
Each task follows a strictly validated lifecycle enforced in the service layer.

### ✅ Audit Logging
Every critical action is logged with user, task, action, and timestamp.

### ✅ Asynchronous Processing
Audit logging runs in the background to keep APIs fast and responsive.

### ✅ Caching
Frequently accessed task data is cached and safely evicted on updates.

---

## 🧠 Mapping Core Concepts (Interview-Ready)

| Concept | Implementation | Purpose |
|--------|----------------|--------|
| Workflow | TaskService state validation | Business rule enforcement |
| Audit Logs | AuditService + AuditLog | Accountability |
| Async | `@Async` logging | Performance |
| Caching | `@Cacheable`, `@CacheEvict` | Load optimization |

---

## 🗄️ Database Design
Core entities:
- `users`
- `tasks`
- `audit_logs`

Relationships:
- One User → Many Tasks
- One Task → Many Audit Logs
- One User → Many Audit Logs


---

<details>
<summary>🧱 <b>Project Structure</b></summary>

TaskFlowX/
├── backend/
│ ├── src/main/java/com/taskflowx/
│ │ ├── config/
│ │ ├── controller/
│ │ ├── dto/
│ │ ├── enums/
│ │ ├── exception/
│ │ ├── model/
│ │ ├── repository/
│ │ ├── security/
│ │ └── service/
│ └── resources/
│ └── application.properties
│
├── frontend/ # reserved for future use
└── README.md

</details>

---

<details>
<summary>🧮 <b>API Endpoints Overview</b></summary>

### 🔐 Authentication
| Action | Method | Endpoint |
|------|--------|----------|
| Login | POST | `/api/auth/login` |

### 👑 Admin APIs
| Action | Method | Endpoint |
|------|--------|----------|
| Create user | POST | `/api/admin/users` |
| Get users | GET | `/api/admin/users` |
| View audit logs | GET | `/api/admin/audit-logs` |

### 👨‍💼 Manager APIs
| Action | Method | Endpoint |
|------|--------|----------|
| Create task | POST | `/api/manager/tasks` |
| Assign task | PUT | `/api/manager/tasks/{id}/assign` |
| Review task | PUT | `/api/manager/tasks/{id}/review` |
| View tasks | GET | `/api/manager/tasks` |

### 👨‍💻 Employee APIs
| Action | Method | Endpoint |
|------|--------|----------|
| View tasks | GET | `/api/employee/tasks` |
| Task details | GET | `/api/employee/tasks/{id}` |
| Update status | PUT | `/api/employee/tasks/{id}/status` |

</details>

---

## 📘 API Documentation
Swagger UI is available at:
http://localhost:8080/swagger-ui.html


---

## 🧾 Output Showcase


### 🔐 Authentication
| Image | Preview |
|------|--------|
| Login | ![](OUTPUT/auth/login.png) |

### 👨‍💼 Manager APIs (Postman)
| Image | Preview |
|------|--------|
| Create Task | ![](OUTPUT/postman/create-task.png) |
| Assign Task | ![](OUTPUT/postman/assign-task.png) |

### 👨‍💻 Employee APIs (Postman)
| Image | Preview |
|------|--------|
| Update Status | ![](OUTPUT/postman/update-status.png) |

---

<details>
<summary>🚀 <b>Future Enhancements</b></summary>

- Notification center
- File attachments
- Dashboard analytics
- Role hierarchy expansion

</details>

---

## 📈 Conclusion
**TaskFlowX** demonstrates how **real enterprise backend systems** are designed — with controlled workflows, accountability, and performance in mind.  

---

## 🤝 Happy to Connect  

I'm always open to discussions, collaborations, and feedback!  
Feel free to reach out if you'd like to connect or learn more about this project.  

📧 **Email:** [haridaskhambe2003@gmail.com](mailto:haridaskhambe2003@gmail.com)  
💼 **LinkedIn:** [https://www.linkedin.com/in/haridas-khambe-aa650926b](https://www.linkedin.com/in/haridas-khambe-aa650926b/) 
🌐 **Portfolio:** [Portfolio ](https://haridaskhambe.github.io/react-personal-portfolio/)

⭐ If you find this project helpful or inspiring, consider giving it a star on GitHub!  


