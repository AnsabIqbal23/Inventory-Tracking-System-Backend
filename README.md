# 🛒 Inventory Tracking System

A scalable, secure, and efficient backend system to manage inventory for single and multi-store kiryana operations — designed for Bazaar Technologies' case study challenge.

---

## 📦 Project Overview

This system enables product stock-in, sales, manual removals, and store-specific inventory tracking. It evolves across three stages:

- **Stage 1:** Local inventory tracking for a single store
- **Stage 2:** REST API with PostgreSQL for 500+ stores
- **Stage 3:** Scalable architecture for thousands of stores with async updates, caching, and auditing

---

## ✅ Features by Stage

### Stage 1: Single Store Support
- CLI & REST API support
- Local data modeling with `Product`, `StockMovement`, and `Store`
- Basic stock-in, sell, and remove operations

### Stage 2: Multi-Store System
- RESTful APIs for all entities
- Store-specific inventory tracking
- PostgreSQL integration
- Basic Authentication (Spring Security)
- Request Throttling (via Bucket4j)
- Filtering & reporting support via APIs

### Stage 3: Scalable & Event-Driven
- Event-driven updates with Spring `@EventListener`
- API rate-limiting via Bucket4j
- Audit trail through `StockMovement` history
- Prepared for horizontal scalability (e.g., microservices, read/write DBs)
- Extendable for caching using Redis (not yet integrated)

---

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **Bucket4j** (rate-limiting)
- **Maven**

---

## 🧱 Entity Design

- `Product`: Holds product catalog info
- `Store`: Represents an individual store
- `StockMovement`: Logs inventory changes (stock-in, sale, manual)
- `User`: Handles access control and roles

---

## 🔐 Security

- Basic Authentication using Spring Security
- Users must log in to access core APIs

---

## ⚙️ Rate Limiting

- Implemented via Bucket4j
- Limits per endpoint/IP to prevent abuse

---

## 📬 Event-Driven Architecture

- `StockUpdateEvent` + `StockUpdateListener` ensure async inventory syncing
- Makes the system ready for distributed, decoupled environments

---

## 📊 Reporting & Filtering

APIs support filtering by:
- Store
- Product
- Date range
- Quantity thresholds

---

## 📂 API Endpoint Reference


### 🌐 Base URL

When running locally, all API requests should be made to:

http://localhost:8081


Example:
(GET http://localhost:8081/api/products)


Make sure the server is running on port `8081`. If you've changed it, update your request URLs accordingly.

---

### 🧾 Product APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/products` | Create a new product |
| `GET` | `/api/products` | Get all products |
| `GET` | `/api/products/{id}` | Get product by ID |
| `GET` | `/api/products/store/{storeId}` | Get all products by Store ID |
| `PUT` | `/api/products/{id}` | Update product |
| `DELETE` | `/api/products/{id}` | Delete product |

---

### 🏬 Store APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/stores` | Create a store |
| `GET` | `/api/stores` | Get all stores |
| `GET` | `/api/stores/{id}` | Get store by ID |
| `PUT` | `/api/stores/{id}` | Update store |
| `DELETE` | `/api/stores/{id}` | Delete store |

---

### 📦 Stock Movement APIs
| Method   | Endpoint                    | Description                                 |
|----------|-----------------------------|---------------------------------------------|
| `POST`   | `/api/stock-movements`      | Record stock-increase                       |
| `GET`    | `/api/stock-movements`      | Get all stock movement records              |
| `GET`    | `/api/stock-movements/{id}` | Get stock movement by ID                    |
| `GET`    | `/api/stock-movements/product/{id}` | Get stock movement by Product ID            |
| `PUT`    | `/api/stock-movements/{id}` | Update stock movement by ID (Only Quantity) |
| `DELETE` | `/api/stock-movements/{id}` | Delete stock movement by ID         |

---

### 👤 User APIs
| Method | Endpoint | Description                  |
|--------|----------|------------------------------|
| `POST` | `/api/users/register` | Register a normal user       |
| `POST` | `/api/users/register/admin` | Register an admin user       |
| `GET` | `/api/users` | Get all users                |
| `GET` | `/api/users/{id}` | Get user by ID               |
| `PUT` | `/api/users/password` | Update user password |
| `PUT` | `/api/users/{id}/password` | Update user password by admin |
| `DELETE` | `/api/users/{id}` | Delete user                  |

---

### 🔐 Authentication & Headers

All protected routes require:
- `Authorization: Basic <base64(username:password)>`

For JSON requests:
- `Content-Type: application/json`

---

## 👨‍💻 Author

**[Syed Ansab Iqbal]**  
Spring Boot Developer | Backend Engineer

---