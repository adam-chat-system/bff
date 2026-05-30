# BFF

Backend For Frontend (BFF) for the chat-system.

## Technologies

* Java 21
* Spring Boot
* Spring Security
* JWT
* WebClient

## Features

* User authentication
* JWT validation
* Protected API endpoints
* Request forwarding to Auth Service
* Request forwarding to Message Service
* User identity extraction from JWT tokens

## Endpoints

### Authentication

```http
POST /auth/signup
POST /auth/login
GET /auth/me
```

### Messages

```http
POST /messages
GET /messages
GET /messages/my
GET /messages/sender/{sender}
```

## Run

```bash
mvn clean package

docker build -t bff .
```

## Configuration

```properties
auth.service.url=http://auth-service:8081
message.service.url=http://message-service:8083

security.jwt.secret=<jwt-secret>
```

## Architecture

```text
Client
  ↓
  BFF
  ↓
 ├── Auth Service
 └── Message Service
```
