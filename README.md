# 🛒 Inventory Tracking System - Updated API Documentation

## 📬 API Endpoint Reference

### 🌐 Base URL
When running locally, all API requests should be made to:
```
http://localhost:8081
```

---

## 🔐 Authentication APIs (No Auth Required)

### User Login
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/api/users/login` | User login | No |

**Request Body:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Response (Success):**
```json
{
  "message": "Login successful",
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "roles": ["ROLE_USER"],
  "success": true
}
```

### Admin Login
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/api/users/admin/login` | Admin login | No |

**Request Body:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Response (Success):**
```json
{
  "message": "Login successful",
  "userId": 1,
  "username": "admin_user",
  "email": "admin@example.com",
  "roles": ["ROLE_ADMIN"],
  "success": true
}
```

---

## 📝 Registration APIs (No Auth Required)

### User Registration
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/api/users/register` | Register a new user | No |

**Request Body:**
```json
{
  "username": "string",
  "email": "string",
  "password": "string"
}
```

**Response:**
```json
{
  "message": "User registered successfully",
  "success": true
}
```

### Admin Registration
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/api/users/admin/register` | Register a new admin | No |

**Request Body:**
```json
{
  "username": "string",
  "email": "string",
  "password": "string",
  "phone": "string",
  "location": "string",
  "city": "string",
  "state": "string",
  "country": "string"
}
```

**Response:**
```json
{
  "message": "Admin registered successfully",
  "success": true
}
```

---

## 👤 User Management APIs (Auth Required)

| Method | Endpoint | Description | Auth Required | Role Required |
|--------|----------|-------------|---------------|---------------|
| `GET` | `/api/users` | Get all users | Yes | ADMIN |
| `GET` | `/api/users/{id}` | Get user by ID | Yes | ADMIN |
| `PUT` | `/api/users/password` | Update own password | Yes | USER/ADMIN |
| `PUT` | `/api/users/admin/password/{username}` | Admin update user password | Yes | ADMIN |
| `DELETE` | `/api/users/{id}` | Delete user | Yes | ADMIN |

### Update Own Password
**Request Body:**
```json
{
  "currentpassword": "string",
  "newpassword": "string"
}
```

### Admin Update User Password
**Request Body:**
```json
{
  "password": "string"
}
```

---

## 🧾 Product APIs (Auth Required)
| Method | Endpoint | Description | Auth Required | Role Required |
|--------|----------|-------------|---------------|---------------|
| `POST` | `/api/products` | Create a new product | Yes | ADMIN |
| `GET` | `/api/products` | Get all products | Yes | ADMIN |
| `GET` | `/api/products/{id}` | Get product by ID | Yes | ADMIN |
| `GET` | `/api/products/store/{storeId}` | Get products by Store ID | Yes | USER/ADMIN |
| `PUT` | `/api/products/{id}` | Update product | Yes | ADMIN |
| `DELETE` | `/api/products/{id}` | Delete product | Yes | ADMIN |

---

## 🏬 Store APIs (Auth Required)
| Method | Endpoint | Description | Auth Required | Role Required |
|--------|----------|-------------|---------------|---------------|
| `POST` | `/api/stores` | Create a store | Yes | ADMIN |
| `GET` | `/api/stores` | Get all stores | Yes | USER/ADMIN |
| `GET` | `/api/stores/{id}` | Get store by ID | Yes | USER/ADMIN |
| `PUT` | `/api/stores/{id}` | Update store | Yes | ADMIN |
| `DELETE` | `/api/stores/{id}` | Delete store | Yes | ADMIN |

---

## 📦 Stock Movement APIs (Auth Required)
| Method | Endpoint | Description | Auth Required | Role Required |
|--------|----------|-------------|---------------|---------------|
| `POST` | `/api/stock-movements` | Record stock movement | Yes | ADMIN |
| `GET` | `/api/stock-movements` | Get all stock movements | Yes | USER/ADMIN |
| `GET` | `/api/stock-movements/{id}` | Get stock movement by ID | Yes | USER/ADMIN |
| `GET` | `/api/stock-movements/product/{id}` | Get stock movements by Product ID | Yes | USER/ADMIN |
| `PUT` | `/api/stock-movements/{id}` | Update stock movement | Yes | ADMIN |
| `DELETE` | `/api/stock-movements/{id}` | Delete stock movement | Yes | ADMIN |

---

## 🔐 Authentication Headers

### For Login and Registration Endpoints
**No authentication required**

### For Protected Endpoints
All protected routes require:
- `Authorization: Basic <base64(username:password)>`

For JSON requests:
- `Content-Type: application/json`

---

## 📝 Usage Examples

### 1. Register a New User
```bash
curl -X POST http://localhost:8081/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```

### 2. Login as User
```bash
curl -X POST http://localhost:8081/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

### 3. Register a New Admin
```bash
curl -X POST http://localhost:8081/api/users/admin/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin_user",
    "email": "admin@example.com",
    "password": "admin123",
    "phone": "+1234567890",
    "location": "123 Main St",
    "city": "New York",
    "state": "NY",
    "country": "USA"
  }'
```

### 4. Access Protected Endpoint
```bash
curl -X GET http://localhost:8081/api/products \
  -H "Authorization: Basic $(echo -n 'admin_user:admin123' | base64)" \
  -H "Content-Type: application/json"
```

---

## 🔄 Changes Made

### New Features:
1. **Login Endpoints**: Separate login endpoints for users and admins
2. **No Auth Registration**: Registration endpoints no longer require authentication
3. **Extended Admin Registration**: Admin registration includes additional fields (phone, location, city, state, country)
4. **Email Field**: Added email field to user entity and registration

### Security Updates:
1. **Public Endpoints**: Login and registration endpoints are now publicly accessible
2. **Role-based Access**: Admin login checks for ROLE_ADMIN specifically
3. **Enhanced User Entity**: Added email and location fields to user profiles

### Database Changes Required:
You'll need to update your database schema to include the new fields:
- `email` (varchar, unique, not null)
- `phone` (varchar, nullable)
- `location` (varchar, nullable)
- `city` (varchar, nullable)
- `state` (varchar, nullable)
- `country` (varchar, nullable)