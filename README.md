# Franchise Reactive API

Reactive Backend API built with Spring WebFlux following Clean Architecture principles.

## Tech Stack
- Java 17
- Spring Boot (WebFlux)
- Reactive MongoDB
- Docker & Docker Compose
- JUnit 5 & Mockito

## Architecture
The project follows a Clean Architecture-inspired layered structure, separating domain, application, infrastructure, and API layers.

## How to Run (Local)
Instructions will be provided using Docker Compose.

### Error Handling


The domain layer validates duplicate branches and throws a `DuplicateBranchException`.
Currently, this exception is propagated as an internal server error (HTTP 500).
This can be improved by adding a global WebFlux error handler to map domain
exceptions to proper HTTP responses (e.g., 409 Conflict).

**Products are modeled as embedded documents and are identified by name
within a branch. An explicit product id can be added if product-level
operations grow in complexity.**

Branches are identified by name in the API paths for simplicity.
In a production scenario, branches would have a unique identifier
to avoid URL encoding issues and to improve scalability.
