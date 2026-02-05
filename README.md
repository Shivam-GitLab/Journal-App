# Journal Application With Security

## Overview
A Spring Boot RESTful application for creating and managing personal journal entries. The application persists data to MongoDB and secures endpoints using Spring Security (HTTP Basic and form login). It includes actuator support and uses Lombok to reduce boilerplate.

## Problem Statement
Users need a simple, secure way to create, update, and manage journal entries with persistence to a NoSQL datastore (MongoDB) and role-based access control.

## Features
- Create single or multiple journal entries
- Update journal entries by id
- Authentication using Spring Security (HTTP Basic / form login)
- Passwords stored using BCrypt hashing
- Spring Boot Actuator enabled for monitoring
- Unit testing support via Spring Boot test and Spring Security test

## Tech Stack
- Java (recommended JDK 17+)
- Spring Boot
    - Spring Web
    - Spring Data MongoDB
    - Spring Security
    - Spring Boot Actuator
- MongoDB
- Maven
- Lombok

## Architecture Overview
- Controller layer: REST endpoints (package `com.sm.journalApp.controllers`)
- Service layer: business logic (e.g., `journalEntryService`, `UserDetailsServiceImpl`)
- Repository layer: Spring Data MongoDB repositories (not shown in excerpts)
- Model: Domain entities (e.g., `JournalEntry` in `com.sm.journalApp.entities`)
- Security: `SecurityConfig` configures authentication, password encoder, and security filters

Security highlights:
- Endpoints under `/journal/**` and `/user/**` require authentication
- Passwords encoded with `BCryptPasswordEncoder`
- `AuthenticationManager` exposed as a bean for programmatic auth if needed

## Folder Structure
Assumed project layout (based on available sources):

- `pom.xml`
- `README.md`
- `src/main/java/com/sm/journalApp/`
    - `JournalApplication.java`
    - `config/SecurityConfig.java`
    - `controllers/JournalEntryController.java`
    - `entities/JournalEntry.java`
    - `services/` (service implementations, e.g., `UserDetailsServiceImpl`)
    - `repositories/` (MongoDB repositories)
- `src/main/resources/`
    - `application.properties` (environment config)
- `src/test/java/` (tests)

## Installation & Setup

Prerequisites:
- Linux OS (development environment)
- Java JDK 17 or newer
- Maven 3.6+
- MongoDB (local or remote)

1. Clone the repository
```bash
git clone <repository-url>
cd <repository-directory>
```

2. Configure environment (see next section)

3. Build the project
```bash
mvn clean package
```

4. Run the application
```bash
# Run with Maven
mvn spring-boot:run

# Or run the packaged jar
java -jar target/*.jar
```

## Environment Configuration

Create or edit `src/main/resources/application.properties` with values similar to:

```properties
spring.application.name=journal-app
server.port=8080

# MongoDB - replace with your connection URI
spring.data.mongodb.uri=mongodb://localhost:27017/journaldb

# Actuator endpoints (optional)
management.endpoints.web.exposure.include=info,health,metrics,env
```

Security:
- The project uses a custom `UserDetailsServiceImpl` to load users. Create users in MongoDB or implement a registration endpoint.
- Passwords must be stored as BCrypt hashes. To generate a hash for password `password` using Java BCrypt:
    - Use a snippet with `new BCryptPasswordEncoder().encode("password")`, or an online BCrypt generator.

Example user document (pseudo-document — adapt to your user model/repository):
```json
{
  "username": "alice",
  "password": "$2a$10$VhE...your_bcrypt_hash...9G", 
  "roles": ["ROLE_USER"]
}
```

## How to Run the Project

1. Ensure MongoDB is running and `application.properties` is configured.
2. Build and run:
```bash
mvn spring-boot:run
```
3. Access the app at `http://localhost:8080`.
4. For monitoring (if enabled): `http://localhost:8080/actuator/health`

## API Endpoints

Note: Base controller mappings are assumed from available code snippets. Adjust paths if actual controller annotations differ.

- Authentication
    - HTTP Basic or form-login (default Spring Security pages enabled unless overridden)

- Journal endpoints (require authentication)
    - POST `/journal/create` — Create a single journal entry (assumed)
        - Request: `application/json`
        - Body example:
          ```json
          {
            "title": "My first entry",
            "content": "Today I started a journal."
          }
          ```
        - Response: `201 Created`
          ```json
          {
            "id": "642e2f...abcd",
            "title": "My first entry",
            "content": "Today I started a journal.",
            "date": "2025-01-15T14:30:00"
          }
          ```

    - POST `/journal/create-post-multiple` — Create multiple journal entries
        - Request body example:
          ```json
          [
            { "title": "Entry 1", "content": "Content 1" },
            { "title": "Entry 2", "content": "Content 2" }
          ]
          ```
        - Response: `201 Created` with list of created entries (each now contains `date` set by server)

    - PUT `/journal/{id}` — Update a journal entry by id
        - Request body example:
          ```json
          {
            "title": "Updated title",
            "content": "Updated content"
          }
          ```
        - Response:
            - `200 OK` with updated entry if found
            - `404 Not Found` if entry does not exist

    - (Assumed) GET `/journal` — List entries for authenticated user
    - (Assumed) GET `/journal/{id}` — Get specific entry
    - (Assumed) DELETE `/journal/{id}` — Delete an entry (snippet had commented delete)

- Actuator endpoints (if enabled)
    - GET `/actuator/health`
    - GET `/actuator/metrics`

## Sample Requests

Curl example — creating multiple entries (replace username/password):

```bash
curl -X POST http://localhost:8080/journal/create-post-multiple \
  -u alice:password \
  -H "Content-Type: application/json" \
  -d '[{"title":"A","content":"..."} , {"title":"B","content":"..."}]'
```

Curl example — update entry:

```bash
curl -X PUT http://localhost:8080/journal/642e2f...abcd \
  -u alice:password \
  -H "Content-Type: application/json" \
  -d '{"title":"New Title","content":"New content"}'
```

Authentication header example:

- Basic Auth header:
    - Header name: `Authorization`
    - Header value: `Basic <base64(username:password)>`

## Testing
Run unit and integration tests:
```bash
mvn test
```

The project includes `spring-boot-starter-test` and `spring-security-test` for security-related tests.

## Future Enhancements
- Implement user registration and password reset endpoints
- Add role-based authorization (e.g., admin vs user)
- Add pagination and search for journal entries
- Add DTOs and validation for request payloads
- Enhance tests and add integration tests using Testcontainers (MongoDB)
- Add OpenAPI / Swagger documentation
- Add CI/CD pipeline and containerization (Docker)

---

Author: Project codebase in `com.sm.journalApp` package.