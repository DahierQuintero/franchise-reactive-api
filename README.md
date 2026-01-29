# Franchise Reactive API

Backend reactive API built as part of the **Accenture Backend Developer technical challenge**.

The project manages **Franchises**, **Branches**, and **Products**, applying **Reactive Programming**, **Clean Architecture**, **Unit Testing**, **Docker**, and **Infrastructure as Code (IaC)** principles.

---

## 🧠 Architecture

The solution follows **Clean Architecture**, clearly separating responsibilities:

```
api/            → WebFlux handlers, routers, DTOs
application/    → Use cases (business logic)
domain/         → Domain models and repository interfaces
infrastructure/ → MongoDB persistence and mappers
common/         → Shared exceptions and utilities
```

Reactive programming is implemented using **Spring WebFlux** and **Project Reactor (Mono / Flux)**.

---

## 🚀 Tech Stack

* Java 17
* Spring Boot 3 (WebFlux)
* Reactive MongoDB
* Gradle
* Docker & Docker Compose
* JUnit 5 + Mockito + Reactor Test

---

## ▶️ Running the project

### 1️⃣ Start MongoDB using Docker

```bash
docker-compose up -d
```

### 2️⃣ Run the application

```bash
./gradlew bootRun
```

The API will be available at:

```
http://localhost:8080
```

---

## 📌 API Endpoints

### ➕ Create Franchise

```bash
curl -X POST http://localhost:8080/franchises \
  -H "Content-Type: application/json" \
  -d '{ "name": "KFC" }'
```

---

### ➕ Add Branch to Franchise

```bash
curl -X POST http://localhost:8080/franchises/{franchiseId}/branches \
  -H "Content-Type: application/json" \
  -d '{ "name": "Sucursal Centro" }'
```

---

### ➕ Add Product to Branch

```bash
curl -X POST http://localhost:8080/franchises/{franchiseId}/branches/Sucursal-Centro/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Hamburguesa",
    "stock": 80
  }'
```

> Branch names are normalized internally (e.g. `Sucursal-Centro` → `Sucursal Centro`).

---

### 🔄 Update Product Stock

```bash
curl -X PUT http://localhost:8080/franchises/{franchiseId}/branches/Sucursal-Centro/products/Hamburguesa/stock \
  -H "Content-Type: application/json" \
  -d '{ "stock": 120 }'
```

---

### ❌ Delete Product from Branch

```bash
curl -X DELETE http://localhost:8080/franchises/{franchiseId}/branches/Sucursal-Centro/products/Hamburguesa
```

---

### ⭐ Get Top Stock Products by Franchise

Returns the product with the highest stock **per branch** for a specific franchise.

```bash
curl -X GET http://localhost:8080/franchises/{franchiseId}/products/top-stock
```

Example response:

```json
[
  {
    "branch": "Sucursal Centro",
    "product": "Hamburguesa",
    "stock": 120
  },
  {
    "branch": "Sucursal Norte",
    "product": "Papas",
    "stock": 200
  }
]
```

---

## 🧪 Testing

Unit tests are implemented at the **application layer**, validating business rules independently of infrastructure.

```bash
./gradlew test
```

Test tools used:

* JUnit 5
* Mockito
* Reactor Test (StepVerifier)

---

## 📦 Docker

The project includes a `docker-compose.yml` file for local MongoDB execution.

The application can be easily containerized if required.

---

## ⚠️ Design Notes & Improvements

The following design decisions were taken consciously for this technical challenge and are documented for clarity:

* **Domain exception handling**
  The domain layer validates duplicate branches and throws a `DuplicateBranchException`.
  Currently, this exception is propagated as an internal server error (**HTTP 500**).
  This can be improved by adding a **global WebFlux error handler** to map domain exceptions to proper HTTP responses (e.g., **409 Conflict**).

* **Product identification strategy**
  Products are modeled as **embedded documents** and are identified by **name within a branch**.
  An explicit product ID can be added if product-level operations grow in complexity or require independent lifecycle management.

* **Branch identification in API paths**
  Branches are identified by **name in the API paths** for simplicity.
  In a production-grade system, branches would have a **unique identifier** to avoid URL encoding issues and to improve scalability.

---

## ✅ Evaluation Criteria Coverage

✔ Reactive Programming (WebFlux)
✔ Clean Architecture
✔ Unit Tests
✔ Docker
✔ Infrastructure as Code (Docker Compose)
✔ Code readability and maintainability

---

## 👨‍💻 Author

**Dilan Quintero**
Backend Java Developer

---

Thanks for reviewing this technical challenge 🚀

