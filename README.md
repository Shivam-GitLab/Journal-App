
# Journal App
## 📋 Table of Contents
- [Overview](#overview)
- [Problem Statement](#problem-statement)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Folder Structure](#folder-structure)
- [Database Design](#database-design)
- [API Documentation](#api-documentation)
- [Installation & Setup](#installation--setup)
- [Environment Configuration](#environment-configuration)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)
- [Future Enhancements](#future-enhancements)
- [Contributing](#contributing)
- [License](#license)
- [Author](#author)

---
## Overview

**Journal Application With Security** is a modern Spring Boot RESTful web application designed for creating, managing, and persisting personal journal entries. The application leverages MongoDB for NoSQL data persistence and implements comprehensive Spring Security for authentication and authorization, supporting both HTTP Basic Authentication and form-based login mechanisms.

Built with industry best practices, this project demonstrates:
- Clean architecture with separation of concerns
- Secure password handling using BCrypt encryption
- Role-based access control (RBAC)
- RESTful API design principles
- Production-ready monitoring with Spring Boot Actuator

---

## Problem Statement

Users require a simple yet secure solution to:
- Create and manage personal journal entries
- Persist entries durably to a NoSQL database
- Access entries through a secure, authenticated API
- Retrieve entry history and update existing entries
- Monitor application health and metrics

Traditional relational databases may not be ideal for flexible, document-oriented journal data. This application solves this by combining the flexibility of MongoDB with the security and robustness of Spring Boot.

---

## Features

✅ **Core Functionality**
- Create single journal entries via REST endpoint
- Batch create multiple journal entries in one request
- Update existing journal entries by ID
- Retrieve journal entries (authenticated access)
- Delete journal entries
- Automatic timestamp generation for entries

✅ **Security**
- Spring Security framework integration
- HTTP Basic Authentication support
- Form-based login capability
- BCrypt password hashing (configurable strength)
- Role-based endpoint protection (`/journal/**`, `/user/**`)
- Programmatic authentication via exposed `AuthenticationManager` bean

✅ **Monitoring & Observability**
- Spring Boot Actuator enabled
- Health check endpoint (`/actuator/health`)
- Metrics collection (`/actuator/metrics`)
- Environment info endpoint (`/actuator/env`)

✅ **Developer Experience**
- Lombok annotation processor for reduced boilerplate
- Comprehensive unit and integration test support
- Spring Security test utilities included
- Maven-based build system

---

## Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Java | 17+ |
| **Framework** | Spring Boot | Latest (from starter dependencies) |
| **Web** | Spring Web (MVC) | - |
| **Database** | MongoDB | 4.0+ (local or Atlas) |
| **ORM/Mapping** | Spring Data MongoDB | - |
| **Security** | Spring Security | - |
| **Monitoring** | Spring Boot Actuator | - |
| **Build Tool** | Maven | 3.6+ |
| **Code Generation** | Lombok | Latest |
| **Testing** | JUnit 5, Mockito, Spring Test | - |
| **OS (Dev)** | Linux | Any distribution |

---

## Architecture

### High-Level Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                     HTTP Client / Browser                   │
└────────────────────────┬────────────────────────────────────┘
│
▼┌────────────────────────────────┐
│   Spring Security Filter Chain │
│ (Authentication/Authorization) │
└────────────────┬───────────────┘
│
▼┌────────────────────────────────┐
│      Controller Layer          │
│  JournalEntryController        │
│  (REST Endpoints)              │
└────────────────┬───────────────┘
│
▼
┌────────────────────────────────┐
│      Service Layer             │
│  JournalEntryService           │
│  UserDetailsServiceImpl         │
│  (Business Logic)              │
└────────────────┬───────────────┘
│
▼
┌────────────────────────────────┐
│    Repository Layer            │
│  Spring Data MongoDB           │
│  JournalEntryRepository        │
│  UserRepository                │
└────────────────┬───────────────┘
│
▼
┌────────────────────────────────┐
│      MongoDB Database          │
│  journaldb (Documents)         │
└────────────────────────────────┘
```

### Design Patterns Used

1. **MVC Pattern** — Model-View-Controller separation via Spring Web
2. **Service Layer Pattern** — Business logic encapsulation in service classes
3. **Repository Pattern** — Data access abstraction via Spring Data MongoDB
4. **Dependency Injection** — Spring IoC container management
5. **Security Filter Chain** — Spring Security's filter-based architecture
6. **Bean Configuration** — Explicit bean definitions in `SecurityConfig`

---

## Folder Structure

```
Journal-App/
├── pom.xml                                  # Maven project configuration
├── README.md                                # This file
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/sm/journalApp/
│   │   │       ├── JournalApplication.java          # Spring Boot entry point
│   │   │       ├── config/
│   │   │       │   └── SecurityConfig.java          # Spring Security configuration
│   │   │       ├── controllers/
│   │   │       │   └── JournalEntryController.java  # REST API endpoints
│   │   │       ├── entities/
│   │   │       │   ├── JournalEntry.java            # Journal entry domain model
│   │   │       │   └── User.java                    # User domain model
│   │   │       ├── services/
│   │   │       │   ├── JournalEntryService.java     # Business logic
│   │   │       │   └── UserDetailsServiceImpl.java   # Custom user details loading
│   │   │       └── repositories/
│   │   │           ├── JournalEntryRepository.java  # MongoDB repository for entries
│   │   │           └── UserRepository.java          # MongoDB repository for users
│   │   └── resources/
│   │       ├── application.properties               # Application configuration
│   │       └── application-dev.properties           # Development profile (optional)
│   └── test/
│       └── java/
│           └── com/sm/journalApp/
│               └── [Test classes]                   # Unit and integration tests
└── target/                                  # Build output (generated)
```

---

## Database Design

### Entity: JournalEntry

**MongoDB Collection:** `journalEntry`

```json
{
  "_id": "ObjectId",
  "title": "string",
  "content": "string",
  "date": "ISO 8601 timestamp (e.g., 2025-01-15T14:30:00Z)",
  "userId": "string (reference to User)",
  "lastModified": "ISO 8601 timestamp (optional)"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `_id` | ObjectId | Yes | Unique document identifier (auto-generated by MongoDB) |
| `title` | String | Yes | Journal entry title |
| `content` | String | Yes | Journal entry body/content |
| `date` | LocalDateTime | Yes | Creation timestamp (server-set) |
| `userId` | String | Yes | Reference to owning user |
| `lastModified` | LocalDateTime | No | Last update timestamp |

### Entity: User

**MongoDB Collection:** `user`

```json
{
  "_id": "ObjectId",
  "username": "string (unique)",
  "password": "string (BCrypt hash)",
  "roles": ["array of strings (e.g., ROLE_USER, ROLE_ADMIN)"],
  "enabled": "boolean"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `_id` | ObjectId | Yes | Unique document identifier |
| `username` | String | Yes | Unique username for authentication |
| `password` | String | Yes | BCrypt-hashed password (never store plaintext) |
| `roles` | Array | Yes | List of assigned roles for authorization |
| `enabled` | Boolean | Yes | Account status flag |

### Sample Documents

**User Document:**
```json
{
  "_id": ObjectId("507f1f77bcf86cd799439011"),
  "username": "alice",
  "password": "$2a$10$VhE2FkbTJLWzz04D6HjJU.eYYUDp6HqSo3R7BzXXf/0ZdLYGl5WBi",
  "roles": ["ROLE_USER"],
  "enabled": true
}
```

**JournalEntry Document:**
```json
{
  "_id": ObjectId("507f1f77bcf86cd799439012"),
  "title": "My First Day",
  "content": "Today I started using this journal app!",
  "date": ISODate("2025-01-15T14:30:00Z"),
  "userId": "507f1f77bcf86cd799439011"
}
```

---

## API Documentation

### Base URL
```
http://localhost:8080
```

### Authentication

All protected endpoints require authentication using one of the following methods:

#### HTTP Basic Authentication
```
Authorization: Basic <base64(username:password)>
```

**Example (curl):**
```bash
curl -u alice:password http://localhost:8080/journal
```

#### Form-Based Login
Uses default Spring Security login form at `/login`.

---

### Endpoints

#### 1. Create Single Journal Entry

**Endpoint:** `POST /journal/create`

**Authentication:** Required (HTTP Basic)

**Request Headers:**
```
Content-Type: application/json
Authorization: Basic <credentials>
```

**Request Body:**
```json
{
  "title": "My First Entry",
  "content": "Today I started journaling. It feels great!"
}
```

**Response (201 Created):**
```json
{
  "id": "642e2f7b3c4d5e6f7g8h9i0j",
  "title": "My First Entry",
  "content": "Today I started journaling. It feels great!",
  "date": "2025-01-15T14:30:00",
  "userId": "507f1f77bcf86cd799439011"
}
```

**Response (401 Unauthorized):**
```json
{
  "error": "Unauthorized",
  "message": "Invalid credentials"
}
```

---

#### 2. Create Multiple Journal Entries

**Endpoint:** `POST /journal/create-post-multiple`

**Authentication:** Required (HTTP Basic)

**Request Headers:**
```
Content-Type: application/json
Authorization: Basic <credentials>
```

**Request Body:**
```json
[
  {
    "title": "Entry 1",
    "content": "First entry content"
  },
  {
    "title": "Entry 2",
    "content": "Second entry content"
  }
]
```

**Response (201 Created):**
```json
[
  {
    "id": "642e2f7b3c4d5e6f7g8h9i0j",
    "title": "Entry 1",
    "content": "First entry content",
    "date": "2025-01-15T14:30:00",
    "userId": "507f1f77bcf86cd799439011"
  },
  {
    "id": "642e2f7b3c4d5e6f7g8h9i0k",
    "title": "Entry 2",
    "content": "Second entry content",
    "date": "2025-01-15T14:30:15",
    "userId": "507f1f77bcf86cd799439011"
  }
]
```

---

#### 3. Update Journal Entry

**Endpoint:** `PUT /journal/{id}`

**Authentication:** Required (HTTP Basic)

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | String | MongoDB ObjectId of the journal entry |

**Request Headers:**
```
Content-Type: application/json
Authorization: Basic <credentials>
```

**Request Body:**
```json
{
  "title": "Updated Title",
  "content": "This is the updated content after reflection."
}
```

**Response (200 OK):**
```json
{
  "id": "642e2f7b3c4d5e6f7g8h9i0j",
  "title": "Updated Title",
  "content": "This is the updated content after reflection.",
  "date": "2025-01-15T14:30:00",
  "userId": "507f1f77bcf86cd799439011"
}
```

**Response (404 Not Found):**
```json
{
  "error": "Not Found",
  "message": "Journal entry with id 642e2f7b3c4d5e6f7g8h9i0j not found"
}
```

---

#### 4. Get All Journal Entries

**Endpoint:** `GET /journal` or `GET /journal/`

**Authentication:** Required (HTTP Basic)

**Request Headers:**
```
Authorization: Basic <credentials>
```

**Response (200 OK):**
```json
[
  {
    "id": "642e2f7b3c4d5e6f7g8h9i0j",
    "title": "My First Entry",
    "content": "Today I started journaling.",
    "date": "2025-01-15T14:30:00",
    "userId": "507f1f77bcf86cd799439011"
  },
  {
    "id": "642e2f7b3c4d5e6f7g8h9i0k",
    "title": "Day Two",
    "content": "Journaling is becoming a habit.",
    "date": "2025-01-16T09:15:00",
    "userId": "507f1f77bcf86cd799439011"
  }
]
```

---

#### 5. Get Single Journal Entry by ID

**Endpoint:** `GET /journal/{id}`

**Authentication:** Required (HTTP Basic)

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | String | MongoDB ObjectId of the journal entry |

**Request Headers:**
```
Authorization: Basic <credentials>
```

**Response (200 OK):**
```json
{
  "id": "642e2f7b3c4d5e6f7g8h9i0j",
  "title": "My First Entry",
  "content": "Today I started journaling.",
  "date": "2025-01-15T14:30:00",
  "userId": "507f1f77bcf86cd799439011"
}
```

**Response (404 Not Found):**
```json
{
  "error": "Not Found",
  "message": "Entry not found"
}
```

---

#### 6. Delete Journal Entry

**Endpoint:** `DELETE /journal/{id}`

**Authentication:** Required (HTTP Basic)

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | String | MongoDB ObjectId of the journal entry |

**Request Headers:**
```
Authorization: Basic <credentials>
```

**Response (204 No Content):**
```
(No response body)
```

**Response (404 Not Found):**
```json
{
  "error": "Not Found",
  "message": "Entry not found"
}
```

---

#### 7. Health Check (Actuator)

**Endpoint:** `GET /actuator/health`

**Authentication:** Not required

**Response (200 OK):**
```json
{
  "status": "UP",
  "components": {
    "mongoHealthIndicator": {
      "status": "UP"
    }
  }
}
```

---

#### 8. Metrics (Actuator)

**Endpoint:** `GET /actuator/metrics`

**Authentication:** Not required

**Response (200 OK):**
```json
{
  "names": [
    "jvm.memory.used",
    "jvm.threads.live",
    "process.uptime",
    "http.server.requests"
  ]
}
```

---

## Installation & Setup

### Prerequisites

Ensure your development environment meets these requirements:

- **OS:** Linux (Ubuntu 20.04+, Fedora, Debian, or equivalent)
- **Java:** JDK 17 or newer
  ```bash
  java -version
  # Expected: openjdk version "17" or higher
  ```
- **Maven:** 3.6.0 or newer
  ```bash
  mvn -version
  # Expected: Apache Maven 3.6.0 or higher
  ```
- **MongoDB:** Running locally or accessible remotely
  ```bash
  mongod --version
  # Or use MongoDB Atlas cloud service
  ```
- **Git:** For cloning the repository
  ```bash
  git --version
  ```

### Step 1: Clone the Repository

```bash
git clone https://github.com/Shivam-GitLab/Journal-App.git
cd Journal-App
```

### Step 2: Verify Project Structure

```bash
ls -la
# Expected output includes: pom.xml, README.md, src/, target/
```

### Step 3: Configure Environment Variables

Create or edit `src/main/resources/application.properties`:

```bash
nano src/main/resources/application.properties
```

Add the following configuration (see next section for details):

```properties
spring.application.name=journal-app
server.port=8080

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/journaldb

# Actuator Configuration
management.endpoints.web.exposure.include=info,health,metrics,env
management.endpoint.health.show-details=when-authorized
```

### Step 4: Setup MongoDB

#### Option A: Local MongoDB (Linux)

Install MongoDB:
```bash
# Ubuntu/Debian
sudo apt-get update
sudo apt-get install -y mongodb

# Start MongoDB service
sudo systemctl start mongodb
sudo systemctl enable mongodb

# Verify connection
mongo --version
```

#### Option B: MongoDB Atlas (Cloud)

1. Create account at [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Create a cluster
3. Get connection string (looks like):
   ```
   mongodb+srv://username:password@cluster0.mongodb.net/journaldb?retryWrites=true&w=majority
   ```
4. Update `application.properties`:
   ```properties
   spring.data.mongodb.uri=mongodb+srv://username:password@cluster0.mongodb.net/journaldb
   ```

### Step 5: Build the Project

```bash
# Clean previous builds
mvn clean

# Compile and package
mvn package

# Or build without running tests (if needed)
mvn clean package -DskipTests
```

**Expected Output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX.XXXs
```

### Step 6: Create Initial Users in MongoDB

Connect to MongoDB and insert a test user:

```bash
mongo journaldb
```

Or using `mongosh` (newer MongoDB):

```bash
mongosh journaldb
```

Insert a test user document:

```javascript
db.user.insertOne({
  username: "alice",
  password: "$2a$10$VhE2FkbTJLWzz04D6HjJU.eYYUDp6HqSo3R7BzXXf/0ZdLYGl5WBi",
  roles: ["ROLE_USER"],
  enabled: true
});

// Password for above: password (BCrypt hash)
```

**Generate your own BCrypt hash:**

Use this Java snippet in a test class or Spring Boot test:

```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashPasswordUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String plainPassword = "myPassword123";
        String hashedPassword = encoder.encode(plainPassword);
        System.out.println("Hash: " + hashedPassword);
    }
}
```

Or use an online BCrypt generator: [bcrypt-generator.com](https://bcrypt-generator.com)

---

## Environment Configuration

### application.properties Reference

Create `src/main/resources/application.properties` with the following variables:

```properties
# ============================================
# Application Settings
# ============================================
spring.application.name=journal-app
server.port=8080
server.servlet.context-path=/
server.error.include-message=always

# ============================================
# MongoDB Configuration
# ============================================
# Local MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/journaldb

# Or MongoDB Atlas (Cloud)
# spring.data.mongodb.uri=mongodb+srv://username:password@cluster0.mongodb.net/journaldb?retryWrites=true&w=majority

# Database name
spring.data.mongodb.database=journaldb

# ============================================
# Spring Security
# ============================================
# Enable/disable form login (default: enabled)
spring.security.user.name=admin
spring.security.user.password=admin123
spring.security.user.roles=ADMIN

# ============================================
# Spring Boot Actuator
# ============================================
management.endpoints.web.exposure.include=info,health,metrics,env
management.endpoint.health.show-details=when-authorized
management.endpoints.web.base-path=/actuator

# ============================================
# Logging
# ============================================
logging.level.root=INFO
logging.level.com.sm.journalApp=DEBUG
logging.level.org.springframework.security=DEBUG

# ============================================
# Jackson (JSON Serialization)
# ============================================
spring.jackson.serialization.write-dates-as-timestamps=false
spring.jackson.time-zone=UTC
```

### Environment Variables (Optional)

For production or sensitive configurations, use environment variables:

```bash
export SPRING_DATA_MONGODB_URI="mongodb+srv://user:pass@cluster.mongodb.net/journaldb"
export SERVER_PORT=8080
export LOGGING_LEVEL_COM_SM_JOURNALAPP=INFO

mvn spring-boot:run
```

### Configuration Profiles

Create profile-specific files for different environments:

**Development: `application-dev.properties`**
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/journaldb-dev
logging.level.com.sm.journalApp=DEBUG
management.endpoints.web.exposure.include=*
```

**Production: `application-prod.properties`**
```properties
spring.data.mongodb.uri=mongodb+srv://user:pass@prod-cluster.mongodb.net/journaldb
logging.level.com.sm.journalApp=WARN
management.endpoints.web.exposure.include=health,metrics
```

**Run with profile:**
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

---

## Running the Application

### Option 1: Run with Maven

```bash
mvn spring-boot:run
```

**Expected Console Output:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_|\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 
 :: Spring Boot ::                (v3.x.x)

2025-01-15 14:30:00.123  INFO 12345 --- [main] com.sm.journalApp.JournalApplication: Starting JournalApplication...
2025-01-15 14:30:02.456  INFO 12345 --- [main] com.sm.journalApp.JournalApplication: Started JournalApplication in 2.3 seconds
```

### Option 2: Run Packaged JAR

```bash
# Build first
mvn clean package

# Run the JAR
java -jar target/Journal-App-1.0.0.jar

# Or with specific profile
java -jar target/Journal-App-1.0.0.jar --spring.profiles.active=prod
```

### Option 3: Run in IDE (IntelliJ IDEA)

1. Open `JournalApplication.java`
2. Right-click → Run 'JournalApplication'
3. Or use keyboard shortcut: `Ctrl+Shift+F10`

---

## Accessing the Application

Once running, access the application at:

- **Home/API Base:** `http://localhost:8080`
- **Health Check:** `http://localhost:8080/actuator/health`
- **Metrics:** `http://localhost:8080/actuator/metrics`
- **Login (Form):** `http://localhost:8080/login` (if enabled)

**Test with curl:**
```bash
# Without authentication (should fail)
curl http://localhost:8080/journal

# With Basic Auth
curl -u alice:password http://localhost:8080/journal
```

---

## Testing

### Running Tests

Execute all unit and integration tests:

```bash
mvn test
```

**Run specific test class:**
```bash
mvn test -Dtest=JournalEntryControllerTest
```

**Run specific test method:**
```bash
mvn test -Dtest=JournalEntryControllerTest#testCreateEntry
```

**Skip tests during build:**
```bash
mvn clean package -DskipTests
```

### Test Coverage

Generate code coverage report:

```bash
mvn clean test jacoco:report
# Report available at: target/site/jacoco/index.html
```

### Example Test Class

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class JournalEntryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "alice")
    void testGetAllEntries() throws Exception {
        mockMvc.perform(get("/journal"))
            .andExpect(status().isOk());
    }
}
```

---

## Troubleshooting

### Issue 1: MongoDB Connection Failed

**Error:**
```
Exception in initializeObjectToStringConverter
com.mongodb.MongoSocketOpenException: Exception opening socket
```

**Solution:**
1. Verify MongoDB is running:
   ```bash
   sudo systemctl status mongodb
   # Or for macOS: brew services list
   ```
2. Check connection URI in `application.properties`
3. For MongoDB Atlas, ensure IP whitelist includes your machine:
    - Go to Security → Network Access
    - Add your public IP or 0.0.0.0/0 for development

---

### Issue 2: Port Already in Use

**Error:**
```
Address already in use: bind
```

**Solution:**
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or use a different port
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

---

### Issue 3: 401 Unauthorized on All Requests

**Error:**
```
{"error":"Unauthorized","message":"Full authentication is required"}
```

**Solution:**
1. Ensure user exists in MongoDB:
   ```bash
   mongosh journaldb
   db.user.findOne({username: "alice"})
   ```
2. Provide correct credentials:
   ```bash
   curl -u alice:password http://localhost:8080/journal
   ```
3. Check `UserDetailsServiceImpl` loads users correctly

---

### Issue 4: BCrypt Password Mismatch

**Error:**
```
Bad credentials
```

**Solution:**
1. Regenerate password hash:
   ```java
   new BCryptPasswordEncoder().encode("newPassword")
   ```
2. Update user document in MongoDB:
   ```javascript
   db.user.updateOne(
     {username: "alice"},
     {$set: {password: "$2a$10$..."}}
   )
   ```

---

### Issue 5: Spring Boot Won't Start in IDE

**Error:**
```
Cannot resolve symbol 'JournalApplication'
```

**Solution:**
1. Ensure Maven is indexed:
    - IntelliJ: File → Invalidate Caches → Restart
2. Mark `src/main/java` as source root:
    - Right-click folder → Mark Directory as → Sources Root
3. Rebuild project:
   ```bash
   mvn clean install -DskipTests
   ```

---

### Issue 6: Lombok Annotations Not Working

**Error:**
```
Cannot resolve symbol 'lombok'
```

**Solution:**
1. Install Lombok plugin in IDE:
    - IntelliJ: Settings → Plugins → Search "Lombok" → Install
2. Enable annotation processing:
    - Settings → Build → Compiler → Annotation Processors → Enable

---

## Future Enhancements

### Short-term (1-2 months)

- [ ] **User Registration Endpoint**
    - `POST /auth/register` — Allow users to self-register
    - Email verification workflow
    - Password strength validation

- [ ] **Password Reset Flow**
    - `POST /auth/forgot-password` — Request reset token
    - `POST /auth/reset-password` — Reset with token

- [ ] **JWT Authentication**
    - Replace HTTP Basic with JWT tokens
    - Implement refresh token mechanism
    - Improve statelessness

- [ ] **Request/Response DTOs**
    - Separate domain models from API contracts
    - Add validation annotations (`@NotBlank`, `@Length`)
    - Prevent over-exposure of internal fields

### Medium-term (3-6 months)

- [ ] **Advanced Authorization**
    - Method-level security (`@PreAuthorize`)
    - Admin role with user management
    - Share journal entries between users

- [ ] **Pagination & Search**
    - `GET /journal?page=0&size=10` — Paginated results
    - `GET /journal/search?q=keyword` — Full-text search
    - Sort by date (ascending/descending)

- [ ] **Tagging & Categories**
    - Add tags field to journal entries
    - Filter by tag: `GET /journal?tags=happy,personal`
    - Tag management endpoints

- [ ] **OpenAPI/Swagger Documentation**
    - Add `springdoc-openapi` dependency
    - Auto-generated API docs at `/swagger-ui.html`
    - Interactive endpoint testing

- [ ] **Caching**
    - Redis caching layer for frequently accessed entries
    - Cache invalidation strategy
    - Improve response times

### Long-term (6-12 months)

- [ ] **Containerization**
    - Dockerfile for application
    - Docker Compose for app + MongoDB + Redis
    - Container registry push

- [ ] **CI/CD Pipeline**
    - GitHub Actions workflows
    - Automated testing on every push
    - Automated deployment to staging/production

- [ ] **Advanced Monitoring**
    - ELK stack integration (Elasticsearch, Logstash, Kibana)
    - Application Performance Monitoring (APM)
    - Distributed tracing (Jaeger/Zipkin)

- [ ] **Mobile Application**
    - React Native or Flutter client
    - Offline synchronization
    - Push notifications

- [ ] **Social Features**
    - Share journal entries publicly
    - Comments and likes
    - Follow other users' public journals

- [ ] **Analytics**
    - Entry frequency dashboard
    - Mood tracking over time
    - Word cloud generation

---

## Contributing

Contributions are welcome! Please follow these guidelines:

### How to Contribute

1. **Fork the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/Journal-App.git
   cd Journal-App
   ```

2. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make changes and commit**
   ```bash
   git add .
   git commit -m "feat: add user registration endpoint"
   ```

4. **Push to your fork**
   ```bash
   git push origin feature/your-feature-name
   ```

5. **Create a Pull Request**
    - Describe changes clearly
    - Reference related issues
    - Ensure tests pass

### Code Standards

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Write unit tests for new features
- Ensure code compiles and tests pass: `mvn clean test`
- Use meaningful commit messages
- Keep commits atomic and focused

### Reporting Issues

Report bugs via GitHub Issues with:
- Clear description of the problem
- Steps to reproduce
- Expected vs. actual behavior
- Environment details (OS, Java version, MongoDB version)

---

## License

This project is licensed under the **MIT License** — see [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2025 Shivam Jha

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

---

## Author

**Shivam Jha**

- GitHub: [@Shivam-GitLab](https://github.com/Shivam-GitLab)
- Repository: [Journal-App](https://github.com/Shivam-GitLab/Journal-App)

### Project Information

- **Package:** `com.sm.journalApp`
- **Started:** January 2025
- **Version:** 1.0.0
- **Status:** Active Development

---

## Acknowledgments

- Spring Boot Community for excellent documentation
- MongoDB for flexible document database
- Stack Overflow for problem-solving assistance
- Contributors and users reporting issues

---

## Quick Reference

| Task | Command |
|------|---------|
| Clone repo | `git clone https://github.com/Shivam-GitLab/Journal-App.git` |
| Build project | `mvn clean package` |
| Run application | `mvn spring-boot:run` |
| Run tests | `mvn test` |
| Generate password hash | Use `BCryptPasswordEncoder.encode()` |
| Check health | `curl http://localhost:8080/actuator/health` |
| Create entry | `curl -X POST -u user:pass -H "Content-Type: application/json" -d '{}' http://localhost:8080/journal/create` |

---

**Last Updated:** January 2025  
**Maintained by:** Shivam Jha

```


