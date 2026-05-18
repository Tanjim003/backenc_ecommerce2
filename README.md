E-Commerce REST API

A Spring Boot REST API for an e-commerce platform with product management, cart, orders, and returns.

Tech Stack

- **Java 21**
- **Spring Boot 4.0.6**
- **Spring Security** (HTTP Basic Auth)
- **Spring JDBC** (JdbcTemplate — no JPA)
- **MySQL**
- **Bean Validation** (jakarta.validation)

---

## Prerequisites

- Java 21+
- Maven 3.8+
- MySQL 8+

---

## Setup

### 1. Create the database

```sql
CREATE DATABASE ecom_db;
```

### 2. Configure credentials

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecom_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

> The schema (`users`, `products`, `cart_items`, `orders`, `order_items`, `return_requests`) is auto-created on first run via `schema.sql`.

### 3. Run the app

```bash
./mvnw spring-boot:run
```

Server starts at `http://localhost:8080`

---

## Authentication

All protected endpoints use **HTTP Basic Authentication**.  
Pass credentials with every request: `-u username:password`

| Role | Access |
|------|--------|
| `CUSTOMER` | Cart, Orders, Returns |
| `ADMIN` | Everything above + Admin panel |

---

## API Endpoints

### Auth — Public

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register a new user |
| GET | `/auth/me` | Get current logged-in user |

**Register:**
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "secret123",
    "role": "CUSTOMER"
  }'
```

**Register Admin:**
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "adminpass",
    "role": "ADMIN"
  }'
```

---

### Products — Public

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/products` | List all products |
| GET | `/products/{id}` | Get product by ID |

```bash
curl http://localhost:8080/products
curl http://localhost:8080/products/1
```

---

### Cart — Auth required (CUSTOMER)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/cart` | View cart |
| POST | `/cart/add` | Add item to cart |
| DELETE | `/cart/remove/{cartItemId}` | Remove item from cart |

```bash
# View cart
curl http://localhost:8080/cart -u alice:secret123

# Add to cart
curl -X POST http://localhost:8080/cart/add \
  -u alice:secret123 \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 2}'

# Remove from cart (use cartItemId from GET /cart, not productId)
curl -X DELETE http://localhost:8080/cart/remove/1 -u alice:secret123
```

---

### Orders — Auth required (CUSTOMER)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/orders/checkout` | Place order from cart |
| GET | `/orders/my` | List my orders |
| GET | `/orders/my/{id}` | Get order details + items |

```bash
# Checkout
curl -X POST http://localhost:8080/orders/checkout \
  -u alice:secret123 \
  -H "Content-Type: application/json" \
  -d '{
    "street": "123 Main St",
    "city": "Dhaka",
    "postalCode": "1200"
  }'

# My orders
curl http://localhost:8080/orders/my -u alice:secret123

# Order detail
curl http://localhost:8080/orders/my/1 -u alice:secret123
```

---

### Returns — Auth required (CUSTOMER)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/returns/request` | Request a return |
| GET | `/returns/my` | View my return requests |

```bash
# Request return
curl -X POST http://localhost:8080/returns/request \
  -u alice:secret123 \
  -H "Content-Type: application/json" \
  -d '{"orderId": "1", "reason": "Damaged on arrival"}'

# My returns
curl http://localhost:8080/returns/my -u alice:secret123
```

---

### Admin — ADMIN role only

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/admin/products` | Add a product (multipart/form-data) |
| GET | `/admin/products` | List all products |
| DELETE | `/admin/products/{id}` | Delete a product |
| GET | `/admin/orders` | List all orders |
| GET | `/admin/orders/{id}` | Order detail + items |
| PUT | `/admin/orders/{id}/status` | Update order status |
| GET | `/admin/returns` | List all returns |
| PUT | `/admin/returns/{id}/approve` | Approve return (restores stock) |
| PUT | `/admin/returns/{id}/reject` | Reject return |

```bash
# Add product (use -F flags, NOT JSON)
curl -X POST http://localhost:8080/admin/products \
  -u admin:adminpass \
  -F "name=Dell XPS 15" \
  -F "description=Intel i7 laptop" \
  -F "price=1299.99" \
  -F "stockQuantity=20"

# Add product with image
curl -X POST http://localhost:8080/admin/products \
  -u admin:adminpass \
  -F "name=Dell XPS 15" \
  -F "description=Intel i7 laptop" \
  -F "price=1299.99" \
  -F "stockQuantity=20" \
  -F "image=@/path/to/photo.jpg"

# Update order status (CONFIRMED / SHIPPED / DELIVERED / CANCELLED)
curl -X PUT http://localhost:8080/admin/orders/1/status \
  -u admin:adminpass \
  -H "Content-Type: application/json" \
  -d '{"status": "SHIPPED"}'

# Approve / reject return
curl -X PUT http://localhost:8080/admin/returns/1/approve -u admin:adminpass
curl -X PUT http://localhost:8080/admin/returns/1/reject -u admin:adminpass
```

---

## Validation Rules

| Field | Rule |
|-------|------|
| `username` | Required, 3–50 characters |
| `password` | Required, 6–100 characters |
| `product name` | Required, 2–100 characters |
| `price` | Required, 0.01–999999.99 |
| `stockQuantity` | Required, 0 or more |

---

## Error Responses

| Status | Meaning |
|--------|---------|
| `400` | Validation failed — check field error messages in response body |
| `401` | Wrong credentials or missing auth header |
| `403` | Authenticated but insufficient role (e.g. CUSTOMER hitting `/admin/**`) |
| `409` | Username already taken |
| `500` | Server error — check DB connection in `application.properties` |

---

## Project Structure

```
src/main/java/com/advanceJava/e_com/
├── controller/
│   ├── AuthController.java
│   ├── ProductController.java
│   ├── CartController.java
│   ├── OrderController.java
│   ├── ReturnController.java
│   ├── AdminController.java
│   └── GlobalExceptionHandler.java
├── service/
│   ├── AuthService.java
│   ├── ProductService.java
│   ├── CartService.java
│   ├── OrderService.java
│   ├── ReturnService.java
│   └── CustomUserDetailsService.java
├── repository/
│   ├── UserRepository.java / UserRepositoryImpl.java
│   ├── ProductRepository.java / ProductRepositoryImpl.java
│   ├── CartRepository.java / CartRepositoryImpl.java
│   ├── OrderRepository.java / OrderRepositoryImpl.java
│   └── ReturnRepository.java / ReturnRequestRepositoryImpl.java
├── models/
│   ├── User.java
│   ├── Product.java
│   ├── CartItem.java
│   ├── Order.java
│   ├── OrderItem.java
│   └── ReturnRequest.java
├── dto/
│   ├── UserDTO.java
│   ├── ProductDTO.java
│   ├── CartItemDTO.java
│   ├── OrderDTO.java
│   ├── OrderItemDTO.java
│   └── ReturnRequestDTO.java
├── security/
│   └── SecurityConfig.java
└── util/
    └── DTOMapper.java
```

---

## Quick Test Flow

```bash
# 1. Register admin and customer
curl -X POST http://localhost:8080/auth/register -H "Content-Type: application/json" -d '{"username":"admin","password":"adminpass","role":"ADMIN"}'
curl -X POST http://localhost:8080/auth/register -H "Content-Type: application/json" -d '{"username":"alice","password":"secret123","role":"CUSTOMER"}'

# 2. Admin adds a product
curl -X POST http://localhost:8080/admin/products -u admin:adminpass -F "name=iPhone 15" -F "description=Apple smartphone" -F "price=999.99" -F "stockQuantity=30"

# 3. Customer browses and adds to cart
curl http://localhost:8080/products
curl -X POST http://localhost:8080/cart/add -u alice:secret123 -H "Content-Type: application/json" -d '{"productId":1,"quantity":1}'

# 4. Customer checks out
curl -X POST http://localhost:8080/orders/checkout -u alice:secret123 -H "Content-Type: application/json" -d '{"street":"123 Main St","city":"Dhaka","postalCode":"1200"}'

# 5. Admin ships the order
curl -X PUT http://localhost:8080/admin/orders/1/status -u admin:adminpass -H "Content-Type: application/json" -d '{"status":"SHIPPED"}'

# 6. Customer requests return
curl -X POST http://localhost:8080/returns/request -u alice:secret123 -H "Content-Type: application/json" -d '{"orderId":"1","reason":"Wrong item"}'

# 7. Admin approves return
curl -X PUT http://localhost:8080/admin/returns/1/approve -u admin:adminpass
```
